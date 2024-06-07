package me.progfrog.flog.api;

import lombok.RequiredArgsConstructor;
import me.progfrog.flog.dto.article.ArticleReqAddDto;
import me.progfrog.flog.dto.article.ArticleReqUpdateDto;
import me.progfrog.flog.dto.article.ArticleResDto;
import me.progfrog.flog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class BlogApiController {

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<List<ArticleResDto>> getAllArticles() {
        List<ArticleResDto> articles = blogService.findAll()
                .stream()
                .map(ArticleResDto::new)
                .toList();
        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResDto> getArticle(@PathVariable(name = "articleId") Long articleId) {
        ArticleResDto resDto = new ArticleResDto(blogService.findById(articleId));
        return ResponseEntity.ok()
                .body(resDto);
    }

    @PostMapping
    public ResponseEntity<ArticleResDto> addArticle(@RequestBody ArticleReqAddDto reqAddDto) {
        ArticleResDto resDto = new ArticleResDto(blogService.save(reqAddDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resDto);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResDto> updateArticle(@PathVariable(name = "articleId") Long articleId, @RequestBody ArticleReqUpdateDto reqUpdateDto) {
        ArticleResDto resDto = new ArticleResDto(blogService.update(articleId, reqUpdateDto));
        return ResponseEntity.ok()
                .body(resDto);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable(name = "articleId") Long articleId) {
        blogService.delete(articleId);
        return ResponseEntity.ok().build();
    }
}
