package serverspring.springreact.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Rollback;
import serverspring.springreact.Data.UserInfoTransferOnly;
import serverspring.springreact.Entity.Member;
import serverspring.springreact.Entity.Video;
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
    VideoRepository videoRepository;

    @Test
    void test(){
        Video video = new Video("Title1","DESC1","/1");
        Video video2 = new Video("Title2","DESC2","/2");
        Video video3 = new Video("Title3","DESC3","/3");
        Video video4 = new Video("Title4","DESC4","/4");

        videoRepository.save(video);
        videoRepository.save(video2);
        videoRepository.save(video3);
        videoRepository.save(video4);


        Slice<Video> allVideo = videoRepository.findAll(PageRequest.of(1, 2));

        System.out.println("allVideo = " + allVideo.getContent());
    }
}