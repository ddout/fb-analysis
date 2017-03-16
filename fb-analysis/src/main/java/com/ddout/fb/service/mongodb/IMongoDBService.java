package com.ddout.fb.service.mongodb;

import java.util.List;

import net.sf.json.JSONObject;

public interface IMongoDBService {
    /** 国家/联赛 */
    public static final String COLNAME_COUNTRY = "country_league";
    /** 赛季 */
    public static final String COLNAME_SEASON = "country_season";
    /** 球队 */
    public static final String COLNAME_TEAM = "country_team";
    /** 比赛 */
    public static final String COLNAME_MATCH = "country_match";

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

}
