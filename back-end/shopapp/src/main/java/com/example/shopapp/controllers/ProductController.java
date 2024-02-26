package com.example.shopapp.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.services.ProductService;
import com.github.javafaker.Faker;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "") String keyword) {
        try {
            Page<ProductResponse> products = productService.getAllProducts(page, limit, categoryId, keyword);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting all products");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        // create a unique file name
        String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;

        // path to store file
        Path uploadDir = Paths.get("uploads");

        // check directory is exist
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // full path to store file
        Path destinationPath = Paths.get(uploadDir.toString(), uniqueFileName);

        // copy file to directory
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errors);
            }

            ProductDTO product = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product");
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable Long id,
            @ModelAttribute("files") List<MultipartFile> files) {
        try {
            // Check if product exists
            Product existingProduct = productService.getProductById(id);

            // Check if files are empty
            if (files.isEmpty()) {
                return ResponseEntity.badRequest().body("No images provided");
            }

            List<ProductImageDTO> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                // Skip empty files
                if (file.isEmpty()) {
                    continue;
                }

                // Check file size
                long maxSizeBytes = 10 * 1024 * 1024; // 10MB
                if (file.getSize() > maxSizeBytes) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size 10MB");
                }

                // Check file format
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }

                // Store file and update thumbnail in DTO
                String filename = storeFile(file);

                // Save to product_images table in DB
                ProductImageDTO productImageDTO = ProductImageDTO.builder()
                        .productId(existingProduct.getId())
                        .imageUrl(filename)
                        .build();
                productImageDTO = productService.createProductImage(id, productImageDTO);

                // Add to list of product images
                productImages.add(productImageDTO);
            }

            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading product images");
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            // Create a path to the image based on the provided imageName
            Path imagePath = Paths.get("uploads/" + imageName);

            // Create a UrlResource object from the image file path
            UrlResource resource = new UrlResource(imagePath.toUri());

            // Check if the image file exists
            if (resource.exists()) {
                // If the image file exists, return HTTP 200 OK response with the image content
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                // If the image file does not exist, return HTTP 404 Not Found response
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // If any error occurs, return HTTP 404 Not Found response
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO product = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product successfully with id: " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
        }
    }

    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 1_000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 10_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(1, 5))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Products created successfully");
    }
}
