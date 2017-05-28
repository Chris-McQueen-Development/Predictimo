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
 * A PredictionType.
 */
@Entity
@Table(name = "prediction_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PredictionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "type_description")
    private String typeDescription;

    @Column(name = "type_expiration_date")
    private LocalDate typeExpirationDate;

    @OneToMany(mappedBy = "typeName")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prediction> predictionTitles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public PredictionType typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public PredictionType typeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
        return this;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public LocalDate getTypeExpirationDate() {
        return typeExpirationDate;
    }

    public PredictionType typeExpirationDate(LocalDate typeExpirationDate) {
        this.typeExpirationDate = typeExpirationDate;
        return this;
    }

    public void setTypeExpirationDate(LocalDate typeExpirationDate) {
        this.typeExpirationDate = typeExpirationDate;
    }

    public Set<Prediction> getPredictionTitles() {
        return predictionTitles;
    }

    public PredictionType predictionTitles(Set<Prediction> predictions) {
        this.predictionTitles = predictions;
        return this;
    }

    public PredictionType addPredictionTitle(Prediction prediction) {
        this.predictionTitles.add(prediction);
        prediction.setTypeName(this);
        return this;
    }

    public PredictionType removePredictionTitle(Prediction prediction) {
        this.predictionTitles.remove(prediction);
        prediction.setTypeName(null);
        return this;
    }

    public void setPredictionTitles(Set<Prediction> predictions) {
        this.predictionTitles = predictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PredictionType predictionType = (PredictionType) o;
        if (predictionType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predictionType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredictionType{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            ", typeDescription='" + getTypeDescription() + "'" +
            ", typeExpirationDate='" + getTypeExpirationDate() + "'" +
            "}";
    }
}
