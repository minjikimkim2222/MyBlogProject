package org.myblog.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.comment.domain.Comment;
import org.myblog.domain.comment.dto.CommentCreatedDTO;
import org.myblog.domain.comment.repository.CommentRepository;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.exception.PostNotFoundException;
import org.myblog.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment createComment(CommentCreatedDTO commentDTO){
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException("post not found with id : " + commentDTO.getPostId()));

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setPost(post); // comment - post 연관관계 설정 !!

        return commentRepository.save(comment);
    }
}
