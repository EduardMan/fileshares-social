package tech.itparklessons.fileshares.social.model.entity;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;

@Data
@Entity
public class FilesharesFileShareLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FilesharesSocialFile filesharesFile;

    @Column(nullable = false)
    private String shareLink;

    private boolean deleted;

    public FilesharesFileShareLink() {
        this.shareLink = RandomStringUtils.random(35, true, true);
    }
}