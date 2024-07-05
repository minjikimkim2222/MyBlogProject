package org.myblog.domain.series.controller;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.series.service.SeriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<String>> showSeriesList(){
        List<String> tagNameList = seriesService.findAllSeries()
                .stream()
                .map(series -> series.getSeriesName())
                .collect(Collectors.toList());

        return ResponseEntity.ok(tagNameList);
    }
}
