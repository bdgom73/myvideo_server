package serverspring.springreact.Data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter @Setter
public class UserPageVideoDTO {

    private Long id;
    private String title;
    private String desc;
    private Date date = new Date();
    private String videoUrl;



    public UserPageVideoDTO(Long id,String title,String desc,String videoUrl){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.videoUrl = videoUrl;
    }
}
