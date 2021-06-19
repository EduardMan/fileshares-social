package tech.itparklessons.fileshares.social.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tech.itparklessons.fileshares.social.client.FilesClient;
import tech.itparklessons.fileshares.social.model.Access;
import tech.itparklessons.fileshares.social.model.User;
import tech.itparklessons.fileshares.social.model.dto.AddingFileInfo;
import tech.itparklessons.fileshares.social.model.dto.ArchiveFileRabbitMessage;
import tech.itparklessons.fileshares.social.model.dto.ChangeAccessRequest;
import tech.itparklessons.fileshares.social.model.dto.CommentRequest;
import tech.itparklessons.fileshares.social.model.entity.Attitude;
import tech.itparklessons.fileshares.social.model.entity.Comment;
import tech.itparklessons.fileshares.social.model.entity.FilesharesSocialFile;
import tech.itparklessons.fileshares.social.model.entity.FilesharesFileShareLink;
import tech.itparklessons.fileshares.social.repository.AttitudeRepository;
import tech.itparklessons.fileshares.social.repository.CommentRepository;
import tech.itparklessons.fileshares.social.repository.FilesharesFileRepository;
import tech.itparklessons.fileshares.social.repository.FilesharesFileShareLinkRepository;
import tech.itparklessons.fileshares.social.service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FilesharesFileRepository fileRepository;
    private final AttitudeRepository attitudeRepository;
    private final CommentRepository commentRepository;
    private final FilesClient filesClient;
    private final RabbitTemplate template;
    private final FilesharesFileShareLinkRepository filesharesFileShareLinkRepository;

    @Override
    public void addToStorage(List<AddingFileInfo> addingFileInfos, User user) {
        if (!checkOwner(addingFileInfos, user)) {
            return;
        }

        List<FilesharesSocialFile> filesharesFilesForAddingToStorage = new ArrayList<>();
        for (AddingFileInfo addingFileInfo : addingFileInfos) {
            filesharesFilesForAddingToStorage.add(new FilesharesSocialFile(user.getId(), addingFileInfo.getFileUUID()));
        }

        fileRepository.saveAll(filesharesFilesForAddingToStorage);
        addToQueue(addingFileInfos);
    }

    private boolean checkOwner(List<AddingFileInfo> addingFileInfos, User user) {
        List<UUID> uuidList = addingFileInfos.stream().map(AddingFileInfo::getFileUUID).collect(Collectors.toList());
        return filesClient.checkOwner(uuidList, user.getId());
    }

    private void addToQueue(List<AddingFileInfo> addingFileInfos) {
        for (AddingFileInfo addingFileInfo : addingFileInfos) {
            UUID fileUUID = addingFileInfo.getFileUUID();
            Integer compressLevel = addingFileInfo.getCompressLevel();
            ArchiveFileRabbitMessage archiveFileRabbitMessage = new ArchiveFileRabbitMessage(fileUUID, compressLevel);
            String routingKey = "medium";
            if (compressLevel <= 3) {
                routingKey = "low";
            } else if (compressLevel > 6) {
                routingKey = "high";
            }

            template.convertAndSend("fileshares", routingKey, archiveFileRabbitMessage);
        }
    }

    @Override
    public void changeAccess(ChangeAccessRequest changeAccessRequest, User user) {
        List<UUID> filesUUID = changeAccessRequest.getFilesUUID();
        List<FilesharesSocialFile> allFilesByFilesServiceUUIDs = fileRepository.findAllByFilesServiceFileUUIDIn(filesUUID);

        boolean anyFileUserNotOwner = allFilesByFilesServiceUUIDs.stream().anyMatch(file -> !file.getOwnerId().equals(user.getId()));
        if (anyFileUserNotOwner)
            return;

        for (FilesharesSocialFile filesharesFile : allFilesByFilesServiceUUIDs) {
            filesharesFile.setAccess(changeAccessRequest.getAccess());
        }

        filesharesFileShareLinkRepository.markAsDeleted(filesUUID);
        fileRepository.saveAll(allFilesByFilesServiceUUIDs);
    }

    @Override
    public String getPublicLink(UUID fileUUID, User user) {
        FilesharesSocialFile filesharesFile = fileRepository.findByFilesServiceFileUUID(fileUUID);
        if (!filesharesFile.getOwnerId().equals(user.getId())) {
            return null;
        }

        FilesharesFileShareLink filesharesFileShareLink = new FilesharesFileShareLink();
        filesharesFileShareLink.setFilesharesFile(filesharesFile);
        FilesharesFileShareLink filesharesFileShareLinkSaved = filesharesFileShareLinkRepository.save(filesharesFileShareLink);

        filesharesFile.setAccess(Access.BY_LINK);
        fileRepository.save(filesharesFile);

        return filesharesFileShareLinkSaved.getShareLink();
    }

    @Override
    public void addComment(UUID fileUUID, CommentRequest commentRequest, User user) {
        FilesharesSocialFile filesharesFile = fileRepository.findByFilesServiceFileUUID(fileUUID);

        if (!isInteractionAcceptable(filesharesFile)) {
            return;
        }

        Comment comment = new Comment();
        comment.setOwnerId(user.getId());
        comment.setFilesharesFile(filesharesFile);
        comment.setText(commentRequest.getText());

        commentRepository.save(comment);
    }

    @Override
    public void addComment(String shareLink, CommentRequest commentRequest, User user) {
        FilesharesFileShareLink filesharesFileShareLink = filesharesFileShareLinkRepository.findByShareLink(shareLink);

        if (filesharesFileShareLink != null) {
            Comment comment = new Comment();
            comment.setOwnerId(user.getId());
            comment.setFilesharesFile(filesharesFileShareLink.getFilesharesFile());
            comment.setText(commentRequest.getText());

            commentRepository.save(comment);
        }
    }

    @Override
    public void removeComment(Long commentId, User user) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();

            if (comment.getOwnerId().equals(user.getId())) {
                comment.setDeleted(true);
            }
            commentRepository.save(comment);
        }
    }

    @Override
    public boolean checkAccess(UUID fileUuid) {
        return isInteractionAcceptable(fileRepository.findByFilesServiceFileUUID(fileUuid));
    }

    @Override
    public FilesharesSocialFile getFilesharesSocialFile(String shareLink) {
        return filesharesFileShareLinkRepository.findByShareLink(shareLink).getFilesharesFile();
    }

    @Override
    public void like(UUID fileUUID, User user) {
        FilesharesSocialFile filesharesFile = fileRepository.findByFilesServiceFileUUID(fileUUID);

        if (isInteractionAcceptable(filesharesFile)) {
            Attitude attitude = attitudeRepository.findByFilesharesFile_FilesServiceFileUUID(fileUUID);
            attitude.setLikeDislike(true);
            attitudeRepository.save(attitude);
        }
    }

    @Override
    public void like(String shareLink, User user) {
        FilesharesFileShareLink filesharesFileShareLink = filesharesFileShareLinkRepository.findByShareLink(shareLink);

        if (filesharesFileShareLink != null) {
            Attitude attitude = attitudeRepository.findByFilesharesFile_FilesServiceFileUUID(filesharesFileShareLink.getFilesharesFile().getFilesServiceFileUUID());
            attitude.setLikeDislike(true);
            attitudeRepository.save(attitude);
        }
    }

    @Override
    public void dislike(UUID fileUUID, User user) {
        FilesharesSocialFile filesharesFile = fileRepository.findByFilesServiceFileUUID(fileUUID);

        if (isInteractionAcceptable(filesharesFile)) {
            Attitude attitude = attitudeRepository.findByFilesharesFile_FilesServiceFileUUID(fileUUID);
            attitude.setLikeDislike(false);
            attitudeRepository.save(attitude);
        }
    }

    @Override
    public void dislike(String shareLink, User user) {
        FilesharesFileShareLink filesharesFileShareLink = filesharesFileShareLinkRepository.findByShareLink(shareLink);

        if (filesharesFileShareLink != null) {
            Attitude attitude = attitudeRepository.findByFilesharesFile_FilesServiceFileUUID(filesharesFileShareLink.getFilesharesFile().getFilesServiceFileUUID());
            attitude.setLikeDislike(false);
            attitudeRepository.save(attitude);
        }
    }

    @Override
    public void removeAttitude(UUID fileUUID, User user) {
        FilesharesSocialFile filesharesFile = fileRepository.findByFilesServiceFileUUID(fileUUID);

        if (isInteractionAcceptable(filesharesFile)) {
            Attitude attitude = attitudeRepository.findByFilesharesFile_FilesServiceFileUUID(fileUUID);
            attitude.setLikeDislike(null);
            attitudeRepository.save(attitude);
        }
    }

    @Override
    public void removeAttitude(String shareLink, User user) {
        FilesharesFileShareLink filesharesFileShareLink = filesharesFileShareLinkRepository.findByShareLink(shareLink);

        if (filesharesFileShareLink != null) {
            Attitude attitude = attitudeRepository.findByFilesharesFile_FilesServiceFileUUID(filesharesFileShareLink.getFilesharesFile().getFilesServiceFileUUID());
            attitude.setLikeDislike(null);
            attitudeRepository.save(attitude);
        }
    }

    private boolean isInteractionAcceptable(FilesharesSocialFile filesharesFile) {
        return filesharesFile.getAccess() == Access.PUBLIC;
    }
}