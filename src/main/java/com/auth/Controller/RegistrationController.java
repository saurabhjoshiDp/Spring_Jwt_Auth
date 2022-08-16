package com.auth.Controller;

import com.auth.Entity.User;
import com.auth.Model.UserModel;
import com.auth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String Register(@RequestBody UserModel userModel){
        User user = userService.registerUser(userModel);
        return "User Successfully Registered";
    }

}
