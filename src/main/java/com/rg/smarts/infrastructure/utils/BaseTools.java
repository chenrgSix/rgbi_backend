package com.rg.smarts.infrastructure.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: czr
 * @CreateTime: 2025-02-23
 * @Description: 为AI提供工具
 */
@Component
public class BaseTools {

    @Value("${langchain4j.zhipu.apiKey}")
    private String apiKey;  //智普AI的apiKey

    @Tool("获取当前时间、日期、几点")
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }

    @Tool("今日天气")
    public String getCurrentWeather(String city) {
        /**
         * 仅供学习 使用，请勿用于生产环境
         */
        long currentTimeMillis = System.currentTimeMillis();
        String cityCodeUrl = "https://weather.cma.cn/api/autocomplete?q=" + city + "&limit=10&timestamp=" + currentTimeMillis;
        String cityCodes = HttpUtil.get(cityCodeUrl);
        JSONObject entries = JSONUtil.parseObj(cityCodes);
        List<String> data = (List<String>) entries.get("data");
        String code = data.get(0).split("\\|")[0];
        String cityDataUrl = "https://weather.cma.cn/api/now/" + code;
        return HttpUtil.get(cityDataUrl);
    }

    @Tool("通过搜索获取网络信息")
    public String getWebSearch(String searchContent) {
        /**
         * 智普提供
         */
        String url = "https://open.bigmodel.cn/api/paas/v4/tools";
        // 构建消息内容
        String json = "{"
                + "\"request_id\":\"" + UUID.randomUUID().toString() + "\","
                + "\"tool\":\"web-search-pro\","
                + "\"stream\":false,"
                + "\"messages\":["
                + "{"
                + "\"role\":\"user\","
                + "\"content\":\"" + searchContent + "\""
                + "}"
                + "]"
                + "}";
        String result2 = HttpRequest.post(url)
                .header("Authorization", apiKey)
                .body(json)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        System.out.println(result2);
        return result2;
    }


}
