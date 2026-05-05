package com.example.backend.order.service;

import com.example.backend.cart.entity.Cart;
import com.example.backend.cart.entity.CartItem;
import com.example.backend.cart.repository.CartItemRepository;
import com.example.backend.cart.repository.CartRepository;
import com.example.backend.order.dto.*;
import com.example.backend.order.entity.Order;
import com.example.backend.order.entity.OrderItem;
import com.example.backend.order.repository.OrderItemRepository;
import com.example.backend.order.repository.OrderRepository;
import com.example.backend.product.entity.ProductOption;
import com.example.backend.product.repository.ProductOptionRepository;
import com.example.backend.user.entity.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrderFromCart(OrderCreateRequest request) {
        User user = getUser(request.getUserId());
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 없습니다."));
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) throw new IllegalArgumentException("장바구니가 비어 있습니다.");

        int totalPrice = cartItems.stream()
                .mapToInt(item -> item.getProductOption().getSalePrice() * item.getQuantity())
                .sum();
        int deliveryFee = request.getDeliveryFee() == null ? 0 : request.getDeliveryFee();

        Order order = orderRepository.save(Order.builder()
                .user(user)
                .totalPrice(totalPrice)
                .deliveryFee(deliveryFee)
                .finalPrice(totalPrice + deliveryFee)
                .status("PENDING")
                .build());

        for (CartItem cartItem : cartItems) {
            ProductOption option = cartItem.getProductOption();
            option.decreaseStock(cartItem.getQuantity());
            productOptionRepository.save(option);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productOption(option)
                    .quantity(cartItem.getQuantity())
                    .price(option.getSalePrice())
                    .build();
            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteByCart(cart);
        return getOrder(order.getId());
    }

    public List<OrderResponse> getOrders(UUID userId) {
        User user = getUser(userId);
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(order -> OrderResponse.from(order, getOrderItems(order)))
                .toList();
    }

    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        return OrderResponse.from(order, getOrderItems(order));
    }

    @Transactional
    public OrderResponse changeStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        order.setStatus(status);
        return getOrder(order.getId());
    }

    private List<OrderItemResponse> getOrderItems(Order order) {
        return orderItemRepository.findByOrder(order).stream()
                .map(OrderItemResponse::from)
                .toList();
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
