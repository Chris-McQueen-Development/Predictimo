package chris.mcqueen.development.predictimo.web.rest;

import chris.mcqueen.development.predictimo.PredictimoApp;

import chris.mcqueen.development.predictimo.domain.PredictionType;
import chris.mcqueen.development.predictimo.repository.PredictionTypeRepository;
import chris.mcqueen.development.predictimo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PredictionTypeResource REST controller.
 *
 * @see PredictionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PredictimoApp.class)
public class PredictionTypeResourceIntTest {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TYPE_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TYPE_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PredictionTypeRepository predictionTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPredictionTypeMockMvc;

    private PredictionType predictionType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PredictionTypeResource predictionTypeResource = new PredictionTypeResource(predictionTypeRepository);
        this.restPredictionTypeMockMvc = MockMvcBuilders.standaloneSetup(predictionTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PredictionType createEntity(EntityManager em) {
        PredictionType predictionType = new PredictionType()
            .typeName(DEFAULT_TYPE_NAME)
            .typeDescription(DEFAULT_TYPE_DESCRIPTION)
            .typeExpirationDate(DEFAULT_TYPE_EXPIRATION_DATE);
        return predictionType;
    }

    @Before
    public void initTest() {
        predictionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPredictionType() throws Exception {
        int databaseSizeBeforeCreate = predictionTypeRepository.findAll().size();

        // Create the PredictionType
        restPredictionTypeMockMvc.perform(post("/api/prediction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionType)))
            .andExpect(status().isCreated());

        // Validate the PredictionType in the database
        List<PredictionType> predictionTypeList = predictionTypeRepository.findAll();
        assertThat(predictionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PredictionType testPredictionType = predictionTypeList.get(predictionTypeList.size() - 1);
        assertThat(testPredictionType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);
        assertThat(testPredictionType.getTypeDescription()).isEqualTo(DEFAULT_TYPE_DESCRIPTION);
        assertThat(testPredictionType.getTypeExpirationDate()).isEqualTo(DEFAULT_TYPE_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void createPredictionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = predictionTypeRepository.findAll().size();

        // Create the PredictionType with an existing ID
        predictionType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPredictionTypeMockMvc.perform(post("/api/prediction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PredictionType> predictionTypeList = predictionTypeRepository.findAll();
        assertThat(predictionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionTypeRepository.findAll().size();
        // set the field null
        predictionType.setTypeName(null);

        // Create the PredictionType, which fails.

        restPredictionTypeMockMvc.perform(post("/api/prediction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionType)))
            .andExpect(status().isBadRequest());

        List<PredictionType> predictionTypeList = predictionTypeRepository.findAll();
        assertThat(predictionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPredictionTypes() throws Exception {
        // Initialize the database
        predictionTypeRepository.saveAndFlush(predictionType);

        // Get all the predictionTypeList
        restPredictionTypeMockMvc.perform(get("/api/prediction-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(predictionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].typeDescription").value(hasItem(DEFAULT_TYPE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].typeExpirationDate").value(hasItem(DEFAULT_TYPE_EXPIRATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPredictionType() throws Exception {
        // Initialize the database
        predictionTypeRepository.saveAndFlush(predictionType);

        // Get the predictionType
        restPredictionTypeMockMvc.perform(get("/api/prediction-types/{id}", predictionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(predictionType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.typeDescription").value(DEFAULT_TYPE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.typeExpirationDate").value(DEFAULT_TYPE_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPredictionType() throws Exception {
        // Get the predictionType
        restPredictionTypeMockMvc.perform(get("/api/prediction-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePredictionType() throws Exception {
        // Initialize the database
        predictionTypeRepository.saveAndFlush(predictionType);
        int databaseSizeBeforeUpdate = predictionTypeRepository.findAll().size();

        // Update the predictionType
        PredictionType updatedPredictionType = predictionTypeRepository.findOne(predictionType.getId());
        updatedPredictionType
            .typeName(UPDATED_TYPE_NAME)
            .typeDescription(UPDATED_TYPE_DESCRIPTION)
            .typeExpirationDate(UPDATED_TYPE_EXPIRATION_DATE);

        restPredictionTypeMockMvc.perform(put("/api/prediction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPredictionType)))
            .andExpect(status().isOk());

        // Validate the PredictionType in the database
        List<PredictionType> predictionTypeList = predictionTypeRepository.findAll();
        assertThat(predictionTypeList).hasSize(databaseSizeBeforeUpdate);
        PredictionType testPredictionType = predictionTypeList.get(predictionTypeList.size() - 1);
        assertThat(testPredictionType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
        assertThat(testPredictionType.getTypeDescription()).isEqualTo(UPDATED_TYPE_DESCRIPTION);
        assertThat(testPredictionType.getTypeExpirationDate()).isEqualTo(UPDATED_TYPE_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPredictionType() throws Exception {
        int databaseSizeBeforeUpdate = predictionTypeRepository.findAll().size();

        // Create the PredictionType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPredictionTypeMockMvc.perform(put("/api/prediction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionType)))
            .andExpect(status().isCreated());

        // Validate the PredictionType in the database
        List<PredictionType> predictionTypeList = predictionTypeRepository.findAll();
        assertThat(predictionTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePredictionType() throws Exception {
        // Initialize the database
        predictionTypeRepository.saveAndFlush(predictionType);
        int databaseSizeBeforeDelete = predictionTypeRepository.findAll().size();

        // Get the predictionType
        restPredictionTypeMockMvc.perform(delete("/api/prediction-types/{id}", predictionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PredictionType> predictionTypeList = predictionTypeRepository.findAll();
        assertThat(predictionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PredictionType.class);
        PredictionType predictionType1 = new PredictionType();
        predictionType1.setId(1L);
        PredictionType predictionType2 = new PredictionType();
        predictionType2.setId(predictionType1.getId());
        assertThat(predictionType1).isEqualTo(predictionType2);
        predictionType2.setId(2L);
        assertThat(predictionType1).isNotEqualTo(predictionType2);
        predictionType1.setId(null);
        assertThat(predictionType1).isNotEqualTo(predictionType2);
    }
}
