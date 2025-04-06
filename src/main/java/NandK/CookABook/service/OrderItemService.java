package NandK.CookABook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.response.order.OrderItemFoundResponse;
import NandK.CookABook.dto.response.order.OrderItemPreviewResponse;
import NandK.CookABook.entity.OrderItem;

@Service
public class OrderItemService {

    public List<OrderItemPreviewResponse> convertToOrderItemPreviewResponse(List<OrderItem> orderItems) {
        List<OrderItemPreviewResponse> orderItemPreviewResponses = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemPreviewResponse orderItemPreviewResponse = new OrderItemPreviewResponse();
            orderItemPreviewResponse.setId(orderItem.getId());
            orderItemPreviewResponse.setQuantity(orderItem.getQuantity());
            orderItemPreviewResponse.setPrice(orderItem.getFinalPrice());
            OrderItemPreviewResponse.BookResponse bookResponse = new OrderItemPreviewResponse.BookResponse(
                    orderItem.getBook().getId(),
                    orderItem.getBook().getTitle(),
                    orderItem.getBook().getImageURL(),
                    orderItem.getBook().getFinalPrice());
            orderItemPreviewResponse.setBook(bookResponse);
            orderItemPreviewResponses.add(orderItemPreviewResponse);
        }
        return orderItemPreviewResponses;
    }

    public List<OrderItemFoundResponse> convertToOrderItemFoundResponse(List<OrderItem> orderItems) {
        List<OrderItemFoundResponse> orderItemFoundResponses = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemFoundResponse orderItemFoundResponse = new OrderItemFoundResponse();
            orderItemFoundResponse.setId(orderItem.getId());
            orderItemFoundResponse.setQuantity(orderItem.getQuantity());
            orderItemFoundResponse.setPrice(orderItem.getFinalPrice());
            OrderItemFoundResponse.BookResponse bookResponse = new OrderItemFoundResponse.BookResponse(
                    orderItem.getBook().getId(),
                    orderItem.getBook().getTitle(),
                    orderItem.getBook().getImageURL(),
                    orderItem.getBook().getOriginalPrice(),
                    orderItem.getBook().getOriginalPrice() - orderItem.getBook().getFinalPrice(),
                    orderItem.getBook().getFinalPrice());
            orderItemFoundResponse.setBook(bookResponse);
            orderItemFoundResponses.add(orderItemFoundResponse);
        }
        return orderItemFoundResponses;
    }
}
