package serverspring.springreact.Data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import serverspring.springreact.Entity.Video;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class UserPageDTO {

    private String email;
    private String name;
    private String nickname;
    private String avatar_url;

    List<UserPageVideoDTO> videos ;

    public  UserPageDTO(String email,String name,String nickname,String avatar_url,List<UserPageVideoDTO> videos){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.avatar_url = avatar_url;
        this.videos = videos;
    }

}
