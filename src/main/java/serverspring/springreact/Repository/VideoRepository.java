package serverspring.springreact.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serverspring.springreact.Data.VideoDto;
import serverspring.springreact.Entity.Video;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByTitleLike(String query);

}
