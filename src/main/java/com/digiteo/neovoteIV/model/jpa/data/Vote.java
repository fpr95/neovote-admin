package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "vote")
@Table(name = "votes")
public class Vote extends BaseEntity {

    @Column(
            name = "creation_timestamp",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    @Setter(AccessLevel.PRIVATE)
    //@CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voterId")
    private VoterEntity voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalId")
    private Proposal targetProposal;

    @Override
    public Long getId() { return super.id; }
    @Override
    public void setId(Long id) { super.setId(id); }
}
