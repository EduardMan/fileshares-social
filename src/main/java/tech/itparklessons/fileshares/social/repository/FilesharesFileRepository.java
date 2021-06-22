package tech.itparklessons.fileshares.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.itparklessons.fileshares.social.model.entity.FilesharesSocialFile;

import java.util.List;
import java.util.UUID;

public interface FilesharesFileRepository extends JpaRepository<FilesharesSocialFile, Long> {
    List<FilesharesSocialFile> findAllByFilesServiceFileUUIDIn(List<UUID> filesUUID);

    FilesharesSocialFile findByFilesServiceFileUUID(UUID fileId);

    @Query(value = "UPDATE fileshares_social_file SET deleted = true WHERE files_service_file_uuid IN (:fileUUID)", nativeQuery = true)
    void markAsDeleted(List<UUID> fileUUID);

    @Query(value = "SELECT * FROM fileshares_social_file WHERE owner_id = :userId AND access = 'PUBLIC' AND deleted = false", nativeQuery = true)
    List<FilesharesSocialFile> findAllByOwnerIdAndAccess_PublicAndDeletedIsFalse(Long userId);

    List<FilesharesSocialFile> findAllByOwnerIdAndDeletedFalse(Long userId);
}