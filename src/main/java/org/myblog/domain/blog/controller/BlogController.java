package org.myblog.domain.blog.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.blog.dto.BlogCreateForm;
import org.myblog.domain.blog.service.BlogService;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.service.PostService;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.myblog.web.login.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/myblog")
@RequiredArgsConstructor
@Slf4j
public class BlogController {

    private final UserService userService;
    private final BlogService blogService;
    private final PostService postService;

    @GetMapping("/myblog/blogs")
    public String blogForm(Model model){
        model.addAttribute("blogCreateForm", new BlogCreateForm());
        return "blog/blogForm";
    }

    @PostMapping("/myblog/blogs")
    public String createBlog(@Validated @ModelAttribute("blogCreateForm")BlogCreateForm form,
                             BindingResult bindingResult,
                             @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm){ // 블로그 생성

        if (bindingResult.hasErrors()){
            return "redirect:/myblog/blogs"; // 타이틀 다시 입력하게끔
        }

        Blog blog = new Blog();
        blog.setTitle(form.getTitle());
        blog.setUser(userService.findByUsername(userLoginForm.getUsername())); // 세션에 저장된 유저name을 기반으로, 블로그에 유저 넣어주기
        blog.setPosts(null); // 아직 새 블로그를 만드는 단계인데, 포스트가 있을리 없음

        blogService.saveBlog(blog);

        // 블로그 생성 완료 (로그인 O, 블로그 생성 완 O)
        return "login/loginhome"; // 그제서야 기타 기능 사용 가능 !!
    }

    // 로그인된 블로그 메인화면
    @GetMapping("/myblog/home")
    public String showBlogHome(
            Model model,
           @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm){

        User foundUser = userService.findById(userLoginForm.getId());
        List<Post> posts = postService.findAllPosts();

        String encodedUsername = URLEncoder.encode(foundUser.getName(), StandardCharsets.UTF_8);

        model.addAttribute("encodedUsername", encodedUsername);
        model.addAttribute("posts", posts);
        model.addAttribute("username", foundUser.getUsername());

        return "login/loginhome";
    }

    // 블로그메인화면에서, 타이틀 클릭하면 이동할 화면
    @GetMapping("/@{blogUsername}/{postTitle}/blog")
    public String showBlogPost(@PathVariable String blogUsername, @PathVariable String postTitle,
                               @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm,
                               Model model
                               ){

        User foundUser = userService.findById(userLoginForm.getId());
        Post foundPost = postService.findByTitle(postTitle);

        if (blogUsername.equals(foundUser.getUsername())){
            // 해당 post에 매핑된 Username이 현재 로그인된 userName이 같음..> 내 글화면으로 이동

            String encodedUsername = URLEncoder.encode(foundUser.getName(), StandardCharsets.UTF_8);
            String encodedPostTitle = URLEncoder.encode(foundPost.getTitle(), StandardCharsets.UTF_8);
            return "redirect:/@" + encodedUsername + "/" + encodedPostTitle;

        } else {
            // 현재 로그인한 유저(userSession)과 현재포스트작성자(userBlog)가 서로 다름 -> 다른 사람의 글

            User blogUser = userService.findByUsername(blogUsername);

            model.addAttribute("userBlog", blogUser);
            model.addAttribute("userSession", foundUser);
            model.addAttribute("post", foundPost);

            return "blog/showBlogPost";
        }
    }
}
