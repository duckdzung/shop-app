package com.example.shopapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import com.example.shopapp.responses.ProductResponse;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.services.interfaces.IProductService;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public Page<ProductResponse> getAllProducts(int page, int limit, Long categoryId, String keyword) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<Product> productsPage = productRepository.findAllByCategoryIdAndKeyword(categoryId, keyword, pageable);

        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return product;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        product = productRepository.save(product);

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        Product newProduct = modelMapper.map(productDTO, Product.class);
        newProduct.setId(id);
        newProduct = productRepository.save(newProduct);

        return modelMapper.map(newProduct, ProductDTO.class);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public ProductImageDTO createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception {
        // find product before insert images
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + productId));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // check number of images
        int size = productImageRepository.findByProductId(productId).size();
        int maximumImages = ProductImage.MAXIMUM_IMAGES_PER_PRODUCT;
        if (size > maximumImages) {
            throw new InvalidParamException("Number of images must be less or equals to" + maximumImages);
        }

        // save to DB
        productImageRepository.save(newProductImage);

        return modelMapper.map(newProductImage, ProductImageDTO.class);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
}
