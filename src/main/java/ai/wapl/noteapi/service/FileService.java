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

    public void copyFileListByPageId(String fromPageId, String toPageId) {
    }

    public void deleteFileByPageId(String pageId) {
    }

    // 전달받은 챕터, 전달받은 페이지
    // 1. 챕터 id로 페이지 조회
    // 2. 페이지 하위 첨부된 파일 조회
    // 3. 파일 삭제
    public void deleteFileByChapterId(String chapterId) {
    }

}
