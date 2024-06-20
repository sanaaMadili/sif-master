package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Reception;
import com.mycompany.myapp.repository.ReceptionRepository;
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
 * Integration tests for the {@link ReceptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceptionResourceIT {

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/receptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceptionMockMvc;

    private Reception reception;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reception createEntity(EntityManager em) {
        Reception reception = new Reception().pays(DEFAULT_PAYS).address(DEFAULT_ADDRESS);
        return reception;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reception createUpdatedEntity(EntityManager em) {
        Reception reception = new Reception().pays(UPDATED_PAYS).address(UPDATED_ADDRESS);
        return reception;
    }

    @BeforeEach
    public void initTest() {
        reception = createEntity(em);
    }

    @Test
    @Transactional
    void createReception() throws Exception {
        int databaseSizeBeforeCreate = receptionRepository.findAll().size();
        // Create the Reception
        restReceptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reception)))
            .andExpect(status().isCreated());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeCreate + 1);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testReception.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createReceptionWithExistingId() throws Exception {
        // Create the Reception with an existing ID
        reception.setId(1L);

        int databaseSizeBeforeCreate = receptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reception)))
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReceptions() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        // Get all the receptionList
        restReceptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reception.getId().intValue())))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getReception() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        // Get the reception
        restReceptionMockMvc
            .perform(get(ENTITY_API_URL_ID, reception.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reception.getId().intValue()))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingReception() throws Exception {
        // Get the reception
        restReceptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReception() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();

        // Update the reception
        Reception updatedReception = receptionRepository.findById(reception.getId()).get();
        // Disconnect from session so that the updates on updatedReception are not directly saved in db
        em.detach(updatedReception);
        updatedReception.pays(UPDATED_PAYS).address(UPDATED_ADDRESS);

        restReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReception.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReception))
            )
            .andExpect(status().isOk());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testReception.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reception.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reception)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceptionWithPatch() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();

        // Update the reception using partial update
        Reception partialUpdatedReception = new Reception();
        partialUpdatedReception.setId(reception.getId());

        partialUpdatedReception.pays(UPDATED_PAYS).address(UPDATED_ADDRESS);

        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReception))
            )
            .andExpect(status().isOk());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testReception.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateReceptionWithPatch() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();

        // Update the reception using partial update
        Reception partialUpdatedReception = new Reception();
        partialUpdatedReception.setId(reception.getId());

        partialUpdatedReception.pays(UPDATED_PAYS).address(UPDATED_ADDRESS);

        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReception))
            )
            .andExpect(status().isOk());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testReception.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReception() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeDelete = receptionRepository.findAll().size();

        // Delete the reception
        restReceptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reception.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
