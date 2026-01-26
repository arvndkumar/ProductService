package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.exception.ProductAlreadyExistsException;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.search.ProductSearchService;
import com.ecommerce.productservice.util.RedisKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSearchService productSearchService;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ProductSearchService productSearchService,
                              RedisTemplate<String, Object> redisTemplate
                              )
    {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productSearchService = productSearchService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO)
    {
        Optional<Product> productOptional= productRepository.findByNameAndDeletedFalse(productRequestDTO.getName());

        if(productOptional.isPresent()){
            throw new ProductAlreadyExistsException("Product with name \"" + productRequestDTO.getName() + "\" already exists");
        }

        Optional<Category> categoryOptional = categoryRepository.findByName(productRequestDTO.getCategoryName());

        Category category = categoryOptional.orElseGet(()-> categoryRepository.save(new Category(productRequestDTO.getCategoryName())));

        Product product = productRequestDTO.toProduct();
        product.setCategory(category);
        Product savedProduct  = productRepository.save(product);
        productSearchService.save(savedProduct.toDocument());
        return savedProduct.toResponseDTO();
    }

    @Override
    public ProductResponseDTO getProductByID(Long id)
    {
        String key = RedisKeys.productById(id);

        ProductResponseDTO cachedProduct = (ProductResponseDTO) redisTemplate.opsForValue().get(key);

        if(cachedProduct != null){
            return cachedProduct;
        }

        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id " + id));

        ProductResponseDTO response = product.toResponseDTO();

        redisTemplate.opsForValue().set(key, response, Duration.ofMinutes(10));


        return response;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts(Pageable pageable) {

        Page<Product> products = productRepository.findAllByDeletedFalse(pageable);
        return products.stream().map(Product::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO updatedDto){
        Product existingProduct = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));

        existingProduct.setName(updatedDto.getName());
        existingProduct.setPrice(updatedDto.getPrice());
        existingProduct.setDescription(updatedDto.getDescription());
        existingProduct.setQuantity(updatedDto.getQuantity());
        existingProduct.setImageUrl(updatedDto.getImage_url());

        Category category = categoryRepository.findByName(updatedDto.getCategoryName())
                .orElseGet(()-> {
                    Category newCategory = new Category(updatedDto.getCategoryName());
                    categoryRepository.save(newCategory);
                    return newCategory;
                });

        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        productSearchService.save(updatedProduct.toDocument());

        redisTemplate.delete(RedisKeys.productById(id));

        return updatedProduct.toResponseDTO();

    }

    @Override
    public ProductResponseDTO patchProduct(Long id, Map<String, Object> updates) {

        Product existingProduct = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));

        if(updates.isEmpty()) {throw new IllegalArgumentException("No updates given");}

        for(Map.Entry<String, Object> update: updates.entrySet()) {
            String key = update.getKey();
            Object value = update.getValue();

            switch (key) {
                case "name" ->  existingProduct.setName(value.toString());
                case "description" -> existingProduct.setDescription(value.toString());

                case "price" -> existingProduct.setPrice(Double.parseDouble(value.toString()));

                case "image_url" -> existingProduct.setImageUrl(value.toString());

                case "quantity" -> existingProduct.setQuantity(Integer.parseInt(value.toString()));

                case "categoryName" -> {
                        String categoryName = value.toString();
                        Category category = categoryRepository.findByName(categoryName)
                                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
                        existingProduct.setCategory(category);
                    }

                    default -> throw new IllegalArgumentException("Invalid key: " + key);
                }

            }

        Product updatedProduct = productRepository.save(existingProduct);

        redisTemplate.delete(RedisKeys.productById(id));

        return updatedProduct.toResponseDTO();

    }

    @Override
    public void deleteProduct(Long id)
    {
        Product existingProduct = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));

        if(existingProduct.getDeleted()){
            throw new IllegalStateException("Product has been deleted already");
        }

        existingProduct.setDeleted(Boolean.TRUE);
        productRepository.save(existingProduct);
        productSearchService.deleteById(existingProduct.getId().toString());
        redisTemplate.delete(RedisKeys.productById(id));
    }

    @Override
    public List<ProductResponseDTO> findAllProducts(int page, int size, String sortParam)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortParam).descending());
        Page<Product> productsPage = productRepository.findAllByDeletedFalse(pageable);

        return productsPage.getContent()
                .stream()
                .map(Product::toResponseDTO)
                .toList();

    }

    @Override
    public List<ProductResponseDTO> findAllProductsByCategory(Long categoryId, int page, int size, String sortParam) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortParam).descending());

        Page<Product> productsPage =
                productRepository.findByCategory_IdAndDeletedFalse(categoryId, pageable);

        return productsPage.getContent()
                .stream()
                .map(Product::toResponseDTO)
                .toList();
    }
}
