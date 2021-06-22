package tech.itparklessons.fileshares.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tech.itparklessons.fileshares.social.model.User;
import tech.itparklessons.fileshares.social.model.dto.CommentRequest;
import tech.itparklessons.fileshares.social.model.entity.FilesharesSocialFile;
import tech.itparklessons.fileshares.social.service.FileService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/social/attitude")
@RequiredArgsConstructor
@RestController
public class AttitudeController {
    private final FileService fileService;

    @PostMapping("/like")
    public void like(@RequestParam UUID fileUUID,
                     @AuthenticationPrincipal User user) {
        fileService.like(fileUUID, user);
    }

    @PostMapping("/likeByShareLink")
    public void like(@RequestParam String shareLink,
                     @AuthenticationPrincipal User user) {
        fileService.like(shareLink, user);
    }

    @PostMapping("/dislike")
    public void dislike(@RequestParam UUID fileUUID,
                        @AuthenticationPrincipal User user) {
        fileService.dislike(fileUUID, user);
    }

    @PostMapping("/dislikeByShareLink")
    public void dislike(@RequestParam String shareLink,
                        @AuthenticationPrincipal User user) {
        fileService.dislike(shareLink, user);
    }

    @PostMapping("/removeAttitude")
    public void removeAttitude(@RequestParam UUID fileUUID,
                               @AuthenticationPrincipal User user) {
        fileService.removeAttitude(fileUUID, user);
    }

    @PostMapping("/removeAttitudeByShareLink")
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

    @PostMapping("/commentByShareLink")
    public void addComment(@RequestParam String shareLink,
                           @RequestBody CommentRequest commentRequest,
                           @AuthenticationPrincipal User user) {
        fileService.addComment(shareLink, commentRequest, user);
    }

    @PostMapping("/removeComment")
    public void removeComment(@RequestParam Long commentId,
                              @AuthenticationPrincipal User user) {
        fileService.removeComment(commentId, user);
    }
}