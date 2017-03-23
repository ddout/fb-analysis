package com.ddout.fb.dao.fb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IViewMapper {

    Map<String, Object> getSystemView();

    List<Map<String, Object>> queryMatchSreach(HashMap<String, Object> hashMap);

    Map<String, Object> getMatchInfo(String matchId);

    List<Map<String, Object>> getOddsInfo(String matchId);

}
