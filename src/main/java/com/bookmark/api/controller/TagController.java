package com.bookmark.api.controller;

import com.bookmark.api.entity.Tag;
import com.bookmark.api.repository.TagRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "태그 조회 API")
public class TagController {

    private final TagRepository tagRepository;

    @GetMapping
    @Operation(summary = "태그 전체 조회", description = "등록된 모든 태그를 조회합니다.")
    public ResponseEntity<List<Tag>> findAll() {
        return ResponseEntity.ok(tagRepository.findAll());
    }
}
