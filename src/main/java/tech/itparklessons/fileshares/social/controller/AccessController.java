package tech.itparklessons.fileshares.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tech.itparklessons.fileshares.social.model.User;
import tech.itparklessons.fileshares.social.model.dto.AddingFileInfo;
import tech.itparklessons.fileshares.social.model.dto.ChangeAccessRequest;
import tech.itparklessons.fileshares.social.model.entity.FilesharesSocialFile;
import tech.itparklessons.fileshares.social.service.FileService;

import java.util.List;
import java.util.UUID;

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