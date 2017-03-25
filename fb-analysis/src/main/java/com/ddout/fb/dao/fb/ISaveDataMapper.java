package com.ddout.fb.dao.fb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ISaveDataMapper {

    Map<String, Object> queryCountryOne(Map<String, Object> map);

    void saveCountry(Map<String, Object> map);

    void saveLeague(Map<String, Object> map);

    Map<String, Object> queryLeagueOne(Map<String, Object> leagueMap);

    Map<String, Object> querySeasonOne(Map<String, Object> seasonMap);

    void saveSeason(Map<String, Object> seasonMap);

    Map<String, Object> queryTeamOne(Map<String, Object> teamMap);

    void saveTeam(Map<String, Object> teamMap);

    void saveSeasonTeam(HashMap<String, Object> hashMap);

    Map<String, Object> queryMatchOne(Map<String, Object> matchMap);

    void saveMatch(Map<String, Object> matchMap);

    Map<String, Object> queryAddsInfoOne(Map<String, Object> addsInfoMap);

    void saveAddsInfo(Map<String, Object> addsInfoMap);

    List<Map<String, Object>> getEndMatchList();

    void deleteMath(String match_id);

    void deleteAddsInfo(String match_id);

    int queryZucaiNoCount(String no);

    Map<String, Object> queryZucaiMatchForOddsURI(String odds_info_uri);

    void saveZucai14Match(HashMap<String, Object> hashMap);

    Map<String, Object> queryZucai14Match(HashMap<String, Object> hashMap);

    void updateLastUpdateTime();

}
