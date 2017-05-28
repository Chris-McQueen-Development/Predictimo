package chris.mcqueen.development.predictimo.web.rest;

import com.codahale.metrics.annotation.Timed;
import chris.mcqueen.development.predictimo.domain.PredictionType;

import chris.mcqueen.development.predictimo.repository.PredictionTypeRepository;
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
 * REST controller for managing PredictionType.
 */
@RestController
@RequestMapping("/api")
public class PredictionTypeResource {

    private final Logger log = LoggerFactory.getLogger(PredictionTypeResource.class);

    private static final String ENTITY_NAME = "predictionType";
        
    private final PredictionTypeRepository predictionTypeRepository;

    public PredictionTypeResource(PredictionTypeRepository predictionTypeRepository) {
        this.predictionTypeRepository = predictionTypeRepository;
    }

    /**
     * POST  /prediction-types : Create a new predictionType.
     *
     * @param predictionType the predictionType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new predictionType, or with status 400 (Bad Request) if the predictionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prediction-types")
    @Timed
    public ResponseEntity<PredictionType> createPredictionType(@Valid @RequestBody PredictionType predictionType) throws URISyntaxException {
        log.debug("REST request to save PredictionType : {}", predictionType);
        if (predictionType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new predictionType cannot already have an ID")).body(null);
        }
        PredictionType result = predictionTypeRepository.save(predictionType);
        return ResponseEntity.created(new URI("/api/prediction-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prediction-types : Updates an existing predictionType.
     *
     * @param predictionType the predictionType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated predictionType,
     * or with status 400 (Bad Request) if the predictionType is not valid,
     * or with status 500 (Internal Server Error) if the predictionType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prediction-types")
    @Timed
    public ResponseEntity<PredictionType> updatePredictionType(@Valid @RequestBody PredictionType predictionType) throws URISyntaxException {
        log.debug("REST request to update PredictionType : {}", predictionType);
        if (predictionType.getId() == null) {
            return createPredictionType(predictionType);
        }
        PredictionType result = predictionTypeRepository.save(predictionType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, predictionType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prediction-types : get all the predictionTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of predictionTypes in body
     */
    @GetMapping("/prediction-types")
    @Timed
    public List<PredictionType> getAllPredictionTypes() {
        log.debug("REST request to get all PredictionTypes");
        List<PredictionType> predictionTypes = predictionTypeRepository.findAll();
        return predictionTypes;
    }

    /**
     * GET  /prediction-types/:id : get the "id" predictionType.
     *
     * @param id the id of the predictionType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the predictionType, or with status 404 (Not Found)
     */
    @GetMapping("/prediction-types/{id}")
    @Timed
    public ResponseEntity<PredictionType> getPredictionType(@PathVariable Long id) {
        log.debug("REST request to get PredictionType : {}", id);
        PredictionType predictionType = predictionTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(predictionType));
    }

    /**
     * DELETE  /prediction-types/:id : delete the "id" predictionType.
     *
     * @param id the id of the predictionType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prediction-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePredictionType(@PathVariable Long id) {
        log.debug("REST request to delete PredictionType : {}", id);
        predictionTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
