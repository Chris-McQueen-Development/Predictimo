package chris.mcqueen.development.predictimo.repository;

import chris.mcqueen.development.predictimo.domain.UserProfile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

}
