package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "roles")
public class Role extends BaseEntity{

    private String name;
    //@ManyToMany(mappedBy = "roles")
    //private List<AdminEntity> users = new ArrayList<>();
}
