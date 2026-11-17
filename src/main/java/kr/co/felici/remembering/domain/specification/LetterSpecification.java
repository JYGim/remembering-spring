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

@Slf4j
@RequiredArgsConstructor
public class LetterSpecification {

    private final UserRepository userRepository;
    private final UserDetailService userDetailService;

    public static Specification<Letter> likeUserEmail(String userEmail) {




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


        return new Specification<Letter>() {
            @Override
            public Predicate toPredicate(Root<Letter> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);

                return criteriaBuilder.like(root.get("contents"), "%" + contents + "%");
            }
        };
    }








}
