package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.dto.FileDTO;

// Drive App CRUD
public interface StorageRepository {

    FileDTO findFileInfo(String fileId);

    FileDTO deepCopy(String channelId, String userId, String fileId);

    void delete(String channelId, String userId, String fileId);
}
