package chris.mcqueen.development.predictimo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "alias", nullable = false)
    private String alias;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "userProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserPollVote> userVotes = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prediction> predictionsCreateds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public UserProfile alias(String alias) {
        this.alias = alias;
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public User getUser() {
        return user;
    }

    public UserProfile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<UserPollVote> getUserVotes() {
        return userVotes;
    }

    public UserProfile userVotes(Set<UserPollVote> userPollVotes) {
        this.userVotes = userPollVotes;
        return this;
    }

    public UserProfile addUserVotes(UserPollVote userPollVote) {
        this.userVotes.add(userPollVote);
        userPollVote.setUserProfile(this);
        return this;
    }

    public UserProfile removeUserVotes(UserPollVote userPollVote) {
        this.userVotes.remove(userPollVote);
        userPollVote.setUserProfile(null);
        return this;
    }

    public void setUserVotes(Set<UserPollVote> userPollVotes) {
        this.userVotes = userPollVotes;
    }

    public Set<Prediction> getPredictionsCreateds() {
        return predictionsCreateds;
    }

    public UserProfile predictionsCreateds(Set<Prediction> predictions) {
        this.predictionsCreateds = predictions;
        return this;
    }

    public UserProfile addPredictionsCreated(Prediction prediction) {
        this.predictionsCreateds.add(prediction);
        prediction.setCreator(this);
        return this;
    }

    public UserProfile removePredictionsCreated(Prediction prediction) {
        this.predictionsCreateds.remove(prediction);
        prediction.setCreator(null);
        return this;
    }

    public void setPredictionsCreateds(Set<Prediction> predictions) {
        this.predictionsCreateds = predictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserProfile userProfile = (UserProfile) o;
        if (userProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", alias='" + getAlias() + "'" +
            "}";
    }
}
