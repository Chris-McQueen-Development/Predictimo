package chris.mcqueen.development.predictimo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PredictionResponse.
 */
@Entity
@Table(name = "prediction_response")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PredictionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "response_date")
    private LocalDate responseDate;

    @Column(name = "answer")
    private String answer;

    @ManyToOne(optional = false)
    @NotNull
    private UserProfile userProfile;

    @ManyToOne(optional = false)
    @NotNull
    private Prediction prediction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getResponseDate() {
        return responseDate;
    }

    public PredictionResponse responseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
        return this;
    }

    public void setResponseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
    }

    public String getAnswer() {
        return answer;
    }

    public PredictionResponse answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public PredictionResponse userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public PredictionResponse prediction(Prediction prediction) {
        this.prediction = prediction;
        return this;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PredictionResponse predictionResponse = (PredictionResponse) o;
        if (predictionResponse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predictionResponse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredictionResponse{" +
            "id=" + getId() +
            ", responseDate='" + getResponseDate() + "'" +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
