package org.jhipster.petclinic.service.mapper;

import org.jhipster.petclinic.domain.Owner;
import org.jhipster.petclinic.domain.Pet;
import org.jhipster.petclinic.domain.PetType;
import org.jhipster.petclinic.service.dto.OwnerDTO;
import org.jhipster.petclinic.service.dto.PetDTO;
import org.jhipster.petclinic.service.dto.PetTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pet} and its DTO {@link PetDTO}.
 */
@Mapper(componentModel = "spring", uses = { VisitMapper.class })
public interface PetMapper extends EntityMapper<PetDTO, Pet> {
    @Mapping(target = "type", source = "type", qualifiedByName = "petTypeName")
    @Mapping(target = "owner", source = "owner", qualifiedByName = "ownerLastName")
    PetDTO toDto(Pet s);

    @Named("petTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PetTypeDTO toDtoPetTypeName(PetType petType);

    @Named("ownerLastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    OwnerDTO toDtoOwnerLastName(Owner owner);
}
