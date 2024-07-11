package org.myblog.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.exception.PostNotFoundException;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.tag.repository.TagRepository;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.exception.UserNotFoundException;
import org.myblog.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public Post findById(Long id){
        return postRepository.findById(id).get();
    }

    public Blog findBlogByUserLoginForm(UserLoginForm userLoginForm){
        Long userId = userLoginForm.getId();
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()){
            throw new UserNotFoundException("User not found with userId : " + userId);
        }

        return user.get().getBlog();
    }

    public Post findByTitle(String title){
        Optional<Post> post = postRepository.findByTitle(title);

        if (!post.isPresent()){
            throw new PostNotFoundException("Post not found with postTitle : " + title);
        }

        return post.get();
    }

    public void deletePost(Post post){
        Post findPost = findById(post.getId());

        if (findPost == null){
            throw new PostNotFoundException("Post not found with postId : " + post.getId() + " ,cannot delete");
        }

        postRepository.delete(post);
    }

}
