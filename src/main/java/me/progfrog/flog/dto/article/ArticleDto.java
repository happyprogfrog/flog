package me.progfrog.flog.dto.article;

import me.progfrog.flog.domain.Article;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ArticleDto(Article article) {
        this(
            article.getId(),
            article.getTitle(),
            article.getContent(),
            article.getCreatedAt(),
            article.getUpdatedAt()
        );
    }
}
