package edu.miu.cs.cs590.accountservice.Models;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor

public class User  {
    private Long id;

    private String name;
    private String username;
    private String email;
    private Set<Role> roles = new HashSet<>();


    public User(String name, String username, String email, Set<Role> roles) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
