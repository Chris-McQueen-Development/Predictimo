package chris.mcqueen.development.predictimo.web.rest;

import chris.mcqueen.development.predictimo.PredictimoApp;

import chris.mcqueen.development.predictimo.domain.PredictionResponse;
import chris.mcqueen.development.predictimo.domain.UserProfile;
import chris.mcqueen.development.predictimo.domain.Prediction;
import chris.mcqueen.development.predictimo.repository.PredictionResponseRepository;
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
 * Test class for the PredictionResponseResource REST controller.
 *
 * @see PredictionResponseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PredictimoApp.class)
public class PredictionResponseResourceIntTest {

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    private static final LocalDate DEFAULT_RESPONSE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESPONSE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PredictionResponseRepository predictionResponseRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPredictionResponseMockMvc;

    private PredictionResponse predictionResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PredictionResponseResource predictionResponseResource = new PredictionResponseResource(predictionResponseRepository);
        this.restPredictionResponseMockMvc = MockMvcBuilders.standaloneSetup(predictionResponseResource)
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
    public static PredictionResponse createEntity(EntityManager em) {
        PredictionResponse predictionResponse = new PredictionResponse()
            .accepted(DEFAULT_ACCEPTED)
            .responseDate(DEFAULT_RESPONSE_DATE);
        // Add required entity
        UserProfile userProfile = UserProfileResourceIntTest.createEntity(em);
        em.persist(userProfile);
        em.flush();
        predictionResponse.setUserProfile(userProfile);
        // Add required entity
        Prediction prediction = PredictionResourceIntTest.createEntity(em);
        em.persist(prediction);
        em.flush();
        predictionResponse.setPrediction(prediction);
        return predictionResponse;
    }

    @Before
    public void initTest() {
        predictionResponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createPredictionResponse() throws Exception {
        int databaseSizeBeforeCreate = predictionResponseRepository.findAll().size();

        // Create the PredictionResponse
        restPredictionResponseMockMvc.perform(post("/api/prediction-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionResponse)))
            .andExpect(status().isCreated());

        // Validate the PredictionResponse in the database
        List<PredictionResponse> predictionResponseList = predictionResponseRepository.findAll();
        assertThat(predictionResponseList).hasSize(databaseSizeBeforeCreate + 1);
        PredictionResponse testPredictionResponse = predictionResponseList.get(predictionResponseList.size() - 1);
        assertThat(testPredictionResponse.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testPredictionResponse.getResponseDate()).isEqualTo(DEFAULT_RESPONSE_DATE);
    }

    @Test
    @Transactional
    public void createPredictionResponseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = predictionResponseRepository.findAll().size();

        // Create the PredictionResponse with an existing ID
        predictionResponse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPredictionResponseMockMvc.perform(post("/api/prediction-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionResponse)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PredictionResponse> predictionResponseList = predictionResponseRepository.findAll();
        assertThat(predictionResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAcceptedIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionResponseRepository.findAll().size();
        // set the field null
        predictionResponse.setAccepted(null);

        // Create the PredictionResponse, which fails.

        restPredictionResponseMockMvc.perform(post("/api/prediction-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionResponse)))
            .andExpect(status().isBadRequest());

        List<PredictionResponse> predictionResponseList = predictionResponseRepository.findAll();
        assertThat(predictionResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPredictionResponses() throws Exception {
        // Initialize the database
        predictionResponseRepository.saveAndFlush(predictionResponse);

        // Get all the predictionResponseList
        restPredictionResponseMockMvc.perform(get("/api/prediction-responses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(predictionResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].responseDate").value(hasItem(DEFAULT_RESPONSE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPredictionResponse() throws Exception {
        // Initialize the database
        predictionResponseRepository.saveAndFlush(predictionResponse);

        // Get the predictionResponse
        restPredictionResponseMockMvc.perform(get("/api/prediction-responses/{id}", predictionResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(predictionResponse.getId().intValue()))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.responseDate").value(DEFAULT_RESPONSE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPredictionResponse() throws Exception {
        // Get the predictionResponse
        restPredictionResponseMockMvc.perform(get("/api/prediction-responses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePredictionResponse() throws Exception {
        // Initialize the database
        predictionResponseRepository.saveAndFlush(predictionResponse);
        int databaseSizeBeforeUpdate = predictionResponseRepository.findAll().size();

        // Update the predictionResponse
        PredictionResponse updatedPredictionResponse = predictionResponseRepository.findOne(predictionResponse.getId());
        updatedPredictionResponse
            .accepted(UPDATED_ACCEPTED)
            .responseDate(UPDATED_RESPONSE_DATE);

        restPredictionResponseMockMvc.perform(put("/api/prediction-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPredictionResponse)))
            .andExpect(status().isOk());

        // Validate the PredictionResponse in the database
        List<PredictionResponse> predictionResponseList = predictionResponseRepository.findAll();
        assertThat(predictionResponseList).hasSize(databaseSizeBeforeUpdate);
        PredictionResponse testPredictionResponse = predictionResponseList.get(predictionResponseList.size() - 1);
        assertThat(testPredictionResponse.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testPredictionResponse.getResponseDate()).isEqualTo(UPDATED_RESPONSE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPredictionResponse() throws Exception {
        int databaseSizeBeforeUpdate = predictionResponseRepository.findAll().size();

        // Create the PredictionResponse

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPredictionResponseMockMvc.perform(put("/api/prediction-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionResponse)))
            .andExpect(status().isCreated());

        // Validate the PredictionResponse in the database
        List<PredictionResponse> predictionResponseList = predictionResponseRepository.findAll();
        assertThat(predictionResponseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePredictionResponse() throws Exception {
        // Initialize the database
        predictionResponseRepository.saveAndFlush(predictionResponse);
        int databaseSizeBeforeDelete = predictionResponseRepository.findAll().size();

        // Get the predictionResponse
        restPredictionResponseMockMvc.perform(delete("/api/prediction-responses/{id}", predictionResponse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PredictionResponse> predictionResponseList = predictionResponseRepository.findAll();
        assertThat(predictionResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PredictionResponse.class);
        PredictionResponse predictionResponse1 = new PredictionResponse();
        predictionResponse1.setId(1L);
        PredictionResponse predictionResponse2 = new PredictionResponse();
        predictionResponse2.setId(predictionResponse1.getId());
        assertThat(predictionResponse1).isEqualTo(predictionResponse2);
        predictionResponse2.setId(2L);
        assertThat(predictionResponse1).isNotEqualTo(predictionResponse2);
        predictionResponse1.setId(null);
        assertThat(predictionResponse1).isNotEqualTo(predictionResponse2);
    }
}
