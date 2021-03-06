package tech.itparklessons.fileshares.social.model.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import tech.itparklessons.fileshares.social.model.Access;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class FilesharesSocialFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, unique = true)
    private UUID filesServiceFileUUID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Access access;

    private boolean deleted;

    public FilesharesSocialFile(Long ownerId, UUID filesServiceFileUUID) {
        this.ownerId = ownerId;
        this.filesServiceFileUUID = filesServiceFileUUID;
        this.access = Access.PRIVATE;
        this.deleted = false;
    }
}
