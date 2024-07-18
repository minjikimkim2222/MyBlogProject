package org.myblog.domain.series.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.series.domain.Series;
import org.myblog.domain.series.repository.SeriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;

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
}
