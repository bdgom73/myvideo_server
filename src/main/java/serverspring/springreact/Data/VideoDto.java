package serverspring.springreact.Data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import serverspring.springreact.Entity.Member;
import serverspring.springreact.Entity.Video;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Getter @Setter
public class VideoDto{

    private String title;
    private String desc;
    private Date date = new Date();
    private String videoUrl;
    private Member member;

    public VideoDto(String title, String desc, String videoUrl,Member member){
        this.title = title;
        this.desc = desc;
        this.videoUrl = videoUrl;
        this.member = member;
    }

}
