package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Bookmark;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.dto.SearchDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PageServiceTest {
    @Resource
    PageService pageService;

    @Test
    public void search() {
        // given
        String channelId = "deef09e9-9f67-4e24-aef7-23b6be588cd2";
        String text = "aa";

        // when
        SearchDTO search = pageService.search(channelId, text);

        // then
        assertThat(search.getChapterList().size()).isEqualTo(1);
        assertThat(search.getPageList().size()).isEqualTo(17);
        assertThat(search.getTagList().size()).isEqualTo(1);
    }

    @Test
    public void changeStateToUnLock() {
        // given

        // when
        long l = pageService.changeStateToUnLock();

        // then
        assertThat(l).isEqualTo(8);
    }

    @Test
    public void createBookmark() {
        // given
        String pageId = "pageId";
        String userId = "userId";

        // when
        Bookmark bookmark = pageService.createBookmark(userId, pageId);

        // then
        assertThat(bookmark.getPageId()).isEqualTo(pageId);
        assertThat(bookmark.getUserId()).isEqualTo(userId);
    }

    @Test
    public void getBookmark() {
        // given
        String channelId = "deef09e9-9f67-4e24-aef7-23b6be588cd2";
        String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9"; // 오다은

        // when
        List<Page> pages = pageService.getBookmark(userId, channelId);

        // then
        assertThat(pages.size()).isEqualTo(2);
        assertThat(pages.get(0).getId()).isEqualTo("897262bc-5a6b-422a-a5d8-5407a19f58e4");
    }

    @Test
    public void getBookmarkByUser() {
        // given
        String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9"; // 오다은

        // when
        List<Page> pages = pageService.getBookmark(userId, null);

        // then
        assertThat(pages.size()).isEqualTo(2);
        assertThat(pages.get(0).getId()).isEqualTo("897262bc-5a6b-422a-a5d8-5407a19f58e4");
    }

    @Test
    public void deleteBookmark() {
        // given
        String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9"; // 오다은
        String pageId = "897262bc-5a6b-422a-a5d8-5407a19f58e4";

        // when
        Bookmark bookmark = pageService.deleteBookmark(userId, pageId);

        // then
        assertThat(bookmark.getUserId()).isEqualTo(userId);
        assertThat(bookmark.getPageId()).isEqualTo(pageId);
    }

    @Test
    public void deleteAllByChannel() {
        // given
        String channelId = "deef09e9-9f67-4e24-aef7-23b6be588cd2";

        // when
        long l = pageService.deleteAllByChannel(channelId);

        // then
        assertThat(l).isEqualTo(113);
    }

    @Test
    public void getAllPageList() {
        // given
        String channelId = "deef09e9-9f67-4e24-aef7-23b6be588cd2";
        String userId = "userId";

        // when
        List<PageDTO> l = pageService.getAllPageList(userId, channelId);

        // then
        assertThat(l.size()).isEqualTo(122);
    }

}
