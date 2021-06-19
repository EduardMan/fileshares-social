package tech.itparklessons.fileshares.social.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class FilesharesFileShareLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FilesharesFile filesharesFile;

    @GenericGenerator(name = "share_link", strategy = "tech.itparklessons.fileshares.social.model.generator.ShareLinkGenerator")
    @GeneratedValue(generator = "share_link")
    @Column(nullable = false)
    private String shareLink;

    private boolean deleted;
}
