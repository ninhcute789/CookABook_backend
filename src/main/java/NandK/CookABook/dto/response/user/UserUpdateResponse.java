package NandK.CookABook.dto.response.user;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateResponse {
    private Long id;
    private String name;
    private GenderEnum gender;
    private LocalDate dob;
    private String email;
    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
}
