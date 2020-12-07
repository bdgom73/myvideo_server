package serverspring.springreact.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverspring.springreact.Data.UserPageDTO;
import serverspring.springreact.Data.UserPageVideoDTO;
import serverspring.springreact.Data.VideoDto;
import serverspring.springreact.Entity.Member;
import serverspring.springreact.Entity.Video;
import serverspring.springreact.Repository.MemberRepository;
import serverspring.springreact.Repository.VideoRepository;
import serverspring.springreact.Service.JwtTokenProvider;
import serverspring.springreact.Service.UserService;
import serverspring.springreact.Service.VideoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoRepository videoRepository;

    // USER JOIN
    @PostMapping("/user/signup")
    public Boolean signup(@RequestBody Map<String,String> userinfo){
        System.out.println( userinfo.get("password1").equals(userinfo.get("password2")));
        return userService.userSignUp(userinfo);
    }

    @PostMapping("/user/login")
    public String login(
            @RequestBody Map<String,String> userinfo,
            HttpServletResponse response
    )
    {
        String token = userService.userLogin(userinfo);
        if(token == null){
            response.setStatus(400);
        }
        response.setStatus(200);
        return token;

    }

    @PostMapping("/user/passwordChange")
    public void passwordChange(
            @RequestBody Map<String,String> password,
            @RequestHeader(value = "Cookies") String token,
            HttpServletResponse response
    ) {
        try {
            userService.userPasswordChange(password, token);
            response.setStatus(200);
        } catch (Exception e) {
            response.setStatus(400);
        }
    }
    @PostMapping("/user/emailCheck")
    public Boolean emailRedundancyCheck(@RequestBody Map<String,String> user){
        String email = user.get("email");
        return userService.userSignUpEmailValidation(email);
    }

    @PostMapping("/user/changeInfo")
    public void changeInfo(
            @RequestParam(value = "avatar") MultipartFile avatar,
            @RequestHeader(value = "Cookies") String token,
            HttpServletResponse response
    ){
        userService.changeMe(avatar,token);
        response.setStatus(200);
    }

    @PostMapping("/user/changeProfile")
    public void changeProfile(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "nickname") String nickname,
            @RequestHeader(value = "Cookies") String token,
            HttpServletResponse response
    ){
        try{
            userService.changeProfile(name,nickname,token);
            response.setStatus(200);
        }catch (Exception e){
            System.out.println("e = " + e);
            response.setStatus(400);
        }
    }

    // USER CHECK
    @GetMapping("/user/send/id")
    public Long sendId(
            @RequestHeader(value = "Cookies") String token,
            HttpServletResponse response
    ){
        Long userId = jwtTokenProvider.getUserId(token);
        if(userId == null){
            response.setStatus(400);
        }
        response.setStatus(200);
        return userId;
    }
    @PostMapping("/user/send/videoUserid/{id}")
    public Long sendInfo(
            @RequestParam("id") String id,
            HttpServletResponse response
    ){
        Optional<Video> video = videoRepository.findById(Long.parseLong(id));
        if(!video.isPresent()){
            response.setStatus(400);
        }
        response.setStatus(200);
        VideoDto videoDto = new VideoDto(video.get().getMember().getId());
        return videoDto.getId();
    }

    @PostMapping("/user/check/{id}")
    public Boolean userCheck(
            @RequestParam(value = "id") String id,
            @RequestHeader("Cookies") String token
    ){
        try{
            Optional<Video> video = videoRepository.findById(Long.parseLong(id));
            Long userId = jwtTokenProvider.getUserId(token);
            Optional<Member> user = memberRepository.findById(userId);
            Long id1 = video.get().getMember().getId();
            Long id2 = user.get().getId();
            if(id1.equals(id2)){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    // USER UPLOADS
    @PostMapping("/video/upload")
    public String videoUpload(
            @RequestParam("file") MultipartFile video,
            @RequestParam("title") String title,
            @RequestParam("desc") String desc,
            @RequestHeader(value = "Cookies") String token,
            HttpServletResponse response
    ) throws IOException {

        if(!jwtTokenProvider.validateToken(token)){
            response.setStatus(400);
        }
        response.setStatus(200);
        return videoService.uploadVideo(title,desc,video,token);
    }


    // USER PAGE
    @GetMapping("/me")
    public UserPageDTO me(
            @RequestHeader("Cookies") String cookie
    ){
        Long userId = jwtTokenProvider.getUserId(cookie);
        Optional<Member> me = memberRepository.findById(userId);
        List<Video> videos = me.get().getVideos();

        List<UserPageVideoDTO> pageVideo = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            pageVideo.add( new UserPageVideoDTO(
                    videos.get(i).getId(),
                    videos.get(i).getTitle(),
                    videos.get(i).getDesc(),
                    videos.get(i).getVideoUrl()
            ));
        }

        Optional<UserPageDTO> userPageDTO = me.map(member -> new UserPageDTO(
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getAvatar_url(),
                pageVideo
        ));

        return userPageDTO.get();
    }
}
