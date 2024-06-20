package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AppelCommande;
import com.mycompany.myapp.repository.AppelCommandeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AppelCommande}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AppelCommandeResource {

    private final Logger log = LoggerFactory.getLogger(AppelCommandeResource.class);

    private static final String ENTITY_NAME = "appelCommande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppelCommandeRepository appelCommandeRepository;

    public AppelCommandeResource(AppelCommandeRepository appelCommandeRepository) {
        this.appelCommandeRepository = appelCommandeRepository;
    }

    /**
     * {@code POST  /appel-commandes} : Create a new appelCommande.
     *
     * @param appelCommande the appelCommande to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appelCommande, or with status {@code 400 (Bad Request)} if the appelCommande has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appel-commandes")
    public ResponseEntity<AppelCommande> createAppelCommande(@RequestBody AppelCommande appelCommande) throws URISyntaxException {
        log.debug("REST request to save AppelCommande : {}", appelCommande);
        if (appelCommande.getId() != null) {
            throw new BadRequestAlertException("A new appelCommande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppelCommande result = appelCommandeRepository.save(appelCommande);
        return ResponseEntity
            .created(new URI("/api/appel-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appel-commandes/:id} : Updates an existing appelCommande.
     *
     * @param id the id of the appelCommande to save.
     * @param appelCommande the appelCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appelCommande,
     * or with status {@code 400 (Bad Request)} if the appelCommande is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appelCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appel-commandes/{id}")
    public ResponseEntity<AppelCommande> updateAppelCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppelCommande appelCommande
    ) throws URISyntaxException {
        log.debug("REST request to update AppelCommande : {}, {}", id, appelCommande);
        if (appelCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appelCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appelCommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppelCommande result = appelCommandeRepository.save(appelCommande);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appelCommande.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appel-commandes/:id} : Partial updates given fields of an existing appelCommande, field will ignore if it is null
     *
     * @param id the id of the appelCommande to save.
     * @param appelCommande the appelCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appelCommande,
     * or with status {@code 400 (Bad Request)} if the appelCommande is not valid,
     * or with status {@code 404 (Not Found)} if the appelCommande is not found,
     * or with status {@code 500 (Internal Server Error)} if the appelCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appel-commandes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppelCommande> partialUpdateAppelCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppelCommande appelCommande
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppelCommande partially : {}, {}", id, appelCommande);
        if (appelCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appelCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appelCommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppelCommande> result = appelCommandeRepository
            .findById(appelCommande.getId())
            .map(existingAppelCommande -> {
                if (appelCommande.getNumCommande() != null) {
                    existingAppelCommande.setNumCommande(appelCommande.getNumCommande());
                }
                if (appelCommande.getDateCommande() != null) {
                    existingAppelCommande.setDateCommande(appelCommande.getDateCommande());
                }
                if (appelCommande.getDateLivraison() != null) {
                    existingAppelCommande.setDateLivraison(appelCommande.getDateLivraison());
                }
                if (appelCommande.getDateExpedition() != null) {
                    existingAppelCommande.setDateExpedition(appelCommande.getDateExpedition());
                }
                if (appelCommande.getStatus() != null) {
                    existingAppelCommande.setStatus(appelCommande.getStatus());
                }
                if (appelCommande.getAnnomalie() != null) {
                    existingAppelCommande.setAnnomalie(appelCommande.getAnnomalie());
                }

                return existingAppelCommande;
            })
            .map(appelCommandeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appelCommande.getId().toString())
        );
    }

    /**
     * {@code GET  /appel-commandes} : get all the appelCommandes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appelCommandes in body.
     */
    @GetMapping("/appel-commandes")
    public ResponseEntity<List<AppelCommande>> getAllAppelCommandes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AppelCommandes");
        Page<AppelCommande> page = appelCommandeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appel-commandes/:id} : get the "id" appelCommande.
     *
     * @param id the id of the appelCommande to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appelCommande, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appel-commandes/{id}")
    public ResponseEntity<AppelCommande> getAppelCommande(@PathVariable Long id) {
        log.debug("REST request to get AppelCommande : {}", id);
        Optional<AppelCommande> appelCommande = appelCommandeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appelCommande);
    }

    /**
     * {@code DELETE  /appel-commandes/:id} : delete the "id" appelCommande.
     *
     * @param id the id of the appelCommande to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appel-commandes/{id}")
    public ResponseEntity<Void> deleteAppelCommande(@PathVariable Long id) {
        log.debug("REST request to delete AppelCommande : {}", id);
        appelCommandeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
