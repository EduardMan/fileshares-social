package tech.itparklessons.fileshares.social.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class AddingFileInfo implements Serializable {
    private UUID fileUUID;
    private Integer compressLevel;
}
