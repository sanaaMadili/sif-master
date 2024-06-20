package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Clr;
import com.mycompany.myapp.repository.ClrRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ClrResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClrResourceIT {

    private static final String DEFAULT_CONSTRUCTEUR_AUTOMOBILE = "AAAAAAAAAA";
    private static final String UPDATED_CONSTRUCTEUR_AUTOMOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_VOITURE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_VOITURE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNEE_VOITURE = 1;
    private static final Integer UPDATED_ANNEE_VOITURE = 2;

    private static final String DEFAULT_ETAT_PNEU = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_PNEU = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE_PRODUCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PRODUCTION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/clrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClrRepository clrRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClrMockMvc;

    private Clr clr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clr createEntity(EntityManager em) {
        Clr clr = new Clr()
            .constructeurAutomobile(DEFAULT_CONSTRUCTEUR_AUTOMOBILE)
            .modelVoiture(DEFAULT_MODEL_VOITURE)
            .anneeVoiture(DEFAULT_ANNEE_VOITURE)
            .etatPneu(DEFAULT_ETAT_PNEU)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .dateProduction(DEFAULT_DATE_PRODUCTION);
        return clr;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clr createUpdatedEntity(EntityManager em) {
        Clr clr = new Clr()
            .constructeurAutomobile(UPDATED_CONSTRUCTEUR_AUTOMOBILE)
            .modelVoiture(UPDATED_MODEL_VOITURE)
            .anneeVoiture(UPDATED_ANNEE_VOITURE)
            .etatPneu(UPDATED_ETAT_PNEU)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .dateProduction(UPDATED_DATE_PRODUCTION);
        return clr;
    }

    @BeforeEach
    public void initTest() {
        clr = createEntity(em);
    }

    @Test
    @Transactional
    void createClr() throws Exception {
        int databaseSizeBeforeCreate = clrRepository.findAll().size();
        // Create the Clr
        restClrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clr)))
            .andExpect(status().isCreated());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeCreate + 1);
        Clr testClr = clrList.get(clrList.size() - 1);
        assertThat(testClr.getConstructeurAutomobile()).isEqualTo(DEFAULT_CONSTRUCTEUR_AUTOMOBILE);
        assertThat(testClr.getModelVoiture()).isEqualTo(DEFAULT_MODEL_VOITURE);
        assertThat(testClr.getAnneeVoiture()).isEqualTo(DEFAULT_ANNEE_VOITURE);
        assertThat(testClr.getEtatPneu()).isEqualTo(DEFAULT_ETAT_PNEU);
        assertThat(testClr.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testClr.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testClr.getDateProduction()).isEqualTo(DEFAULT_DATE_PRODUCTION);
    }

    @Test
    @Transactional
    void createClrWithExistingId() throws Exception {
        // Create the Clr with an existing ID
        clr.setId(1L);

        int databaseSizeBeforeCreate = clrRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clr)))
            .andExpect(status().isBadRequest());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClrs() throws Exception {
        // Initialize the database
        clrRepository.saveAndFlush(clr);

        // Get all the clrList
        restClrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clr.getId().intValue())))
            .andExpect(jsonPath("$.[*].constructeurAutomobile").value(hasItem(DEFAULT_CONSTRUCTEUR_AUTOMOBILE)))
            .andExpect(jsonPath("$.[*].modelVoiture").value(hasItem(DEFAULT_MODEL_VOITURE)))
            .andExpect(jsonPath("$.[*].anneeVoiture").value(hasItem(DEFAULT_ANNEE_VOITURE)))
            .andExpect(jsonPath("$.[*].etatPneu").value(hasItem(DEFAULT_ETAT_PNEU)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].dateProduction").value(hasItem(DEFAULT_DATE_PRODUCTION.toString())));
    }

    @Test
    @Transactional
    void getClr() throws Exception {
        // Initialize the database
        clrRepository.saveAndFlush(clr);

        // Get the clr
        restClrMockMvc
            .perform(get(ENTITY_API_URL_ID, clr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clr.getId().intValue()))
            .andExpect(jsonPath("$.constructeurAutomobile").value(DEFAULT_CONSTRUCTEUR_AUTOMOBILE))
            .andExpect(jsonPath("$.modelVoiture").value(DEFAULT_MODEL_VOITURE))
            .andExpect(jsonPath("$.anneeVoiture").value(DEFAULT_ANNEE_VOITURE))
            .andExpect(jsonPath("$.etatPneu").value(DEFAULT_ETAT_PNEU))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.dateProduction").value(DEFAULT_DATE_PRODUCTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClr() throws Exception {
        // Get the clr
        restClrMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClr() throws Exception {
        // Initialize the database
        clrRepository.saveAndFlush(clr);

        int databaseSizeBeforeUpdate = clrRepository.findAll().size();

        // Update the clr
        Clr updatedClr = clrRepository.findById(clr.getId()).get();
        // Disconnect from session so that the updates on updatedClr are not directly saved in db
        em.detach(updatedClr);
        updatedClr
            .constructeurAutomobile(UPDATED_CONSTRUCTEUR_AUTOMOBILE)
            .modelVoiture(UPDATED_MODEL_VOITURE)
            .anneeVoiture(UPDATED_ANNEE_VOITURE)
            .etatPneu(UPDATED_ETAT_PNEU)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .dateProduction(UPDATED_DATE_PRODUCTION);

        restClrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClr.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClr))
            )
            .andExpect(status().isOk());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
        Clr testClr = clrList.get(clrList.size() - 1);
        assertThat(testClr.getConstructeurAutomobile()).isEqualTo(UPDATED_CONSTRUCTEUR_AUTOMOBILE);
        assertThat(testClr.getModelVoiture()).isEqualTo(UPDATED_MODEL_VOITURE);
        assertThat(testClr.getAnneeVoiture()).isEqualTo(UPDATED_ANNEE_VOITURE);
        assertThat(testClr.getEtatPneu()).isEqualTo(UPDATED_ETAT_PNEU);
        assertThat(testClr.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testClr.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testClr.getDateProduction()).isEqualTo(UPDATED_DATE_PRODUCTION);
    }

    @Test
    @Transactional
    void putNonExistingClr() throws Exception {
        int databaseSizeBeforeUpdate = clrRepository.findAll().size();
        clr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clr.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClr() throws Exception {
        int databaseSizeBeforeUpdate = clrRepository.findAll().size();
        clr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClr() throws Exception {
        int databaseSizeBeforeUpdate = clrRepository.findAll().size();
        clr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClrMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clr)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClrWithPatch() throws Exception {
        // Initialize the database
        clrRepository.saveAndFlush(clr);

        int databaseSizeBeforeUpdate = clrRepository.findAll().size();

        // Update the clr using partial update
        Clr partialUpdatedClr = new Clr();
        partialUpdatedClr.setId(clr.getId());

        partialUpdatedClr.modelVoiture(UPDATED_MODEL_VOITURE).dateProduction(UPDATED_DATE_PRODUCTION);

        restClrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClr))
            )
            .andExpect(status().isOk());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
        Clr testClr = clrList.get(clrList.size() - 1);
        assertThat(testClr.getConstructeurAutomobile()).isEqualTo(DEFAULT_CONSTRUCTEUR_AUTOMOBILE);
        assertThat(testClr.getModelVoiture()).isEqualTo(UPDATED_MODEL_VOITURE);
        assertThat(testClr.getAnneeVoiture()).isEqualTo(DEFAULT_ANNEE_VOITURE);
        assertThat(testClr.getEtatPneu()).isEqualTo(DEFAULT_ETAT_PNEU);
        assertThat(testClr.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testClr.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testClr.getDateProduction()).isEqualTo(UPDATED_DATE_PRODUCTION);
    }

    @Test
    @Transactional
    void fullUpdateClrWithPatch() throws Exception {
        // Initialize the database
        clrRepository.saveAndFlush(clr);

        int databaseSizeBeforeUpdate = clrRepository.findAll().size();

        // Update the clr using partial update
        Clr partialUpdatedClr = new Clr();
        partialUpdatedClr.setId(clr.getId());

        partialUpdatedClr
            .constructeurAutomobile(UPDATED_CONSTRUCTEUR_AUTOMOBILE)
            .modelVoiture(UPDATED_MODEL_VOITURE)
            .anneeVoiture(UPDATED_ANNEE_VOITURE)
            .etatPneu(UPDATED_ETAT_PNEU)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .dateProduction(UPDATED_DATE_PRODUCTION);

        restClrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClr))
            )
            .andExpect(status().isOk());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
        Clr testClr = clrList.get(clrList.size() - 1);
        assertThat(testClr.getConstructeurAutomobile()).isEqualTo(UPDATED_CONSTRUCTEUR_AUTOMOBILE);
        assertThat(testClr.getModelVoiture()).isEqualTo(UPDATED_MODEL_VOITURE);
        assertThat(testClr.getAnneeVoiture()).isEqualTo(UPDATED_ANNEE_VOITURE);
        assertThat(testClr.getEtatPneu()).isEqualTo(UPDATED_ETAT_PNEU);
        assertThat(testClr.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testClr.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testClr.getDateProduction()).isEqualTo(UPDATED_DATE_PRODUCTION);
    }

    @Test
    @Transactional
    void patchNonExistingClr() throws Exception {
        int databaseSizeBeforeUpdate = clrRepository.findAll().size();
        clr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClr() throws Exception {
        int databaseSizeBeforeUpdate = clrRepository.findAll().size();
        clr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClr() throws Exception {
        int databaseSizeBeforeUpdate = clrRepository.findAll().size();
        clr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClrMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clr)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clr in the database
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClr() throws Exception {
        // Initialize the database
        clrRepository.saveAndFlush(clr);

        int databaseSizeBeforeDelete = clrRepository.findAll().size();

        // Delete the clr
        restClrMockMvc.perform(delete(ENTITY_API_URL_ID, clr.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clr> clrList = clrRepository.findAll();
        assertThat(clrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
