package org.jhipster.petclinic.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.jhipster.petclinic.domain.Pet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PetDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String name;

    private LocalDate birthDate;

    private PetTypeDTO type;

    private OwnerDTO owner;

    private Set<VisitDTO> visits = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public PetTypeDTO getType() {
        return type;
    }

    public void setType(PetTypeDTO type) {
        this.type = type;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PetDTO)) {
            return false;
        }

        PetDTO petDTO = (PetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, petDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public Set<VisitDTO> getVisits() {
        return visits;
    }

    public void setVisits(Set<VisitDTO> visits) {
        this.visits = visits;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", type=" + getType() +
            ", owner=" + getOwner() +
            "}";
    }
}
