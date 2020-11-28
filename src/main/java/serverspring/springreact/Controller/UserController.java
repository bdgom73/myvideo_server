package serverspring.springreact.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverspring.springreact.Data.UserInfoTransferOnly;
import serverspring.springreact.Service.JwtTokenProvider;
import serverspring.springreact.Service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/user/signup")
    public Boolean signup(@RequestBody Map<String,String> userinfo){
        System.out.println( userinfo.get("password1").equals(userinfo.get("password2")));
        return userService.userSignUp(userinfo);
    }
    @PostMapping("/user/login")
    public String login(@RequestBody Map<String,String> userinfo){
       return userService.userLogin(userinfo);
    }
    @PostMapping("/user/passwordChange")
    public void passwordChange(@RequestBody Map<String,String> password, @RequestHeader(value = "AUTHENTICATE") String token){
        userService.userPasswordChange(password,token);
    }

    @PostMapping("/user/emailCheck")
    public Boolean emailRedundancyCheck(@RequestBody Map<String,String> user){
        String email = user.get("email");
        return userService.userSignUpEmailValidation(email);
    }




}
