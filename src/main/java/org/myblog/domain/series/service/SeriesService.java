package org.myblog.domain.series.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.series.domain.Series;
import org.myblog.domain.series.exception.SeriesNotFoundException;
import org.myblog.domain.series.repository.SeriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final PostRepository postRepository;

    public List<Series> findAllSeries(){
        return seriesRepository.findAll();
    }

    public Series findBySeriesName(String seriesName){
        return seriesRepository.findBySeriesName(seriesName);
    }

    public Series save(Series series){
        return seriesRepository.save(series);
    }

    // 각 유저블로그 포스트들에 할당된 시리즈 리스트 반환하기
    // List<Post>에서 시리즈를 추출해, 중복 제거 + null인 Series 필터링 해서 반환해주세요
    public List<Series> getSeriesFromPosts(List<Post> posts){
        return posts.stream()
                .map(Post::getSeries)
                .filter(Objects::nonNull) // null인 Series 필터링
                .distinct() // 중복 제거
                .collect(Collectors.toList());

    }

    // 시리즈 삭제 (단, 시리즈에 해당된 post글은 삭제 X) -- cascade all 주의
    @Transactional
    public void deleteSeries(Long seriesId) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new SeriesNotFoundException("Series not found with id :: " + seriesId));

        List<Post> posts = series.getPosts();
        for (Post post : posts) {
            post.setSeries(null);
            postRepository.save(post); // 변경사항 저장
        }

        seriesRepository.delete(series); // 시리즈 삭제
    }

    // 시리즈 수정
    @Transactional
    public void updateSeriesName(Long seriesId, String seriesName){
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new SeriesNotFoundException("series not found with seriesId :: " + seriesId));
        // 하나의 트랜잭션 단위에서 find 했으니까, 영속성 컨텍스트에 담긴 것
        series.setSeriesName(seriesName);
        seriesRepository.save(series); // 저장해서, 변경감지로 update !
    }
}
