package chris.mcqueen.development.predictimo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserPollVote.
 */
@Entity
@Table(name = "user_poll_vote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserPollVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "is_correct_vote", nullable = false)
    private Boolean isCorrectVote;

    @ManyToOne
    private UserProfile userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsCorrectVote() {
        return isCorrectVote;
    }

    public UserPollVote isCorrectVote(Boolean isCorrectVote) {
        this.isCorrectVote = isCorrectVote;
        return this;
    }

    public void setIsCorrectVote(Boolean isCorrectVote) {
        this.isCorrectVote = isCorrectVote;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public UserPollVote userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPollVote userPollVote = (UserPollVote) o;
        if (userPollVote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userPollVote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserPollVote{" +
            "id=" + getId() +
            ", isCorrectVote='" + isIsCorrectVote() + "'" +
            "}";
    }
}
