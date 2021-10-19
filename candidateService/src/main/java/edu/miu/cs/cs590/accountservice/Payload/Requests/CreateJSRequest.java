package edu.miu.cs.cs590.accountservice.Payload.Requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateJSRequest {

    @NotNull
    private Long user_id;

    @NotBlank
    @Size(min = 4, max = 40)
    private String currentPosition;

    @NotBlank
    @Size(min = 3, max = 100)
    private String bio;
}
