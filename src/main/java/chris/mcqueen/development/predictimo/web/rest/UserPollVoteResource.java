package chris.mcqueen.development.predictimo.web.rest;

import com.codahale.metrics.annotation.Timed;
import chris.mcqueen.development.predictimo.domain.UserPollVote;

import chris.mcqueen.development.predictimo.repository.UserPollVoteRepository;
import chris.mcqueen.development.predictimo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserPollVote.
 */
@RestController
@RequestMapping("/api")
public class UserPollVoteResource {

    private final Logger log = LoggerFactory.getLogger(UserPollVoteResource.class);

    private static final String ENTITY_NAME = "userPollVote";
        
    private final UserPollVoteRepository userPollVoteRepository;

    public UserPollVoteResource(UserPollVoteRepository userPollVoteRepository) {
        this.userPollVoteRepository = userPollVoteRepository;
    }

    /**
     * POST  /user-poll-votes : Create a new userPollVote.
     *
     * @param userPollVote the userPollVote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userPollVote, or with status 400 (Bad Request) if the userPollVote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-poll-votes")
    @Timed
    public ResponseEntity<UserPollVote> createUserPollVote(@Valid @RequestBody UserPollVote userPollVote) throws URISyntaxException {
        log.debug("REST request to save UserPollVote : {}", userPollVote);
        if (userPollVote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userPollVote cannot already have an ID")).body(null);
        }
        UserPollVote result = userPollVoteRepository.save(userPollVote);
        return ResponseEntity.created(new URI("/api/user-poll-votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-poll-votes : Updates an existing userPollVote.
     *
     * @param userPollVote the userPollVote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userPollVote,
     * or with status 400 (Bad Request) if the userPollVote is not valid,
     * or with status 500 (Internal Server Error) if the userPollVote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-poll-votes")
    @Timed
    public ResponseEntity<UserPollVote> updateUserPollVote(@Valid @RequestBody UserPollVote userPollVote) throws URISyntaxException {
        log.debug("REST request to update UserPollVote : {}", userPollVote);
        if (userPollVote.getId() == null) {
            return createUserPollVote(userPollVote);
        }
        UserPollVote result = userPollVoteRepository.save(userPollVote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userPollVote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-poll-votes : get all the userPollVotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userPollVotes in body
     */
    @GetMapping("/user-poll-votes")
    @Timed
    public List<UserPollVote> getAllUserPollVotes() {
        log.debug("REST request to get all UserPollVotes");
        List<UserPollVote> userPollVotes = userPollVoteRepository.findAll();
        return userPollVotes;
    }

    /**
     * GET  /user-poll-votes/:id : get the "id" userPollVote.
     *
     * @param id the id of the userPollVote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userPollVote, or with status 404 (Not Found)
     */
    @GetMapping("/user-poll-votes/{id}")
    @Timed
    public ResponseEntity<UserPollVote> getUserPollVote(@PathVariable Long id) {
        log.debug("REST request to get UserPollVote : {}", id);
        UserPollVote userPollVote = userPollVoteRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userPollVote));
    }

    /**
     * DELETE  /user-poll-votes/:id : delete the "id" userPollVote.
     *
     * @param id the id of the userPollVote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-poll-votes/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserPollVote(@PathVariable Long id) {
        log.debug("REST request to delete UserPollVote : {}", id);
        userPollVoteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
