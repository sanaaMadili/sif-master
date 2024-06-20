package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Coe;
import com.mycompany.myapp.repository.CoeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Coe}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CoeResource {

    private final Logger log = LoggerFactory.getLogger(CoeResource.class);

    private static final String ENTITY_NAME = "coe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoeRepository coeRepository;

    public CoeResource(CoeRepository coeRepository) {
        this.coeRepository = coeRepository;
    }

    /**
     * {@code POST  /coes} : Create a new coe.
     *
     * @param coe the coe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coe, or with status {@code 400 (Bad Request)} if the coe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coes")
    public ResponseEntity<Coe> createCoe(@RequestBody Coe coe) throws URISyntaxException {
        log.debug("REST request to save Coe : {}", coe);
        if (coe.getId() != null) {
            throw new BadRequestAlertException("A new coe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Coe result = coeRepository.save(coe);
        return ResponseEntity
            .created(new URI("/api/coes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coes/:id} : Updates an existing coe.
     *
     * @param id the id of the coe to save.
     * @param coe the coe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coe,
     * or with status {@code 400 (Bad Request)} if the coe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coes/{id}")
    public ResponseEntity<Coe> updateCoe(@PathVariable(value = "id", required = false) final Long id, @RequestBody Coe coe)
        throws URISyntaxException {
        log.debug("REST request to update Coe : {}, {}", id, coe);
        if (coe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Coe result = coeRepository.save(coe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coes/:id} : Partial updates given fields of an existing coe, field will ignore if it is null
     *
     * @param id the id of the coe to save.
     * @param coe the coe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coe,
     * or with status {@code 400 (Bad Request)} if the coe is not valid,
     * or with status {@code 404 (Not Found)} if the coe is not found,
     * or with status {@code 500 (Internal Server Error)} if the coe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Coe> partialUpdateCoe(@PathVariable(value = "id", required = false) final Long id, @RequestBody Coe coe)
        throws URISyntaxException {
        log.debug("REST request to partial update Coe partially : {}, {}", id, coe);
        if (coe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Coe> result = coeRepository
            .findById(coe.getId())
            .map(existingCoe -> {
                if (coe.getTypeVoiture() != null) {
                    existingCoe.setTypeVoiture(coe.getTypeVoiture());
                }
                if (coe.getPoidsVoiture() != null) {
                    existingCoe.setPoidsVoiture(coe.getPoidsVoiture());
                }
                if (coe.getVitesseVoiture() != null) {
                    existingCoe.setVitesseVoiture(coe.getVitesseVoiture());
                }

                return existingCoe;
            })
            .map(coeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coe.getId().toString())
        );
    }

    /**
     * {@code GET  /coes} : get all the coes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coes in body.
     */
    @GetMapping("/coes")
    public ResponseEntity<List<Coe>> getAllCoes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Coes");
        Page<Coe> page = coeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coes/:id} : get the "id" coe.
     *
     * @param id the id of the coe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coes/{id}")
    public ResponseEntity<Coe> getCoe(@PathVariable Long id) {
        log.debug("REST request to get Coe : {}", id);
        Optional<Coe> coe = coeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(coe);
    }

    /**
     * {@code DELETE  /coes/:id} : delete the "id" coe.
     *
     * @param id the id of the coe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coes/{id}")
    public ResponseEntity<Void> deleteCoe(@PathVariable Long id) {
        log.debug("REST request to delete Coe : {}", id);
        coeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
