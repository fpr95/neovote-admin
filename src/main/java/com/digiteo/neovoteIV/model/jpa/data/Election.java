package com.digiteo.neovoteIV.model.jpa.data;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

//@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@ToString
@Entity(name = "election")
@Table(
        name = "elections",
        uniqueConstraints = {
                @UniqueConstraint(name = "election_field_unique", columnNames = {"title"})
        }
)
public class Election extends BaseEntity {

    @Column(
            name = "title",
            nullable = false
    )
    @Setter
    private String title;

    @Column(
            name = "image_path"
    )
    @Setter
    private String profileImagePath;

    @Column( name = "topics" )
    @Setter
    private String topics;

    //@Transient
    //private final String DELIMITER = " ";

    @Column(
            name = "brief_description",
            nullable = false
    )
    @Setter
    private String briefDescription;

    // To avoid mapping breaks in case of changing ElectionStatus, the persisting is managed by an AttributeConverter
    @Column( name = "current_status" )
    @Setter
    private ElectionStatus electionStatus;

    @Column(
            name = "init_timestamp",
            columnDefinition = "TIMESTAMP"
    )
    @Setter
    private LocalDateTime initTimestamp;

    @Column(
            name = "finish_timestamp",
            columnDefinition = "TIMESTAMP"
    )
    @Setter
    private LocalDateTime finishTimestamp;

    @Column(
            name = "details",
            columnDefinition = "TEXT"
    )
    @Setter
    private String details;

    @Column(
            name = "register_timestamp",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime creationTimestamp;

    //@Transient // This annotation is temporary while find a way to bind the user's username to the new Election
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name="admin_id")
    @Setter
    private String creatorUsername;

    //ORDER:no, UNIQUENESS:yep, UPDATES:few, SIZE:10-20, OWNING:no, ACCESS:field(@Id on model.BaseEntity.id)
    @ToString.Exclude
    @OneToMany(
            mappedBy = "bindingProcess",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Proposal> proposals = new ArrayList<Proposal>();

    /*
    //ORDER:no, UNIQUENESS:yes, UPDATES:could b many, SIZE:undefined, OWNING:yes, ACCESS:field(@Id on model.BaseEntity.id)
    @ToString.Exclude
    @ManyToMany(
            //mappedBy = "rolls",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Getter
    private List<VoterEntity> electoralRoll = new ArrayList<VoterEntity>();
     */

    //ORDER:no, UNIQUENESS:yes, UPDATES:should b few, SIZE:undefined, OWNING:no, ACCESS:field(@Id on model.BaseEntity.id)
    @ToString.Exclude
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "targetElection"
    )
    private Set<ElectionRoll> roll = new HashSet<>();

    public Election (
            String title,
            String briefDescription,
            String topics,
            LocalDateTime initTimestamp,
            LocalDateTime finishTimestamp,
            String details
    ){
        this.title = title;
        this.briefDescription = briefDescription;
        this.topics = topics;
        this.initTimestamp = initTimestamp;
        this.finishTimestamp = finishTimestamp;
        this.details = details;
        this.creationTimestamp = LocalDateTime.now();
        this.electionStatus = ElectionStatus.SUSPENDED;
    }

    @Override
    public Long getId() { return super.id; }

    @Override
    public void setId(Long id) { super.setId(id); }

    @Override
    public int hashCode(){
        return Objects.hash(getId().intValue());
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Election other = (Election) o;
        return Objects.equals(getId(), other.getId());
    }

    /*
    public ElectionStatus getElectionStatus(){
        if(LocalDateTime.now().isBefore(initTimestamp)){
            return ElectionStatus.NOT_INIT;
        } else if(LocalDateTime.now().isAfter(initTimestamp) && LocalDateTime.now().isBefore(finishTimestamp)){
            return ElectionStatus.IN_PROGRESS;
        } else if(LocalDateTime.now().isAfter(finishTimestamp)){
            return ElectionStatus.FINISHED;
        }
        return ElectionStatus.SUSPENDED;
    }
    */

    public String getElectionStatusCode(){
        if(LocalDateTime.now().isBefore(initTimestamp)){
            return ElectionStatus.NOT_INIT.getCode();
        } else if(LocalDateTime.now().isAfter(initTimestamp) && LocalDateTime.now().isBefore(finishTimestamp)){
            return ElectionStatus.IN_PROGRESS.getCode();
        } else if(LocalDateTime.now().isAfter(finishTimestamp)){
            return ElectionStatus.FINISHED.getCode();
        }
        return ElectionStatus.SUSPENDED.getCode();
    }

    public String[] getTopicsCollection(){
        return topics.split(",", 0);
    }

    public void addProposal(Proposal p){
        proposals.add(p);
        p.setBindingProcess(this);
    }

    public void removeProposal(Proposal p){
        proposals.remove(p);
        p.setBindingProcess(null);
    }

    //public void setProfileImagePath(byte[] imageBytes){
    //    this.profileImagePath = imageBytes.toString();
    //}

    //public byte[] getProfileImageFromPath(String imagePath){
        //imagePath;
    //}

    // CUSTOM IMPL TO CONVERT topics INTO AN ARRAY AND VICE VERSA
    //public String[] getTopics() {
    //    return topics != null ? topics.split(DELIMITER) : null;
    //}

    //public void setTopics(String[] topics) {
    //    this.topics = topics != null ? String.join(DELIMITER, topics) : null;
    //}
}

