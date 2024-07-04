package org.myblog.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public Post findById(Long id){
        return postRepository.findById(id).get();
    }
}
