package edu.miu.cs.cs590.accountservice.Payload.Response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserSummaryResponse {
    private Long id;
    private String username;
    private String name;

    public UserSummaryResponse(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

}
