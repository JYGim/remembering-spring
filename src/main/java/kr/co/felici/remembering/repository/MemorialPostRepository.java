package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.dto.MemorialPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MemorialPostRepository
        extends JpaRepository<MemorialPost, Long>, JpaSpecificationExecutor<MemorialPost> {

    Page<MemorialPost> findAll(Specification<MemorialPost> spec, Pageable pageable);
    Page<MemorialPost> findAll(Pageable pageable);

    List<MemorialPost> findAll();

}
