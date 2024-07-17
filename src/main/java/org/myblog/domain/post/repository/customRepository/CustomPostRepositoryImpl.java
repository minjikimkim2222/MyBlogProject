package org.myblog.domain.post.repository.customRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.post.controller.OrderSearch;
import org.myblog.domain.post.domain.Post;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
    1. 제목과 부제목 모두 빈칸 -> 모든 포스트 반환
    2. 제목만 검색, 부제목은 빈칸 -> 제목으로 검색
    3. 제목은 빈칸이고, 부제목만 값이 있는 경우 -> 부제목으로 검색
    4. 제목, 부제목 모두 값이 있는 경우 -> 제목과 부제목으로 검색
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final EntityManager em;
    @Override
    public List<Post> findAllByCriteria(OrderSearch orderSearch) {

        // select * from post p 쿼리 생성
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> post = cq.from(Post.class);

        List<Predicate> criteria = new ArrayList<>();

        // 제목 검색
        if (StringUtils.hasText(orderSearch.getTitle())) {
            Predicate titlePredicate = cb.like(post.get("title"), "%" + orderSearch.getTitle() + "%");
            criteria.add(titlePredicate);
        }

        // 부제목 검색
        if (StringUtils.hasText(orderSearch.getSubtitle())) {
            Predicate subtitlePredicate = cb.like(post.get("subtitle"), "%" + orderSearch.getSubtitle() + "%");
            criteria.add(subtitlePredicate);
        }

        if (!criteria.isEmpty()) {
            cq.where(cb.and(criteria.toArray(new Predicate[0]))); // where 조건절 추가
        }

        TypedQuery<Post> query = em.createQuery(cq).setMaxResults(1000); // 최대 1000건

        return query.getResultList();
    }
}
