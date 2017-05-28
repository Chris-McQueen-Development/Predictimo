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

    @ManyToOne
    private Prediction predictionTitle;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_profile_prediction",
               joinColumns = @JoinColumn(name="user_profiles_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="predictions_id", referencedColumnName="id"))
    private Set<Prediction> predictions = new HashSet<>();

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

    public Prediction getPredictionTitle() {
        return predictionTitle;
    }

    public UserProfile predictionTitle(Prediction prediction) {
        this.predictionTitle = prediction;
        return this;
    }

    public void setPredictionTitle(Prediction prediction) {
        this.predictionTitle = prediction;
    }

    public Set<Prediction> getPredictions() {
        return predictions;
    }

    public UserProfile predictions(Set<Prediction> predictions) {
        this.predictions = predictions;
        return this;
    }

    public UserProfile addPrediction(Prediction prediction) {
        this.predictions.add(prediction);
        prediction.getUserProfileCreators().add(this);
        return this;
    }

    public UserProfile removePrediction(Prediction prediction) {
        this.predictions.remove(prediction);
        prediction.getUserProfileCreators().remove(this);
        return this;
    }

    public void setPredictions(Set<Prediction> predictions) {
        this.predictions = predictions;
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
