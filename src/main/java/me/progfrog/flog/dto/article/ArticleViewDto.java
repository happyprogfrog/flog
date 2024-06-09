package me.progfrog.flog.dto.article;

import java.time.LocalDateTime;

public record ArticleViewDto(
        Long id,
        String author,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public ArticleViewDto(ArticleDto dto) {
        this(
                dto.id(),
                dto.author(),
                dto.title(),
                dto.content(),
                dto.createdAt()
        );
    }
}
