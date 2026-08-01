package com.company;

import java.util.Random;

public class otpGenerator {
    public  static int generateOTP(){
        Random random =new Random();
        return  100000 + random.nextInt(900000);
    }
}
