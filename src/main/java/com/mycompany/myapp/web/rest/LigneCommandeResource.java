package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LigneCommande;
import com.mycompany.myapp.repository.LigneCommandeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LigneCommande}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LigneCommandeResource {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeResource.class);

    private static final String ENTITY_NAME = "ligneCommande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneCommandeRepository ligneCommandeRepository;

    public LigneCommandeResource(LigneCommandeRepository ligneCommandeRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    /**
     * {@code POST  /ligne-commandes} : Create a new ligneCommande.
     *
     * @param ligneCommande the ligneCommande to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneCommande, or with status {@code 400 (Bad Request)} if the ligneCommande has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligne-commandes")
    public ResponseEntity<LigneCommande> createLigneCommande(@RequestBody LigneCommande ligneCommande) throws URISyntaxException {
        log.debug("REST request to save LigneCommande : {}", ligneCommande);
        if (ligneCommande.getId() != null) {
            throw new BadRequestAlertException("A new ligneCommande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneCommande result = ligneCommandeRepository.save(ligneCommande);
        return ResponseEntity
            .created(new URI("/api/ligne-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligne-commandes/:id} : Updates an existing ligneCommande.
     *
     * @param id the id of the ligneCommande to save.
     * @param ligneCommande the ligneCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommande,
     * or with status {@code 400 (Bad Request)} if the ligneCommande is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligne-commandes/{id}")
    public ResponseEntity<LigneCommande> updateLigneCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneCommande ligneCommande
    ) throws URISyntaxException {
        log.debug("REST request to update LigneCommande : {}, {}", id, ligneCommande);
        if (ligneCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneCommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LigneCommande result = ligneCommandeRepository.save(ligneCommande);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneCommande.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ligne-commandes/:id} : Partial updates given fields of an existing ligneCommande, field will ignore if it is null
     *
     * @param id the id of the ligneCommande to save.
     * @param ligneCommande the ligneCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommande,
     * or with status {@code 400 (Bad Request)} if the ligneCommande is not valid,
     * or with status {@code 404 (Not Found)} if the ligneCommande is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ligne-commandes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LigneCommande> partialUpdateLigneCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneCommande ligneCommande
    ) throws URISyntaxException {
        log.debug("REST request to partial update LigneCommande partially : {}, {}", id, ligneCommande);
        if (ligneCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneCommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigneCommande> result = ligneCommandeRepository
            .findById(ligneCommande.getId())
            .map(existingLigneCommande -> {
                if (ligneCommande.getQte() != null) {
                    existingLigneCommande.setQte(ligneCommande.getQte());
                }

                return existingLigneCommande;
            })
            .map(ligneCommandeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneCommande.getId().toString())
        );
    }

    /**
     * {@code GET  /ligne-commandes} : get all the ligneCommandes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneCommandes in body.
     */
    @GetMapping("/ligne-commandes")
    public ResponseEntity<List<LigneCommande>> getAllLigneCommandes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LigneCommandes");
        Page<LigneCommande> page = ligneCommandeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligne-commandes/:id} : get the "id" ligneCommande.
     *
     * @param id the id of the ligneCommande to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneCommande, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligne-commandes/{id}")
    public ResponseEntity<LigneCommande> getLigneCommande(@PathVariable Long id) {
        log.debug("REST request to get LigneCommande : {}", id);
        Optional<LigneCommande> ligneCommande = ligneCommandeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ligneCommande);
    }

    /**
     * {@code DELETE  /ligne-commandes/:id} : delete the "id" ligneCommande.
     *
     * @param id the id of the ligneCommande to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligne-commandes/{id}")
    public ResponseEntity<Void> deleteLigneCommande(@PathVariable Long id) {
        log.debug("REST request to delete LigneCommande : {}", id);
        ligneCommandeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
