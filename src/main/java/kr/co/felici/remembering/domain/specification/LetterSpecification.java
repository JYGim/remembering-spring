package kr.co.felici.remembering.domain.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.domain.User;
import kr.co.felici.remembering.dto.AddUserDto;
import kr.co.felici.remembering.dto.LetterDto;
import kr.co.felici.remembering.dto.UserDto;
import kr.co.felici.remembering.repository.UserRepository;
import kr.co.felici.remembering.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * author: felici
 */
@Slf4j
@RequiredArgsConstructor
public class LetterSpecification {

    private final UserRepository userRepository;
    private final UserDetailService userDetailService;

    public static Specification<Letter> likeUserEmail(String userEmail) {
        log.info("log, userEmail: " + userEmail);

//        return (root, query, builder) -> {
//            return builder.equal(root.get(writer), writer);
//        };



        return new Specification<Letter>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Letter> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);

                return criteriaBuilder.equal(root.get("user"), "%" + userEmail + "%");
            }
        };
    }

    public static Specification<Letter> likeContents(String contents) {
        log.info("log, contents: " + contents);

//        return (root, query, builder) -> {
//
//            return builder.like(root.get("contents"), "%" + contents + "%");
//        };

        return new Specification<Letter>() {
            @Override
            public Predicate toPredicate(Root<Letter> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);

                return criteriaBuilder.like(root.get("contents"), "%" + contents + "%");
            }
        };
    }








}
