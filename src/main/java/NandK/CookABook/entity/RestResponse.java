package NandK.CookABook.entity;

import lombok.Data;

@Data
public class RestResponse<T> {
    private int status;
    private String error;
    // message co the la string hoac object
    private Object message;
    private T data;
}
