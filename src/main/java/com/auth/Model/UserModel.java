package com.auth.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;

}
