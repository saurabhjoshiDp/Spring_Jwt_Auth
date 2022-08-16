package com.auth.Controller;

import com.auth.Entity.User;
import com.auth.Event.RegistrationCompleteEvent;
import com.auth.Model.UserModel;
import com.auth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String Register(@RequestBody UserModel userModel, final HttpServletRequest request){
        User user = userService.registerUser(userModel);

        String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        publisher.publishEvent(new RegistrationCompleteEvent(user,url));
        return "User Successfully Registered";
    }

    @GetMapping("register/verify")
    public String VerifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "user verified Successfully";
        }
        return "Bad User";
    }




}
