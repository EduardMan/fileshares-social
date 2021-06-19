package tech.itparklessons.fileshares.social.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tech.itparklessons.fileshares.social.repository.AttitudeRepository;
import tech.itparklessons.fileshares.social.repository.CommentRepository;
import tech.itparklessons.fileshares.social.repository.FilesharesFileRepository;
import tech.itparklessons.fileshares.social.repository.FilesharesFileShareLinkRepository;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteFileRabbitListener {
    private final AttitudeRepository attitudeRepository;
    private final CommentRepository commentRepository;
    private final FilesharesFileRepository fileRepository;
    private final FilesharesFileShareLinkRepository filesharesFileShareLinkRepository;

    @RabbitListener(queues = "${application.queues.deleted-files}")
    public void deleteFile(List<UUID> fileUUID) {
        attitudeRepository.markAsDeleted(fileUUID);
        commentRepository.markAsDeleted(fileUUID);
        fileRepository.markAsDeleted(fileUUID);
        filesharesFileShareLinkRepository.markAsDeleted(fileUUID);
    }
}