package chris.mcqueen.development.predictimo.repository;

import chris.mcqueen.development.predictimo.domain.UserPollVote;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserPollVote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPollVoteRepository extends JpaRepository<UserPollVote,Long> {

}
