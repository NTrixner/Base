package eu.trixner.base.server.controller;

import eu.trixner.base.dto.UserListDto;
import eu.trixner.base.server.service.UserListService;
import eu.trixner.base.server.utils.PaginationUtils;
import eu.trixner.base.user.UserlistApi;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.annotation.Nullable;
import java.util.Optional;

@Slf4j
@Controller
public class UserListController implements UserlistApi
{

    UserListService userListService;

    @Autowired
    public UserListController(UserListService userListService)
    {
        this.userListService = userListService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest()
    {
        return Optional.empty();
    }

    @Override
    @Secured("ROLE_USER_CAN_WATCH_USERLIST")
    public ResponseEntity<UserListDto> listUsers(@Nullable Integer page,
                                                 @Nullable Integer pageSize,
                                                 @Nullable String orderField,
                                                 @Nullable String orderDirection)
    {

        return ResponseEntity.ok(userListService.getUsers(PaginationUtils.getPageRequest(page,
                pageSize,
                orderField,
                orderDirection)));
    }

    @Override
    public ResponseEntity<Integer> getUserCount()
    {
        return ResponseEntity.ok(userListService.countUsers().intValue());
    }
}
