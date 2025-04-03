package NandK.CookABook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.order.OrderStatusUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.order.OrderFoundResponse;
import NandK.CookABook.dto.response.order.OrderItemResponse;
import NandK.CookABook.dto.response.order.OrderPreviewResponse;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.CartItem;
import NandK.CookABook.entity.Order;
import NandK.CookABook.entity.OrderItem;
import NandK.CookABook.entity.Payment;
import NandK.CookABook.entity.ShippingAddress;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.OrderItemRepository;
import NandK.CookABook.repository.OrderRepository;
import NandK.CookABook.repository.PaymentRepository;
import NandK.CookABook.utils.constant.OrderStatusEnum;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemService orderItemService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            PaymentRepository paymentRepository, OrderItemService orderItemService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
    }

    public Order createOrder(User user, Cart cart, ShippingAddress shippingAddress, Payment payment) {
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        payment.setOrder(order);
        order.setPayment(payment);
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setTotalFinalPrice(cart.getTotalFinalPrice());
        order.setStatus(OrderStatusEnum.PENDING);

        List<CartItem> selectedItems = this.cartService.getSelectedItems(cart);
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem selectedItem : selectedItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(selectedItem.getBook());
            orderItem.setQuantity(selectedItem.getQuantity());
            orderItem.setFinalPrice(selectedItem.getFinalPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        this.orderRepository.save(order);
        this.orderItemRepository.saveAll(orderItems);
        this.paymentRepository.save(payment);
        this.cartService.deleteSelectedItems(selectedItems);
        this.cartService.resetCartToZero(cart);
        return order;
    }

    public ResultPagination getAllOrdersByUser(User user, Pageable pageable) {
        Page<Order> orders = this.orderRepository.findByUser(user, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(orders.getTotalPages());
        meta.setTotalElements(orders.getTotalElements());
        result.setMeta(meta);

        List<OrderPreviewResponse> orderPreviewResponses = new ArrayList<>();
        for (Order order : orders.getContent()) {
            orderPreviewResponses.add(this.convertToOrderPreviewResponse(order));
        }
        result.setData(orderPreviewResponses);
        return result;
    }

    public ResultPagination getAllOrders(Specification<Order> spec, Pageable pageable) {
        Page<Order> orders = this.orderRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(orders.getTotalPages());
        meta.setTotalElements(orders.getTotalElements());
        result.setMeta(meta);

        List<OrderPreviewResponse> orderPreviewResponses = new ArrayList<>();
        for (Order order : orders.getContent()) {
            orderPreviewResponses.add(this.convertToOrderPreviewResponse(order));
        }
        result.setData(orderPreviewResponses);
        return result;
    }

    public OrderPreviewResponse convertToOrderPreviewResponse(Order order) {
        OrderPreviewResponse orderPreviewResponse = new OrderPreviewResponse();
        orderPreviewResponse.setId(order.getId());
        orderPreviewResponse.setTotalPrice(order.getTotalFinalPrice());
        orderPreviewResponse.setTotalQuantity(order.getTotalQuantity());
        orderPreviewResponse.setStatus(order.getStatus());
        orderPreviewResponse.setCreatedAt(order.getCreatedAt());
        orderPreviewResponse.setUpdatedAt(order.getUpdatedAt());

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemResponse> orderItemResponses = this.orderItemService
                .convertToOrderItemResponse(orderItems);
        orderPreviewResponse.setOrderItems(orderItemResponses);

        return orderPreviewResponse;
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> order = this.orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            return null;
        }
    }

    public OrderFoundResponse convertToOrderFoundResponse(Order order) {
        OrderFoundResponse orderFoundResponse = new OrderFoundResponse();
        OrderFoundResponse.Payment payment = new OrderFoundResponse.Payment(
                order.getPayment().getId(),
                order.getPayment().getMethod(),
                order.getPayment().getStatus());
        OrderFoundResponse.ShippingAddress shippingAddress = new OrderFoundResponse.ShippingAddress(
                order.getShippingAddress().getId(),
                order.getShippingAddress().getName(),
                order.getShippingAddress().getPhoneNumber(),
                order.getShippingAddress().getCity(),
                order.getShippingAddress().getDistrict(),
                order.getShippingAddress().getWard(),
                order.getShippingAddress().getAddress());

        orderFoundResponse.setId(order.getId());
        orderFoundResponse.setTotalQuantity(order.getTotalQuantity());
        orderFoundResponse.setTotalPrice(order.getTotalFinalPrice());
        orderFoundResponse.setStatus(order.getStatus());
        orderFoundResponse.setCreatedAt(order.getCreatedAt());
        orderFoundResponse.setUpdatedAt(order.getUpdatedAt());
        orderFoundResponse.setUserId(order.getUser().getId());
        orderFoundResponse.setShippingAddress(shippingAddress);
        orderFoundResponse.setPayment(payment);

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemResponse> orderItemResponses = this.orderItemService
                .convertToOrderItemResponse(orderItems);
        orderFoundResponse.setOrderItems(orderItemResponses);

        return orderFoundResponse;
    }

    public Order updateOrderStatus(OrderStatusUpdateRequest request) {
        Order order = this.getOrderById(request.getId());
        if (order != null) {
            if (request.getStatus() != null) {
                order.setStatus(request.getStatus());
            }
            return this.orderRepository.save(order);
        } else {
            return null;
        }
    }

    public void cancelOrder(Order order) {
        order.setStatus(OrderStatusEnum.CANCELLED);
        this.orderRepository.save(order);
    }

    public void deleteOrder(Order order) {
        this.orderRepository.delete(order);
    }
}
