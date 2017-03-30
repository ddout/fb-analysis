package com.ddout.fb.dao.fb;

import java.util.List;
import java.util.Map;

public interface IAnalysisMapper {

    List<Map<String, Object>> queryOddsList(String matchId);

    int getEndMatchCount();

    List<Map<String, Object>> queryEndMatch(Map<String, Object> hashMap);

    void saveAnalysisView1(Map<String, Object> map);

    List<Map<String, Object>> queryHomeAndHomeState(String matchId);

    List<Map<String, Object>> queryAwayAndAwayState(String matchId);
}
