package com.bombanya.web4.security;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class NewUser {
    @NotBlank
    @Size(max = 50)
    String username;

    @NotBlank
    @Size(max = 100)
    String password;
}
