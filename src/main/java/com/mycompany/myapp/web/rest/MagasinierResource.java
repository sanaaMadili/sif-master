package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Magasinier;
import com.mycompany.myapp.repository.MagasinierRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Magasinier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MagasinierResource {

    private final Logger log = LoggerFactory.getLogger(MagasinierResource.class);

    private static final String ENTITY_NAME = "magasinier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MagasinierRepository magasinierRepository;

    public MagasinierResource(MagasinierRepository magasinierRepository) {
        this.magasinierRepository = magasinierRepository;
    }

    /**
     * {@code POST  /magasiniers} : Create a new magasinier.
     *
     * @param magasinier the magasinier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new magasinier, or with status {@code 400 (Bad Request)} if the magasinier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/magasiniers")
    public ResponseEntity<Magasinier> createMagasinier(@RequestBody Magasinier magasinier) throws URISyntaxException {
        log.debug("REST request to save Magasinier : {}", magasinier);
        if (magasinier.getId() != null) {
            throw new BadRequestAlertException("A new magasinier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Magasinier result = magasinierRepository.save(magasinier);
        return ResponseEntity
            .created(new URI("/api/magasiniers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /magasiniers/:id} : Updates an existing magasinier.
     *
     * @param id the id of the magasinier to save.
     * @param magasinier the magasinier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magasinier,
     * or with status {@code 400 (Bad Request)} if the magasinier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the magasinier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/magasiniers/{id}")
    public ResponseEntity<Magasinier> updateMagasinier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Magasinier magasinier
    ) throws URISyntaxException {
        log.debug("REST request to update Magasinier : {}, {}", id, magasinier);
        if (magasinier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magasinier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magasinierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Magasinier result = magasinierRepository.save(magasinier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magasinier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /magasiniers/:id} : Partial updates given fields of an existing magasinier, field will ignore if it is null
     *
     * @param id the id of the magasinier to save.
     * @param magasinier the magasinier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magasinier,
     * or with status {@code 400 (Bad Request)} if the magasinier is not valid,
     * or with status {@code 404 (Not Found)} if the magasinier is not found,
     * or with status {@code 500 (Internal Server Error)} if the magasinier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/magasiniers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Magasinier> partialUpdateMagasinier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Magasinier magasinier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Magasinier partially : {}, {}", id, magasinier);
        if (magasinier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magasinier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magasinierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Magasinier> result = magasinierRepository
            .findById(magasinier.getId())
            .map(existingMagasinier -> {
                if (magasinier.getProfession() != null) {
                    existingMagasinier.setProfession(magasinier.getProfession());
                }

                return existingMagasinier;
            })
            .map(magasinierRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magasinier.getId().toString())
        );
    }

    /**
     * {@code GET  /magasiniers} : get all the magasiniers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of magasiniers in body.
     */
    @GetMapping("/magasiniers")
    public ResponseEntity<List<Magasinier>> getAllMagasiniers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Magasiniers");
        Page<Magasinier> page = magasinierRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /magasiniers/:id} : get the "id" magasinier.
     *
     * @param id the id of the magasinier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the magasinier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/magasiniers/{id}")
    public ResponseEntity<Magasinier> getMagasinier(@PathVariable Long id) {
        log.debug("REST request to get Magasinier : {}", id);
        Optional<Magasinier> magasinier = magasinierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(magasinier);
    }

    /**
     * {@code DELETE  /magasiniers/:id} : delete the "id" magasinier.
     *
     * @param id the id of the magasinier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/magasiniers/{id}")
    public ResponseEntity<Void> deleteMagasinier(@PathVariable Long id) {
        log.debug("REST request to delete Magasinier : {}", id);
        magasinierRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
