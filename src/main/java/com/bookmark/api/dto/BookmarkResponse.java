package com.bookmark.api.dto;

import com.bookmark.api.entity.Bookmark;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkResponse {
    private Long id;
    private String url;
    private String title;
    private String description;
    private Set<String> tagNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookmarkResponse from(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .url(bookmark.getUrl())
                .title(bookmark.getTitle())
                .description(bookmark.getDescription())
                .tagNames(bookmark.getTags().stream()
                        .map(tag -> tag.getName())
                        .collect(Collectors.toSet()))
                .createdAt(bookmark.getCreatedAt())
                .updatedAt(bookmark.getUpdatedAt())
                .build();
    }
}
