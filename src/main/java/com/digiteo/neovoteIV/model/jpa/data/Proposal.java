package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString
@Entity(name = "proposal")
@Table(
        name = "proposals",
        uniqueConstraints = {
                @UniqueConstraint(name = "proposal_field_unique", columnNames = {"name"})
        }
)
public class Proposal extends BaseEntity {

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "image_path"
    )
    private String profileImagePath;

    @Column(
            name = "contact_email"
    )
    private String contactEmail;

    private String webId;

    private String facebookId;

    private String linkedinId;

    private String xId;

    private String instagramId;

    @Column(
            name = "details",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String details;

    @Column(
            name = "is_visible",
            columnDefinition = "TINYINT"
    )
    private boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    // remember that hibernate execute a query for EACH proposal in lazy type, to avoid performance issues try
    // creating a custom @Query in repo with JOIN FETCH
    @JoinColumn(name = "election_id")
    private Election bindingProcess;

    /* ORDER:chronological, UNIQUENESS:yep, UPDATES:for adding, SIZE:undefined, OWNING:no, ACCESS:field(@Id on BaseEntity.id)
    */
    @ToString.Exclude
    @OneToMany(
            mappedBy = "targetProposal",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Vote> votes;

    @Column(
            name = "creation_timestamp",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime timestamp;

    /* this constructor works
    public Proposal(
            String name,
            String contactEmail,
            String details,
            boolean visible){
        this.name = name;
        this.contactEmail = contactEmail;
        this.details = details;
        this.visible = visible;
        this.timestamp = LocalDateTime.now();
    }
    */

    public Proposal(
            String name,
            String contactEmail,
            String webId,
            String facebookId,
            String linkedinId,
            String xId,
            String instagramId,
            String details,
            boolean visible){
        this.name = name;
        this.contactEmail = contactEmail;
        this.webId = webId;
        this.facebookId = facebookId;
        this.linkedinId = linkedinId;
        this.xId = xId;
        this.instagramId = instagramId;
        this.details = details;
        this.visible = visible;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public Long getId() { return super.id; }

    @Override
    public void setId(Long id) { super.setId(id); }

    /*
    public void addVote(Vote v){
        votes.add(v);
        v.setTargetProposal(this);
    }
     */
}
