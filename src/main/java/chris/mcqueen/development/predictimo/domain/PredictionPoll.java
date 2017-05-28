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
 * A PredictionPoll.
 */
@Entity
@Table(name = "prediction_poll")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PredictionPoll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "poll_name", nullable = false)
    private String pollName;

    @Column(name = "poll_end_date")
    private LocalDate pollEndDate;

    @Column(name = "poll_correct")
    private Boolean pollCorrect;

    @OneToMany(mappedBy = "predictionPoll")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserPollVote> userVotes = new HashSet<>();

    @OneToOne(mappedBy = "pollName")
    @JsonIgnore
    private Prediction predictionTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPollName() {
        return pollName;
    }

    public PredictionPoll pollName(String pollName) {
        this.pollName = pollName;
        return this;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public LocalDate getPollEndDate() {
        return pollEndDate;
    }

    public PredictionPoll pollEndDate(LocalDate pollEndDate) {
        this.pollEndDate = pollEndDate;
        return this;
    }

    public void setPollEndDate(LocalDate pollEndDate) {
        this.pollEndDate = pollEndDate;
    }

    public Boolean isPollCorrect() {
        return pollCorrect;
    }

    public PredictionPoll pollCorrect(Boolean pollCorrect) {
        this.pollCorrect = pollCorrect;
        return this;
    }

    public void setPollCorrect(Boolean pollCorrect) {
        this.pollCorrect = pollCorrect;
    }

    public Set<UserPollVote> getUserVotes() {
        return userVotes;
    }

    public PredictionPoll userVotes(Set<UserPollVote> userPollVotes) {
        this.userVotes = userPollVotes;
        return this;
    }

    public PredictionPoll addUserVotes(UserPollVote userPollVote) {
        this.userVotes.add(userPollVote);
        userPollVote.setPredictionPoll(this);
        return this;
    }

    public PredictionPoll removeUserVotes(UserPollVote userPollVote) {
        this.userVotes.remove(userPollVote);
        userPollVote.setPredictionPoll(null);
        return this;
    }

    public void setUserVotes(Set<UserPollVote> userPollVotes) {
        this.userVotes = userPollVotes;
    }

    public Prediction getPredictionTitle() {
        return predictionTitle;
    }

    public PredictionPoll predictionTitle(Prediction prediction) {
        this.predictionTitle = prediction;
        return this;
    }

    public void setPredictionTitle(Prediction prediction) {
        this.predictionTitle = prediction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PredictionPoll predictionPoll = (PredictionPoll) o;
        if (predictionPoll.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predictionPoll.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredictionPoll{" +
            "id=" + getId() +
            ", pollName='" + getPollName() + "'" +
            ", pollEndDate='" + getPollEndDate() + "'" +
            ", pollCorrect='" + isPollCorrect() + "'" +
            "}";
    }
}
