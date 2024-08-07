package org.myblog.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.post.controller.OrderSearch;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.dto.PostPagingResponseDTO;
import org.myblog.domain.post.exception.PostNotFoundException;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.tag.repository.TagRepository;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.exception.UserNotFoundException;
import org.myblog.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public List<Post> findAllPosts(){
        return postRepository.findAll();
    }

    // 페이징 처리를 위한 서비스 코드
    public Page<PostPagingResponseDTO> getPostsByPaging(Pageable pageable, String sort){
        Page<Post> posts;

        if ("recent".equals(sort)){ // 최신순
            posts = postRepository.findAllByOrderByUpdatedAtDesc(pageable);
        } else { // 좋아요순 (디폴트값)
            posts = postRepository.findAllByOrderByLikeCountDesc(pageable);
        }

        return posts.map(PostPagingResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<Post> searchPosts(OrderSearch orderSearch){
        return postRepository.findAllByCriteria(orderSearch);
    }
}
