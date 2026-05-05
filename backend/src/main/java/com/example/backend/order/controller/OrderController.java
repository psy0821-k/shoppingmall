package com.example.backend.order.controller;

import com.example.backend.order.dto.OrderCreateRequest;
import com.example.backend.order.dto.OrderResponse;
import com.example.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrderFromCart(request);
    }

    @GetMapping
    public List<OrderResponse> getOrders(@RequestParam UUID userId) {
        return orderService.getOrders(userId);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponse changeStatus(@PathVariable UUID orderId, @RequestParam String status) {
        return orderService.changeStatus(orderId, status);
    }
}
