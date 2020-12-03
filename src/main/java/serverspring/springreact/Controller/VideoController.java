package serverspring.springreact.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import serverspring.springreact.Data.VideoDto;
import serverspring.springreact.Entity.Video;
import serverspring.springreact.Repository.MemberRepository;
import serverspring.springreact.Repository.VideoRepository;
import serverspring.springreact.Service.JwtTokenProvider;
import serverspring.springreact.Service.UserService;
import serverspring.springreact.Service.VideoService;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
