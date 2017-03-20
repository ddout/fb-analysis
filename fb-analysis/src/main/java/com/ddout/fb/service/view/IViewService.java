package com.ddout.fb.service.view;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public interface IViewService {
    /**
     * 获取当前服务信息
     * 
     * @return
     */
    JSONObject viewServerInfo();

    /**
     * 获取联赛信息--赛季
     * 
     * @return
     */
    List<JSONObject> viewSeasonInfo(Map<String, Object> parm);

}
