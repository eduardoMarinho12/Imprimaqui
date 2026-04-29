package springproject.imprimaqui.dto.auth;

import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegisterDTO {

    @NotBlank
    private String name;

    @NotNull
    @Min(0)
    private Integer age;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;


}
