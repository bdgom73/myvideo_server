package serverspring.springreact.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import serverspring.springreact.Data.UserInfoTransferOnly;
import serverspring.springreact.Entity.Member;
import serverspring.springreact.Repository.MemberRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtTokenProvider {

    @Autowired
    private MemberRepository memberRepository;

    private String secretKey = "EV3oLcKbBU1kJmV14932H7zoh83Uzcu4";

    private long tokenValidTime = 24 * 60 * 60 * 1000L;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(Long id) {
        String xid = id.toString();
        Claims claims = Jwts.claims().setSubject(xid); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    // 토큰에서 회원 정보 추출
    public Long getUserId(String token) {
        String subject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return Long.parseLong(subject);
    }

    // 회원아이디를 정보로 변경
    public UserInfoTransferOnly tokenAuthenticationValueDelivery(Long id){
        Optional<Member> user = memberRepository.findById(id);
        UserInfoTransferOnly userInfoTransferOnly = new UserInfoTransferOnly(
                user.get().getId(),
                user.get().getEmail(),
                user.get().getName(),
                user.get().getAvatar_url()
        );
        return userInfoTransferOnly;
    }


    // 정해진 정보 전달
    public UserInfoTransferOnly getToken(String token){
        Long userId = this.getUserId(token);
        return this.tokenAuthenticationValueDelivery(userId);
    }
    public boolean validateToken(String jwtToken){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }
}

