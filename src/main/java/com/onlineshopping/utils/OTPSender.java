package com.onlineshopping.utils;
import java.util.Random;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import io.github.cdimascio.dotenv.Dotenv;

// import com.twilio.Twilio;
// import com.twilio.rest.api.v2010.account.Message;



public class OTPSender{
    // public static final Logger logger = LoggerFactory.getLogger(OTPSender.class);
    // public static final Dotenv dotenv = DBUtils.dotenv;
    // public static final String ACCOUNT_SID = dotenv.get("TWILIO_ACCOUNT_SID");
    // public static final String AUTH_TOKEN = dotenv.get("TWILIO_AUTH_TOKEN");
    // Method for sending Otp to user via Twilio api
    public static void sendOTPToUser(String mobile, String otp) {
    // try {
    //     Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    //     Message message = Message.creator(
    //       new com.twilio.type.PhoneNumber("+91"+mobile),
    //       new com.twilio.type.PhoneNumber("+13203639924"),
    //       "OTP received: " + otp)
    //     .create();
    //     logger.info("OTP sent to the User's registerd Mobile number: {}", mobile);
    // } catch (Exception e) {
    //     logger.error("error sending OTP to the User's mobile", e.getMessage());
    //     e.printStackTrace();
    // }
    }
    
    public static String generateOTP() {
        Random rand = new Random();
        int otp = 1000 + rand.nextInt(9000);
        return String.valueOf(otp);
    }
}

