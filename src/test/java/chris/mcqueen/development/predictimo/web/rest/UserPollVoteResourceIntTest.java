package chris.mcqueen.development.predictimo.web.rest;

import chris.mcqueen.development.predictimo.PredictimoApp;

import chris.mcqueen.development.predictimo.domain.UserPollVote;
import chris.mcqueen.development.predictimo.repository.UserPollVoteRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserPollVoteResource REST controller.
 *
 * @see UserPollVoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PredictimoApp.class)
public class UserPollVoteResourceIntTest {

    private static final Boolean DEFAULT_IS_CORRECT_VOTE = false;
    private static final Boolean UPDATED_IS_CORRECT_VOTE = true;

    @Autowired
    private UserPollVoteRepository userPollVoteRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserPollVoteMockMvc;

    private UserPollVote userPollVote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserPollVoteResource userPollVoteResource = new UserPollVoteResource(userPollVoteRepository);
        this.restUserPollVoteMockMvc = MockMvcBuilders.standaloneSetup(userPollVoteResource)
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
    public static UserPollVote createEntity(EntityManager em) {
        UserPollVote userPollVote = new UserPollVote()
            .isCorrectVote(DEFAULT_IS_CORRECT_VOTE);
        return userPollVote;
    }

    @Before
    public void initTest() {
        userPollVote = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPollVote() throws Exception {
        int databaseSizeBeforeCreate = userPollVoteRepository.findAll().size();

        // Create the UserPollVote
        restUserPollVoteMockMvc.perform(post("/api/user-poll-votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPollVote)))
            .andExpect(status().isCreated());

        // Validate the UserPollVote in the database
        List<UserPollVote> userPollVoteList = userPollVoteRepository.findAll();
        assertThat(userPollVoteList).hasSize(databaseSizeBeforeCreate + 1);
        UserPollVote testUserPollVote = userPollVoteList.get(userPollVoteList.size() - 1);
        assertThat(testUserPollVote.isIsCorrectVote()).isEqualTo(DEFAULT_IS_CORRECT_VOTE);
    }

    @Test
    @Transactional
    public void createUserPollVoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPollVoteRepository.findAll().size();

        // Create the UserPollVote with an existing ID
        userPollVote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPollVoteMockMvc.perform(post("/api/user-poll-votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPollVote)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserPollVote> userPollVoteList = userPollVoteRepository.findAll();
        assertThat(userPollVoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIsCorrectVoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPollVoteRepository.findAll().size();
        // set the field null
        userPollVote.setIsCorrectVote(null);

        // Create the UserPollVote, which fails.

        restUserPollVoteMockMvc.perform(post("/api/user-poll-votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPollVote)))
            .andExpect(status().isBadRequest());

        List<UserPollVote> userPollVoteList = userPollVoteRepository.findAll();
        assertThat(userPollVoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserPollVotes() throws Exception {
        // Initialize the database
        userPollVoteRepository.saveAndFlush(userPollVote);

        // Get all the userPollVoteList
        restUserPollVoteMockMvc.perform(get("/api/user-poll-votes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPollVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].isCorrectVote").value(hasItem(DEFAULT_IS_CORRECT_VOTE.booleanValue())));
    }

    @Test
    @Transactional
    public void getUserPollVote() throws Exception {
        // Initialize the database
        userPollVoteRepository.saveAndFlush(userPollVote);

        // Get the userPollVote
        restUserPollVoteMockMvc.perform(get("/api/user-poll-votes/{id}", userPollVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userPollVote.getId().intValue()))
            .andExpect(jsonPath("$.isCorrectVote").value(DEFAULT_IS_CORRECT_VOTE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserPollVote() throws Exception {
        // Get the userPollVote
        restUserPollVoteMockMvc.perform(get("/api/user-poll-votes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPollVote() throws Exception {
        // Initialize the database
        userPollVoteRepository.saveAndFlush(userPollVote);
        int databaseSizeBeforeUpdate = userPollVoteRepository.findAll().size();

        // Update the userPollVote
        UserPollVote updatedUserPollVote = userPollVoteRepository.findOne(userPollVote.getId());
        updatedUserPollVote
            .isCorrectVote(UPDATED_IS_CORRECT_VOTE);

        restUserPollVoteMockMvc.perform(put("/api/user-poll-votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserPollVote)))
            .andExpect(status().isOk());

        // Validate the UserPollVote in the database
        List<UserPollVote> userPollVoteList = userPollVoteRepository.findAll();
        assertThat(userPollVoteList).hasSize(databaseSizeBeforeUpdate);
        UserPollVote testUserPollVote = userPollVoteList.get(userPollVoteList.size() - 1);
        assertThat(testUserPollVote.isIsCorrectVote()).isEqualTo(UPDATED_IS_CORRECT_VOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPollVote() throws Exception {
        int databaseSizeBeforeUpdate = userPollVoteRepository.findAll().size();

        // Create the UserPollVote

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserPollVoteMockMvc.perform(put("/api/user-poll-votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPollVote)))
            .andExpect(status().isCreated());

        // Validate the UserPollVote in the database
        List<UserPollVote> userPollVoteList = userPollVoteRepository.findAll();
        assertThat(userPollVoteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserPollVote() throws Exception {
        // Initialize the database
        userPollVoteRepository.saveAndFlush(userPollVote);
        int databaseSizeBeforeDelete = userPollVoteRepository.findAll().size();

        // Get the userPollVote
        restUserPollVoteMockMvc.perform(delete("/api/user-poll-votes/{id}", userPollVote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPollVote> userPollVoteList = userPollVoteRepository.findAll();
        assertThat(userPollVoteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPollVote.class);
        UserPollVote userPollVote1 = new UserPollVote();
        userPollVote1.setId(1L);
        UserPollVote userPollVote2 = new UserPollVote();
        userPollVote2.setId(userPollVote1.getId());
        assertThat(userPollVote1).isEqualTo(userPollVote2);
        userPollVote2.setId(2L);
        assertThat(userPollVote1).isNotEqualTo(userPollVote2);
        userPollVote1.setId(null);
        assertThat(userPollVote1).isNotEqualTo(userPollVote2);
    }
}
