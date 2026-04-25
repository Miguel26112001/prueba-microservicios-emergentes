package com.example.sales.products.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sales.products.domain.model.commands.DeleteProductCommand;
import com.example.sales.products.domain.model.queries.GetAllProductsQuery;
import com.example.sales.products.domain.model.queries.GetProductByIdQuery;
import com.example.sales.products.domain.model.queries.GetProductByNameQuery;
import com.example.sales.products.domain.services.ProductCommandService;
import com.example.sales.products.domain.services.ProductQueryService;
import com.example.sales.products.interfaces.rest.resources.CreateProductResource;
import com.example.sales.products.interfaces.rest.resources.ProductResource;
import com.example.sales.products.interfaces.rest.resources.UpdateProductResource;
import com.example.sales.products.interfaces.rest.transform.CreateProductCommandFromResourceAssembler;
import com.example.sales.products.interfaces.rest.transform.ProductResourceFromEntityAssembler;
import com.example.sales.products.interfaces.rest.transform.UpdateProductCommandFromResourceAssembler;
import com.example.sales.shared.interfaces.rest.resources.MessageResource;

@RestController
@RequestMapping(
    value = "/api/v1/products",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
    name = "Products",
    description = "API for managing product resources. Provides endpoints to retrieve product information."
)
public class ProductController {

  private final ProductQueryService productQueryService;
  private final ProductCommandService productCommandService;

  public ProductController(
      ProductQueryService productQueryService,
      ProductCommandService productCommandService) {
    this.productQueryService = productQueryService;
    this.productCommandService = productCommandService;
  }

  @GetMapping()
  @Operation(
      summary = "Get all products",
      description = "Retrieves a list of all available products in the system. Returns no content if no products exist.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved products list",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = ProductResource.class)
                  ),
                  examples = @ExampleObject(
                      name = "Successful response",
                      value = """
                          [
                            {
                              "id": 1,
                              "name": "Laptop Lenovo IdeaPad",
                              "price": 1499.99,
                              "stock": 10
                            },
                            {
                              "id": 2,
                              "name": "Mouse Logitech",
                              "price": 59.90,
                              "stock": 50
                            }
                          ]
                          """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "204",
              description = "No products found",
              content = @Content(
                  mediaType = "application/json",
                  examples = @ExampleObject(value = "[]")
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = MessageResource.class
                  ),
                  examples = @ExampleObject(
                      value = """
                          {
                            "timestamp": "2026-04-23T00:17:20.539",
                            "status": 500,
                            "error": "Internal Server Error",
                            "code": "INTERNAL_ERROR",
                            "message": "An unexpected error occurred",
                            "path": "/api/v1/products"
                          }
                          """
                  )
              )
          )
      }
  )
  public ResponseEntity<List<ProductResource>> getAllProducts() {

    var products = productQueryService.handle(new GetAllProductsQuery());

    var productResources = products.stream()
        .map(ProductResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    return ResponseEntity.ok(productResources);
  }


  @GetMapping("/{productId}")
  @Operation(
      summary = "Get product by ID",
      description = "Retrieves a specific product by its unique identifier. Returns detailed product information if found.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Product found successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProductResource.class),
                  examples = @ExampleObject(
                      name = "Product found",
                      value = """
                        {
                          "id": 1,
                          "name": "Laptop Lenovo IdeaPad",
                          "price": 1499.99,
                          "stock": 10
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid product ID",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 400,
                          "error": "Bad Request",
                          "code": "INVALID_ID",
                          "message": "Product id must be greater than 0",
                          "path": "/api/v1/products/0"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Product not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "PRODUCT_NOT_FOUND",
                          "message": "Product with id 1 does not exist",
                          "path": "/api/v1/products/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/products/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProductResource> getProductById(
      @Parameter(description = "Product ID to retrieve", example = "1", required = true)
      @PathVariable Long productId) {

    var getProductByIdQuery = new GetProductByIdQuery(productId);

    var productOptional = productQueryService.handle(getProductByIdQuery);
    if (productOptional.isEmpty()) {
      return ResponseEntity
          .notFound()
          .build();
    }

    var productResource = ProductResourceFromEntityAssembler
        .toResourceFromEntity(productOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(productResource);
  }

  @PostMapping
  @Operation(
      summary = "Create a new product",
      description = "Creates a new product with the provided information.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Product creation data",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CreateProductResource.class),
              examples = {
                  @ExampleObject(
                      name = "Valid product example",
                      value = """
                        {
                          "name": "Laptop Lenovo IdeaPad",
                          "price": 1499.99,
                          "stock": 10
                        }
                        """
                  ),
                  @ExampleObject(
                      name = "Invalid product example",
                      value = """
                        {
                          "name": "",
                          "price": -10,
                          "stock": -1
                        }
                        """
                  )
              }
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Product created successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProductResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "id": 1,
                          "name": "Laptop Lenovo IdeaPad",
                          "price": 1499.99,
                          "stock": 10
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid input data",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 400,
                          "error": "Bad Request",
                          "code": "VALIDATION_ERROR",
                          "message": "Invalid product data",
                          "path": "/api/v1/products"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "409",
              description = "Conflict - Product Name already exists",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "Duplicate name",
                      value = """
                        {
                          "timestamp": "2024-01-01T00:00:00",
                          "status": 409,
                          "error": "Conflict",
                          "code": "PRODUCT_NAME_ALREADY_EXISTS",
                          "message": "Product with name Laptop Lenovo IdeaPad already exists",
                          "path": "/api/v1/products"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/products"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProductResource> createProduct(
      @RequestBody @Valid CreateProductResource resource) {

    var createProductCommand = CreateProductCommandFromResourceAssembler
        .toCommandFromResource(resource);

    var productOptional = productCommandService.handle(createProductCommand);
    if (productOptional.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(null);
    }

    var productResource = ProductResourceFromEntityAssembler
        .toResourceFromEntity(productOptional.get());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(productResource);
  }

  @PutMapping("/{productId}")
  @Operation(
      summary = "Update an existing product",
      description = "Updates a product's information by their unique identifier. All fields are required for full update.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Updated product data",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UpdateProductResource.class),
              examples = {
                  @ExampleObject(
                      name = "Valid update example",
                      summary = "Complete product data update",
                      value = """
                        {
                          "name": "Laptop Lenovo IdeaPad Pro",
                          "price": 1699.99,
                          "stock": 8
                        }
                        """
                  ),
                  @ExampleObject(
                      name = "Invalid update example - Negative price",
                      summary = "Invalid price",
                      value = """
                        {
                          "name": "Laptop Lenovo IdeaPad Pro",
                          "price": -100.00,
                          "stock": 8
                        }
                        """
                  ),
                  @ExampleObject(
                      name = "Invalid update example - Negative stock",
                      summary = "Invalid stock",
                      value = """
                        {
                          "name": "Laptop Lenovo IdeaPad Pro",
                          "price": 1699.99,
                          "stock": -5
                        }
                        """
                  )
              }
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Product updated successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProductResource.class),
                  examples = @ExampleObject(
                      name = "Updated product response",
                      value = """
                        {
                          "id": 1,
                          "name": "Laptop Lenovo IdeaPad Pro",
                          "price": 1699.99,
                          "stock": 8
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad request - Invalid input data",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = {
                      @ExampleObject(
                          name = "Validation errors",
                          summary = "Bean validation failed",
                          value = """
                            {
                              "timestamp": "2026-04-23T00:17:20.539",
                              "status": 400,
                              "error": "Bad Request",
                              "code": "VALIDATION_ERROR",
                              "message": "Name is required, Price must be greater than 0, Stock must be greater than or equal to 0",
                              "path": "/api/v1/products/1"
                            }
                            """
                      )
                  }
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Product not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "Product not found",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "PRODUCT_NOT_FOUND",
                          "message": "Product with id 1 does not exist",
                          "path": "/api/v1/products/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "409",
              description = "Conflict - Product name already exists",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      name = "Duplicate name",
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 409,
                          "error": "Conflict",
                          "code": "PRODUCT_NAME_ALREADY_EXISTS",
                          "message": "Product with name 'Laptop Lenovo IdeaPad Pro' already exists",
                          "path": "/api/v1/products/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/products/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<ProductResource> updateProduct(
      @Parameter(description = "User ID to update", example = "1", required = true)
      @Min(value = 1, message = "Product id must be greater than 0")
      @PathVariable Long productId,
      @RequestBody @Valid UpdateProductResource resource
  ) {

    var updateProductCommand = UpdateProductCommandFromResourceAssembler
        .toCommandFromResource(productId, resource);

    var productOptional = productCommandService.handle(updateProductCommand);
    if (productOptional.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(null);
    }

    var productResource = ProductResourceFromEntityAssembler
        .toResourceFromEntity(productOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(productResource);
  }

  @DeleteMapping("/{productId}")
  @Operation(
      summary = "Delete a product",
      description = "Deletes a product by its unique identifier. Returns no content if deletion is successful.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Product deleted successfully",
              content = @Content
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Product not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 404,
                          "error": "Not Found",
                          "code": "PRODUCT_NOT_FOUND",
                          "message": "Product with id 1 does not exist",
                          "path": "/api/v1/products/1"
                        }
                        """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResource.class),
                  examples = @ExampleObject(
                      value = """
                        {
                          "timestamp": "2026-04-23T00:17:20.539",
                          "status": 500,
                          "error": "Internal Server Error",
                          "code": "INTERNAL_ERROR",
                          "message": "An unexpected error occurred",
                          "path": "/api/v1/products/1"
                        }
                        """
                  )
              )
          )
      }
  )
  public ResponseEntity<Void> deleteProduct(
      @Parameter(description = "Product ID to delete", example = "1", required = true)
      @Min(value = 1, message = "Product id must be greater than 0")
      @PathVariable @Valid Long productId) {

    var deleteProductCommand = new DeleteProductCommand(productId);
    productCommandService.handle(deleteProductCommand);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/name/{productName}")
  @Operation(
      summary = "Get product by product name",
      description = "Retrieves a specific product by their name. Returns detailed information if found.",
      parameters = {
          @Parameter(
              name = "name",
              description = "Name of the product to retrieve",
              example = "Laptop Lenovo IdeaPad Pro",
              required = true,
              schema = @Schema(type = "string")
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Product found successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProductResource.class),
                  examples = @ExampleObject(
                      name = "Product found",
                      value = """
                        {
                          "id": 1,
                          "name": "Laptop Lenovo IdeaPad",
                          "price": 1499.99,
                          "stock": 10
                        }
                        """
                  )
              )
          ),
      }
  )
  public ResponseEntity<ProductResource> getProductByName(
      @Parameter(description = "Product name to retrieve", example = "Laptop Lenovo IdeaPad", required = true)
      @PathVariable
      @NotBlank(message = "Product name is required") String productName
  ) {

    var getProductByNameQuery = new GetProductByNameQuery(productName);

    var productOptional = productQueryService.handle(getProductByNameQuery);
    if (productOptional.isEmpty()) {
      return ResponseEntity
          .notFound()
          .build();
    }

    var productResource = ProductResourceFromEntityAssembler
        .toResourceFromEntity(productOptional.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(productResource);
  }
}
