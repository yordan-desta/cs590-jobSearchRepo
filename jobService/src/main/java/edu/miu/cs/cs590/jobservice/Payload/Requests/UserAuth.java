package edu.miu.cs.cs590.jobservice.Payload.Requests;


import lombok.Data;

import java.util.List;


@Data
public class UserAuth {
    private Long id;
    private String name;
    private String username;
    private String email;
    private List<Authties> authorities;
}
