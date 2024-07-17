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

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final EntityManager em;
    @Override
    public List<Post> findAllByCriteria(OrderSearch orderSearch) {

        // 1. SELECT * FROM posts p 쿼리 생성
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class); // Post 엔디티를 대상으로 하는 쿼리 생성
        Root<Post> post = cq.from(Post.class); // 쿼리에서 사용할 루트 엔디티

        List<Predicate> criteria = new ArrayList<>(); // 검색 조건을 담을, 리스트 생성

        // 2-1. 제목 검색 -- WHERE p.title LIKE '%{orderSearch.getTitle()}%'
        if (orderSearch.getTitle() != null){
            Predicate titlePredicate = cb.like(post.get("title"), "%" + orderSearch.getTitle() + "%");
            criteria.add(titlePredicate);
        }

        // 2-2. 내용 검색 -- WHERE p.subtitle LIKE '%{orderSearch.getSubtitle()}%'
        if (StringUtils.hasText(orderSearch.getSubtitle())) {
            Predicate subtitlePredicate = cb.like(post.get("content"), "%" + orderSearch.getSubtitle() + "%");
            criteria.add(subtitlePredicate);
        }

        // 3. 조건들을 OR로 연결해서 WHERE 절에 추가
        // -- WHERE (p.title LIKE '%{orderSearch.getTitle()}%' OR p.subtitle LIKE '%{orderSearch.getSubtitle()}%')
        if (!criteria.isEmpty()){
            cq.where(cb.or(criteria.toArray(new Predicate[0]))); // where 조건절 추가
        }

        // 4. 최종 쿼리
        // -- SELECT * FROM posts p
        //    WHERE (p.title LIKE '%{orderSearch.getTitle()}%' OR p.subtitle LIKE '%{orderSearch.getSubtitle()}%')
        //    LIMIT 1000
        TypedQuery<Post> query = em.createQuery(cq).setMaxResults(1000);// 최대 1000건

        return query.getResultList();
    }
}
