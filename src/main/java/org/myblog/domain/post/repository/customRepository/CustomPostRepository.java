package org.myblog.domain.post.repository.customRepository;

import org.myblog.domain.post.controller.OrderSearch;
import org.myblog.domain.post.domain.Post;

import java.util.List;

public interface CustomPostRepository {
    List<Post> findAllByCriteria(OrderSearch orderSearch);
}
