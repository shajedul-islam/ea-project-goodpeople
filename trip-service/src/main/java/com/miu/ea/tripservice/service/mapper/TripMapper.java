package com.miu.ea.tripservice.service.mapper;

import com.miu.ea.tripservice.domain.Trip;
import com.miu.ea.tripservice.service.dto.TripDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trip} and its DTO {@link TripDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripMapper extends EntityMapper<TripDTO, Trip> {}
