package me.progfrog.flog.dto.article;

import java.time.LocalDateTime;

public record ArticleResDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
    public ArticleResDto(ArticleDto dto) {
        this(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
