package tech.itparklessons.fileshares.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.itparklessons.fileshares.social.model.entity.FilesharesSocialFile;
import tech.itparklessons.fileshares.social.service.FileService;

import java.util.UUID;

@Secured("ROLE_BACKEND")
@RequestMapping("/internal/social")
@RequiredArgsConstructor
@RestController
public class InternalSocialController {
    private final FileService fileService;

    @GetMapping("/checkAccess")
    public boolean checkAccess(@RequestParam UUID fileUuid) {
        return fileService.checkAccess(fileUuid);
    }

    @GetMapping("/getByShareLink")
    public FilesharesSocialFile getFilesharesSocialFile(@RequestParam String shareLink) {
        return fileService.getFilesharesSocialFile(shareLink);
    }
}