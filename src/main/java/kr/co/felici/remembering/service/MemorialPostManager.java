package kr.co.felici.remembering.service;

import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.dto.MemorialPostDto;
import kr.co.felici.remembering.repository.MemorialPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: felici
 */
@Service
@RequiredArgsConstructor
public class MemorialPostManager {
//
//    private final MemorialPostRepository memorialPostRepository;
//    private final MemorialPostManager memorialPostManager;
//
//    public List<MemorialPostDto> find(Map<String, String> filters) {
//        return memorialPostRepository
//                .findAllWithPaginationAndSorting(filters, MemorialPost.class)
//                .stream().map(this::toDTO).toList();
//    }

//    private MemorialPostDto toDTO(MemorialPost memorialPost) {
//
//    }



    // ...
//    private Map<String, String> filters = new HashMap<>();
//
//    filters.put("writer_eq", "true");
//    filters.put("content_iContains", "true");

    // Without pagination
//    List<MemorialPost> fullSearch = memorialPostRepository.findAll(filters, MemorialPost.class);

//    filters.put("modifiedAt_sort" : "ASC");
//    filters.put("_limit", "10");
//    filters.put("_offset", "0");

    // With pagination
//    Page<MemorialPost> sortedAndPaginatedSearch = memorialPostRepository.findAllWithPaginationAndSorting(filters, MemorialPost.class);




}
