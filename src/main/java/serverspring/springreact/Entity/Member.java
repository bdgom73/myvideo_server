package serverspring.springreact.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"email","password","name","avatar_url"})
public class Member {

    @Id
    @GeneratedValue
    @Column(name ="member_id")
    private Long id;

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String avatar_url;

    @OneToMany(mappedBy = "member")
    List<Video> videos = new ArrayList<>();

    public Member(String email,String password,String name,String nickname,String avatar_url){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.avatar_url = avatar_url;
    }
    public Member(String email,String password,String name,String nickname){
        this(email, password, name, nickname,"");
    }


}
