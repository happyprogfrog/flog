package me.progfrog.flog.dto.article;

import me.progfrog.flog.domain.Article;

public record ArticleReqAddDto(
        String title,
        String content
) {
    public Article toEntity() {
        return new Article(title, content);
    }
}
