package com.codehows.taelim.dto;

import com.codehows.taelim.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;
    private String email;
    private String name;
    private String role;
    private String id;

    public LoginResponse(String accessToken, String email, String name, String role, String id) {
        this.accessToken = accessToken;
        this.email = email;
        this.name = name;
        this.role = role;
        this.id = id;
    }
}
