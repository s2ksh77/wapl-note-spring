package ai.wapl.noteapi.service;

import java.util.*;

import ai.wapl.noteapi.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.TagDTO;
import ai.wapl.noteapi.repository.TagRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;
    private final PageRepository pageRepository;

    public Map<String, Map<String, List<Tag>>> getAllTagList(String channelId) {
        Set<Tag> tagList = tagRepository.findByChannelId(channelId);
        Map<String, Map<String, List<Tag>>> tagMap = new LinkedHashMap<>() {
            {
                put("KOR", null);
                put("ENG", null);
                put("NUM", null);
                put("ETC", null);
            }
        };

        tagList.forEach(tag -> {
            String key = getInitialSound(tag.getName());
            String localize = getLocalization(key);
            Map<String, List<Tag>> sortMap = tagMap.get(localize);
            if (sortMap == null) {
                sortMap = new HashMap<>();
                tagMap.put(localize, sortMap);
            }

            List<Tag> sortList = sortMap.get(key);
            if (sortList == null) {
                sortList = new ArrayList<>();
                sortMap.put(key, sortList);
            }
            sortList.add(tag);

            Object[] mapkey = sortMap.keySet().toArray();
            Arrays.sort(mapkey);
        });

        return tagMap;
    }

    public Set<Tag> getTagList(String pageId) {
        return tagRepository.findByPageId(pageId);
//        Page result = new Page();
//        result.setId(pageId);
//        result.addTagList(tagList);
//        result.setTagCount(tagList.size());
//        return pageRepository.findById(pageId).orElseThrow();
    }

    public Tag getTagId(String text) {
        return tagRepository.findByName(text);
    }

    public Tag createTag(List<TagDTO> inputList) {
        Tag result = new Tag();
        try {
            inputList.forEach(this::createTag);
            result.setResultMsg("Success");
        } catch (Exception e) {
            System.out.println("Execption occur with Delete Page ::" + e);
            result.setResultMsg("Fail");
        }

        return result;
    }

    private Tag createTag(TagDTO dto) {
        Page page = pageRepository.findById(dto.getPageId()).orElseThrow();
        Tag tag = getTagId(dto.getName());

        if (tag == null) {
            tag = new Tag(dto.getName());
            tagRepository.save(tag);
        }

        page.addTag(tag);
        return tag;
    }

    public Tag deleteTag(List<TagDTO> inputList) {
        Tag result = new Tag();
        try {
            inputList.forEach(this::deleteTag);
            result.setResultMsg("Success");
        } catch (Exception e) {
            System.out.println("Execption occur with Delete Page ::" + e);
            result.setResultMsg("Fail");
        }
        return result;
    }

    private void deleteTag(TagDTO input) {
        Page page = pageRepository.findById(input.getPageId()).orElseThrow();
//        page.deleteTag(input.getId());
    }

    public Tag updateTag(List<TagDTO> inputList) {
        Tag result = new Tag();
        try {
            for (TagDTO dto : inputList) {
                Page page = pageRepository.findById(dto.getPageId()).orElseThrow();
                Tag tag = getTagId(dto.getName());
                if (tag == null) { // 기존에 있는지 확인
                    tag = createTag(dto);
                }

                page.updateTag(tag, tagRepository.findById(dto.getId()).orElseThrow());
            }
            result.setResultMsg("Success");
        } catch (Exception e) {
            System.out.println("Execption occur with Delete Page ::" + e);
            result.setResultMsg("Fail");
        }
        return result;
    }

    public String getInitialSound(String text) {
        String[] chs = {
                "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
                "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
                "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
                "ㅋ", "ㅌ", "ㅍ", "ㅎ"
        };
        String[] initialchs = {
                "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄸ",
                "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ",
                "ㅁ", "ㅂ", "ㅃ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ",
                "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
        };
        String[] alphabet = {
                "A", "B", "C", "D", "E",
                "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y",
                "Z"
        };
        String[] alphabet2 = {
                "a", "b", "c", "d",
                "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x",
                "y", "z"
        };
        String[] number = {
                "0", "1", "2", "3", "4",
                "5", "6", "7", "8", "9"
        };
        if (text.length() > 0) {
            char chName = text.charAt(0);
            // 한글 ascii 코드
            String initailString = text.substring(0, 1);
            char initailChar = initailString.charAt(0);

            if (0x3131 <= initailChar && initailChar <= 0x314E) {
                int uniInitialVal = initailChar - 0x3131;

                return initialchs[uniInitialVal];
            }

            if (0xAC00 <= chName && chName <= 0xD7A3) {
                int uniVal = chName - 0xAC00;
                int cho = ((uniVal - (uniVal % 28)) / 28) / 21;

                return chs[cho];
            }
            // 영어 대문자 ascii 코드
            if (0x41 <= chName && chName <= 0x5A) {
                int uniVal = chName - 0x41;
                return alphabet[uniVal];
            }
            // 영어 소문자 ascii 코드
            if (0x61 <= chName && chName <= 0x7A) {
                int uniVal = chName - 0x61;
                return alphabet2[uniVal].toUpperCase();
            }
            if (0x30 <= chName && chName <= 0x39) {
                int uniVal = chName - 0x30;
                return number[uniVal];
            } else {
                return Character.toString(chName);
            }
        }
        return null;
    }

    public String getLocalization(String key) {
        String[] chs = {
                "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
                "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
                "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
                "ㅋ", "ㅌ", "ㅍ", "ㅎ"
        };
        String[] initialchs = {
                "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄸ",
                "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ",
                "ㅁ", "ㅂ", "ㅃ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ",
                "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
        };
        String[] alphabet = {
                "A", "B", "C", "D", "E",
                "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y",
                "Z"
        };
        String[] alphabet2 = {
                "a", "b", "c", "d",
                "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x",
                "y", "z"
        };
        String[] number = {
                "0", "1", "2", "3", "4",
                "5", "6", "7", "8", "9"
        };

        if (Arrays.asList(chs).contains(key) || Arrays.asList(initialchs).contains(key)) {
            return "KOR";
        } else if (Arrays.asList(alphabet).contains(key) || Arrays.asList(alphabet2).contains(key)) {
            return "ENG";
        } else if (Arrays.asList(number).contains(key)) {
            return "NUM";
        } else
            return "ETC";
    }
}
