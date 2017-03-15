package com.ddout.fb.service.parse;

import java.util.List;

import net.sf.json.JSONObject;

public interface ISerializeForDB {
    /**
     * 序列化国家-联赛
     * 
     * @param countrys
     */
    public void saveCountry();

    /**
     * 获取所有国家-联赛
     * 
     * @return
     */
    public List<JSONObject> queryCountry();
}
