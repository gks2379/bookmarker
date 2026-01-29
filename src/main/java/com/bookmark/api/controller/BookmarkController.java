package com.bookmark.api.controller;

import com.bookmark.api.dto.BookmarkRequest;
import com.bookmark.api.dto.BookmarkResponse;
import com.bookmark.api.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmark", description = "북마크 관리 API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "북마크 전체 조회", description = "페이징된 북마크 목록을 조회합니다.")
    public ResponseEntity<Page<BookmarkResponse>> findAll(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(bookmarkService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "북마크 단건 조회", description = "ID로 특정 북마크를 조회합니다.")
    public ResponseEntity<BookmarkResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookmarkService.findById(id));
    }

    @PostMapping
    @Operation(summary = "북마크 생성", description = "새 북마크를 생성합니다.")
    public ResponseEntity<BookmarkResponse> create(@RequestBody BookmarkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookmarkService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "북마크 수정", description = "기존 북마크를 수정합니다.")
    public ResponseEntity<BookmarkResponse> update(
            @PathVariable Long id,
            @RequestBody BookmarkRequest request) {
        return ResponseEntity.ok(bookmarkService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "북마크 삭제", description = "북마크를 삭제합니다.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookmarkService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "북마크 검색", description = "키워드로 북마크를 검색합니다.")
    public ResponseEntity<Page<BookmarkResponse>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(bookmarkService.search(keyword, pageable));
    }

    @GetMapping("/tag/{tagName}")
    @Operation(summary = "태그로 조회", description = "특정 태그가 붙은 북마크를 조회합니다.")
    public ResponseEntity<Page<BookmarkResponse>> findByTag(
            @PathVariable String tagName,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(bookmarkService.findByTag(tagName, pageable));
    }
}
