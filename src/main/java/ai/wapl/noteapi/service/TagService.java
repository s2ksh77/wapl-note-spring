package ai.wapl.noteapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.repository.TagRepository;

@Service
public class TagService {
    @Autowired
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Page getTagList(String pageId) {
        List<Tag> tagList = tagRepository.pageforTagList(pageId);
        Page result = new Page();
        result.setId(pageId);
        result.addTagList(tagList);
        result.setTagCount(tagList.size());
        return result;
    }
}
