package serverspring.springreact.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import serverspring.springreact.Data.VideoDto;
import serverspring.springreact.Entity.Video;
import serverspring.springreact.Repository.MemberRepository;
import serverspring.springreact.Repository.VideoRepository;
import serverspring.springreact.Service.JwtTokenProvider;
import serverspring.springreact.Service.UserService;
import serverspring.springreact.Service.VideoService;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class VideoController {

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

    @PostMapping("/video/detail/{id}")
    public Map<String,String> sendVideoDetail(@RequestParam(value = "id") String id) throws Exception {
       return videoService.videoDetail(id);
    }

    @GetMapping("/videos/all")
    public Page<VideoDto> sendVideoHome() throws Exception{
        PageRequest pageRequest = PageRequest.of(0,20);
        Page<Video> videos = videoRepository.findAll(pageRequest);
        Page<VideoDto> videoDtos = videos.map(video -> new VideoDto(video.getId(),video.getTitle(),video.getDesc(),video.getVideoUrl(),video.getDate(),video.getMember()));
        return videoDtos;
    }

    @PostMapping("/videos/update")
    public void updateVideo(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "desc") String desc,
            HttpServletResponse response
    ) throws Exception {
        try{
            Optional<Video> video = videoRepository.findById(Long.parseLong(id));
            if(video.isPresent()){
                video.get().setTitle(title);
                video.get().setDesc(desc);
                videoRepository.save(video.get());
                response.setStatus(200);
            }else{
                response.setStatus(400);
            }
        }catch (Exception e){
            response.setStatus(400);
            throw new Exception("update Fail");
        }
    }

    @PostMapping("/video/delete/{id}")
    public void deleteVideo(
            @RequestParam("id") String id,
            HttpServletResponse response
    ){
        try {
            Optional<Video> video = videoRepository.findById(Long.parseLong(id));
            videoRepository.delete(video.get());
            response.setStatus(200);
        }catch (Exception e){
            response.setStatus(400);
        }
    }
    @PostMapping("/video/search/{query}")
    public Slice<VideoDto> searchVideo(
            @RequestParam("query") String query
    ){
        Slice<Video> video = videoRepository.findByTitleContaining(query);
        Slice<VideoDto> map = video.map(m -> new VideoDto(m.getId(), m.getTitle(), m.getDesc(), m.getVideoUrl(), m.getDate(), m.getMember()));
        return map;
    }

}
