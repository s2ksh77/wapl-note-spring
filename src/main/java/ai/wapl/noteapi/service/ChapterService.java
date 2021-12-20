package ai.wapl.noteapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.repository.ChapterRepository;

@Service
public class ChapterService {

    @Autowired
    private final ChapterRepository chapterRepository;

    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    /**
     * 챕터 전체 조회 서비스
     * 
     * 
     * @param channelId
     * @return
     */

    public List<Chapter> getChapterList(String channelId) {
        List<Chapter> result = chapterRepository.findByChannelId(channelId);
        return result;
    }

    /**
     * 챕터 하위 페이지 조회 서비스
     * 
     * 
     * @param chapterId
     * @return
     */
    public Chapter getChapterInfoList(String chapterId) {
        Chapter result = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter"));

        return result;
    }
}
