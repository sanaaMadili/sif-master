package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EtatStock;
import com.mycompany.myapp.repository.EtatStockRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EtatStock}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EtatStockResource {

    private final Logger log = LoggerFactory.getLogger(EtatStockResource.class);

    private static final String ENTITY_NAME = "etatStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtatStockRepository etatStockRepository;

    public EtatStockResource(EtatStockRepository etatStockRepository) {
        this.etatStockRepository = etatStockRepository;
    }

    /**
     * {@code POST  /etat-stocks} : Create a new etatStock.
     *
     * @param etatStock the etatStock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etatStock, or with status {@code 400 (Bad Request)} if the etatStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etat-stocks")
    public ResponseEntity<EtatStock> createEtatStock(@RequestBody EtatStock etatStock) throws URISyntaxException {
        log.debug("REST request to save EtatStock : {}", etatStock);
        if (etatStock.getId() != null) {
            throw new BadRequestAlertException("A new etatStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EtatStock result = etatStockRepository.save(etatStock);
        return ResponseEntity
            .created(new URI("/api/etat-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etat-stocks/:id} : Updates an existing etatStock.
     *
     * @param id the id of the etatStock to save.
     * @param etatStock the etatStock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etatStock,
     * or with status {@code 400 (Bad Request)} if the etatStock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etatStock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etat-stocks/{id}")
    public ResponseEntity<EtatStock> updateEtatStock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EtatStock etatStock
    ) throws URISyntaxException {
        log.debug("REST request to update EtatStock : {}, {}", id, etatStock);
        if (etatStock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etatStock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etatStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EtatStock result = etatStockRepository.save(etatStock);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etatStock.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etat-stocks/:id} : Partial updates given fields of an existing etatStock, field will ignore if it is null
     *
     * @param id the id of the etatStock to save.
     * @param etatStock the etatStock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etatStock,
     * or with status {@code 400 (Bad Request)} if the etatStock is not valid,
     * or with status {@code 404 (Not Found)} if the etatStock is not found,
     * or with status {@code 500 (Internal Server Error)} if the etatStock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etat-stocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EtatStock> partialUpdateEtatStock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EtatStock etatStock
    ) throws URISyntaxException {
        log.debug("REST request to partial update EtatStock partially : {}, {}", id, etatStock);
        if (etatStock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etatStock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etatStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EtatStock> result = etatStockRepository
            .findById(etatStock.getId())
            .map(existingEtatStock -> {
                if (etatStock.getQte() != null) {
                    existingEtatStock.setQte(etatStock.getQte());
                }
                if (etatStock.getLocation() != null) {
                    existingEtatStock.setLocation(etatStock.getLocation());
                }

                return existingEtatStock;
            })
            .map(etatStockRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etatStock.getId().toString())
        );
    }

    /**
     * {@code GET  /etat-stocks} : get all the etatStocks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etatStocks in body.
     */
    @GetMapping("/etat-stocks")
    public ResponseEntity<List<EtatStock>> getAllEtatStocks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EtatStocks");
        Page<EtatStock> page = etatStockRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etat-stocks/:id} : get the "id" etatStock.
     *
     * @param id the id of the etatStock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etatStock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etat-stocks/{id}")
    public ResponseEntity<EtatStock> getEtatStock(@PathVariable Long id) {
        log.debug("REST request to get EtatStock : {}", id);
        Optional<EtatStock> etatStock = etatStockRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(etatStock);
    }

    /**
     * {@code DELETE  /etat-stocks/:id} : delete the "id" etatStock.
     *
     * @param id the id of the etatStock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etat-stocks/{id}")
    public ResponseEntity<Void> deleteEtatStock(@PathVariable Long id) {
        log.debug("REST request to delete EtatStock : {}", id);
        etatStockRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
