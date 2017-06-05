package chris.mcqueen.development.predictimo.service;

import java.util.List;

import chris.mcqueen.development.predictimo.domain.Prediction;

public interface IPredictionService {

	List<Prediction> getOpenPredictions();
}
