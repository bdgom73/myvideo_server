package serverspring.springreact.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@ToString(of = {"title","desc","videoUrl"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {

    @Id
    @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    private String title;
    private String desc;
    private Date date = new Date();
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Video(String title, String desc, String videoUrl) {
        this.title = title;
        this.desc = desc;
        this.videoUrl = videoUrl;
    }

    public void authorSettings(Member member){
        this.member = member;
        member.getVideos().add(this);
    }

    public Video(String title, String desc,String videoUrl,Member member){
        this.title = title;
        this.desc = desc;
        this.videoUrl = videoUrl;
        this.member = member;
    }
}
