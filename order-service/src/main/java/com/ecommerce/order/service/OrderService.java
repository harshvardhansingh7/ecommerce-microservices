package com.ecommerce.order.service;

import com.ecommerce.order.client.InventoryClient;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderDTO;
import com.ecommerce.order.dto.OrderEvent;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository orderRepository,
                        KafkaTemplate<String, Object> kafkaTemplate,
                        InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.inventoryClient = inventoryClient;
    }

    public OrderDTO createOrder(CreateOrderRequest request) {
        // Create order entity
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(request.getShippingAddress());

        // Convert items
        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemDTO -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(itemDTO.getProductId());
                    item.setProductName(itemDTO.getProductName());
                    item.setPrice(itemDTO.getPrice());
                    item.setQuantity(itemDTO.getQuantity());
                    item.setImageUrl(itemDTO.getImageUrl());
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // Calculate total
        BigDecimal total = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Send order created event
        OrderEvent orderEvent = createOrderEvent(savedOrder);
        kafkaTemplate.send("order-created", orderEvent);

        return convertToDTO(savedOrder);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }

    public OrderDTO getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Order.OrderStatus oldStatus = order.getStatus();
        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);

        // Send status update event
        if (status != oldStatus) {
            OrderEvent orderEvent = createOrderEvent(updatedOrder);
            String topic = getTopicForStatus(status);
            if (topic != null) {
                kafkaTemplate.send(topic, orderEvent);
            }
        }

        return convertToDTO(updatedOrder);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderEvent createOrderEvent(Order order) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setStatus(convertStatus(order.getStatus()));

        event.setItems(order.getItems().stream()
                .map(item -> {
                    OrderEvent.OrderItem eventItem = new OrderEvent.OrderItem();
                    eventItem.setProductId(item.getProductId());
                    eventItem.setQuantity(item.getQuantity());
                    return eventItem;
                })
                .collect(Collectors.toList()));

        return event;
    }

    private OrderEvent.OrderStatus convertStatus(Order.OrderStatus status) {
        return switch (status) {
            case CONFIRMED -> OrderEvent.OrderStatus.CONFIRMED;
            case CANCELLED -> OrderEvent.OrderStatus.CANCELLED;
            default -> OrderEvent.OrderStatus.CREATED;
        };
    }

    private String getTopicForStatus(Order.OrderStatus status) {
        return switch (status) {
            case CONFIRMED -> "order-confirmed";
            case CANCELLED -> "order-cancelled";
            default -> null;
        };
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        dto.setItems(order.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setImageUrl(item.getImageUrl());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}