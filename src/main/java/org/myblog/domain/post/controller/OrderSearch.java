package org.myblog.domain.post.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String title; // post 글 제목
    private String subtitle; // post 글 부제목
}
