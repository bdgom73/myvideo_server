package serverspring.springreact.Service;

import net.bytebuddy.utility.RandomString;
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
import java.util.*;

@Service
public class VideoService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private VideoRepository videoRepository;

    String baseFileUrl = "http://localhost:8080/uploads/videos/";
    String absoluteUrl = "C:\\Users\\bdgom\\OneDrive\\desktop\\hello-spring\\spring-react\\src\\main\\resources\\static\\uploads\\videos";
    public String uploadVideo(String title, String desc,MultipartFile video,String token){
        try{
            String ext = video.getOriginalFilename().substring(video.getOriginalFilename().lastIndexOf("."));
            String randomStr = this.randomString()+ ext;
            Long userId = jwtTokenProvider.getUserId(token);
            Optional<Member> user = memberRepository.findById(userId);
            String FileUrl = absoluteUrl+"/"+randomStr;
            String relativeUrl = baseFileUrl + randomStr;
            if(user.isPresent()){
                video.transferTo(new File(FileUrl));
                Video video1 = new Video(title, desc, relativeUrl, user.get());
                video1.authorSettings(user.get());
                Video save = videoRepository.save(video1);
                System.out.println("save = " + save);
                return save.getId().toString();
            }else {
                return "fail";
            }
        }catch (Exception error){
            System.out.println("error = " + error);
            return "error";
        }
    }

    public Map<String,String> videoDetail(String id) throws Exception {
        try {
            Long xid = Long.parseLong(id);
            Video video = videoRepository.findById(xid).get();
            Map<String,String> sendVideo = new HashMap<>();
            sendVideo.put("id",video.getId().toString());
            sendVideo.put("title",video.getTitle());
            sendVideo.put("desc",video.getDesc());
            sendVideo.put("date",video.getDate().toString());
            sendVideo.put("videoUrl",video.getVideoUrl());
            sendVideo.put("name",video.getMember().getName());
            return sendVideo;
        }catch (Exception e){
            throw new Exception("Failed to load video.");
        }
    }

    public String randomString(){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 40; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }
}
