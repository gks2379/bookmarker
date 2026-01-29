package com.bookmark.api.dto;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkRequest {
    private String url;
    private String title;
    private String description;
    private Set<String> tagNames;
}
