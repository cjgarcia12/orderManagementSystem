package org.example.ordermanagementsystem;

import org.example.exceptions.ResourceNotFoundException;
import org.example.models.Order;
import org.example.models.Product;
import org.example.repositories.OrderRepository;
import org.example.services.OrderService;
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
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService; // Class under test

    @Mock
    private OrderRepository orderRepository; // Mocked dependency

    @Mock
    private ProductService productService; // Mocked dependency (if needed)

    @Test
    public void testCreateOrder() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(10.0);

        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(2);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setProduct(product);
        savedOrder.setQuantity(2);

        when(orderRepository.save(order)).thenReturn(savedOrder);

        // Act
        Order result = orderService.createOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getQuantity());
        assertEquals(product.getId(), result.getProduct().getId());
    }

    @Test
    public void testUpdateOrder() {
        // Arrange
        Long orderId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(20.0);

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setProduct(product);
        existingOrder.setQuantity(1);

        Order updatedOrderDetails = new Order();
        updatedOrderDetails.setProduct(product);
        updatedOrderDetails.setQuantity(5);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrderDetails);

        // Act
        Order result = orderService.updateOrder(orderId, updatedOrderDetails);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getQuantity());
    }

    @Test
    public void testDeleteOrder() {
        // Arrange
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        doNothing().when(orderRepository).delete(existingOrder);

        // Act
        orderService.deleteOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).delete(existingOrder);
    }

    @Test
    public void testFindOrderById() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setQuantity(4);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.findOrderById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(4, result.getQuantity());
    }

    @Test
    public void testFindAllOrders() {
        // Arrange
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<Order> result = orderService.findAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
