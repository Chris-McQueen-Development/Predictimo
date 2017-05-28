package chris.mcqueen.development.predictimo.repository;

import chris.mcqueen.development.predictimo.domain.PredictionPoll;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PredictionPoll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PredictionPollRepository extends JpaRepository<PredictionPoll,Long> {

}
