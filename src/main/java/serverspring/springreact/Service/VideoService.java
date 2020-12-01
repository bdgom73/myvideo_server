package serverspring.springreact.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import serverspring.springreact.Data.VideoDto;
import serverspring.springreact.Entity.Member;
import serverspring.springreact.Entity.Video;
import serverspring.springreact.Repository.MemberRepository;
import serverspring.springreact.Repository.VideoRepository;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private VideoRepository videoRepository;

    String baseFileUrl = "C:\\Users\\bdgom\\OneDrive\\desktop\\hello-spring\\spring-react\\src\\main\\resources\\static\\uploads\\videos";
    public void uploadVideo(String title, String desc,MultipartFile video,String token){
        try{
            Long userId = jwtTokenProvider.getUserId(token);
            System.out.println("userId = " + userId);
            Optional<Member> user = memberRepository.findById(userId);
            System.out.println("user = " + user);
            String FileUrl = baseFileUrl + "\\"+ video.getOriginalFilename();
            System.out.println("FileUrl = " + FileUrl);
            System.out.println("orginname : "+video.getOriginalFilename());
            if(user.isPresent()){
                video.transferTo(new File(FileUrl));
                Video video1 = new Video(title, desc, FileUrl, user.get());
                video1.authorSettings(user.get());
                videoRepository.save(video1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
