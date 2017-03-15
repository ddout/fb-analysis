package com.ddout.fb.service.parse;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface ICrawlerService {
    /**
     * 抓取国家/联赛信息
     * 
     * @return
     */
    public JSONArray parseCountry();

    /**
     * 解析赛季-球队信息
     * 
     * @param countrys
     * @return
     */
    public JSONArray parseSeasonAndTeam(List<JSONObject> countrys);
}
