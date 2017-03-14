package com.ddout.fb.service.analysis;

import net.sf.json.JSONArray;

public interface ICrawlerService {
    /**
     * 抓取国家信息
     * 
     * @return
     */
    public JSONArray parseCountry();
}
