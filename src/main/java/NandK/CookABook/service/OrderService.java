package NandK.CookABook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.order.OrderCreationRequest;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.CartItem;
import NandK.CookABook.entity.Order;
import NandK.CookABook.entity.OrderItem;
import NandK.CookABook.repository.OrderItemRepository;
import NandK.CookABook.repository.OrderRepository;
import NandK.CookABook.utils.constant.OrderStatusEnum;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }

    public Order createOrderFromCart(OrderCreationRequest request, Cart cart) {
        // this.cartService.calculateCartPrice(cart);
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setTotalPrice(cart.getTotalFinalPrice());
        order.setStatus(OrderStatusEnum.PENDING);
        // order.setPaymentMethod(request.getPaymentMethod());
        // order.setShippingAddress(request.getShippingAddress());

        List<CartItem> cartItems = cart.getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getBook().getStockQuantity() < cartItem.getQuantity()) {
                return null;
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getFinalPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        return this.orderRepository.save(order);
    }
}
