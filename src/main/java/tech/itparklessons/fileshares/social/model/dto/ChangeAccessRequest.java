package tech.itparklessons.fileshares.social.model.dto;

import lombok.Data;
import tech.itparklessons.fileshares.social.model.Access;

import java.util.List;
import java.util.UUID;

@Data
public class ChangeAccessRequest {
    List<UUID> filesUUID;
    Access access;
}
