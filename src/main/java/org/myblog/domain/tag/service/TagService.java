package org.myblog.domain.tag.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.tag.domain.Tag;
import org.myblog.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> findAllTags(){
        return tagRepository.findAll();
    }

    public Tag saveTag(Tag tag){

        Optional<Tag> existingTag = tagRepository.findByTagName(tag.getTagName());

        if (existingTag.isPresent()){
            // 이미 존재하는 태그라면, 저장하지 않음
            return existingTag.get();
        }

        // 새로운 태그명이면, 태그 엔디티에 저장
        return tagRepository.save(tag);
    }

    public Tag saveTagByName(String tagName){
        // 태그가 존재하면 Optional안에 태그 객체가, 없으면 비어있는 Optional
        Optional<Tag> existingTag = tagRepository.findByTagName(tagName);

        // 람다식을 이용해, existingTag가 존재하면 그값 반환, 없으면 람다표현식의 값 반환
        return existingTag.orElseGet(() -> {
           Tag newTag = new Tag();
           newTag.setTagName(tagName);

           return tagRepository.save(newTag);
        });
    }

    public Tag findByTagName(String tagName){
        return tagRepository.findByTagName(tagName).orElse(null); // 해당 태그명에 있는 태그가 없다면 null 리턴
    }

    public void deleteById(Long id){
        tagRepository.deleteById(id);
    }

    public void deleteByTagName(String tagName){
        tagRepository.deleteByTagName(tagName);
    }

    // 태그 중복 처리 -- 각각의 태그명 종류(tagName--String)와 개수(Integer) 카운팅
    public Map<String, Integer> getTagCounts(List<Post> posts){
        Map<String, Integer> tagCountMap = new HashMap<>();

        for (Post post : posts){
            for (Tag tag : post.getTags()){
                tagCountMap.put(tag.getTagName(),
                        tagCountMap.getOrDefault(tag.getTagName(),0) + 1);

                // Value - tagCountMap.getOrDefault() 부분을 통해, 현재 태그 이름 개수를 1 증가시킨다.
                //       - 만일, tagCountMap에 해당 태그가 없다면, 기본값 0을 사용해서 1을 더한다.
            }
        }

        return tagCountMap;
    }
}
