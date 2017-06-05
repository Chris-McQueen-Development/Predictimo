package chris.mcqueen.development.predictimo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

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

import chris.mcqueen.development.predictimo.PredictimoApp;
import chris.mcqueen.development.predictimo.domain.Prediction;
import chris.mcqueen.development.predictimo.repository.PredictionPollRepository;
import chris.mcqueen.development.predictimo.repository.PredictionRepository;
import chris.mcqueen.development.predictimo.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the PredictionResource REST controller.
 *
 * @see PredictionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PredictimoApp.class)
public class PredictionResourceIntTest {

    private static final String DEFAULT_PREDICTION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PREDICTION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PREDICTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PREDICTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREDICTION_WORTH = 1;
    private static final Integer UPDATED_PREDICTION_WORTH = 2;

    private static final LocalDate DEFAULT_PREDICTION_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PREDICTION_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PredictionRepository predictionRepository;
    
    @Autowired
    private PredictionPollRepository predictionPollRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPredictionMockMvc;

    private Prediction prediction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PredictionResource predictionResource = new PredictionResource(predictionRepository, predictionPollRepository);
        this.restPredictionMockMvc = MockMvcBuilders.standaloneSetup(predictionResource)
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
    public static Prediction createEntity(EntityManager em) {
        Prediction prediction = new Prediction()
            .predictionTitle(DEFAULT_PREDICTION_TITLE)
            .predictionDescription(DEFAULT_PREDICTION_DESCRIPTION)
            .predictionWorth(DEFAULT_PREDICTION_WORTH)
            .predictionCreatedDate(DEFAULT_PREDICTION_CREATED_DATE);
        return prediction;
    }

    @Before
    public void initTest() {
        prediction = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrediction() throws Exception {
        int databaseSizeBeforeCreate = predictionRepository.findAll().size();

        // Create the Prediction
        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isCreated());

        // Validate the Prediction in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeCreate + 1);
        Prediction testPrediction = predictionList.get(predictionList.size() - 1);
        assertThat(testPrediction.getPredictionTitle()).isEqualTo(DEFAULT_PREDICTION_TITLE);
        assertThat(testPrediction.getPredictionDescription()).isEqualTo(DEFAULT_PREDICTION_DESCRIPTION);
        assertThat(testPrediction.getPredictionWorth()).isEqualTo(DEFAULT_PREDICTION_WORTH);
        assertThat(testPrediction.getPredictionCreatedDate()).isEqualTo(DEFAULT_PREDICTION_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createPredictionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = predictionRepository.findAll().size();

        // Create the Prediction with an existing ID
        prediction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPredictionTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionRepository.findAll().size();
        // set the field null
        prediction.setPredictionTitle(null);

        // Create the Prediction, which fails.

        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPredictionWorthIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionRepository.findAll().size();
        // set the field null
        prediction.setPredictionWorth(null);

        // Create the Prediction, which fails.

        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPredictionCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionRepository.findAll().size();
        // set the field null
        prediction.setPredictionCreatedDate(null);

        // Create the Prediction, which fails.

        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPredictions() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList
        restPredictionMockMvc.perform(get("/api/predictions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].predictionTitle").value(hasItem(DEFAULT_PREDICTION_TITLE.toString())))
            .andExpect(jsonPath("$.[*].predictionDescription").value(hasItem(DEFAULT_PREDICTION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].predictionWorth").value(hasItem(DEFAULT_PREDICTION_WORTH)))
            .andExpect(jsonPath("$.[*].predictionCreatedDate").value(hasItem(DEFAULT_PREDICTION_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPrediction() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get the prediction
        restPredictionMockMvc.perform(get("/api/predictions/{id}", prediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prediction.getId().intValue()))
            .andExpect(jsonPath("$.predictionTitle").value(DEFAULT_PREDICTION_TITLE.toString()))
            .andExpect(jsonPath("$.predictionDescription").value(DEFAULT_PREDICTION_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.predictionWorth").value(DEFAULT_PREDICTION_WORTH))
            .andExpect(jsonPath("$.predictionCreatedDate").value(DEFAULT_PREDICTION_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrediction() throws Exception {
        // Get the prediction
        restPredictionMockMvc.perform(get("/api/predictions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrediction() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);
        int databaseSizeBeforeUpdate = predictionRepository.findAll().size();

        // Update the prediction
        Prediction updatedPrediction = predictionRepository.findOne(prediction.getId());
        updatedPrediction
            .predictionTitle(UPDATED_PREDICTION_TITLE)
            .predictionDescription(UPDATED_PREDICTION_DESCRIPTION)
            .predictionWorth(UPDATED_PREDICTION_WORTH)
            .predictionCreatedDate(UPDATED_PREDICTION_CREATED_DATE);

        restPredictionMockMvc.perform(put("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrediction)))
            .andExpect(status().isOk());

        // Validate the Prediction in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeUpdate);
        Prediction testPrediction = predictionList.get(predictionList.size() - 1);
        assertThat(testPrediction.getPredictionTitle()).isEqualTo(UPDATED_PREDICTION_TITLE);
        assertThat(testPrediction.getPredictionDescription()).isEqualTo(UPDATED_PREDICTION_DESCRIPTION);
        assertThat(testPrediction.getPredictionWorth()).isEqualTo(UPDATED_PREDICTION_WORTH);
        assertThat(testPrediction.getPredictionCreatedDate()).isEqualTo(UPDATED_PREDICTION_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPrediction() throws Exception {
        int databaseSizeBeforeUpdate = predictionRepository.findAll().size();

        // Create the Prediction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPredictionMockMvc.perform(put("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isCreated());

        // Validate the Prediction in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrediction() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);
        int databaseSizeBeforeDelete = predictionRepository.findAll().size();

        // Get the prediction
        restPredictionMockMvc.perform(delete("/api/predictions/{id}", prediction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prediction.class);
        Prediction prediction1 = new Prediction();
        prediction1.setId(1L);
        Prediction prediction2 = new Prediction();
        prediction2.setId(prediction1.getId());
        assertThat(prediction1).isEqualTo(prediction2);
        prediction2.setId(2L);
        assertThat(prediction1).isNotEqualTo(prediction2);
        prediction1.setId(null);
        assertThat(prediction1).isNotEqualTo(prediction2);
    }
}
