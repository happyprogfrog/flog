package me.progfrog.flog.service;

import lombok.RequiredArgsConstructor;
import me.progfrog.flog.domain.Article;
import me.progfrog.flog.dto.article.ArticleDto;
import me.progfrog.flog.dto.article.ArticleReqAddDto;
import me.progfrog.flog.dto.article.ArticleReqUpdateDto;
import me.progfrog.flog.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogService {

    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public List<ArticleDto> findAll() {
        return blogRepository.findAll()
                .stream()
                .map(ArticleDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(Long articleId) {
        return new ArticleDto(findArticleById(articleId));
    }

    public ArticleDto save(ArticleReqAddDto reqAddDto, String username) {
        Article article = blogRepository.save(reqAddDto.toEntity(username));
        return new ArticleDto(article);
    }

    public ArticleDto update(Long articleId, ArticleReqUpdateDto reqUpdateDto) {
        Article article = findArticleById(articleId);
        authorizeArticleAuthor(article);
        article.update(reqUpdateDto.title(), reqUpdateDto.content());
        return new ArticleDto(article);
    }

    public void delete(Long articleId) {
        Article article = findArticleById(articleId);
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    private Article findArticleById(Long articleId) {
        return blogRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("article not found: " + articleId));
    }

    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
