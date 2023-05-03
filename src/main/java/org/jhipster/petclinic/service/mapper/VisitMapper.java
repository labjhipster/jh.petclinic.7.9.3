package org.jhipster.petclinic.service.mapper;

import org.jhipster.petclinic.domain.Pet;
import org.jhipster.petclinic.domain.Visit;
import org.jhipster.petclinic.service.dto.PetDTO;
import org.jhipster.petclinic.service.dto.VisitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Visit} and its DTO {@link VisitDTO}.
 */
@Mapper(componentModel = "spring")
public interface VisitMapper extends EntityMapper<VisitDTO, Visit> {
    @Mapping(target = "pet", source = "pet", qualifiedByName = "petName")
    VisitDTO toDto(Visit s);

    @Named("petName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PetDTO toDtoPetName(Pet pet);
}
