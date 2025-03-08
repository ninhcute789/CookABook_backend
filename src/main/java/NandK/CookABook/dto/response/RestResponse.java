package NandK.CookABook.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int status;
    private String error;
    // message co the la string hoac object
    private Object message;
    private T data;
}
