package edu.miu.cs.cs590.jobservice.Payload.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeVacancyStatusRequest {
    @NotBlank
    @Pattern(regexp = "Published|Canceled", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String status;

}
