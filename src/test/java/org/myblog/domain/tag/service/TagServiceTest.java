package org.myblog.domain.tag.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.tag.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TagServiceTest {

    @Autowired TagService tagService;
    @Test
    public void 태그종류와_개수_맵체크() throws Exception {
        // given
        Blog blog = new Blog();
        // 블로그에 필요한 필드 설정
        Tag tag1 = new Tag();
        tag1.setTagName("tag1");

        Tag tag2 = new Tag();
        tag2.setTagName("tag2");

        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setTags(Arrays.asList(tag1));  // tags 초기화

        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setTags(Arrays.asList(tag2));  // tags 초기화

        Post post3 = new Post();
        post3.setTitle("Post 3");
        post3.setTags(Arrays.asList(tag1));  // tags 초기화

        List<Post> posts = Arrays.asList(post1, post2, post3);

        // when
        Map<String, Integer> tagCountMap = tagService.getTagCounts(posts);

        // then
        assertThat(tagCountMap).containsEntry("tag1", 2);
        assertThat(tagCountMap).containsEntry("tag2", 1);

    }
}