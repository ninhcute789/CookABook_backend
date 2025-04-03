package NandK.CookABook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.response.order.OrderItemResponse;
import NandK.CookABook.entity.OrderItem;

@Service
public class OrderItemService {

    public List<OrderItemResponse> convertToOrderItemResponse(List<OrderItem> orderItems) {
        List<OrderItemResponse> orderItemPreviewResponses = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setId(orderItem.getId());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponse.setPrice(orderItem.getFinalPrice());
            OrderItemResponse.BookResponse bookResponse = new OrderItemResponse.BookResponse();
            bookResponse.setBookId(orderItem.getBook().getId());
            bookResponse.setBookTitle(orderItem.getBook().getTitle());
            bookResponse.setBookImageURL(orderItem.getBook().getImageURL());
            bookResponse.setBookFinalPrice(orderItem.getBook().getFinalPrice());
            orderItemResponse.setBook(bookResponse);
            orderItemPreviewResponses.add(orderItemResponse);
        }
        return orderItemPreviewResponses;
    }
}
