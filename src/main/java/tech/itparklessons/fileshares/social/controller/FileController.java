package tech.itparklessons.fileshares.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tech.itparklessons.fileshares.social.model.User;
import tech.itparklessons.fileshares.social.model.dto.AddingFileInfo;
import tech.itparklessons.fileshares.social.model.entity.FilesharesSocialFile;
import tech.itparklessons.fileshares.social.service.FileService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/social")
@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;

    @PostMapping("/storage")
    public void addToStorage(@RequestBody List<AddingFileInfo> addingFileInfos,
                             @AuthenticationPrincipal User user) {
        fileService.addToStorage(addingFileInfos, user);
    }

    @PostMapping("/getPublicLink")
    public String getPublicLink(@RequestParam UUID fileUUID,
                                @AuthenticationPrincipal User user) {
        return fileService.getPublicLink(fileUUID, user);
    }

    @GetMapping("/getAllOwnerFiles")
    public List<FilesharesSocialFile> getAllOwnerFiles(@AuthenticationPrincipal User user) {
        return fileService.getAllOwnerFiles(user);
    }

    @GetMapping("/getAllUserFiles")
    public List<FilesharesSocialFile> getAllUserFiles(@RequestParam Long ownerId) {
        return fileService.getAllUserFiles(ownerId);
    }
}