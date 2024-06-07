package me.progfrog.flog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.progfrog.flog.domain.Article;
import me.progfrog.flog.dto.article.ArticleReqAddDto;
import me.progfrog.flog.dto.article.ArticleReqUpdateDto;
import me.progfrog.flog.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BlogApiControllerTest {
    private final static String URL_PREFIX = "/api/articles";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("getAllArticles(): 블로그 글 목록 조회에 성공한다.")
    @Test
    void getAllArticles() throws Exception {
        // given
        Article savedArticle = createDefaultArticle();

        // when
        final ResultActions resultActions = mockMvc.perform(get(URL_PREFIX)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()))
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()));
    }

    @DisplayName("getArticle(): 블로그 글 조회에 성공한다,")
    @Test
    void getArticle() throws Exception {
        // given
        final String url = URL_PREFIX + "/{articleId}";
        Article savedArticle = createDefaultArticle();

        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()))
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()));
    }

    @DisplayName("addArticle(): 블로그 글 추가에 성공한다.")
    @Test
    void addArticle() throws Exception {
        // given
        final String title = "title~~~";
        final String content = "content!!";
        final ArticleReqAddDto reqAddDto = new ArticleReqAddDto(title, content);

        // 객체 -> JSON 직렬화
        final String requestBody = objectMapper.writeValueAsString(reqAddDto);

        Principal principal = Mockito.mock(Principal.class);

        // when
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(URL_PREFIX)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("updateArticle(): 블로그 글 수정에 성공한다.")
    @Test
    void updateArticle() throws Exception {
        // given
        final String url = URL_PREFIX + "/{articleId}";
        Article savedArticle = createDefaultArticle();

        final String newTitle = "new title!!!";
        final String newContent = "new content~~";

        ArticleReqUpdateDto reqUpdateDto = new ArticleReqUpdateDto(newTitle, newContent);

        // when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(reqUpdateDto)));

        // then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    @DisplayName("deleteArticle(): 블로그 글 삭제에 성공한다.")
    @Test
    void deleteArticle() throws Exception {
        // given
        final String url = URL_PREFIX + "/{articleId}";
        Article savedArticle = createDefaultArticle();

        // when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    private Article createDefaultArticle() {
        String title = "title~~~";
        String content = "content!!!";
        return blogRepository.save(new Article(title, content));
    }
}