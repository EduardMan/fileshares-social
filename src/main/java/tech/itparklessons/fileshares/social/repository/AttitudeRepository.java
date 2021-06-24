package tech.itparklessons.fileshares.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tech.itparklessons.fileshares.social.model.entity.Attitude;

import java.util.List;
import java.util.UUID;

public interface AttitudeRepository extends JpaRepository<Attitude, Long> {
    Attitude findByFilesharesFile_FilesServiceFileUUID(UUID uuid);

    @Modifying
    @Query(value = "UPDATE attitude SET deleted = true WHERE fileshares_file_id IN " +
            "(SELECT fileshares_social_file.id FROM fileshares_social_file WHERE files_service_fileuuid IN (:fileUUID))", nativeQuery = true)
    void markAsDeleted(List<UUID> fileUUID);
}
