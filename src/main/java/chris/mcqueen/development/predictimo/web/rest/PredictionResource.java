package chris.mcqueen.development.predictimo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import chris.mcqueen.development.predictimo.domain.Prediction;
import chris.mcqueen.development.predictimo.domain.PredictionPoll;
import chris.mcqueen.development.predictimo.repository.PredictionPollRepository;
import chris.mcqueen.development.predictimo.repository.PredictionRepository;
import chris.mcqueen.development.predictimo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Prediction.
 */
@RestController
@RequestMapping("/api")
public class PredictionResource {

    private final Logger log = LoggerFactory.getLogger(PredictionResource.class);

    private static final String ENTITY_NAME = "prediction";
        
    private final PredictionRepository predictionRepository;
    
    private final PredictionPollRepository predictionPollRepository;

    public PredictionResource(PredictionRepository predictionRepository, PredictionPollRepository predictionPollRepository) {
        this.predictionRepository = predictionRepository;
        this.predictionPollRepository = predictionPollRepository;
    }

    /**
     * POST  /predictions : Create a new prediction.
     *
     * @param prediction the prediction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prediction, or with status 400 (Bad Request) if the prediction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/predictions")
    @Timed
    public ResponseEntity<Prediction> createPrediction(@Valid @RequestBody Prediction prediction) throws URISyntaxException {
        log.debug("REST request to save Prediction : {}", prediction);
        if (prediction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new prediction cannot already have an ID")).body(null);
        }
        Prediction result = predictionRepository.save(prediction);
        
        /*
         * Any time a prediction is created a corresponding poll should also be created.
         */
        
        PredictionPoll predictionPoll = new PredictionPoll();
        predictionPoll.setPollName(result.getPredictionTitle() + " Poll");
        predictionPoll.setPredictionTitle(result);
        
        PredictionPoll predictionPollResult = predictionPollRepository.save(predictionPoll);
        result.setPollName(predictionPollResult);
        predictionRepository.save(result);
        
        return ResponseEntity.created(new URI("/api/predictions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /predictions : Updates an existing prediction.
     *
     * @param prediction the prediction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prediction,
     * or with status 400 (Bad Request) if the prediction is not valid,
     * or with status 500 (Internal Server Error) if the prediction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/predictions")
    @Timed
    public ResponseEntity<Prediction> updatePrediction(@Valid @RequestBody Prediction prediction) throws URISyntaxException {
        log.debug("REST request to update Prediction : {}", prediction);
        if (prediction.getId() == null) {
            return createPrediction(prediction);
        }
        Prediction result = predictionRepository.save(prediction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prediction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /predictions : get all the predictions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of predictions in body
     */
    @GetMapping("/predictions")
    @Timed
    public List<Prediction> getAllPredictions() {
        log.debug("REST request to get all Predictions");
        List<Prediction> predictions = predictionRepository.findAll();
        return predictions;
    }

    /**
     * GET  /predictions/:id : get the "id" prediction.
     *
     * @param id the id of the prediction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prediction, or with status 404 (Not Found)
     */
    @GetMapping("/predictions/{id}")
    @Timed
    public ResponseEntity<Prediction> getPrediction(@PathVariable Long id) {
        log.debug("REST request to get Prediction : {}", id);
        Prediction prediction = predictionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(prediction));
    }

    /**
     * DELETE  /predictions/:id : delete the "id" prediction.
     *
     * @param id the id of the prediction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/predictions/{id}")
    @Timed
    public ResponseEntity<Void> deletePrediction(@PathVariable Long id) {
        log.debug("REST request to delete Prediction : {}", id);
        predictionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
