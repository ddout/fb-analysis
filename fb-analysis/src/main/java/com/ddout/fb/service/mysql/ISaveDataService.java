package com.ddout.fb.service.mysql;

import java.util.Map;

import net.sf.json.JSONObject;

public interface ISaveDataService {
    Map<String, Object> queryCountryOne(Map<String, Object> map);

    void saveCountry(JSONObject json);

    void saveSeasonAndTeam(JSONObject season);

    void saveMatch(JSONObject matchObj);

}
