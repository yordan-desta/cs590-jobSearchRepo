package edu.miu.cs.cs590.jobservice.Payload.Requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CompanyRequestModel{
    @NotBlank
    @Size(min = 4, max = 40)
    private String street;
    @NotBlank
    @Size(min = 4, max = 40)
    private String city;
    @NotBlank
    @Size(min = 4, max = 40)
    private String state;
    @NotBlank
    @Size(min = 4, max = 10)
    private Long zipcode;

    @NotBlank
    private Long userId;

}
