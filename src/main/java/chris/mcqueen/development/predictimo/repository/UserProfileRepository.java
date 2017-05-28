package chris.mcqueen.development.predictimo.repository;

import chris.mcqueen.development.predictimo.domain.UserProfile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    @Query("select distinct user_profile from UserProfile user_profile left join fetch user_profile.predictions")
    List<UserProfile> findAllWithEagerRelationships();

    @Query("select user_profile from UserProfile user_profile left join fetch user_profile.predictions where user_profile.id =:id")
    UserProfile findOneWithEagerRelationships(@Param("id") Long id);

}
