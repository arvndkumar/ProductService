# Product Service

REST API service for managing products in the e-commerce system.

## API Endpoints

### Create Product

- **URL:** `/products/create`
- **Method:** POST
- **Request Body:** ProductRequestDTO
- **Response:** ProductResponseDTO

### Get Product by ID

- **URL:** `/products/{id}`
- **Method:** GET
- **Response:** ProductResponseDTO

### Update Product

- **URL:** `/products/{id}`
- **Method:** PUT
- **Headers:** Authorization (Bearer token required)
- **Request Body:** ProductRequestDTO
- **Response:** ProductResponseDTO

### Patch Product

- **URL:** `/products/{id}`
- **Method:** PATCH
- **Headers:** Authorization (Bearer token required)
- **Request Body:** Map of fields to update
- **Response:** ProductResponseDTO

### Delete Product

- **URL:** `/products/{id}`
- **Method:** DELETE
- **Headers:** Authorization (Bearer token required)
- **Response:** 204 No Content

### Browse Products

- **URL:** `/products/browse`
- **Method:** GET
- **Parameters:**
    - page (default: 0)
    - size (default: 10)
    - sort (default: "name.keyword")
- **Response:** List<ProductResponseDTO>

### Browse Products by Category

- **URL:** `/products/browse/{categoryId}`
- **Method:** GET
- **Parameters:**
    - page (default: 0)
    - size (default: 10)
    - sort (default: "name.keyword")
- **Response:** List<ProductResponseDTO>

## Error Handling

The service handles the following error scenarios:

### HTTP 409 Conflict

- Thrown when attempting to create a product that already exists
- Response includes error message describing the conflict

### HTTP 404 Not Found

- Thrown when requested product cannot be found
- Response includes error message with details

### HTTP 400 Bad Request

- Thrown for invalid input parameters
- Response includes error message describing the validation failure

### HTTP 500 Internal Server Error

- Thrown for unexpected server-side errors
- Generic error message to avoid exposing internal details

