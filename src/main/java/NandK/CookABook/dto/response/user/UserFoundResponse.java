package NandK.CookABook.dto.response.user;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // tao constructor co tham so
@NoArgsConstructor // tao constructor khong tham so
public class UserFoundResponse {
    private Long id;
    private String username;
    private String name;
    private GenderEnum gender;
    private LocalDate dob;
    private String email;
    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
}
