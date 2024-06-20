package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Magasinier;
import com.mycompany.myapp.repository.MagasinierRepository;
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
 * Integration tests for the {@link MagasinierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MagasinierResourceIT {

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/magasiniers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MagasinierRepository magasinierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMagasinierMockMvc;

    private Magasinier magasinier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magasinier createEntity(EntityManager em) {
        Magasinier magasinier = new Magasinier().profession(DEFAULT_PROFESSION);
        return magasinier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magasinier createUpdatedEntity(EntityManager em) {
        Magasinier magasinier = new Magasinier().profession(UPDATED_PROFESSION);
        return magasinier;
    }

    @BeforeEach
    public void initTest() {
        magasinier = createEntity(em);
    }

    @Test
    @Transactional
    void createMagasinier() throws Exception {
        int databaseSizeBeforeCreate = magasinierRepository.findAll().size();
        // Create the Magasinier
        restMagasinierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(magasinier)))
            .andExpect(status().isCreated());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeCreate + 1);
        Magasinier testMagasinier = magasinierList.get(magasinierList.size() - 1);
        assertThat(testMagasinier.getProfession()).isEqualTo(DEFAULT_PROFESSION);
    }

    @Test
    @Transactional
    void createMagasinierWithExistingId() throws Exception {
        // Create the Magasinier with an existing ID
        magasinier.setId(1L);

        int databaseSizeBeforeCreate = magasinierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagasinierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(magasinier)))
            .andExpect(status().isBadRequest());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMagasiniers() throws Exception {
        // Initialize the database
        magasinierRepository.saveAndFlush(magasinier);

        // Get all the magasinierList
        restMagasinierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magasinier.getId().intValue())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)));
    }

    @Test
    @Transactional
    void getMagasinier() throws Exception {
        // Initialize the database
        magasinierRepository.saveAndFlush(magasinier);

        // Get the magasinier
        restMagasinierMockMvc
            .perform(get(ENTITY_API_URL_ID, magasinier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(magasinier.getId().intValue()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION));
    }

    @Test
    @Transactional
    void getNonExistingMagasinier() throws Exception {
        // Get the magasinier
        restMagasinierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMagasinier() throws Exception {
        // Initialize the database
        magasinierRepository.saveAndFlush(magasinier);

        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();

        // Update the magasinier
        Magasinier updatedMagasinier = magasinierRepository.findById(magasinier.getId()).get();
        // Disconnect from session so that the updates on updatedMagasinier are not directly saved in db
        em.detach(updatedMagasinier);
        updatedMagasinier.profession(UPDATED_PROFESSION);

        restMagasinierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMagasinier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMagasinier))
            )
            .andExpect(status().isOk());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
        Magasinier testMagasinier = magasinierList.get(magasinierList.size() - 1);
        assertThat(testMagasinier.getProfession()).isEqualTo(UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void putNonExistingMagasinier() throws Exception {
        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();
        magasinier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagasinierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, magasinier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magasinier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMagasinier() throws Exception {
        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();
        magasinier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagasinierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(magasinier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMagasinier() throws Exception {
        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();
        magasinier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagasinierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(magasinier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMagasinierWithPatch() throws Exception {
        // Initialize the database
        magasinierRepository.saveAndFlush(magasinier);

        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();

        // Update the magasinier using partial update
        Magasinier partialUpdatedMagasinier = new Magasinier();
        partialUpdatedMagasinier.setId(magasinier.getId());

        restMagasinierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagasinier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMagasinier))
            )
            .andExpect(status().isOk());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
        Magasinier testMagasinier = magasinierList.get(magasinierList.size() - 1);
        assertThat(testMagasinier.getProfession()).isEqualTo(DEFAULT_PROFESSION);
    }

    @Test
    @Transactional
    void fullUpdateMagasinierWithPatch() throws Exception {
        // Initialize the database
        magasinierRepository.saveAndFlush(magasinier);

        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();

        // Update the magasinier using partial update
        Magasinier partialUpdatedMagasinier = new Magasinier();
        partialUpdatedMagasinier.setId(magasinier.getId());

        partialUpdatedMagasinier.profession(UPDATED_PROFESSION);

        restMagasinierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagasinier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMagasinier))
            )
            .andExpect(status().isOk());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
        Magasinier testMagasinier = magasinierList.get(magasinierList.size() - 1);
        assertThat(testMagasinier.getProfession()).isEqualTo(UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void patchNonExistingMagasinier() throws Exception {
        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();
        magasinier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagasinierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, magasinier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(magasinier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMagasinier() throws Exception {
        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();
        magasinier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagasinierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(magasinier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMagasinier() throws Exception {
        int databaseSizeBeforeUpdate = magasinierRepository.findAll().size();
        magasinier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagasinierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(magasinier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Magasinier in the database
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMagasinier() throws Exception {
        // Initialize the database
        magasinierRepository.saveAndFlush(magasinier);

        int databaseSizeBeforeDelete = magasinierRepository.findAll().size();

        // Delete the magasinier
        restMagasinierMockMvc
            .perform(delete(ENTITY_API_URL_ID, magasinier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Magasinier> magasinierList = magasinierRepository.findAll();
        assertThat(magasinierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
