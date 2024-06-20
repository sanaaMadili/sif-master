package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Coe;
import com.mycompany.myapp.repository.CoeRepository;
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
 * Integration tests for the {@link CoeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoeResourceIT {

    private static final String DEFAULT_TYPE_VOITURE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_VOITURE = "BBBBBBBBBB";

    private static final Double DEFAULT_POIDS_VOITURE = 1D;
    private static final Double UPDATED_POIDS_VOITURE = 2D;

    private static final Double DEFAULT_VITESSE_VOITURE = 1D;
    private static final Double UPDATED_VITESSE_VOITURE = 2D;

    private static final String ENTITY_API_URL = "/api/coes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoeRepository coeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoeMockMvc;

    private Coe coe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coe createEntity(EntityManager em) {
        Coe coe = new Coe().typeVoiture(DEFAULT_TYPE_VOITURE).poidsVoiture(DEFAULT_POIDS_VOITURE).vitesseVoiture(DEFAULT_VITESSE_VOITURE);
        return coe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coe createUpdatedEntity(EntityManager em) {
        Coe coe = new Coe().typeVoiture(UPDATED_TYPE_VOITURE).poidsVoiture(UPDATED_POIDS_VOITURE).vitesseVoiture(UPDATED_VITESSE_VOITURE);
        return coe;
    }

    @BeforeEach
    public void initTest() {
        coe = createEntity(em);
    }

    @Test
    @Transactional
    void createCoe() throws Exception {
        int databaseSizeBeforeCreate = coeRepository.findAll().size();
        // Create the Coe
        restCoeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coe)))
            .andExpect(status().isCreated());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeCreate + 1);
        Coe testCoe = coeList.get(coeList.size() - 1);
        assertThat(testCoe.getTypeVoiture()).isEqualTo(DEFAULT_TYPE_VOITURE);
        assertThat(testCoe.getPoidsVoiture()).isEqualTo(DEFAULT_POIDS_VOITURE);
        assertThat(testCoe.getVitesseVoiture()).isEqualTo(DEFAULT_VITESSE_VOITURE);
    }

    @Test
    @Transactional
    void createCoeWithExistingId() throws Exception {
        // Create the Coe with an existing ID
        coe.setId(1L);

        int databaseSizeBeforeCreate = coeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coe)))
            .andExpect(status().isBadRequest());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoes() throws Exception {
        // Initialize the database
        coeRepository.saveAndFlush(coe);

        // Get all the coeList
        restCoeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coe.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeVoiture").value(hasItem(DEFAULT_TYPE_VOITURE)))
            .andExpect(jsonPath("$.[*].poidsVoiture").value(hasItem(DEFAULT_POIDS_VOITURE.doubleValue())))
            .andExpect(jsonPath("$.[*].vitesseVoiture").value(hasItem(DEFAULT_VITESSE_VOITURE.doubleValue())));
    }

    @Test
    @Transactional
    void getCoe() throws Exception {
        // Initialize the database
        coeRepository.saveAndFlush(coe);

        // Get the coe
        restCoeMockMvc
            .perform(get(ENTITY_API_URL_ID, coe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coe.getId().intValue()))
            .andExpect(jsonPath("$.typeVoiture").value(DEFAULT_TYPE_VOITURE))
            .andExpect(jsonPath("$.poidsVoiture").value(DEFAULT_POIDS_VOITURE.doubleValue()))
            .andExpect(jsonPath("$.vitesseVoiture").value(DEFAULT_VITESSE_VOITURE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCoe() throws Exception {
        // Get the coe
        restCoeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoe() throws Exception {
        // Initialize the database
        coeRepository.saveAndFlush(coe);

        int databaseSizeBeforeUpdate = coeRepository.findAll().size();

        // Update the coe
        Coe updatedCoe = coeRepository.findById(coe.getId()).get();
        // Disconnect from session so that the updates on updatedCoe are not directly saved in db
        em.detach(updatedCoe);
        updatedCoe.typeVoiture(UPDATED_TYPE_VOITURE).poidsVoiture(UPDATED_POIDS_VOITURE).vitesseVoiture(UPDATED_VITESSE_VOITURE);

        restCoeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCoe))
            )
            .andExpect(status().isOk());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
        Coe testCoe = coeList.get(coeList.size() - 1);
        assertThat(testCoe.getTypeVoiture()).isEqualTo(UPDATED_TYPE_VOITURE);
        assertThat(testCoe.getPoidsVoiture()).isEqualTo(UPDATED_POIDS_VOITURE);
        assertThat(testCoe.getVitesseVoiture()).isEqualTo(UPDATED_VITESSE_VOITURE);
    }

    @Test
    @Transactional
    void putNonExistingCoe() throws Exception {
        int databaseSizeBeforeUpdate = coeRepository.findAll().size();
        coe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coe.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoe() throws Exception {
        int databaseSizeBeforeUpdate = coeRepository.findAll().size();
        coe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoe() throws Exception {
        int databaseSizeBeforeUpdate = coeRepository.findAll().size();
        coe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoeWithPatch() throws Exception {
        // Initialize the database
        coeRepository.saveAndFlush(coe);

        int databaseSizeBeforeUpdate = coeRepository.findAll().size();

        // Update the coe using partial update
        Coe partialUpdatedCoe = new Coe();
        partialUpdatedCoe.setId(coe.getId());

        partialUpdatedCoe.typeVoiture(UPDATED_TYPE_VOITURE);

        restCoeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoe))
            )
            .andExpect(status().isOk());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
        Coe testCoe = coeList.get(coeList.size() - 1);
        assertThat(testCoe.getTypeVoiture()).isEqualTo(UPDATED_TYPE_VOITURE);
        assertThat(testCoe.getPoidsVoiture()).isEqualTo(DEFAULT_POIDS_VOITURE);
        assertThat(testCoe.getVitesseVoiture()).isEqualTo(DEFAULT_VITESSE_VOITURE);
    }

    @Test
    @Transactional
    void fullUpdateCoeWithPatch() throws Exception {
        // Initialize the database
        coeRepository.saveAndFlush(coe);

        int databaseSizeBeforeUpdate = coeRepository.findAll().size();

        // Update the coe using partial update
        Coe partialUpdatedCoe = new Coe();
        partialUpdatedCoe.setId(coe.getId());

        partialUpdatedCoe.typeVoiture(UPDATED_TYPE_VOITURE).poidsVoiture(UPDATED_POIDS_VOITURE).vitesseVoiture(UPDATED_VITESSE_VOITURE);

        restCoeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoe))
            )
            .andExpect(status().isOk());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
        Coe testCoe = coeList.get(coeList.size() - 1);
        assertThat(testCoe.getTypeVoiture()).isEqualTo(UPDATED_TYPE_VOITURE);
        assertThat(testCoe.getPoidsVoiture()).isEqualTo(UPDATED_POIDS_VOITURE);
        assertThat(testCoe.getVitesseVoiture()).isEqualTo(UPDATED_VITESSE_VOITURE);
    }

    @Test
    @Transactional
    void patchNonExistingCoe() throws Exception {
        int databaseSizeBeforeUpdate = coeRepository.findAll().size();
        coe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoe() throws Exception {
        int databaseSizeBeforeUpdate = coeRepository.findAll().size();
        coe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoe() throws Exception {
        int databaseSizeBeforeUpdate = coeRepository.findAll().size();
        coe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coe in the database
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoe() throws Exception {
        // Initialize the database
        coeRepository.saveAndFlush(coe);

        int databaseSizeBeforeDelete = coeRepository.findAll().size();

        // Delete the coe
        restCoeMockMvc.perform(delete(ENTITY_API_URL_ID, coe.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Coe> coeList = coeRepository.findAll();
        assertThat(coeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
