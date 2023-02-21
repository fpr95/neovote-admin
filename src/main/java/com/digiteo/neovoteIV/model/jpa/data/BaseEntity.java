package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    /**
     * check implementation of serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Getter
    @Setter(AccessLevel.PUBLIC) // this mutator was set as PRIVATE, but Mapstruct need it PUBLIC to map (DTO -> Entity)
    @EqualsAndHashCode.Include
    protected Long id;
}
