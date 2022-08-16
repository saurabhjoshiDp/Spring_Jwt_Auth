package com.auth.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {
    //exiration time as 10 min
    private static final int TTL = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false,foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(User user, String token){
        this.token = token;
        this.user = user;
        this.expirationTime = calExpiryDate();
    }

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calExpiryDate();
    }

    private Date calExpiryDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,TTL);
        return  new Date(calendar.getTime().getTime());
    }
}
