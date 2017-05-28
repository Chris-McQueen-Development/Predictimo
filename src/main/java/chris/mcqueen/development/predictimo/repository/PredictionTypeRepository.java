package chris.mcqueen.development.predictimo.repository;

import chris.mcqueen.development.predictimo.domain.PredictionType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PredictionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PredictionTypeRepository extends JpaRepository<PredictionType,Long> {

}
