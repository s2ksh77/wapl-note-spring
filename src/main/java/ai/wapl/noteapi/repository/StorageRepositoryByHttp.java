package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.dto.FileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StorageRepositoryByHttp implements StorageRepository {

    private Logger logger = LoggerFactory.getLogger(StorageRepositoryByHttp.class);

    @Override
    public FileDTO findFileInfo(String fileId) {
        logger.info("[DEBUG] execute StorageRepositoryByHttp.findFileInfo ");
        return null;
    }

    @Override
    public FileDTO deepCopy(String channelId, String fileId) {
        logger.info("[DEBUG] execute StorageRepositoryByHttp.deepCopy");
        return null;
    }

    @Override
    public void delete(String channelId, String fileId) {
        logger.info("[DEBUG] execute StorageRepositoryByHttp.delete");
    }
}
