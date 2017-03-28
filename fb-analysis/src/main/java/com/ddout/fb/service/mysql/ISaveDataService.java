package com.ddout.fb.service.mysql;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public interface ISaveDataService {
    Map<String, Object> queryCountryOne(Map<String, Object> map);

    void saveCountry(JSONObject json);
    
    void saveSeasonAndTeam(JSONObject season);

    void saveMatch(JSONObject matchObj);

    List<Map<String, Object>> getEndMatchList();

    void updateOldMatch(JSONObject matchObj);

    int queryZucaiNoCount(String no);

    Map<String, Object> queryZucaiMatchForOddsURI(String odds_info_uri);

    void saveZucai14Match(Map<String, Object> hashMap);

    void updateLastUpdateTime();

    String queryMaxZucaiNo();

}
