package com.digiteo.neovoteIV.model.jpa.data;

import com.digiteo.neovoteIV.security.jpa.SecureToken;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "adminEntity")
@Table(
        name = "admins",
        uniqueConstraints = {
                @UniqueConstraint(name = "admin_field_unique", columnNames = {"username", "email"})
        }
)
public class AdminEntity extends BaseEntity {

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

    @OneToMany(mappedBy = "adminEntity")
    private Set<SecureToken> tokens = new HashSet<>();

    //private int failedLoginAttempts;
    //private String loginDisabled;

    public AdminEntity(
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
    //Change the id modifier to PUBLIC as Mapstruct need it that way to achieve the DTO-entity mapping.
    @Override
    public void setId(Long id) { super.setId(id); }

    public void addToken(SecureToken token){
        tokens.add(token);
        token.setAdminEntity(this);
    }

    public void removeToken(SecureToken token){
        tokens.remove(token);
        token.setAdminEntity(null);
    }
}
