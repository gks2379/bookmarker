package com.bookmark.api.repository;

import com.bookmark.api.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.url) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Bookmark> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT b FROM Bookmark b JOIN b.tags t WHERE t.name = :tagName")
    Page<Bookmark> findByTagName(@Param("tagName") String tagName, Pageable pageable);
}
