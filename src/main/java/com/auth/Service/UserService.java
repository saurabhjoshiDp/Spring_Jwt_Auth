package com.auth.Service;

import com.auth.Entity.User;
import com.auth.Model.UserModel;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User registerUser(UserModel userModel);
}
