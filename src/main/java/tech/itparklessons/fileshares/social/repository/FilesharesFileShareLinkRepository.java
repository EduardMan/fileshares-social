package tech.itparklessons.fileshares.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.itparklessons.fileshares.social.model.entity.FilesharesFileShareLink;

import java.util.List;
import java.util.UUID;

public interface FilesharesFileShareLinkRepository extends JpaRepository<FilesharesFileShareLink, Long> {
    FilesharesFileShareLink findByShareLink(String shareLink);

    @Query(value = "UPDATE fileshares_file_share_link SET deleted = true WHERE fileshares_file IN (:fileUUID)", nativeQuery = true)
    void markAsDeleted(List<UUID> fileUUID);
}
