package me.progfrog.flog.web;

import lombok.RequiredArgsConstructor;
import me.progfrog.flog.dto.article.ArticleDto;
import me.progfrog.flog.dto.article.ArticleViewDto;
import me.progfrog.flog.dto.article.ArticleViewForm;
import me.progfrog.flog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getAllArticles(Model model) {
        List<ArticleDto> articles = blogService.findAll();
        model.addAttribute("articles", articles);
        return "articles/articleList";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable(name = "articleId") Long articleId, Model model) {
        ArticleDto article = blogService.findById(articleId);
        model.addAttribute("article", new ArticleViewDto(article));
        return "articles/article";
    }

    @GetMapping("/new-article")
    public String getArticleForm(@RequestParam(required = false, name = "id") Long articleId, Model model) {
        if (articleId == null) {
            model.addAttribute("article", new ArticleViewForm());
        } else {
            ArticleDto article = blogService.findById(articleId);
            model.addAttribute("article", new ArticleViewDto(article));
        }

        return "articles/newArticleForm";
    }
}
