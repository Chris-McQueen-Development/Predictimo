package chris.mcqueen.development.predictimo.web.rest;

import com.codahale.metrics.annotation.Timed;
import chris.mcqueen.development.predictimo.domain.PredictionPoll;

import chris.mcqueen.development.predictimo.repository.PredictionPollRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing PredictionPoll.
 */
@RestController
@RequestMapping("/api")
public class PredictionPollResource {

    private final Logger log = LoggerFactory.getLogger(PredictionPollResource.class);

    private static final String ENTITY_NAME = "predictionPoll";
        
    private final PredictionPollRepository predictionPollRepository;

    public PredictionPollResource(PredictionPollRepository predictionPollRepository) {
        this.predictionPollRepository = predictionPollRepository;
    }

    /**
     * POST  /prediction-polls : Create a new predictionPoll.
     *
     * @param predictionPoll the predictionPoll to create
     * @return the ResponseEntity with status 201 (Created) and with body the new predictionPoll, or with status 400 (Bad Request) if the predictionPoll has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prediction-polls")
    @Timed
    public ResponseEntity<PredictionPoll> createPredictionPoll(@Valid @RequestBody PredictionPoll predictionPoll) throws URISyntaxException {
        log.debug("REST request to save PredictionPoll : {}", predictionPoll);
        if (predictionPoll.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new predictionPoll cannot already have an ID")).body(null);
        }
        PredictionPoll result = predictionPollRepository.save(predictionPoll);
        return ResponseEntity.created(new URI("/api/prediction-polls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prediction-polls : Updates an existing predictionPoll.
     *
     * @param predictionPoll the predictionPoll to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated predictionPoll,
     * or with status 400 (Bad Request) if the predictionPoll is not valid,
     * or with status 500 (Internal Server Error) if the predictionPoll couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prediction-polls")
    @Timed
    public ResponseEntity<PredictionPoll> updatePredictionPoll(@Valid @RequestBody PredictionPoll predictionPoll) throws URISyntaxException {
        log.debug("REST request to update PredictionPoll : {}", predictionPoll);
        if (predictionPoll.getId() == null) {
            return createPredictionPoll(predictionPoll);
        }
        PredictionPoll result = predictionPollRepository.save(predictionPoll);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, predictionPoll.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prediction-polls : get all the predictionPolls.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of predictionPolls in body
     */
    @GetMapping("/prediction-polls")
    @Timed
    public List<PredictionPoll> getAllPredictionPolls(@RequestParam(required = false) String filter) {
        if ("predictiontitle-is-null".equals(filter)) {
            log.debug("REST request to get all PredictionPolls where predictionTitle is null");
            return StreamSupport
                .stream(predictionPollRepository.findAll().spliterator(), false)
                .filter(predictionPoll -> predictionPoll.getPredictionTitle() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all PredictionPolls");
        List<PredictionPoll> predictionPolls = predictionPollRepository.findAll();
        return predictionPolls;
    }

    /**
     * GET  /prediction-polls/:id : get the "id" predictionPoll.
     *
     * @param id the id of the predictionPoll to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the predictionPoll, or with status 404 (Not Found)
     */
    @GetMapping("/prediction-polls/{id}")
    @Timed
    public ResponseEntity<PredictionPoll> getPredictionPoll(@PathVariable Long id) {
        log.debug("REST request to get PredictionPoll : {}", id);
        PredictionPoll predictionPoll = predictionPollRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(predictionPoll));
    }

    /**
     * DELETE  /prediction-polls/:id : delete the "id" predictionPoll.
     *
     * @param id the id of the predictionPoll to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prediction-polls/{id}")
    @Timed
    public ResponseEntity<Void> deletePredictionPoll(@PathVariable Long id) {
        log.debug("REST request to delete PredictionPoll : {}", id);
        predictionPollRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
