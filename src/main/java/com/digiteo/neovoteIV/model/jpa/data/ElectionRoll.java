package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

//@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString
@Entity(name = "electionRoll")
@Table(name = "election_rolls")
public class ElectionRoll extends BaseEntity {

    @Column(name = "voterUsername")
    @Getter
    //@Setter since this is used in hashCode, should NOT be re-assignable after being instantiated
    private String voterUsername;

    @Column(name = "voter_email")
    @Getter
    @Setter
    private String email;

    @Column(
            name = "is_registered",
            columnDefinition = "TINYINT" // check how to set this to 0 (false)
    )
    @Getter
    @Setter
    private boolean isRegistered = false;

    @Column(
            name = "register_timestamp",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    @Setter(AccessLevel.PRIVATE)
    @Getter
    private LocalDateTime creationTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id" )
    @Getter
    private Election targetElection;


    public ElectionRoll(
            String voterUsername,
            Election targetElection){
        this.voterUsername = voterUsername;
        this.targetElection = targetElection;
    }

    @Override
    public Long getId() { return super.id; }

    @Override
    public void setId(Long id) { super.setId(id); }

    @Override
    public int hashCode(){
        return Objects.hash(targetElection, voterUsername);
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        ElectionRoll other = (ElectionRoll) o;
        return Objects.equals(targetElection, other.getTargetElection())
                && Objects.equals(voterUsername, other.getVoterUsername());
    }

}