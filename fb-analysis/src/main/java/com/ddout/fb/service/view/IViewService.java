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
     * 查询match列表
     * 
     * @param parm
     * @return
     */
    List<Map<String, Object>> queryMatchSreach(Map<String, Object> parm);

    /**
     * 获取单个match详情
     * 
     * @param parm
     * @return
     */
    Map<String, Object> queryMatchInfoView(Map<String, Object> parm);

    /**
     * analysis info
     * 
     * @param parm
     * @return
     */
    Map<String, Object> queryViewAnalysis(Map<String, Object> parm);

}
