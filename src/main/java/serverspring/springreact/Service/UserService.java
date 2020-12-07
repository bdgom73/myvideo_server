package serverspring.springreact.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import serverspring.springreact.Entity.Member;
import serverspring.springreact.Entity.Video;
import serverspring.springreact.Repository.MemberRepository;

import java.io.File;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private VideoService videoService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    String baseFileUrl = "http://localhost:8080/uploads/avatars/";
    String absoluteUrl = "C:\\Users\\bdgom\\OneDrive\\desktop\\hello-spring\\spring-react\\src\\main\\resources\\static\\uploads\\avatars";
    public void changeMe(MultipartFile avatar, String token){
        try{
            String ext = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf("."));
            String randomStr = videoService.randomString()+ ext;
            Long userId = jwtTokenProvider.getUserId(token);
            Optional<Member> user = memberRepository.findById(userId);
            String FileUrl = absoluteUrl+"/"+randomStr;
            String relativeUrl = baseFileUrl + randomStr;
            if(user.isPresent()){
                avatar.transferTo(new File(FileUrl));
                user.get().setAvatar_url(relativeUrl);
                memberRepository.save(user.get());
            }
        }catch (Exception error){
            System.out.println("error = " + error);
        }
    }

    public void changeProfile(String name,String nickname,String token){
        Long userId = jwtTokenProvider.getUserId(token);
        Optional<Member> user = memberRepository.findById(userId);
        if(user.isPresent()){
            user.get().setName(name);
            user.get().setNickname(nickname);
            memberRepository.save(user.get());
        }else{
            new Exception("null..");
        }
    }

    private Boolean userSignUpPasswordValidation(String password1 , String password2){
        if(password1.equals(password2)){
            return true;
        }else{
            return false;
        }
    }

    private Boolean userSignUpPasswordRegex(String password){
        if (password.length() < 9 ) {
            return false;
        }
        return true;
    }

    public Boolean userSignUpEmailValidation(String email){
        return memberRepository.findByEmail(email).isPresent(); // False일경우 사용가능
    }



    public Boolean userSignUp(Map<String,String> user){

        String email = user.get("email");
        String password1 = user.get("password1");
        String password2 = user.get("password2");
        String name = user.get("name");
        String nickname = user.get("nickname");

        if(userSignUpPasswordValidation(password1,password2) && password1 != null){
            if(userSignUpPasswordRegex(password1)){
                String password = bCryptPasswordEncoder.encode(password1);
                Member member = new Member(email,password,name,nickname);
                memberRepository.save(member);
                return true;
            }
        }
        return false;
    }

    public String userLogin(Map<String,String> user){
        String email = user.get("email");
        String password = user.get("password");
        Optional<Member> my = memberRepository.findByEmail(email);
        if (my.isPresent()){
            if (bCryptPasswordEncoder.matches(password,my.get().getPassword())) {
                return jwtTokenProvider.createToken(my.get().getId());
            }
        }

        return null;
    }

    public void userPasswordChange(Map<String,String> password,String token) throws Exception {
        Long id = jwtTokenProvider.getUserId(token);
        String prePassword = password.get("previousPassword");
        String newPassword1 = password.get("newPassword1");
        String newPassword2 = password.get("newPassword2");

        Optional<Member> user = memberRepository.findById(id);

        if (bCryptPasswordEncoder.matches(prePassword,user.get().getPassword())) {
            if (userSignUpPasswordValidation(newPassword1,newPassword2)) {
                String newPassword = bCryptPasswordEncoder.encode(newPassword1);
                user.get().setPassword(newPassword);
                memberRepository.save(user.get());
            }else{
                throw new Exception("The two passwords are different.");
            }
        }else{
            throw new Exception("The password is incorrect.");
        }

    }
}
