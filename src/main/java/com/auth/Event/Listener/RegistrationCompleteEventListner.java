package com.auth.Event.Listener;

import com.auth.Entity.User;
import com.auth.Event.RegistrationCompleteEvent;
import com.auth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationCompleteEventListner implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for User with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationToken(token,user);

        // Send mail to user
//        sendVerificationEmail()
        String url = event.getApplicationUrl() + "/register/verify?token=" +token;
        System.out.println("click to verify " + url);
    }
}
