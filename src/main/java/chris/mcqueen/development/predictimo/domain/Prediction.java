package chris.mcqueen.development.predictimo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
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

    @ManyToOne
    private PredictionType typeName;

    @ManyToOne
    private UserProfile creator;

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

    public UserProfile getCreator() {
        return creator;
    }

    public Prediction creator(UserProfile userProfile) {
        this.creator = userProfile;
        return this;
    }

    public void setCreator(UserProfile userProfile) {
        this.creator = userProfile;
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
