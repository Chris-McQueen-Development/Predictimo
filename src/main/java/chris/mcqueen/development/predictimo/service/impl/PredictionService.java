package chris.mcqueen.development.predictimo.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import chris.mcqueen.development.predictimo.domain.Prediction;
import chris.mcqueen.development.predictimo.service.IPredictionService;

@Service
public class PredictionService implements IPredictionService {
	
	@Inject
	private EntityManagerFactory entityManagerFactory;

	@Override
	public List<Prediction> getOpenPredictions() {
		
		EntityManager em = entityManagerFactory.createEntityManager();
		
		String queryString = "SELECT p FROM Prediction AS p WHERE p.predictionFinished != true";
		Query q = em.createQuery(queryString);
		
		List<Prediction> predictions = q.getResultList();
		
		em.close();
		return predictions;
	}

}
