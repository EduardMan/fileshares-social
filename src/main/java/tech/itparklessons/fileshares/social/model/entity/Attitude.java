package tech.itparklessons.fileshares.social.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Attitude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Boolean likeDislike;

    @ManyToOne
    private FilesharesSocialFile filesharesFile;

    private boolean deleted = false;
}
