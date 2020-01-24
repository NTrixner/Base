package eu.trixner.base.server.controller;

import eu.trixner.base.dto.PaginationRequestDto;
import eu.trixner.base.dto.UserListDto;
import eu.trixner.base.server.service.UserListService;
import eu.trixner.base.server.utils.PaginationUtils;
import eu.trixner.base.user.UserlistApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserListController implements UserlistApi {
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    UserListService userListService;

    @Autowired
    public UserListController(UserListService userListService) {
        this.userListService = userListService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return null;
    }

    @Override
    public ResponseEntity<UserListDto> listUsers(@Valid PaginationRequestDto body) {
        if (body == null) {
            return ResponseEntity.ok(userListService.getAllUsers());
        } else {
            return ResponseEntity.ok(userListService.getUsers(PaginationUtils.getPageRequest(body)));
        }
    }

    @Override
    public ResponseEntity<Integer> getUserCount() {
        return ResponseEntity.ok(userListService.countUsers().intValue());
    }
}
