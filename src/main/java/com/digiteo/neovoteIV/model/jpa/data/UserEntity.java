package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "userEntity")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_field_unique", columnNames = {"username", "email"})
        }
)
public class UserEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private String username;
    @Column(name = "email")
    private String email;
    private String pwd;
    private String gender; // change this for an ENUM

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

    // @OneToMany( // <--- define cascade type
     //       mappedBy = "voter",
     //       fetch = FetchType.LAZY
    //)
    //private List<Vote> votes = new ArrayList<Vote>();

    // private String token;
    // private boolean accountVerified;
    // private int failedLoginAttempts;
    // private String loginDisabled;

    public UserEntity(
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
    //Change modifier to PUBLIC as Mapstruct need it that way to achieve the DTO-entity mapping.
    @Override
    public void setId(Long id) { super.setId(id); }
}
