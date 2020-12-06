package serverspring.springreact.Data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import serverspring.springreact.Entity.Video;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter @Setter
public class UserInfoTransferOnly {

    private Long id;
    private String email;
    private String name;
    private String avatar_url;

    public UserInfoTransferOnly(Long id,String email, String name, String avatar_url){
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar_url = avatar_url;
    }

}
