package tech.itparklessons.fileshares.social.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private Long ownerId;

    @OneToOne
    private FilesharesFile filesharesFile;

    private boolean deleted = false;
}
