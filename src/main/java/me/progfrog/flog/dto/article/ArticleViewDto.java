package me.progfrog.flog.dto.article;

import java.time.LocalDateTime;

public record ArticleViewDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public ArticleViewDto(ArticleDto dto) {
        this(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.createdAt()
        );
    }
}
