package com.example.backend.payment.controller;

import com.example.backend.payment.dto.PaymentCreateRequest;
import com.example.backend.payment.dto.PaymentResponse;
import com.example.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/ready")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse ready(@RequestBody PaymentCreateRequest request) {
        return paymentService.ready(request);
    }

    @PatchMapping("/{paymentId}/approve")
    public PaymentResponse approve(@PathVariable UUID paymentId) {
        return paymentService.approve(paymentId);
    }

    @PatchMapping("/{paymentId}/fail")
    public PaymentResponse fail(@PathVariable UUID paymentId) {
        return paymentService.fail(paymentId);
    }

    @GetMapping("/orders/{orderId}")
    public PaymentResponse getPaymentByOrder(@PathVariable UUID orderId) {
        return paymentService.getPaymentByOrder(orderId);
    }
}
