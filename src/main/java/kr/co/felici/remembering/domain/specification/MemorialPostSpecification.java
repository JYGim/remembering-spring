package kr.co.felici.remembering.domain.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.dto.MemorialPostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class MemorialPostSpecification {

    public static Specification<MemorialPost> equalsWriter(String writer) {

        return new Specification<MemorialPost>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<MemorialPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                return criteriaBuilder.equal(root.get("writer"), writer);
            }
        };
    }

    public static Specification<MemorialPost> likeContents(String contents) {


        return new Specification<MemorialPost>() {
            @Override
            public Predicate toPredicate(Root<MemorialPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);

                return criteriaBuilder.like(root.get("contents"), "%" + contents + "%");
            }
        };
    }








}
