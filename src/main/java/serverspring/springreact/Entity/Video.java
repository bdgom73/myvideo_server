package serverspring.springreact.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {

    @Id
    @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    private String title;
    private String desc;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void authorSettings(Member member){
        this.member = member;
        member.getVideos().add(this);
    }
}
