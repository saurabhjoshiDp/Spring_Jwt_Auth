package com.auth.Controller;

import com.auth.Model.AuthenticationRequestModel;
import com.auth.Model.AuthenticationResponseModel;
import com.auth.Service.UserDetailServiceImpl;
import com.auth.Utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequestModel authenticationRequest){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),authenticationRequest.getPassword())
            );
        }catch (DisabledException e){
            throw new DisabledException("User Disabled");
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Bad Credentials");
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUserName());
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponseModel(token));
    }
}
