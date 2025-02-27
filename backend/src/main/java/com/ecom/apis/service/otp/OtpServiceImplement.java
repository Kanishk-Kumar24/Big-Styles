package com.ecom.apis.service.otp;

import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.entity.UserOtp;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import com.ecom.apis.model.ForgetPass;
import com.ecom.apis.model.OTP;
import com.ecom.apis.repository.UserOtpRepository;
import com.ecom.apis.service.Mailer;
import com.ecom.apis.service.user.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class OtpServiceImplement implements OtpServiceInterface {
    @Autowired
    private UserOtpRepository userOtpRepository;
    @Autowired
    private Mailer mailer;
    @Autowired
    private UserServiceImplement userService;



    @Override
    public String sendOtp(String email) {
        if(email==null) return "email is compulsory";
        String otp =otpGenerator();
        UserOtp user = userOtpRepository.findUserOtpByEmail(email);
        if(Objects.isNull(user))
            user = UserOtp.builder().code(otp).email(email).build();
        else
            user.setCode(otp);
        user.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60));
        userOtpRepository.save(user);
        mailer.sendMail(email,otp);
        return "success";
    }
    private String otpGenerator(){
        String numbers = "0123456789";
        Random rndm_method = new Random();
        char[] otp = new char[6];
        for (int i = 0; i < 6; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    @Override
    public String verify(OTP otp) throws NotFoundException {
        UserOtp userOtp = userOtpRepository.findUserOtpByEmail(otp.getEmail());
        if (userOtp==null) return "otp not sent, please send again for email "+otp.getEmail();
        if(otp.getOtp().equals(userOtp.getCode())) {
            if(!userOtp.getExpiration().before(new Date())) {
                UserEntity user = userService.getUserByEmail(otp.getEmail());
                user.setVerified(1);
                return userService.saveUser(user);
            }else return "Otp expired "+otp.getOtp();
        }else return "wrong otp "+otp.getOtp();
    }

    @Override
    public String forgetPass(ForgetPass otp) {
        if (otp!=null && otp.getEmail()!=null && otp.getOtp()!=null && otp.getPassword()!=null ){
            UserOtp userOtpByEmail = userOtpRepository.findUserOtpByEmail(otp.getEmail());
            if(Objects.nonNull(userOtpByEmail))
                if (userOtpByEmail.getCode().equals(otp.getOtp()))
                    if (!userOtpByEmail.getExpiration().before(new Date()))
                        return userService.updatePass(otp.getPassword(), otp.getEmail());
                    else return "Otp expired";
                else return "wrong otp";
            else return "No such User: "+otp.getEmail();
        }else return "check_details";
    }
}
