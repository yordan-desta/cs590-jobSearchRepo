package edu.miu.cs.cs590.jobservice.Payload.Requests;

import edu.miu.cs.cs590.jobservice.Models.enums.RoleName;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 20)
    private String password;

    @NotNull
    //@Pattern(regexp = "ROLE_PROVIDER|ROLE_ADMIN", flags = Pattern.Flag.CASE_INSENSITIVE)
    private RoleName role;
}
