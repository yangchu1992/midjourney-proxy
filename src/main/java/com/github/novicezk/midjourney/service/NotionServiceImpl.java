package com.github.novicezk.midjourney.service;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.novicezk.midjourney.ProxyProperties;
import com.github.novicezk.midjourney.support.PromptWords;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotionServiceImpl implements NotionService {

    private final ProxyProperties properties;

    private String notionApiUrl;

    private String databaseId;

    private String notionUserToken;

    private String userAgent;

    @PostConstruct
    void init() {
        ProxyProperties.NotionConfig notion = this.properties.getNotion();
        this.notionUserToken = notion.getUserToken();
        this.databaseId = notion.getDatabaseId();
        this.userAgent = notion.getUserAgent();
        this.notionApiUrl = "https://api.notion.com/v1/databases/" + databaseId + "/query";
    }

    @Override
    public List<PromptWords> list() {
        String result = postJson(notionApiUrl, null).getBody();
        log.info("{}", result);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONArray resultsNode = jsonObject.getJSONArray("results");
        return resultsNode
                .stream()
                .map(item -> {
                    JSONObject obj = (JSONObject) item;
                    PromptWords promptWords = new PromptWords();
                    promptWords.setId(obj.getByPath("id", String.class));
                    promptWords.setSubType(obj.getByPath("properties.subType.select.name", String.class));
                    promptWords.setDir(obj.getByPath("properties.dir.select.name", String.class));
                    promptWords.setText(ArrayUtil.join(obj.getByPath("properties.text.title.plain_text", String[].class), StrPool.COMMA));
                    promptWords.setDesc(obj.getByPath("properties.desc.rich_text.plain_text", String.class));
                    promptWords.setLangZh(ArrayUtil.join(obj.getByPath("properties.lang_zh.rich_text.plain_text", String[].class), StrPool.COMMA));
                    return promptWords;
                }).collect(Collectors.toList());
    }

    private ResponseEntity<String> postJson(String url, String paramsStr) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + this.notionUserToken);
        headers.add("Notion-Version", "2022-06-28");
        headers.add("User-Agent", this.userAgent);
        HttpEntity<String> httpEntity = new HttpEntity<>(paramsStr, headers);
        return new RestTemplate().postForEntity(url, httpEntity, String.class);
    }
}
