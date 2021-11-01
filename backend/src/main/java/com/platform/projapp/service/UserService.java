package com.platform.projapp.service;

import com.platform.projapp.configuration.jwt.JwtTokenFilter;
import com.platform.projapp.configuration.jwt.JwtUtils;
import com.platform.projapp.dto.request.RegisterOrUpdateUserRequest;
import com.platform.projapp.dto.response.GeneralResponse;
import com.platform.projapp.dto.response.body.CurrentUserProfileResponseBody;
import com.platform.projapp.dto.response.body.CurrentUserResponseBody;
import com.platform.projapp.dto.response.body.MessageResponseBody;
import com.platform.projapp.enumarate.AccessRole;
import com.platform.projapp.error.ErrorConstants;
import com.platform.projapp.error.ErrorInfo;
import com.platform.projapp.model.User;
import com.platform.projapp.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Yarullin Renat
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtUtils jwtUtils;


    public UserDetails findByUserName(String username) {
        return userRepository.findByLogin(username);
    }

    public void addUser(RegisterOrUpdateUserRequest registerRequest) {
        if (registerRequest.getPassword() != null) {
            User user = new User(registerRequest.getLogin(),
                    passwordEncoder.encode(registerRequest.getPassword()),
                    registerRequest.getName(),
                    registerRequest.getSurname(),
                    registerRequest.getMiddleName(),
                    registerRequest.getEmail(),
                    registerRequest.getRoles(),
                    registerRequest.getComment(),
                    registerRequest.getGroup(),
                    Set.of(AccessRole.ROLE_USER));
            userRepository.save(user);
        }
    }

    public GeneralResponse<CurrentUserResponseBody> getCurrentUser(HttpServletRequest req) {
        GeneralResponse<CurrentUserResponseBody> response = new GeneralResponse<>();
        try {
            String token = jwtTokenFilter.parseJwt(req);
            String login = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByLogin(login);
            return response.withPayload(new CurrentUserResponseBody(user.getName(), user.getSurname(), user.getLogin()));
        } catch (ExpiredJwtException e) {
            return response.withErrors(List.of(ErrorInfo.of("Jwt is Expired", "Срок использования токена истек")));
        }
    }

    public GeneralResponse<CurrentUserProfileResponseBody> getCurrentUserProfile(HttpServletRequest req) {
        GeneralResponse<CurrentUserProfileResponseBody> response = new GeneralResponse<>();
        try {
            String token = jwtTokenFilter.parseJwt(req);
            String login = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByLogin(login);
            return response.withPayload(new CurrentUserProfileResponseBody(user.getLogin(), user.getName(), user.getSurname(), user.getInterests(), user.getEmail(), user.getComment(), user.getRoles(), user.getGroupp(), user.getId()));
        } catch (ExpiredJwtException e) {
            return response.withErrors(List.of(ErrorInfo.of("Jwt is Expired", "Срок использования токена истек")));
        }
    }

    public GeneralResponse<MessageResponseBody> changeUserProfile(RegisterOrUpdateUserRequest req) {
        List<ErrorInfo> errors = new ArrayList<>();
        GeneralResponse<MessageResponseBody> response = new GeneralResponse<>();
        try {
            Long Id = req.getId();
            User user = userRepository.getById(Id);
            user.setName(req.getName());
            user.setSurname(req.getSurname());
            user.setLogin(req.getLogin());
            user.setInterests(req.getInterests());
            user.setEmail(req.getEmail());
            user.setComment(req.getComment());
            user.setRoles(req.getRoles());
            user.setGroupp(req.getGroup());
            if (req.getOldPassword() != null && req.getNewPassword() != null && !passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
                errors.add(ErrorConstants.WRONG_PASSWORD);
                return response.withErrors(errors);
            } else if (req.getOldPassword() == null && req.getNewPassword() != null)
                return response.withErrors(List.of(ErrorInfo.of("Old Password Not Confirmed", "Необходимо ввести текущий пароль")));
            else if (req.getNewPassword() != null && passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash()))
                user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));

            userRepository.save(user);
            return response.withPayload(new MessageResponseBody("Информация о пользователе обновлена"));
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException"))
                errors.add(ErrorConstants.LOGIN_IS_BUSY);

            return response.withErrors(errors);
        }
    }
}
