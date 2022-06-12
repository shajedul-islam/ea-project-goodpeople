package com.miu.ea.requestservice.repository;

import com.miu.ea.requestservice.domain.Request;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Request entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {}
