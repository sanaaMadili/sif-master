package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Clr;
import com.mycompany.myapp.repository.ClrRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Clr}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClrResource {

    private final Logger log = LoggerFactory.getLogger(ClrResource.class);

    private static final String ENTITY_NAME = "clr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClrRepository clrRepository;

    public ClrResource(ClrRepository clrRepository) {
        this.clrRepository = clrRepository;
    }

    /**
     * {@code POST  /clrs} : Create a new clr.
     *
     * @param clr the clr to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clr, or with status {@code 400 (Bad Request)} if the clr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clrs")
    public ResponseEntity<Clr> createClr(@RequestBody Clr clr) throws URISyntaxException {
        log.debug("REST request to save Clr : {}", clr);
        if (clr.getId() != null) {
            throw new BadRequestAlertException("A new clr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clr result = clrRepository.save(clr);
        return ResponseEntity
            .created(new URI("/api/clrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clrs/:id} : Updates an existing clr.
     *
     * @param id the id of the clr to save.
     * @param clr the clr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clr,
     * or with status {@code 400 (Bad Request)} if the clr is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clrs/{id}")
    public ResponseEntity<Clr> updateClr(@PathVariable(value = "id", required = false) final Long id, @RequestBody Clr clr)
        throws URISyntaxException {
        log.debug("REST request to update Clr : {}, {}", id, clr);
        if (clr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clr.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clr result = clrRepository.save(clr);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clr.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clrs/:id} : Partial updates given fields of an existing clr, field will ignore if it is null
     *
     * @param id the id of the clr to save.
     * @param clr the clr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clr,
     * or with status {@code 400 (Bad Request)} if the clr is not valid,
     * or with status {@code 404 (Not Found)} if the clr is not found,
     * or with status {@code 500 (Internal Server Error)} if the clr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clrs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Clr> partialUpdateClr(@PathVariable(value = "id", required = false) final Long id, @RequestBody Clr clr)
        throws URISyntaxException {
        log.debug("REST request to partial update Clr partially : {}, {}", id, clr);
        if (clr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clr.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clr> result = clrRepository
            .findById(clr.getId())
            .map(existingClr -> {
                if (clr.getConstructeurAutomobile() != null) {
                    existingClr.setConstructeurAutomobile(clr.getConstructeurAutomobile());
                }
                if (clr.getModelVoiture() != null) {
                    existingClr.setModelVoiture(clr.getModelVoiture());
                }
                if (clr.getAnneeVoiture() != null) {
                    existingClr.setAnneeVoiture(clr.getAnneeVoiture());
                }
                if (clr.getEtatPneu() != null) {
                    existingClr.setEtatPneu(clr.getEtatPneu());
                }
                if (clr.getImage() != null) {
                    existingClr.setImage(clr.getImage());
                }
                if (clr.getImageContentType() != null) {
                    existingClr.setImageContentType(clr.getImageContentType());
                }
                if (clr.getDateProduction() != null) {
                    existingClr.setDateProduction(clr.getDateProduction());
                }

                return existingClr;
            })
            .map(clrRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clr.getId().toString())
        );
    }

    /**
     * {@code GET  /clrs} : get all the clrs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clrs in body.
     */
    @GetMapping("/clrs")
    public ResponseEntity<List<Clr>> getAllClrs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Clrs");
        Page<Clr> page = clrRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clrs/:id} : get the "id" clr.
     *
     * @param id the id of the clr to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clr, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clrs/{id}")
    public ResponseEntity<Clr> getClr(@PathVariable Long id) {
        log.debug("REST request to get Clr : {}", id);
        Optional<Clr> clr = clrRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clr);
    }

    /**
     * {@code DELETE  /clrs/:id} : delete the "id" clr.
     *
     * @param id the id of the clr to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clrs/{id}")
    public ResponseEntity<Void> deleteClr(@PathVariable Long id) {
        log.debug("REST request to delete Clr : {}", id);
        clrRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
