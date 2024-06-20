package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Reclamation;
import com.mycompany.myapp.repository.ReclamationRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ReclamationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReclamationResourceIT {

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PIECE_JOINTE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIECE_JOINTE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PIECE_JOINTE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIECE_JOINTE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_RAISON = "AAAAAAAAAA";
    private static final String UPDATED_RAISON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reclamations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReclamationMockMvc;

    private Reclamation reclamation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .date(DEFAULT_DATE)
            .pieceJointe(DEFAULT_PIECE_JOINTE)
            .pieceJointeContentType(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)
            .raison(DEFAULT_RAISON);
        return reclamation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createUpdatedEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .date(UPDATED_DATE)
            .pieceJointe(UPDATED_PIECE_JOINTE)
            .pieceJointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE)
            .raison(UPDATED_RAISON);
        return reclamation;
    }

    @BeforeEach
    public void initTest() {
        reclamation = createEntity(em);
    }

    @Test
    @Transactional
    void createReclamation() throws Exception {
        int databaseSizeBeforeCreate = reclamationRepository.findAll().size();
        // Create the Reclamation
        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamation)))
            .andExpect(status().isCreated());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeCreate + 1);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReclamation.getPieceJointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testReclamation.getPieceJointeContentType()).isEqualTo(DEFAULT_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getRaison()).isEqualTo(DEFAULT_RAISON);
    }

    @Test
    @Transactional
    void createReclamationWithExistingId() throws Exception {
        // Create the Reclamation with an existing ID
        reclamation.setId(1L);

        int databaseSizeBeforeCreate = reclamationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamation)))
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReclamations() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].pieceJointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].pieceJointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))))
            .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON)));
    }

    @Test
    @Transactional
    void getReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get the reclamation
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL_ID, reclamation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reclamation.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.pieceJointeContentType").value(DEFAULT_PIECE_JOINTE_CONTENT_TYPE))
            .andExpect(jsonPath("$.pieceJointe").value(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE)))
            .andExpect(jsonPath("$.raison").value(DEFAULT_RAISON));
    }

    @Test
    @Transactional
    void getNonExistingReclamation() throws Exception {
        // Get the reclamation
        restReclamationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation
        Reclamation updatedReclamation = reclamationRepository.findById(reclamation.getId()).get();
        // Disconnect from session so that the updates on updatedReclamation are not directly saved in db
        em.detach(updatedReclamation);
        updatedReclamation
            .date(UPDATED_DATE)
            .pieceJointe(UPDATED_PIECE_JOINTE)
            .pieceJointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE)
            .raison(UPDATED_RAISON);

        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReclamation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReclamation.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testReclamation.getPieceJointeContentType()).isEqualTo(UPDATED_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getRaison()).isEqualTo(UPDATED_RAISON);
    }

    @Test
    @Transactional
    void putNonExistingReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reclamation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation
            .date(UPDATED_DATE)
            .pieceJointe(UPDATED_PIECE_JOINTE)
            .pieceJointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE)
            .raison(UPDATED_RAISON);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReclamation.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testReclamation.getPieceJointeContentType()).isEqualTo(UPDATED_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getRaison()).isEqualTo(UPDATED_RAISON);
    }

    @Test
    @Transactional
    void fullUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation
            .date(UPDATED_DATE)
            .pieceJointe(UPDATED_PIECE_JOINTE)
            .pieceJointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE)
            .raison(UPDATED_RAISON);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReclamation.getPieceJointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testReclamation.getPieceJointeContentType()).isEqualTo(UPDATED_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getRaison()).isEqualTo(UPDATED_RAISON);
    }

    @Test
    @Transactional
    void patchNonExistingReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeDelete = reclamationRepository.findAll().size();

        // Delete the reclamation
        restReclamationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reclamation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
