package com.miu.ea.goodpeople.repository;

import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.domain.User;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Request entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select request from Request request where request.requester.login = ?#{principal.username}")
    List<Request> findByRequesterIsCurrentUser();
    
    List<Request> findAllByRequester(User requester);
}
