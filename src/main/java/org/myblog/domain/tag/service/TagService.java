package org.myblog.domain.tag.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.tag.domain.Tag;
import org.myblog.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Tag findByTagName(String tagName){
        return tagRepository.findByTagName(tagName).orElse(null); // 해당 태그명에 있는 태그가 없다면 null 리턴
    }

    public void deleteById(Long id){
        tagRepository.deleteById(id);
    }

    public void deleteByTagName(String tagName){
        tagRepository.deleteByTagName(tagName);
    }
}
