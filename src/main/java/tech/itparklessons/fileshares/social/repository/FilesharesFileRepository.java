package tech.itparklessons.fileshares.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.itparklessons.fileshares.social.model.entity.FilesharesFile;

import java.util.List;
import java.util.UUID;

public interface FilesharesFileRepository extends JpaRepository<FilesharesFile, Long> {
    List<FilesharesFile> findAllByFilesServiceFileUUIDIn(List<UUID> filesUUID);

    FilesharesFile findByFilesServiceFileUUID(UUID fileId);

    @Query(value = "UPDATE fileshares_file SET deleted = true WHERE filesServiceFileUUID IN (:fileUUID)", nativeQuery = true)
    void markAsDeleted(List<UUID> fileUUID);
}
