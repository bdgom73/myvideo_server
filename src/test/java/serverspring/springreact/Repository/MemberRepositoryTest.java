package serverspring.springreact.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import serverspring.springreact.Data.UserInfoTransferOnly;
import serverspring.springreact.Entity.Member;
import serverspring.springreact.Service.JwtTokenProvider;
import serverspring.springreact.Service.UserService;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;
    @Test
    void test(){
        Map<String,String > info = new HashMap<>();
        info.put("email","ba@mav.com");
        info.put("password1","1234");
        info.put("password2","1234");
        info.put("name","zz");
        info.put("avatar_url","1111111");

        userService.userSignUp(info);

        Map<String,String > user = new HashMap<>();
        user.put("email","ba@mav.com");
        user.put("password","1234");
        String s = userService.userLogin(user);
        System.out.println(s);

        UserInfoTransferOnly token = jwtTokenProvider.getToken(s);
        System.out.println("token = " + token);
        UserInfoTransferOnly userInfoTransferOnly = jwtTokenProvider.tokenAuthenticationValueDelivery(1L);
        System.out.println("userInfoTransferOnly = " + userInfoTransferOnly);


    }
}