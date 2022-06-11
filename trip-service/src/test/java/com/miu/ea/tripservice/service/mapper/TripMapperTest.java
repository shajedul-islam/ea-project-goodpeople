package com.miu.ea.tripservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripMapperTest {

    private TripMapper tripMapper;

    @BeforeEach
    public void setUp() {
        tripMapper = new TripMapperImpl();
    }
}
