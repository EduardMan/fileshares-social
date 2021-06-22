package tech.itparklessons.fileshares.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.itparklessons.fileshares.social.model.User;
import tech.itparklessons.fileshares.social.model.dto.ChangeAccessRequest;
import tech.itparklessons.fileshares.social.service.FileService;

@RequestMapping("/api/social/access")
@RequiredArgsConstructor
@RestController
public class AccessController {
    private final FileService fileService;

    @PostMapping("/changeAccess")
    public void changeAccess(@RequestBody ChangeAccessRequest changeAccessRequest,
                             @AuthenticationPrincipal User user) {
        fileService.changeAccess(changeAccessRequest, user);
    }
}