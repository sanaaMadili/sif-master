package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Article;
import com.mycompany.myapp.repository.ArticleRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Article}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArticleResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    private static final String ENTITY_NAME = "article";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleRepository articleRepository;

    public ArticleResource(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * {@code POST  /articles} : Create a new article.
     *
     * @param article the article to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new article, or with status {@code 400 (Bad Request)} if the article has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) throws URISyntaxException {
        log.debug("REST request to save Article : {}", article);
        if (article.getId() != null) {
            throw new BadRequestAlertException("A new article cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Article result = articleRepository.save(article);
        return ResponseEntity
            .created(new URI("/api/articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /articles/:id} : Updates an existing article.
     *
     * @param id the id of the article to save.
     * @param article the article to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated article,
     * or with status {@code 400 (Bad Request)} if the article is not valid,
     * or with status {@code 500 (Internal Server Error)} if the article couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable(value = "id", required = false) final Long id, @RequestBody Article article)
        throws URISyntaxException {
        log.debug("REST request to update Article : {}, {}", id, article);
        if (article.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, article.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Article result = articleRepository.save(article);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, article.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /articles/:id} : Partial updates given fields of an existing article, field will ignore if it is null
     *
     * @param id the id of the article to save.
     * @param article the article to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated article,
     * or with status {@code 400 (Bad Request)} if the article is not valid,
     * or with status {@code 404 (Not Found)} if the article is not found,
     * or with status {@code 500 (Internal Server Error)} if the article couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Article> partialUpdateArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Article article
    ) throws URISyntaxException {
        log.debug("REST request to partial update Article partially : {}, {}", id, article);
        if (article.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, article.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Article> result = articleRepository
            .findById(article.getId())
            .map(existingArticle -> {
                if (article.getCai() != null) {
                    existingArticle.setCai(article.getCai());
                }
                if (article.getRefPneu() != null) {
                    existingArticle.setRefPneu(article.getRefPneu());
                }
                if (article.getTypePneu() != null) {
                    existingArticle.setTypePneu(article.getTypePneu());
                }
                if (article.getValeur() != null) {
                    existingArticle.setValeur(article.getValeur());
                }
                if (article.getImage() != null) {
                    existingArticle.setImage(article.getImage());
                }
                if (article.getImageContentType() != null) {
                    existingArticle.setImageContentType(article.getImageContentType());
                }
                if (article.getDevise() != null) {
                    existingArticle.setDevise(article.getDevise());
                }

                return existingArticle;
            })
            .map(articleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, article.getId().toString())
        );
    }

    /**
     * {@code GET  /articles} : get all the articles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articles in body.
     */
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Articles");
        Page<Article> page = articleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /articles/:id} : get the "id" article.
     *
     * @param id the id of the article to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the article, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Optional<Article> article = articleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(article);
    }

    /**
     * {@code DELETE  /articles/:id} : delete the "id" article.
     *
     * @param id the id of the article to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        articleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
