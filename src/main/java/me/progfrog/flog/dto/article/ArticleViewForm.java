package me.progfrog.flog.dto.article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleViewForm {
    private Long id;
    private String author;
    private String title;
    private String content;
}
