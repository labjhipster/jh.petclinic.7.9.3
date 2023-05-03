package org.jhipster.petclinic.service.mapper;

import org.jhipster.petclinic.domain.Specialty;
import org.jhipster.petclinic.service.dto.SpecialtyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {}
