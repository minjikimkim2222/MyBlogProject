package org.myblog.domain.blog.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    // Post 등록
    @Transactional
    public Blog saveBlog(Blog blog){
        return blogRepository.save(blog);
    }
}
