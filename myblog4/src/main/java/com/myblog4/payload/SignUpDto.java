package com.myblog4.payload;

import lombok.Data;

@Data

public class SignUpDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private String user_roll;
}
