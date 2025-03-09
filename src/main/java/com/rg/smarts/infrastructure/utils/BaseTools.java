package com.rg.smarts.infrastructure.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.agent.tool.Tool;
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


    @Tool(name="获取当前时间", value = "可以获取到当前具体的时间日期")
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
}
