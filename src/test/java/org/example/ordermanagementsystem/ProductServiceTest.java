package org.example.ordermanagementsystem;

import org.example.exceptions.ResourceNotFoundException;
import org.example.models.Product;
import org.example.repositories.ProductRepository;
import org.example.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService; // Class under test

    @Mock
    private ProductRepository productRepository; // Mocked dependency

    @Test
    public void testCreateProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(15.0);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Test Product");
        savedProduct.setPrice(15.0);

        when(productRepository.save(product)).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(15.0, result.getPrice());
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Name");
        existingProduct.setPrice(25.0);

        Product updatedProductDetails = new Product();
        updatedProductDetails.setName("New Name");
        updatedProductDetails.setPrice(30.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProductDetails);

        // Act
        Product result = productService.updateProduct(productId, updatedProductDetails);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(30.0, result.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).delete(existingProduct);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).delete(existingProduct);
    }

    @Test
    public void testFindProductById() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Find Me");
        product.setPrice(45.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.findProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Find Me", result.getName());
    }

    @Test
    public void testFindAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.findAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
