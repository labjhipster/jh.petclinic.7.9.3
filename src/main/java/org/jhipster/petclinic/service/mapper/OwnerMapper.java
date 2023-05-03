package org.jhipster.petclinic.service.mapper;

import org.jhipster.petclinic.domain.Owner;
import org.jhipster.petclinic.service.dto.OwnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Owner} and its DTO {@link OwnerDTO}.
 */
@Mapper(componentModel = "spring")
public interface OwnerMapper extends EntityMapper<OwnerDTO, Owner> {}
