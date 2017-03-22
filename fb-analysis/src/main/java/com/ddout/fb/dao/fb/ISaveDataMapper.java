package com.ddout.fb.dao.fb;

import java.util.HashMap;
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

}
