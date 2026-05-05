package com.example.backend.payment.service;

import com.example.backend.order.entity.Order;
import com.example.backend.order.repository.OrderRepository;
import com.example.backend.payment.dto.PaymentCreateRequest;
import com.example.backend.payment.dto.PaymentResponse;
import com.example.backend.payment.entity.Payment;
import com.example.backend.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse ready(PaymentCreateRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        paymentRepository.findByOrder(order).ifPresent(payment -> {
            throw new IllegalArgumentException("이미 결제 정보가 생성된 주문입니다.");
        });

        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(request.getPaymentMethod())
                .amount(order.getFinalPrice())
                .status("READY")
                .build();

        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse approve(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        if (!payment.getAmount().equals(payment.getOrder().getFinalPrice())) {
            throw new IllegalArgumentException("결제 금액이 주문 금액과 일치하지 않습니다.");
        }
        payment.approve();
        return PaymentResponse.from(payment);
    }

    @Transactional
    public PaymentResponse fail(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.fail();
        return PaymentResponse.from(payment);
    }

    public PaymentResponse getPaymentByOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));
        return PaymentResponse.from(payment);
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));
    }
}
