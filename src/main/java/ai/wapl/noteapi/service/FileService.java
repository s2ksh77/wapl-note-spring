package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.File;
import ai.wapl.noteapi.dto.FileDTO;
import ai.wapl.noteapi.dto.FileDTOinterface;
import ai.wapl.noteapi.repository.FileRepository;
import ai.wapl.noteapi.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final FileRepository fileRepository;
    private final StorageRepository storageRepository;

    @Transactional(readOnly = true)
    public List<FileDTOinterface> getFileListByPageId(String pageId) {
        List<File> files = fileRepository.findByPageId(pageId);

        List<FileDTOinterface> result = new ArrayList<>();
        files.forEach(file -> result.add(fileRepository.findFileInfo(file.getFileId())));

        return result;
    }

    public void copyFileListByPageId(String channelId, String fromPageId, String toPageId) {
        List<File> files = fileRepository.findByPageId(fromPageId);
        files.forEach(file -> {
            FileDTO copy = storageRepository.deepCopy(channelId, file.getFileId());
            fileRepository.save(new File(toPageId, copy.getId()));
        });
    }

    public void deleteFileByPageId(String channelId, String pageId) {
        List<File> files = fileRepository.findByPageId(pageId);
        files.forEach(file -> storageRepository.delete(channelId, file.getFileId()));
        fileRepository.deleteByPageId(pageId);
    }

    // 전달받은 챕터, 전달받은 페이지
    // 1. 챕터 id로 페이지 조회
    // 2. 페이지 하위 첨부된 파일 조회
    // 3. 파일 삭제
    public void deleteFileByChapterId(String channelId, String chapterId) {
        List<File> files = fileRepository.findByChapterId(chapterId);
        files.forEach(file -> storageRepository.delete(channelId, file.getFileId()));
        fileRepository.deleteByChapterId(chapterId);
    }

}
