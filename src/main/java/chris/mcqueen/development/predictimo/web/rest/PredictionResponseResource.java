package chris.mcqueen.development.predictimo.web.rest;

import com.codahale.metrics.annotation.Timed;
import chris.mcqueen.development.predictimo.domain.PredictionResponse;

import chris.mcqueen.development.predictimo.repository.PredictionResponseRepository;
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
 * REST controller for managing PredictionResponse.
 */
@RestController
@RequestMapping("/api")
public class PredictionResponseResource {

    private final Logger log = LoggerFactory.getLogger(PredictionResponseResource.class);

    private static final String ENTITY_NAME = "predictionResponse";
        
    private final PredictionResponseRepository predictionResponseRepository;

    public PredictionResponseResource(PredictionResponseRepository predictionResponseRepository) {
        this.predictionResponseRepository = predictionResponseRepository;
    }

    /**
     * POST  /prediction-responses : Create a new predictionResponse.
     *
     * @param predictionResponse the predictionResponse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new predictionResponse, or with status 400 (Bad Request) if the predictionResponse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prediction-responses")
    @Timed
    public ResponseEntity<PredictionResponse> createPredictionResponse(@Valid @RequestBody PredictionResponse predictionResponse) throws URISyntaxException {
        log.debug("REST request to save PredictionResponse : {}", predictionResponse);
        if (predictionResponse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new predictionResponse cannot already have an ID")).body(null);
        }
        PredictionResponse result = predictionResponseRepository.save(predictionResponse);
        return ResponseEntity.created(new URI("/api/prediction-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prediction-responses : Updates an existing predictionResponse.
     *
     * @param predictionResponse the predictionResponse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated predictionResponse,
     * or with status 400 (Bad Request) if the predictionResponse is not valid,
     * or with status 500 (Internal Server Error) if the predictionResponse couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prediction-responses")
    @Timed
    public ResponseEntity<PredictionResponse> updatePredictionResponse(@Valid @RequestBody PredictionResponse predictionResponse) throws URISyntaxException {
        log.debug("REST request to update PredictionResponse : {}", predictionResponse);
        if (predictionResponse.getId() == null) {
            return createPredictionResponse(predictionResponse);
        }
        PredictionResponse result = predictionResponseRepository.save(predictionResponse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, predictionResponse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prediction-responses : get all the predictionResponses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of predictionResponses in body
     */
    @GetMapping("/prediction-responses")
    @Timed
    public List<PredictionResponse> getAllPredictionResponses() {
        log.debug("REST request to get all PredictionResponses");
        List<PredictionResponse> predictionResponses = predictionResponseRepository.findAll();
        return predictionResponses;
    }

    /**
     * GET  /prediction-responses/:id : get the "id" predictionResponse.
     *
     * @param id the id of the predictionResponse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the predictionResponse, or with status 404 (Not Found)
     */
    @GetMapping("/prediction-responses/{id}")
    @Timed
    public ResponseEntity<PredictionResponse> getPredictionResponse(@PathVariable Long id) {
        log.debug("REST request to get PredictionResponse : {}", id);
        PredictionResponse predictionResponse = predictionResponseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(predictionResponse));
    }

    /**
     * DELETE  /prediction-responses/:id : delete the "id" predictionResponse.
     *
     * @param id the id of the predictionResponse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prediction-responses/{id}")
    @Timed
    public ResponseEntity<Void> deletePredictionResponse(@PathVariable Long id) {
        log.debug("REST request to delete PredictionResponse : {}", id);
        predictionResponseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
