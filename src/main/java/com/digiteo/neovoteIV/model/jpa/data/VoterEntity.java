package com.digiteo.neovoteIV.model.jpa.data;

import com.digiteo.neovoteIV.security.jpa.SecureToken;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "voterEntity")
@Table(
        name = "voters",
        uniqueConstraints = {
                @UniqueConstraint(name = "voter_field_unique", columnNames = {"username", "email"})
        }
)
public class VoterEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private String username;
    @Column(
            name = "image_path"
    )
    private String profileImagePath;
    @Column(name = "email")
    private String email;
    private String pwd;
    private String gender; // change this for an ENUM

    @Column(
            name = "account_verified",
            columnDefinition = "TINYINT"
    )
    private boolean accountVerified;

    @Column(
            name = "register_timestamp",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    @Setter(AccessLevel.PRIVATE)
    //@CreationTimestamp
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "voterEntity")
    private Set<SecureToken> tokens = new HashSet<>();

    @ToString.Exclude
    @OneToMany( // <--- define cascade type when implementing the blockchain
           mappedBy = "voter",
           fetch = FetchType.LAZY
    )
    private List<Vote> votes = new ArrayList<Vote>();

    /*
    @ToString.Exclude
    @ManyToMany(
            mappedBy = "electoralRoll",
            fetch = FetchType.LAZY
    )
    private List<Election> rolls = new ArrayList<>();
     */

    public VoterEntity(
            String firstName,
            String lastName,
            String username,
            String email,
            String pwd,
            String gender){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.pwd = pwd;
        this.gender = gender;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public Long getId() { return super.id; }
    @Override
    public void setId(Long id) { super.setId(id); }

    public void addToken(SecureToken token){
        tokens.add(token);
        token.setVoterEntity(this);
    }

    public void removeToken(SecureToken token){
        tokens.remove(token);
        token.setVoterEntity(null);
    }

}
