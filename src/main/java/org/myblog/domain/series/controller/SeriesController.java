package org.myblog.domain.series.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.series.domain.Series;
import org.myblog.domain.series.exception.SeriesNotFoundException;
import org.myblog.domain.series.repository.SeriesRepository;
import org.myblog.domain.series.service.SeriesService;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.myblog.web.login.SessionConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SeriesController {

    private final SeriesService seriesService;
    private final SeriesRepository seriesRepository;
    private final UserService userService;

    @GetMapping("/series")
    @ResponseBody
    public ResponseEntity<List<String>> showSeriesList(){
        List<String> tagNameList = seriesService.findAllSeries()
                .stream()
                .map(series -> series.getSeriesName())
                .collect(Collectors.toList());

        return ResponseEntity.ok(tagNameList);
    }

    // th:href="@{|/myblog/series/${series.seriesId}|}"
    @GetMapping("/myblog/series/{seriesId}")
    public String showPostBySeries(
            @PathVariable Long seriesId, Model model,
            @SessionAttribute(name = SessionConst.User_Login_Form, required = false) UserLoginForm userLoginForm){

        Series series = seriesRepository
                        .findById(seriesId)
                        .orElseThrow(() -> new SeriesNotFoundException("series not found with id :: " + seriesId));
        User user = userService.findById(userLoginForm.getId());

        List<Post> posts = series.getPosts(); // 해당 시리즈에 post가 없을 수도 있음

        model.addAttribute("series", series);
        model.addAttribute("posts", posts);
        model.addAttribute("encodedUsername", user.getName());

        return "series/postBySeries";
    }

    // /series/${seriesId}/delete
    @DeleteMapping("/series/{seriesId}/delete")
    @ResponseBody
    public ResponseEntity<?> deleteSeries(@PathVariable Long seriesId){
        seriesService.deleteSeries(seriesId);

        return ResponseEntity.ok().build();
    }

    // /series/${seriesId}/update
    @PatchMapping("/series/{seriesId}/update")
    @ResponseBody
    public ResponseEntity<?> updateSeriesName(@PathVariable Long seriesId,
                                              @RequestBody UpdateSeriesNameRequestDto updateSeriesNameRequestDto){

        log.info("수정 후 :: seriesNewName :: {}", updateSeriesNameRequestDto.getSeriesName());

        // 시리즈 이름 업데이트 서비스 호출 등 필요한 로직 추가
        seriesService.updateSeriesName(seriesId, updateSeriesNameRequestDto.getSeriesName());

        UpdateSeriesNameResponseDto updateSeriesNameResponseDto = new UpdateSeriesNameResponseDto(
                updateSeriesNameRequestDto.getSeriesName()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateSeriesNameResponseDto);
    }


    @Getter
    @NoArgsConstructor
    static class UpdateSeriesNameRequestDto {
        private String seriesName;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter @Getter
    static class UpdateSeriesNameResponseDto {
        private String newSeriesName;
    }
}
