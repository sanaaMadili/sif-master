package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AppelCommande;
import com.mycompany.myapp.repository.AppelCommandeRepository;
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

/**
 * Integration tests for the {@link AppelCommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppelCommandeResourceIT {

    private static final Integer DEFAULT_NUM_COMMANDE = 1;
    private static final Integer UPDATED_NUM_COMMANDE = 2;

    private static final LocalDate DEFAULT_DATE_COMMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_LIVRAISON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIVRAISON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EXPEDITION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXPEDITION = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Integer DEFAULT_ANNOMALIE = 1;
    private static final Integer UPDATED_ANNOMALIE = 2;

    private static final String ENTITY_API_URL = "/api/appel-commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppelCommandeRepository appelCommandeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppelCommandeMockMvc;

    private AppelCommande appelCommande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppelCommande createEntity(EntityManager em) {
        AppelCommande appelCommande = new AppelCommande()
            .numCommande(DEFAULT_NUM_COMMANDE)
            .dateCommande(DEFAULT_DATE_COMMANDE)
            .dateLivraison(DEFAULT_DATE_LIVRAISON)
            .dateExpedition(DEFAULT_DATE_EXPEDITION)
            .status(DEFAULT_STATUS)
            .annomalie(DEFAULT_ANNOMALIE);
        return appelCommande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppelCommande createUpdatedEntity(EntityManager em) {
        AppelCommande appelCommande = new AppelCommande()
            .numCommande(UPDATED_NUM_COMMANDE)
            .dateCommande(UPDATED_DATE_COMMANDE)
            .dateLivraison(UPDATED_DATE_LIVRAISON)
            .dateExpedition(UPDATED_DATE_EXPEDITION)
            .status(UPDATED_STATUS)
            .annomalie(UPDATED_ANNOMALIE);
        return appelCommande;
    }

    @BeforeEach
    public void initTest() {
        appelCommande = createEntity(em);
    }

    @Test
    @Transactional
    void createAppelCommande() throws Exception {
        int databaseSizeBeforeCreate = appelCommandeRepository.findAll().size();
        // Create the AppelCommande
        restAppelCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appelCommande)))
            .andExpect(status().isCreated());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeCreate + 1);
        AppelCommande testAppelCommande = appelCommandeList.get(appelCommandeList.size() - 1);
        assertThat(testAppelCommande.getNumCommande()).isEqualTo(DEFAULT_NUM_COMMANDE);
        assertThat(testAppelCommande.getDateCommande()).isEqualTo(DEFAULT_DATE_COMMANDE);
        assertThat(testAppelCommande.getDateLivraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
        assertThat(testAppelCommande.getDateExpedition()).isEqualTo(DEFAULT_DATE_EXPEDITION);
        assertThat(testAppelCommande.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAppelCommande.getAnnomalie()).isEqualTo(DEFAULT_ANNOMALIE);
    }

    @Test
    @Transactional
    void createAppelCommandeWithExistingId() throws Exception {
        // Create the AppelCommande with an existing ID
        appelCommande.setId(1L);

        int databaseSizeBeforeCreate = appelCommandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppelCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appelCommande)))
            .andExpect(status().isBadRequest());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppelCommandes() throws Exception {
        // Initialize the database
        appelCommandeRepository.saveAndFlush(appelCommande);

        // Get all the appelCommandeList
        restAppelCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appelCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numCommande").value(hasItem(DEFAULT_NUM_COMMANDE)))
            .andExpect(jsonPath("$.[*].dateCommande").value(hasItem(DEFAULT_DATE_COMMANDE.toString())))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].dateExpedition").value(hasItem(DEFAULT_DATE_EXPEDITION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].annomalie").value(hasItem(DEFAULT_ANNOMALIE)));
    }

    @Test
    @Transactional
    void getAppelCommande() throws Exception {
        // Initialize the database
        appelCommandeRepository.saveAndFlush(appelCommande);

        // Get the appelCommande
        restAppelCommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, appelCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appelCommande.getId().intValue()))
            .andExpect(jsonPath("$.numCommande").value(DEFAULT_NUM_COMMANDE))
            .andExpect(jsonPath("$.dateCommande").value(DEFAULT_DATE_COMMANDE.toString()))
            .andExpect(jsonPath("$.dateLivraison").value(DEFAULT_DATE_LIVRAISON.toString()))
            .andExpect(jsonPath("$.dateExpedition").value(DEFAULT_DATE_EXPEDITION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.annomalie").value(DEFAULT_ANNOMALIE));
    }

    @Test
    @Transactional
    void getNonExistingAppelCommande() throws Exception {
        // Get the appelCommande
        restAppelCommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppelCommande() throws Exception {
        // Initialize the database
        appelCommandeRepository.saveAndFlush(appelCommande);

        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();

        // Update the appelCommande
        AppelCommande updatedAppelCommande = appelCommandeRepository.findById(appelCommande.getId()).get();
        // Disconnect from session so that the updates on updatedAppelCommande are not directly saved in db
        em.detach(updatedAppelCommande);
        updatedAppelCommande
            .numCommande(UPDATED_NUM_COMMANDE)
            .dateCommande(UPDATED_DATE_COMMANDE)
            .dateLivraison(UPDATED_DATE_LIVRAISON)
            .dateExpedition(UPDATED_DATE_EXPEDITION)
            .status(UPDATED_STATUS)
            .annomalie(UPDATED_ANNOMALIE);

        restAppelCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppelCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppelCommande))
            )
            .andExpect(status().isOk());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
        AppelCommande testAppelCommande = appelCommandeList.get(appelCommandeList.size() - 1);
        assertThat(testAppelCommande.getNumCommande()).isEqualTo(UPDATED_NUM_COMMANDE);
        assertThat(testAppelCommande.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
        assertThat(testAppelCommande.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testAppelCommande.getDateExpedition()).isEqualTo(UPDATED_DATE_EXPEDITION);
        assertThat(testAppelCommande.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAppelCommande.getAnnomalie()).isEqualTo(UPDATED_ANNOMALIE);
    }

    @Test
    @Transactional
    void putNonExistingAppelCommande() throws Exception {
        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();
        appelCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppelCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appelCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appelCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppelCommande() throws Exception {
        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();
        appelCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppelCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appelCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppelCommande() throws Exception {
        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();
        appelCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppelCommandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appelCommande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppelCommandeWithPatch() throws Exception {
        // Initialize the database
        appelCommandeRepository.saveAndFlush(appelCommande);

        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();

        // Update the appelCommande using partial update
        AppelCommande partialUpdatedAppelCommande = new AppelCommande();
        partialUpdatedAppelCommande.setId(appelCommande.getId());

        partialUpdatedAppelCommande.dateCommande(UPDATED_DATE_COMMANDE).dateLivraison(UPDATED_DATE_LIVRAISON).status(UPDATED_STATUS);

        restAppelCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppelCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppelCommande))
            )
            .andExpect(status().isOk());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
        AppelCommande testAppelCommande = appelCommandeList.get(appelCommandeList.size() - 1);
        assertThat(testAppelCommande.getNumCommande()).isEqualTo(DEFAULT_NUM_COMMANDE);
        assertThat(testAppelCommande.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
        assertThat(testAppelCommande.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testAppelCommande.getDateExpedition()).isEqualTo(DEFAULT_DATE_EXPEDITION);
        assertThat(testAppelCommande.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAppelCommande.getAnnomalie()).isEqualTo(DEFAULT_ANNOMALIE);
    }

    @Test
    @Transactional
    void fullUpdateAppelCommandeWithPatch() throws Exception {
        // Initialize the database
        appelCommandeRepository.saveAndFlush(appelCommande);

        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();

        // Update the appelCommande using partial update
        AppelCommande partialUpdatedAppelCommande = new AppelCommande();
        partialUpdatedAppelCommande.setId(appelCommande.getId());

        partialUpdatedAppelCommande
            .numCommande(UPDATED_NUM_COMMANDE)
            .dateCommande(UPDATED_DATE_COMMANDE)
            .dateLivraison(UPDATED_DATE_LIVRAISON)
            .dateExpedition(UPDATED_DATE_EXPEDITION)
            .status(UPDATED_STATUS)
            .annomalie(UPDATED_ANNOMALIE);

        restAppelCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppelCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppelCommande))
            )
            .andExpect(status().isOk());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
        AppelCommande testAppelCommande = appelCommandeList.get(appelCommandeList.size() - 1);
        assertThat(testAppelCommande.getNumCommande()).isEqualTo(UPDATED_NUM_COMMANDE);
        assertThat(testAppelCommande.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
        assertThat(testAppelCommande.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testAppelCommande.getDateExpedition()).isEqualTo(UPDATED_DATE_EXPEDITION);
        assertThat(testAppelCommande.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAppelCommande.getAnnomalie()).isEqualTo(UPDATED_ANNOMALIE);
    }

    @Test
    @Transactional
    void patchNonExistingAppelCommande() throws Exception {
        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();
        appelCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppelCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appelCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appelCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppelCommande() throws Exception {
        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();
        appelCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppelCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appelCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppelCommande() throws Exception {
        int databaseSizeBeforeUpdate = appelCommandeRepository.findAll().size();
        appelCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppelCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appelCommande))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppelCommande in the database
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppelCommande() throws Exception {
        // Initialize the database
        appelCommandeRepository.saveAndFlush(appelCommande);

        int databaseSizeBeforeDelete = appelCommandeRepository.findAll().size();

        // Delete the appelCommande
        restAppelCommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, appelCommande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppelCommande> appelCommandeList = appelCommandeRepository.findAll();
        assertThat(appelCommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
