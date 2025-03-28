package NandK.CookABook.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPagination {
    private Meta meta;
    private Object data;

    @Getter
    @Setter
    public static class Meta {
        private int page;
        private int size;
        private int totalPages;
        private long totalElements;
    }
}
