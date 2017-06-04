package chris.mcqueen.development.predictimo.repository;

import chris.mcqueen.development.predictimo.domain.PredictionResponse;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PredictionResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PredictionResponseRepository extends JpaRepository<PredictionResponse,Long> {

}
