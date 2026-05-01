package springproject.imprimaqui.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFormDTO {

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
