package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Magasin;
import com.mycompany.myapp.repository.MagasinRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Magasin}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MagasinResource {

    private final Logger log = LoggerFactory.getLogger(MagasinResource.class);

    private static final String ENTITY_NAME = "magasin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MagasinRepository magasinRepository;

    public MagasinResource(MagasinRepository magasinRepository) {
        this.magasinRepository = magasinRepository;
    }

    /**
     * {@code POST  /magasins} : Create a new magasin.
     *
     * @param magasin the magasin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new magasin, or with status {@code 400 (Bad Request)} if the magasin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/magasins")
    public ResponseEntity<Magasin> createMagasin(@RequestBody Magasin magasin) throws URISyntaxException {
        log.debug("REST request to save Magasin : {}", magasin);
        if (magasin.getId() != null) {
            throw new BadRequestAlertException("A new magasin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Magasin result = magasinRepository.save(magasin);
        return ResponseEntity
            .created(new URI("/api/magasins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /magasins/:id} : Updates an existing magasin.
     *
     * @param id the id of the magasin to save.
     * @param magasin the magasin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magasin,
     * or with status {@code 400 (Bad Request)} if the magasin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the magasin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/magasins/{id}")
    public ResponseEntity<Magasin> updateMagasin(@PathVariable(value = "id", required = false) final Long id, @RequestBody Magasin magasin)
        throws URISyntaxException {
        log.debug("REST request to update Magasin : {}, {}", id, magasin);
        if (magasin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magasin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magasinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Magasin result = magasinRepository.save(magasin);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magasin.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /magasins/:id} : Partial updates given fields of an existing magasin, field will ignore if it is null
     *
     * @param id the id of the magasin to save.
     * @param magasin the magasin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magasin,
     * or with status {@code 400 (Bad Request)} if the magasin is not valid,
     * or with status {@code 404 (Not Found)} if the magasin is not found,
     * or with status {@code 500 (Internal Server Error)} if the magasin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/magasins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Magasin> partialUpdateMagasin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Magasin magasin
    ) throws URISyntaxException {
        log.debug("REST request to partial update Magasin partially : {}, {}", id, magasin);
        if (magasin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magasin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magasinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Magasin> result = magasinRepository
            .findById(magasin.getId())
            .map(existingMagasin -> {
                if (magasin.getCodeMagasin() != null) {
                    existingMagasin.setCodeMagasin(magasin.getCodeMagasin());
                }
                if (magasin.getPays() != null) {
                    existingMagasin.setPays(magasin.getPays());
                }
                if (magasin.getAddress() != null) {
                    existingMagasin.setAddress(magasin.getAddress());
                }

                return existingMagasin;
            })
            .map(magasinRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magasin.getId().toString())
        );
    }

    /**
     * {@code GET  /magasins} : get all the magasins.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of magasins in body.
     */
    @GetMapping("/magasins")
    public ResponseEntity<List<Magasin>> getAllMagasins(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("magasinier-is-null".equals(filter)) {
            log.debug("REST request to get all Magasins where magasinier is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(magasinRepository.findAll().spliterator(), false)
                    .filter(magasin -> magasin.getMagasinier() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }
        log.debug("REST request to get a page of Magasins");
        Page<Magasin> page = magasinRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /magasins/:id} : get the "id" magasin.
     *
     * @param id the id of the magasin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the magasin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/magasins/{id}")
    public ResponseEntity<Magasin> getMagasin(@PathVariable Long id) {
        log.debug("REST request to get Magasin : {}", id);
        Optional<Magasin> magasin = magasinRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(magasin);
    }

    /**
     * {@code DELETE  /magasins/:id} : delete the "id" magasin.
     *
     * @param id the id of the magasin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/magasins/{id}")
    public ResponseEntity<Void> deleteMagasin(@PathVariable Long id) {
        log.debug("REST request to delete Magasin : {}", id);
        magasinRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
