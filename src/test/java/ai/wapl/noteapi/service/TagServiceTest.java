package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.TagDTO;
import ai.wapl.noteapi.repository.PageRepository;
import ai.wapl.noteapi.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {

    @Mock
    TagRepository tagRepository;
    @Mock
    PageRepository pageRepository;
    TagService tagService;

    @BeforeEach
    public void setUp() {
        tagRepository = mock(TagRepository.class);
        pageRepository = mock(PageRepository.class);
        tagService = new TagService(tagRepository, pageRepository);
    }

    @Test
    public void getAllTagList() throws Exception {
        // given
        String channelId = "1";

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("아아"));
        tags.add(new Tag("ㅇㅇ"));
        tags.add(new Tag("ㅊㅊ"));
        tags.add(new Tag("추카추카추"));
        tags.add(new Tag("islay"));
        tags.add(new Tag("americano"));
        tags.add(new Tag("??"));
        tags.add(new Tag("222"));

        when(tagRepository.findByChannelId(channelId)).thenReturn(tags);

        // when
        Map<String, Map<String, List<Tag>>> allTag = tagService.getAllTagList(channelId);

        // then
        assertThat(allTag.size()).isEqualTo(4);
        assertThat(allTag.get("KOR").get("ㅇ").size()).isEqualTo(2);
        assertThat(allTag.get("KOR").get("ㅊ").size()).isEqualTo(2);
        assertThat(allTag.get("ENG").get("A").size()).isEqualTo(1);
        assertThat(allTag.get("ENG").get("I").size()).isEqualTo(1);
        assertThat(allTag.get("NUM").get("2").size()).isEqualTo(1);
        assertThat(allTag.get("ETC").get("?").size()).isEqualTo(1);
    }

    @Test
    public void getTagList() throws Exception {
        // given
        String pageId = "1";
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("아아"));
        tags.add(new Tag("ㅇㅇ"));
        tags.add(new Tag("ㅊㅊ"));
        tags.add(new Tag("추카추카추"));
        tags.add(new Tag("islay"));
        tags.add(new Tag("americano"));
        tags.add(new Tag("??"));
        tags.add(new Tag("222"));
        when(tagRepository.findByPageId(pageId)).thenReturn(tags);

        // when
        Set<Tag> tagSet = tagService.getTagList(pageId);

        // then
        assertThat(tagSet.size()).isEqualTo(tags.size());
    }

    @Test
    public void getTagId() throws Exception {
        // given
        String text = "test";
        when(tagRepository.findByName(text)).thenReturn(new Tag(text));

        // when
        Tag tag = tagService.getTagId(text);

        // then
        assertThat(tag.getName()).isEqualTo(text);
    }

    @Test
    public void createTag() throws Exception {
        // given
        String text = "노트 서비스";
        String pageId = "1";
        Page page = Page.builder().name("page title").content("<p></p>").build();
        page.addTag(new Tag("text"));
        when(pageRepository.findById(pageId)).thenReturn(Optional.ofNullable(page));

        TagDTO dto = TagDTO.builder().name(text).pageId(pageId).build();

        // when
        tagService.createTag(Arrays.asList(dto));

        // then
        verify(tagRepository).save(new Tag(text));
        assertThat(page.getTagSet().size()).isEqualTo(2);
    }

    @Test
    public void deleteTag() throws Exception {
        // given
        String text = "노트 서비스";
        String pageId = "1";
        Tag tag2 = new Tag("2", text, null);
        Page page = Page.builder().name("page title").content("<p></p>").build();
        page.addTag(new Tag("1","text",null));
        page.addTag(tag2);

        TagDTO dto = new TagDTO(tag2.getId(), tag2.getName(), pageId);

        when(pageRepository.findById(pageId)).thenReturn(Optional.ofNullable(page));
        when(tagRepository.findById(tag2.getId())).thenReturn(Optional.ofNullable(tag2));;
        // when
        tagService.deleteTag(Arrays.asList(dto));

        // then
        assertThat(page.getTagSet().size()).isEqualTo(1);
        assertThat(page.getTagSet()).doesNotContain(tag2);
    }

    @Test
    @DisplayName("update tag 새로 생성")
    public void updateTagNew() throws Exception {
        // given
        String text = "new";
        String pageId = "pageId";
        String oldTagId = "1";

        Tag oldTag = new Tag(oldTagId, "origin", null);
        Page page = Page.builder().build();
        page.setId(pageId);
        page.addTag(oldTag);
        TagDTO dto = new TagDTO(oldTagId, text, pageId);

        when(tagRepository.findByName(text)).thenReturn(null);
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));
        when(tagRepository.findById(oldTagId)).thenReturn(Optional.of(oldTag));

        // when
        tagService.updateTag(Arrays.asList(dto));

        // then
        verify(tagRepository).save(new Tag(text));
        assertThat(page.getTagSet()).doesNotContain(oldTag);
        assertThat(page.getTagSet().stream().map(Tag::getName)).contains(text);
    }

    @Test
    @DisplayName("update tag 기존에 존재하던 태그")
    public void updateTag() throws Exception {
        // given

        // when

        // then

    }
}