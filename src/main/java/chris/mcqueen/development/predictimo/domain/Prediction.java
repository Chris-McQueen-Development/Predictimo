package chris.mcqueen.development.predictimo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Prediction.
 */
@Entity
@Table(name = "prediction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Prediction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "prediction_title", nullable = false)
    private String predictionTitle;

    @Column(name = "prediction_description")
    private String predictionDescription;

    @NotNull
    @Column(name = "prediction_worth", nullable = false)
    private Integer predictionWorth;

    @NotNull
    @Column(name = "prediction_created_date", nullable = false)
    private LocalDate predictionCreatedDate;

    @OneToOne
    @JoinColumn(unique = true)
    private PredictionPoll pollName;

    @ManyToOne
    private PredictionType typeName;

    @OneToMany(mappedBy = "predictionTitle")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserProfile> usersPredictings = new HashSet<>();

    @ManyToMany(mappedBy = "predictions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserProfile> userProfileCreators = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPredictionTitle() {
        return predictionTitle;
    }

    public Prediction predictionTitle(String predictionTitle) {
        this.predictionTitle = predictionTitle;
        return this;
    }

    public void setPredictionTitle(String predictionTitle) {
        this.predictionTitle = predictionTitle;
    }

    public String getPredictionDescription() {
        return predictionDescription;
    }

    public Prediction predictionDescription(String predictionDescription) {
        this.predictionDescription = predictionDescription;
        return this;
    }

    public void setPredictionDescription(String predictionDescription) {
        this.predictionDescription = predictionDescription;
    }

    public Integer getPredictionWorth() {
        return predictionWorth;
    }

    public Prediction predictionWorth(Integer predictionWorth) {
        this.predictionWorth = predictionWorth;
        return this;
    }

    public void setPredictionWorth(Integer predictionWorth) {
        this.predictionWorth = predictionWorth;
    }

    public LocalDate getPredictionCreatedDate() {
        return predictionCreatedDate;
    }

    public Prediction predictionCreatedDate(LocalDate predictionCreatedDate) {
        this.predictionCreatedDate = predictionCreatedDate;
        return this;
    }

    public void setPredictionCreatedDate(LocalDate predictionCreatedDate) {
        this.predictionCreatedDate = predictionCreatedDate;
    }

    public PredictionPoll getPollName() {
        return pollName;
    }

    public Prediction pollName(PredictionPoll predictionPoll) {
        this.pollName = predictionPoll;
        return this;
    }

    public void setPollName(PredictionPoll predictionPoll) {
        this.pollName = predictionPoll;
    }

    public PredictionType getTypeName() {
        return typeName;
    }

    public Prediction typeName(PredictionType predictionType) {
        this.typeName = predictionType;
        return this;
    }

    public void setTypeName(PredictionType predictionType) {
        this.typeName = predictionType;
    }

    public Set<UserProfile> getUsersPredictings() {
        return usersPredictings;
    }

    public Prediction usersPredictings(Set<UserProfile> userProfiles) {
        this.usersPredictings = userProfiles;
        return this;
    }

    public Prediction addUsersPredicting(UserProfile userProfile) {
        this.usersPredictings.add(userProfile);
        userProfile.setPredictionTitle(this);
        return this;
    }

    public Prediction removeUsersPredicting(UserProfile userProfile) {
        this.usersPredictings.remove(userProfile);
        userProfile.setPredictionTitle(null);
        return this;
    }

    public void setUsersPredictings(Set<UserProfile> userProfiles) {
        this.usersPredictings = userProfiles;
    }

    public Set<UserProfile> getUserProfileCreators() {
        return userProfileCreators;
    }

    public Prediction userProfileCreators(Set<UserProfile> userProfiles) {
        this.userProfileCreators = userProfiles;
        return this;
    }

    public Prediction addUserProfileCreator(UserProfile userProfile) {
        this.userProfileCreators.add(userProfile);
        userProfile.getPredictions().add(this);
        return this;
    }

    public Prediction removeUserProfileCreator(UserProfile userProfile) {
        this.userProfileCreators.remove(userProfile);
        userProfile.getPredictions().remove(this);
        return this;
    }

    public void setUserProfileCreators(Set<UserProfile> userProfiles) {
        this.userProfileCreators = userProfiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prediction prediction = (Prediction) o;
        if (prediction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prediction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Prediction{" +
            "id=" + getId() +
            ", predictionTitle='" + getPredictionTitle() + "'" +
            ", predictionDescription='" + getPredictionDescription() + "'" +
            ", predictionWorth='" + getPredictionWorth() + "'" +
            ", predictionCreatedDate='" + getPredictionCreatedDate() + "'" +
            "}";
    }
}
