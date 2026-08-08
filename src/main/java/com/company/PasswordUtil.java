package com.company;

import org.springframework.security.crypto.bcrypt.BCrypt;
public class PasswordUtil {
    public static String hashpassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());

    }
    public static boolean verifyPassword(String password,String hashedPassword){
        return BCrypt.checkpw(password,hashedPassword);
    }
}
