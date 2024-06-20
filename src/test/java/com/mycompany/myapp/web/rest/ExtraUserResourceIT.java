package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExtraUser;
import com.mycompany.myapp.repository.ExtraUserRepository;
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
 * Integration tests for the {@link ExtraUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtraUserResourceIT {

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_NUMERO_TELEPHONE = 1D;
    private static final Double UPDATED_NUMERO_TELEPHONE = 2D;

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/extra-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExtraUserRepository extraUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtraUserMockMvc;

    private ExtraUser extraUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraUser createEntity(EntityManager em) {
        ExtraUser extraUser = new ExtraUser()
            .cin(DEFAULT_CIN)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .numeroTelephone(DEFAULT_NUMERO_TELEPHONE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .adresse(DEFAULT_ADRESSE)
            .pays(DEFAULT_PAYS);
        return extraUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraUser createUpdatedEntity(EntityManager em) {
        ExtraUser extraUser = new ExtraUser()
            .cin(UPDATED_CIN)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresse(UPDATED_ADRESSE)
            .pays(UPDATED_PAYS);
        return extraUser;
    }

    @BeforeEach
    public void initTest() {
        extraUser = createEntity(em);
    }

    @Test
    @Transactional
    void createExtraUser() throws Exception {
        int databaseSizeBeforeCreate = extraUserRepository.findAll().size();
        // Create the ExtraUser
        restExtraUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isCreated());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExtraUser testExtraUser = extraUserList.get(extraUserList.size() - 1);
        assertThat(testExtraUser.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testExtraUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExtraUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExtraUser.getNumeroTelephone()).isEqualTo(DEFAULT_NUMERO_TELEPHONE);
        assertThat(testExtraUser.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testExtraUser.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testExtraUser.getPays()).isEqualTo(DEFAULT_PAYS);
    }

    @Test
    @Transactional
    void createExtraUserWithExistingId() throws Exception {
        // Create the ExtraUser with an existing ID
        extraUser.setId(1L);

        int databaseSizeBeforeCreate = extraUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExtraUsers() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        // Get all the extraUserList
        restExtraUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].numeroTelephone").value(hasItem(DEFAULT_NUMERO_TELEPHONE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)));
    }

    @Test
    @Transactional
    void getExtraUser() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        // Get the extraUser
        restExtraUserMockMvc
            .perform(get(ENTITY_API_URL_ID, extraUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extraUser.getId().intValue()))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.numeroTelephone").value(DEFAULT_NUMERO_TELEPHONE.doubleValue()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS));
    }

    @Test
    @Transactional
    void getNonExistingExtraUser() throws Exception {
        // Get the extraUser
        restExtraUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtraUser() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();

        // Update the extraUser
        ExtraUser updatedExtraUser = extraUserRepository.findById(extraUser.getId()).get();
        // Disconnect from session so that the updates on updatedExtraUser are not directly saved in db
        em.detach(updatedExtraUser);
        updatedExtraUser
            .cin(UPDATED_CIN)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresse(UPDATED_ADRESSE)
            .pays(UPDATED_PAYS);

        restExtraUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtraUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExtraUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
        ExtraUser testExtraUser = extraUserList.get(extraUserList.size() - 1);
        assertThat(testExtraUser.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testExtraUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExtraUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExtraUser.getNumeroTelephone()).isEqualTo(UPDATED_NUMERO_TELEPHONE);
        assertThat(testExtraUser.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testExtraUser.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testExtraUser.getPays()).isEqualTo(UPDATED_PAYS);
    }

    @Test
    @Transactional
    void putNonExistingExtraUser() throws Exception {
        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();
        extraUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extraUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extraUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtraUser() throws Exception {
        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();
        extraUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extraUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtraUser() throws Exception {
        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();
        extraUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtraUserWithPatch() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();

        // Update the extraUser using partial update
        ExtraUser partialUpdatedExtraUser = new ExtraUser();
        partialUpdatedExtraUser.setId(extraUser.getId());

        partialUpdatedExtraUser.cin(UPDATED_CIN).dateNaissance(UPDATED_DATE_NAISSANCE);

        restExtraUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtraUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtraUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
        ExtraUser testExtraUser = extraUserList.get(extraUserList.size() - 1);
        assertThat(testExtraUser.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testExtraUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExtraUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExtraUser.getNumeroTelephone()).isEqualTo(DEFAULT_NUMERO_TELEPHONE);
        assertThat(testExtraUser.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testExtraUser.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testExtraUser.getPays()).isEqualTo(DEFAULT_PAYS);
    }

    @Test
    @Transactional
    void fullUpdateExtraUserWithPatch() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();

        // Update the extraUser using partial update
        ExtraUser partialUpdatedExtraUser = new ExtraUser();
        partialUpdatedExtraUser.setId(extraUser.getId());

        partialUpdatedExtraUser
            .cin(UPDATED_CIN)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .numeroTelephone(UPDATED_NUMERO_TELEPHONE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresse(UPDATED_ADRESSE)
            .pays(UPDATED_PAYS);

        restExtraUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtraUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtraUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
        ExtraUser testExtraUser = extraUserList.get(extraUserList.size() - 1);
        assertThat(testExtraUser.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testExtraUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExtraUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExtraUser.getNumeroTelephone()).isEqualTo(UPDATED_NUMERO_TELEPHONE);
        assertThat(testExtraUser.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testExtraUser.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testExtraUser.getPays()).isEqualTo(UPDATED_PAYS);
    }

    @Test
    @Transactional
    void patchNonExistingExtraUser() throws Exception {
        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();
        extraUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extraUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extraUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtraUser() throws Exception {
        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();
        extraUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extraUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtraUser() throws Exception {
        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();
        extraUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(extraUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtraUser() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        int databaseSizeBeforeDelete = extraUserRepository.findAll().size();

        // Delete the extraUser
        restExtraUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, extraUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
