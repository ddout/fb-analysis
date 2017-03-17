package com.ddout.fb.service.mongodb;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

import net.sf.json.JSONObject;

public interface IMongoDBService {

    /**
     * 存储系统信息
     * 
     * @param json
     */
    public void saveSystemInfo(JSONObject json);

    public JSONObject getSystemInfo();

    /*
     * 
     */
    /**
     * 获取单个对象
     * 
     * @param parm
     * @param collectionName
     * @return
     */
    public JSONObject getOneObj(Map<String, Object> parm, String collectionName);

    /**
     * 获取集合对象
     * 
     * @param parm
     * @param collectionName
     * @return
     */
    public List<JSONObject> getObjsForParm(Map<String, Object> parm, String collectionName);

    public List<JSONObject> getObjsForCriteria(Criteria criatiraTeam, String collectionName);

    /**
     * 更新对象
     * 
     * @param criatiraTeam
     * @param update
     * @param collectionName
     */
    public void updateObj(Criteria criatiraTeam, Update update, String collectionName);

    /**
     * 存储对象
     * 
     * @param obj
     *            对象
     * @param collectionName
     *            集合名称
     */
    public void saveObject(JSONObject obj, String collectionName);

    /**
     * 获取集合中所有对象
     * 
     * @param collectionName
     * @return
     */
    public List<JSONObject> queryAllObject(String collectionName);

    /**
     * 存储国家对象
     * 
     * @param json
     */
    public void saveCountry(JSONObject json);

    /**
     * 存储赛季-球队
     * 
     * @param season
     */
    public void saveSeasonAndTeam(JSONObject season);

    /**
     * 存比赛
     * 
     * @param matchObj
     */
    public void saveMatch(JSONObject matchObj);

    public Object queryTest();

    public long getCount(Criteria criatira, String colnameMatch);

}
