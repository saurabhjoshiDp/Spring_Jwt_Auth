package com.auth.Service;

import com.auth.Entity.User;
import com.auth.Entity.VerificationToken;
import com.auth.Model.UserModel;
import com.auth.Repository.UserRepository;
import com.auth.Repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void saveVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken==null)
            return "Invalid";
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <=0){
            // token expried
            verificationTokenRepository.delete(verificationToken);
            return "Invalid_Expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Valid";
    }

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setUserName(userModel.getUserName());
        user.setRole("ROLE_USER");
        String encodedPassword = passwordEncoder.encode(userModel.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return user;
    }
}
