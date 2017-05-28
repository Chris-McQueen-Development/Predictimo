package chris.mcqueen.development.predictimo.web.rest;

import chris.mcqueen.development.predictimo.PredictimoApp;

import chris.mcqueen.development.predictimo.domain.PredictionPoll;
import chris.mcqueen.development.predictimo.repository.PredictionPollRepository;
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
 * Test class for the PredictionPollResource REST controller.
 *
 * @see PredictionPollResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PredictimoApp.class)
public class PredictionPollResourceIntTest {

    private static final String DEFAULT_POLL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POLL_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_POLL_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_POLL_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_POLL_CORRECT = false;
    private static final Boolean UPDATED_POLL_CORRECT = true;

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

    private MockMvc restPredictionPollMockMvc;

    private PredictionPoll predictionPoll;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PredictionPollResource predictionPollResource = new PredictionPollResource(predictionPollRepository);
        this.restPredictionPollMockMvc = MockMvcBuilders.standaloneSetup(predictionPollResource)
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
    public static PredictionPoll createEntity(EntityManager em) {
        PredictionPoll predictionPoll = new PredictionPoll()
            .pollName(DEFAULT_POLL_NAME)
            .pollEndDate(DEFAULT_POLL_END_DATE)
            .pollCorrect(DEFAULT_POLL_CORRECT);
        return predictionPoll;
    }

    @Before
    public void initTest() {
        predictionPoll = createEntity(em);
    }

    @Test
    @Transactional
    public void createPredictionPoll() throws Exception {
        int databaseSizeBeforeCreate = predictionPollRepository.findAll().size();

        // Create the PredictionPoll
        restPredictionPollMockMvc.perform(post("/api/prediction-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionPoll)))
            .andExpect(status().isCreated());

        // Validate the PredictionPoll in the database
        List<PredictionPoll> predictionPollList = predictionPollRepository.findAll();
        assertThat(predictionPollList).hasSize(databaseSizeBeforeCreate + 1);
        PredictionPoll testPredictionPoll = predictionPollList.get(predictionPollList.size() - 1);
        assertThat(testPredictionPoll.getPollName()).isEqualTo(DEFAULT_POLL_NAME);
        assertThat(testPredictionPoll.getPollEndDate()).isEqualTo(DEFAULT_POLL_END_DATE);
        assertThat(testPredictionPoll.isPollCorrect()).isEqualTo(DEFAULT_POLL_CORRECT);
    }

    @Test
    @Transactional
    public void createPredictionPollWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = predictionPollRepository.findAll().size();

        // Create the PredictionPoll with an existing ID
        predictionPoll.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPredictionPollMockMvc.perform(post("/api/prediction-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionPoll)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PredictionPoll> predictionPollList = predictionPollRepository.findAll();
        assertThat(predictionPollList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPollNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionPollRepository.findAll().size();
        // set the field null
        predictionPoll.setPollName(null);

        // Create the PredictionPoll, which fails.

        restPredictionPollMockMvc.perform(post("/api/prediction-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionPoll)))
            .andExpect(status().isBadRequest());

        List<PredictionPoll> predictionPollList = predictionPollRepository.findAll();
        assertThat(predictionPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPredictionPolls() throws Exception {
        // Initialize the database
        predictionPollRepository.saveAndFlush(predictionPoll);

        // Get all the predictionPollList
        restPredictionPollMockMvc.perform(get("/api/prediction-polls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(predictionPoll.getId().intValue())))
            .andExpect(jsonPath("$.[*].pollName").value(hasItem(DEFAULT_POLL_NAME.toString())))
            .andExpect(jsonPath("$.[*].pollEndDate").value(hasItem(DEFAULT_POLL_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].pollCorrect").value(hasItem(DEFAULT_POLL_CORRECT.booleanValue())));
    }

    @Test
    @Transactional
    public void getPredictionPoll() throws Exception {
        // Initialize the database
        predictionPollRepository.saveAndFlush(predictionPoll);

        // Get the predictionPoll
        restPredictionPollMockMvc.perform(get("/api/prediction-polls/{id}", predictionPoll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(predictionPoll.getId().intValue()))
            .andExpect(jsonPath("$.pollName").value(DEFAULT_POLL_NAME.toString()))
            .andExpect(jsonPath("$.pollEndDate").value(DEFAULT_POLL_END_DATE.toString()))
            .andExpect(jsonPath("$.pollCorrect").value(DEFAULT_POLL_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPredictionPoll() throws Exception {
        // Get the predictionPoll
        restPredictionPollMockMvc.perform(get("/api/prediction-polls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePredictionPoll() throws Exception {
        // Initialize the database
        predictionPollRepository.saveAndFlush(predictionPoll);
        int databaseSizeBeforeUpdate = predictionPollRepository.findAll().size();

        // Update the predictionPoll
        PredictionPoll updatedPredictionPoll = predictionPollRepository.findOne(predictionPoll.getId());
        updatedPredictionPoll
            .pollName(UPDATED_POLL_NAME)
            .pollEndDate(UPDATED_POLL_END_DATE)
            .pollCorrect(UPDATED_POLL_CORRECT);

        restPredictionPollMockMvc.perform(put("/api/prediction-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPredictionPoll)))
            .andExpect(status().isOk());

        // Validate the PredictionPoll in the database
        List<PredictionPoll> predictionPollList = predictionPollRepository.findAll();
        assertThat(predictionPollList).hasSize(databaseSizeBeforeUpdate);
        PredictionPoll testPredictionPoll = predictionPollList.get(predictionPollList.size() - 1);
        assertThat(testPredictionPoll.getPollName()).isEqualTo(UPDATED_POLL_NAME);
        assertThat(testPredictionPoll.getPollEndDate()).isEqualTo(UPDATED_POLL_END_DATE);
        assertThat(testPredictionPoll.isPollCorrect()).isEqualTo(UPDATED_POLL_CORRECT);
    }

    @Test
    @Transactional
    public void updateNonExistingPredictionPoll() throws Exception {
        int databaseSizeBeforeUpdate = predictionPollRepository.findAll().size();

        // Create the PredictionPoll

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPredictionPollMockMvc.perform(put("/api/prediction-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(predictionPoll)))
            .andExpect(status().isCreated());

        // Validate the PredictionPoll in the database
        List<PredictionPoll> predictionPollList = predictionPollRepository.findAll();
        assertThat(predictionPollList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePredictionPoll() throws Exception {
        // Initialize the database
        predictionPollRepository.saveAndFlush(predictionPoll);
        int databaseSizeBeforeDelete = predictionPollRepository.findAll().size();

        // Get the predictionPoll
        restPredictionPollMockMvc.perform(delete("/api/prediction-polls/{id}", predictionPoll.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PredictionPoll> predictionPollList = predictionPollRepository.findAll();
        assertThat(predictionPollList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PredictionPoll.class);
        PredictionPoll predictionPoll1 = new PredictionPoll();
        predictionPoll1.setId(1L);
        PredictionPoll predictionPoll2 = new PredictionPoll();
        predictionPoll2.setId(predictionPoll1.getId());
        assertThat(predictionPoll1).isEqualTo(predictionPoll2);
        predictionPoll2.setId(2L);
        assertThat(predictionPoll1).isNotEqualTo(predictionPoll2);
        predictionPoll1.setId(null);
        assertThat(predictionPoll1).isNotEqualTo(predictionPoll2);
    }
}
