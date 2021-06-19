package tech.itparklessons.fileshares.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tech.itparklessons.fileshares.social.model.User;
import tech.itparklessons.fileshares.social.model.dto.AddingFileInfo;
import tech.itparklessons.fileshares.social.model.dto.ChangeAccessRequest;
import tech.itparklessons.fileshares.social.model.dto.CommentRequest;
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

    @PostMapping("/changeAccess")
    public void changeAccess(@RequestBody ChangeAccessRequest changeAccessRequest,
                             @AuthenticationPrincipal User user) {
        fileService.changeAccess(changeAccessRequest, user);
    }

    @PostMapping("/getPublicLink")
    public String getPublicLink(@RequestParam UUID fileUUID,
                                @AuthenticationPrincipal User user) {
        return fileService.getPublicLink(fileUUID, user);
    }

    @PostMapping("/like")
    public void like(@RequestParam UUID fileUUID,
                     @AuthenticationPrincipal User user) {
        fileService.like(fileUUID, user);
    }

    @PostMapping("/like-by-share-link")
    public void like(@RequestParam String shareLink,
                     @AuthenticationPrincipal User user) {
        fileService.like(shareLink, user);
    }

    @PostMapping("/dislike")
    public void dislike(@RequestParam UUID fileUUID,
                        @AuthenticationPrincipal User user) {
        fileService.dislike(fileUUID, user);
    }

    @PostMapping("/dislike-by-share-link")
    public void dislike(@RequestParam String shareLink,
                        @AuthenticationPrincipal User user) {
        fileService.dislike(shareLink, user);
    }

    @PostMapping("/remove-attitude")
    public void removeAttitude(@RequestParam UUID fileUUID,
                               @AuthenticationPrincipal User user) {
        fileService.removeAttitude(fileUUID, user);
    }

    @PostMapping("/remove-attitude-by-share-link")
    public void removeAttitude(@RequestParam String shareLink,
                               @AuthenticationPrincipal User user) {
        fileService.removeAttitude(shareLink, user);
    }

    @PostMapping("/comment")
    public void addComment(@RequestParam UUID fileUUID,
                           @RequestBody CommentRequest commentRequest,
                           @AuthenticationPrincipal User user) {
        fileService.addComment(fileUUID, commentRequest, user);
    }

    @PostMapping("/comment-by-share-link")
    public void addComment(@RequestParam String shareLink,
                           @RequestBody CommentRequest commentRequest,
                           @AuthenticationPrincipal User user) {
        fileService.addComment(shareLink, commentRequest, user);
    }

    @PostMapping("/remove-comment")
    public void removeComment(@RequestParam Long commentId,
                              @AuthenticationPrincipal User user) {
        fileService.removeComment(commentId, user);
    }

    @GetMapping("/internal/checkAccess")
    public boolean checkAccess(@RequestParam UUID fileUuid) {
        return fileService.checkAccess(fileUuid);
    }

    @GetMapping("/internal/getByShareLink")
    public FilesharesSocialFile getFilesharesSocialFile(@RequestParam String shareLink) {
        return fileService.getFilesharesSocialFile(shareLink);
    }

    @GetMapping("/getAllFilesForUser")
    public List<FilesharesSocialFile> getAllFilesForUser(@AuthenticationPrincipal User user) {
        return fileService.getAllFilesharesSocialFiles(user);
    }
}