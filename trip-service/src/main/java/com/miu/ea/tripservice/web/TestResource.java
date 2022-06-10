package com.miu.ea.tripservice.web.rest;

import com.miu.ea.tripservice.domain.User;
import com.miu.ea.tripservice.repository.UserRepository;
import com.miu.ea.tripservice.security.SecurityUtils;
import com.miu.ea.tripservice.service.MailService;
import com.miu.ea.tripservice.service.UserService;
import com.miu.ea.tripservice.service.dto.AdminUserDTO;
import com.miu.ea.tripservice.service.dto.PasswordChangeDTO;
import com.miu.ea.tripservice.web.rest.errors.*;
import com.miu.ea.tripservice.web.rest.vm.KeyAndPasswordVM;
import com.miu.ea.tripservice.web.rest.vm.ManagedUserVM;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class TestResource {

    @GetMapping("/test")
    public String isAuthenticated(HttpServletRequest request) {
        return "Test Succesful!";
    }
}
