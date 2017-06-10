package chris.mcqueen.development.predictimo.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import chris.mcqueen.development.predictimo.domain.Prediction;
import chris.mcqueen.development.predictimo.service.IPredictionService;

/**
 * REST controller for managing Prediction.
 */
@RestController
@RequestMapping("/api")
public class PredictionCustomResource {
	
    private final Logger log = LoggerFactory.getLogger(PredictionResource.class);
        
    private final IPredictionService predictionService;

    public PredictionCustomResource(IPredictionService predictionService) {
        this.predictionService = predictionService;
    }
    
    @GetMapping("/predictions/open")
    @Timed
    public List<Prediction> getOpenPredictions() throws URISyntaxException {
    	
    	List<Prediction> listOfOpenPredictions = predictionService.getOpenPredictions();
        
        return listOfOpenPredictions;
    }

}
