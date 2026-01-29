package com.bookmark.api.service;

import com.bookmark.api.dto.BookmarkRequest;
import com.bookmark.api.dto.BookmarkResponse;
import com.bookmark.api.entity.Bookmark;
import com.bookmark.api.entity.Tag;
import com.bookmark.api.repository.BookmarkRepository;
import com.bookmark.api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public Page<BookmarkResponse> findAll(Pageable pageable) {
        return bookmarkRepository.findAll(pageable)
                .map(BookmarkResponse::from);
    }

    public BookmarkResponse findById(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found: " + id));
        return BookmarkResponse.from(bookmark);
    }

    @Transactional
    public BookmarkResponse create(BookmarkRequest request) {
        Set<Tag> tags = getOrCreateTags(request.getTagNames());

        Bookmark bookmark = Bookmark.builder()
                .url(request.getUrl())
                .title(request.getTitle())
                .description(request.getDescription())
                .tags(tags)
                .build();

        Bookmark saved = bookmarkRepository.save(bookmark);
        return BookmarkResponse.from(saved);
    }

    @Transactional
    public BookmarkResponse update(Long id, BookmarkRequest request) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found: " + id));

        bookmark.setUrl(request.getUrl());
        bookmark.setTitle(request.getTitle());
        bookmark.setDescription(request.getDescription());
        bookmark.setTags(getOrCreateTags(request.getTagNames()));

        return BookmarkResponse.from(bookmark);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookmarkRepository.existsById(id)) {
            throw new IllegalArgumentException("Bookmark not found: " + id);
        }
        bookmarkRepository.deleteById(id);
    }

    public Page<BookmarkResponse> search(String keyword, Pageable pageable) {
        return bookmarkRepository.searchByKeyword(keyword, pageable)
                .map(BookmarkResponse::from);
    }

    public Page<BookmarkResponse> findByTag(String tagName, Pageable pageable) {
        return bookmarkRepository.findByTagName(tagName, pageable)
                .map(BookmarkResponse::from);
    }

    private Set<Tag> getOrCreateTags(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return new HashSet<>();
        }

        Set<Tag> tags = new HashSet<>();
        for (String name : tagNames) {
            Tag tag = tagRepository.findByName(name)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build()));
            tags.add(tag);
        }
        return tags;
    }
}
