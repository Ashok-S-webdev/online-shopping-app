package com.example.utils;
import java.util.Random;

import io.github.cdimascio.dotenv.Dotenv;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;



public class OTPSender{
    public static final Dotenv dotenv = DBUtils.dotenv;
    public static final String ACCOUNT_SID = dotenv.get("TWILIO_SID");
    public static final String AUTH_TOKEN = dotenv.get("TWILIO_AUTH_TOKEN");
    public static void sendOTPToUser(String mobile, String otp) {
    try {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
          new com.twilio.type.PhoneNumber("+91"+mobile),
          new com.twilio.type.PhoneNumber("+13203639924"),
          "OTP received: " + otp)
        .create();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    
    public static String generateOTP() {
        Random rand = new Random();
        int otp = 1000 + rand.nextInt(9000);
        return String.valueOf(otp);
    }
}

