package tech.itparklessons.fileshares.social.service;

import tech.itparklessons.fileshares.social.model.User;
import tech.itparklessons.fileshares.social.model.dto.AddingFileInfo;
import tech.itparklessons.fileshares.social.model.dto.ChangeAccessRequest;
import tech.itparklessons.fileshares.social.model.dto.CommentRequest;
import tech.itparklessons.fileshares.social.model.entity.FilesharesSocialFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    void addToStorage(List<AddingFileInfo> addingFileInfos, User user);

    void changeAccess(ChangeAccessRequest changeAccessRequest, User user);

    void like(UUID fileUUID, User user);

    void like(String shareLink, User user);

    void dislike(UUID fileUUID, User user);

    void dislike(String shareLink, User user);

    void removeAttitude(UUID fileUUID, User user);

    void removeAttitude(String shareLink, User user);

    String getPublicLink(UUID fileUUID, User user);

    void addComment(UUID fileUUID, CommentRequest commentRequestUser, User user);

    void addComment(String shareLink, CommentRequest commentRequestUser, User user);

    void removeComment(Long commentId, User user);

    boolean checkAccess(UUID fileUuid);

    FilesharesSocialFile getFilesharesSocialFile(String shareLink);

    List<FilesharesSocialFile> getAllFilesharesSocialFiles(User user);
}
