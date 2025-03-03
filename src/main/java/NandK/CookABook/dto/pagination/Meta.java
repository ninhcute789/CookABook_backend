package NandK.CookABook.dto.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page;
    private int pageSize;
    private int totalPage;
    private long totalElement;
}
