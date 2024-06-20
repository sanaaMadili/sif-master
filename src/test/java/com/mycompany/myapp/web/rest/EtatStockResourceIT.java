package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EtatStock;
import com.mycompany.myapp.repository.EtatStockRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EtatStockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtatStockResourceIT {

    private static final Integer DEFAULT_QTE = 1;
    private static final Integer UPDATED_QTE = 2;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/etat-stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtatStockRepository etatStockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtatStockMockMvc;

    private EtatStock etatStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EtatStock createEntity(EntityManager em) {
        EtatStock etatStock = new EtatStock().qte(DEFAULT_QTE).location(DEFAULT_LOCATION);
        return etatStock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EtatStock createUpdatedEntity(EntityManager em) {
        EtatStock etatStock = new EtatStock().qte(UPDATED_QTE).location(UPDATED_LOCATION);
        return etatStock;
    }

    @BeforeEach
    public void initTest() {
        etatStock = createEntity(em);
    }

    @Test
    @Transactional
    void createEtatStock() throws Exception {
        int databaseSizeBeforeCreate = etatStockRepository.findAll().size();
        // Create the EtatStock
        restEtatStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etatStock)))
            .andExpect(status().isCreated());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeCreate + 1);
        EtatStock testEtatStock = etatStockList.get(etatStockList.size() - 1);
        assertThat(testEtatStock.getQte()).isEqualTo(DEFAULT_QTE);
        assertThat(testEtatStock.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void createEtatStockWithExistingId() throws Exception {
        // Create the EtatStock with an existing ID
        etatStock.setId(1L);

        int databaseSizeBeforeCreate = etatStockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtatStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etatStock)))
            .andExpect(status().isBadRequest());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEtatStocks() throws Exception {
        // Initialize the database
        etatStockRepository.saveAndFlush(etatStock);

        // Get all the etatStockList
        restEtatStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etatStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].qte").value(hasItem(DEFAULT_QTE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getEtatStock() throws Exception {
        // Initialize the database
        etatStockRepository.saveAndFlush(etatStock);

        // Get the etatStock
        restEtatStockMockMvc
            .perform(get(ENTITY_API_URL_ID, etatStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etatStock.getId().intValue()))
            .andExpect(jsonPath("$.qte").value(DEFAULT_QTE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getNonExistingEtatStock() throws Exception {
        // Get the etatStock
        restEtatStockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtatStock() throws Exception {
        // Initialize the database
        etatStockRepository.saveAndFlush(etatStock);

        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();

        // Update the etatStock
        EtatStock updatedEtatStock = etatStockRepository.findById(etatStock.getId()).get();
        // Disconnect from session so that the updates on updatedEtatStock are not directly saved in db
        em.detach(updatedEtatStock);
        updatedEtatStock.qte(UPDATED_QTE).location(UPDATED_LOCATION);

        restEtatStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtatStock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtatStock))
            )
            .andExpect(status().isOk());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
        EtatStock testEtatStock = etatStockList.get(etatStockList.size() - 1);
        assertThat(testEtatStock.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testEtatStock.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingEtatStock() throws Exception {
        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();
        etatStock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtatStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etatStock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etatStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtatStock() throws Exception {
        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();
        etatStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtatStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etatStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtatStock() throws Exception {
        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();
        etatStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtatStockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etatStock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtatStockWithPatch() throws Exception {
        // Initialize the database
        etatStockRepository.saveAndFlush(etatStock);

        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();

        // Update the etatStock using partial update
        EtatStock partialUpdatedEtatStock = new EtatStock();
        partialUpdatedEtatStock.setId(etatStock.getId());

        partialUpdatedEtatStock.qte(UPDATED_QTE);

        restEtatStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtatStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtatStock))
            )
            .andExpect(status().isOk());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
        EtatStock testEtatStock = etatStockList.get(etatStockList.size() - 1);
        assertThat(testEtatStock.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testEtatStock.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateEtatStockWithPatch() throws Exception {
        // Initialize the database
        etatStockRepository.saveAndFlush(etatStock);

        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();

        // Update the etatStock using partial update
        EtatStock partialUpdatedEtatStock = new EtatStock();
        partialUpdatedEtatStock.setId(etatStock.getId());

        partialUpdatedEtatStock.qte(UPDATED_QTE).location(UPDATED_LOCATION);

        restEtatStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtatStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtatStock))
            )
            .andExpect(status().isOk());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
        EtatStock testEtatStock = etatStockList.get(etatStockList.size() - 1);
        assertThat(testEtatStock.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testEtatStock.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingEtatStock() throws Exception {
        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();
        etatStock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtatStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etatStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etatStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtatStock() throws Exception {
        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();
        etatStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtatStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etatStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtatStock() throws Exception {
        int databaseSizeBeforeUpdate = etatStockRepository.findAll().size();
        etatStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtatStockMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etatStock))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EtatStock in the database
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtatStock() throws Exception {
        // Initialize the database
        etatStockRepository.saveAndFlush(etatStock);

        int databaseSizeBeforeDelete = etatStockRepository.findAll().size();

        // Delete the etatStock
        restEtatStockMockMvc
            .perform(delete(ENTITY_API_URL_ID, etatStock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EtatStock> etatStockList = etatStockRepository.findAll();
        assertThat(etatStockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
