package org.myblog.domain.series.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.series.domain.Series;
import org.myblog.domain.series.repository.SeriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;

    public List<Series> findAllSeries(){
        return seriesRepository.findAll();
    }
}
