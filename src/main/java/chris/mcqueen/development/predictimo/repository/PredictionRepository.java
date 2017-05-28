package chris.mcqueen.development.predictimo.repository;

import chris.mcqueen.development.predictimo.domain.Prediction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Prediction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PredictionRepository extends JpaRepository<Prediction,Long> {

}
