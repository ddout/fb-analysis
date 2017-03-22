package com.ddout.fb.dao.fb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ISystemInfoMapper {

    Map<String, Object> getSystemInfo();

    void saveSystemInfo(Map<String, Object> json);

    void updateInitCountry();
    void updateInitSeasonAndTeam();
    void updateInitGames();

    
    List<Map<String, Object>> queryAllCountry();
    List<Map<String, Object>> queryAllSeason();

    
    void saveThreadGroup4SeasonAndTeam(HashMap<String, Object> hashMap);
    void saveThreadGroup4Games(HashMap<String, Object> hashMap);

    long queryThreadGroup4SeasonAndTeamCount();
    long queryThreadGroup4GamesCount();

    List<Map<String, Object>> queryAllLeague();

   

}
