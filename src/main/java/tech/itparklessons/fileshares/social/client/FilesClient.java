package tech.itparklessons.fileshares.social.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(url = "http://localhost:8083/api/file-storage", name = "file")
public interface FilesClient {
    @GetMapping("/checkOwner")
    boolean checkOwner(@RequestParam List<UUID> fileUUID, @RequestParam Long userId);
}