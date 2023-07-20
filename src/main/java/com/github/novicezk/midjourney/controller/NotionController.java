package com.github.novicezk.midjourney.controller;

import com.github.novicezk.midjourney.service.NotionService;
import com.github.novicezk.midjourney.support.PromptWords;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(tags = "行业字典")
@RestController
@RequestMapping("/notion")
@RequiredArgsConstructor
public class NotionController {

    private final NotionService notionService;

    @ApiOperation(value = "查询关键词")
    @GetMapping("/list")
    public List<PromptWords> list() {
        return notionService.list();
    }

    @ApiOperation(value = "查询关键词")
    @GetMapping("/tree")
    public Map<String, PromptWords> tree() {
        List<PromptWords> list = notionService.list();
        return list
                .stream()
                .collect(Collectors.toUnmodifiableMap(PromptWords::getSubType, Function.identity()));
    }
}
