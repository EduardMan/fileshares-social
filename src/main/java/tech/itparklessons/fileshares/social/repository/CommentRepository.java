package tech.itparklessons.fileshares.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.itparklessons.fileshares.social.model.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "UPDATE comment SET deleted = true WHERE fileshares_file_id IN (:fileUUID)", nativeQuery = true)
    void markAsDeleted(List<UUID> fileUUID);
}
