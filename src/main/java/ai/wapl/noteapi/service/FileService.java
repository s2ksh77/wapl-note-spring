package ai.wapl.noteapi.service;

import ai.wapl.noteapi.dto.FileDTO;
import ai.wapl.noteapi.repository.FileRepository;
import ai.wapl.noteapi.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final FileRepository fileRepository;
//    private final StorageRepository storageRepository;

    @Transactional(readOnly = true)
    public List<FileDTO> getFileListByPageId(String pageId) {
        return null;
    }

    public void copyFileListByPageId(String pageId) {
    }

    public void deleteFileByPageId(String pageId) {
    }

}
