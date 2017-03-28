package com.ddout.fb.service.mysql.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.commons.utils.ParamsUtil;
import com.ddout.fb.dao.fb.ISaveDataMapper;
import com.ddout.fb.service.mysql.ISaveDataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SaveDataServiceImpl implements ISaveDataService {
    @Autowired
    private ISaveDataMapper mapper;

    @Override
    public Map<String, Object> queryCountryOne(Map<String, Object> map) {
	return mapper.queryCountryOne(map);
    }

    @Override
    public void saveCountry(JSONObject json) {
	try {

	    final String region = json.getString("region");
	    final String matchName = json.getString("matchName");
	    final String matchInfoURI = json.getString("matchInfoURI");
	    JSONArray league = json.getJSONArray("league");
	    Map<String, Object> countryMap = new HashMap<String, Object>() {
		private static final long serialVersionUID = 7851079310909583400L;

		{
		    put("region", region);
		    put("matchName", matchName);
		    put("matchInfoURI", matchInfoURI);
		}
	    };
	    int countryIdDb = 0;
	    Map<String, Object> countryInfo = mapper.queryCountryOne(countryMap);
	    if (null == countryInfo || countryInfo.size() == 0) {
		mapper.saveCountry(countryMap);
		countryIdDb = ParamsUtil.getInt4Map(countryMap, "id");
	    } else {
		countryIdDb = ParamsUtil.getInt4Map(countryInfo, "id");
	    }
	    final int countryId = countryIdDb;
	    for (int i = 0; i < league.size(); i++) {
		final JSONObject lg = league.getJSONObject(i);
		Map<String, Object> leagueMap = new HashMap<String, Object>() {
		    private static final long serialVersionUID = 7851079310909583400L;

		    {
			put("countryId", countryId);
			put("region", region);
			put("matchName", matchName);
			put("leagueName", lg.getString("leagueName"));
			put("leagueURI", lg.getString("leagueURI"));
		    }
		};
		Map<String, Object> leagueInfo = mapper.queryLeagueOne(leagueMap);
		if (null == leagueInfo || leagueInfo.size() == 0) {
		    mapper.saveLeague(leagueMap);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void saveSeasonAndTeam(JSONObject season) {
	try {

	    final String region = season.getString("region");
	    final String matchName = season.getString("matchName");
	    final String leagueName = season.getString("leagueName");
	    //
	    final String seasonURI = season.getString("seasonURI");
	    final String seasonName = season.getString("seasonName");
	    final String seasonOrder = season.getString("seasonOrder");
	    //
	    final Map<String, Object> seasonMap = new HashMap<String, Object>() {
		private static final long serialVersionUID = 7851079310909583400L;

		{
		    put("region", region);
		    put("matchName", matchName);
		    put("leagueName", leagueName);
		    put("seasonURI", seasonURI);
		    put("seasonName", seasonName);
		    put("seasonOrder", seasonOrder);
		}
	    };
	    Map<String, Object> seasonInfo = mapper.querySeasonOne(seasonMap);
	    int seasonIdDb = 0;
	    if (null == seasonInfo || seasonInfo.size() == 0) {
		Map<String, Object> LeagueInfo = mapper.queryLeagueOne(new HashMap<String, Object>() {
		    private static final long serialVersionUID = 513090746017366340L;

		    {
			put("region", region);
			put("matchName", matchName);
			put("leagueName", leagueName);
		    }
		});
		seasonMap.put("leagueId", ParamsUtil.getInt4Map(LeagueInfo, "id"));
		mapper.saveSeason(seasonMap);
		seasonIdDb = ParamsUtil.getInt4Map(seasonMap, "id");
	    } else {
		seasonIdDb = ParamsUtil.getInt4Map(seasonInfo, "id");
	    }
	    final int seasonId = seasonIdDb;
	    //
	    JSONArray teams = season.getJSONArray("teams");
	    for (int i = 0; i < teams.size(); i++) {
		final JSONObject team = teams.getJSONObject(i);
		String teamName = team.getString("teamName");
		if (null == teamName || "".equals(teamName)) {
		    continue;
		}
		final String teamNameStr = teamName.trim();
		Map<String, Object> teamMap = new HashMap<String, Object>() {
		    private static final long serialVersionUID = 7851079310909583400L;
		    // Map<String, Object> LeagueInfo =
		    // mapper.queryLeagueOne(new HashMap<String, Object>() {
		    // private static final long serialVersionUID =
		    // 513090746017366340L;
		    //
		    // {
		    // put("region", region);
		    // put("matchName", matchName);
		    // put("leagueName", leagueName);
		    // }
		    // });
		    Map<String, Object> countryMap = mapper.queryCountryOne(new HashMap<String, Object>() {
			private static final long serialVersionUID = 7851079310909583400L;

			{
			    put("region", region);
			    put("matchName", matchName);
			}
		    });

		    {
			put("countryId", ParamsUtil.getInt4Map(countryMap, "id"));
			// put("leagueId", ParamsUtil.getInt4Map(LeagueInfo,
			// "id"));
			put("teamName", teamNameStr);
			put("teamURI", team.getString("teamURI"));
		    }
		};
		Map<String, Object> teamInfo = mapper.queryTeamOne(teamMap);
		int teamId = 0;
		if (null == teamInfo || teamInfo.size() == 0) {
		    mapper.saveTeam(teamMap);
		    teamId = ParamsUtil.getInt4Map(teamMap, "id");
		} else {
		    teamId = ParamsUtil.getInt4Map(teamInfo, "id");
		}
		final int team_id = teamId;
		try {
		    mapper.saveSeasonTeam(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
			    put("seasonId", seasonId);
			    put("teamId", team_id);
			}
		    });
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void updateOldMatch(JSONObject matchObj) {
	try {
	    final String match_id = matchObj.getString("match_id");
	    mapper.deleteAddsInfo(match_id);
	    mapper.deleteMath(match_id);
	    saveMatch(matchObj);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void saveMatch(JSONObject matchObj) {
	try {
	    //
	    final String match_id = matchObj.getString("match_id");
	    //
	    Map<String, Object> matchMap = new HashMap<String, Object>() {
		private static final long serialVersionUID = 5112137007564308212L;

		{
		    put("match_id", match_id);
		}
	    };
	    Map<String, Object> matchInfo = mapper.queryMatchOne(matchMap);
	    if (null != matchInfo && matchInfo.size() > 0) {
		return;
	    }
	    //
	    final JSONObject title = matchObj.getJSONObject("title");
	    final String region = title.getString("region");
	    final String matchName = title.getString("matchName");
	    final String leagueName = title.getString("leagueName");
	    final String seasonName = title.getString("seasonName");
	    //
	    final Map<String, Object> seasonMap = new HashMap<String, Object>() {
		private static final long serialVersionUID = 7851079310909583400L;

		{
		    put("region", region);
		    put("matchName", matchName);
		    put("leagueName", leagueName);
		    put("seasonName", seasonName);
		}
	    };
	    Map<String, Object> seasonInfo = mapper.querySeasonOne(seasonMap);
	    final int seasonId = ParamsUtil.getInt4Map(seasonInfo, "id");
	    //
	    matchMap.put("seasonId", seasonId);
	    matchMap.put("region", region);
	    matchMap.put("matchName", matchName);
	    matchMap.put("leagueName", leagueName);
	    matchMap.put("seasonName", seasonName);
	    //
	    matchMap.put("boxName", getJSONStringValue(title, "boxName"));
	    matchMap.put("groupName", getJSONStringValue(title, "groupName"));
	    matchMap.put("roundName", getJSONStringValue(title, "roundName"));
	    //
	    matchMap.put("infoURI", getJSONStringValue(matchObj, "infoURI"));
	    matchMap.put("refererURI", getJSONStringValue(matchObj, "refererURI"));
	    //
	    matchMap.put("gameTime", getJSONStringValue(matchObj, "gameTime"));
	    //
	    matchMap.put("home_team", getJSONStringValue(matchObj, "home_team"));
	    matchMap.put("away_team", getJSONStringValue(matchObj, "away_team"));
	    String home_score = getJSONStringValue(matchObj, "home_score");
	    String away_score = getJSONStringValue(matchObj, "away_score");
	    matchMap.put("home_score", home_score);
	    matchMap.put("away_score", away_score);
	    if (!"".equals(home_score) && !"".equals(away_score)) {
		try {
		    if (Integer.parseInt(home_score) > Integer.parseInt(away_score)) {
			matchMap.put("match_result", "3");
		    } else if (Integer.parseInt(home_score) == Integer.parseInt(away_score)) {
			matchMap.put("match_result", "1");
		    } else {
			matchMap.put("match_result", "0");
		    }
		} catch (Exception e) {
		    matchMap.put("match_result", "");
		}
	    } else {
		matchMap.put("match_result", "");
	    }

	    matchMap.put("score_result", getJSONStringValue(matchObj, "score_result"));
	    matchMap.put("match_site", getJSONStringValue(matchObj, "match_site"));

	    matchMap.put("odds_info_URI", getJSONStringValue(matchObj, "odds_info_URI"));
	    matchMap.put("odds_info_3", getJSONStringValue(matchObj, "odds_info_3"));
	    matchMap.put("odds_info_1", getJSONStringValue(matchObj, "odds_info_1"));
	    matchMap.put("odds_info_0", getJSONStringValue(matchObj, "odds_info_0"));
	    //
	    mapper.saveMatch(matchMap);
	    //
	    if (matchObj.containsKey("odds_info")) {
		JSONArray odds_infos = matchObj.getJSONArray("odds_info");
		for (int i = 0; i < odds_infos.size(); i++) {
		    final JSONObject adds_info = odds_infos.getJSONObject(i);
		    Map<String, Object> addsInfoMap = new HashMap<String, Object>() {
			private static final long serialVersionUID = -946627902747268801L;

			{
			    put("matchId", match_id);
			    put("makerID", getJSONStringValue(adds_info, "makerID"));
			    put("companyName", getJSONStringValue(adds_info, "companyName"));
			    put("providerName", getJSONStringValue(adds_info, "providerName"));
			    put("payoff", getJSONStringValue(adds_info, "payoff"));
			    put("start_home", getJSONStringValue(adds_info, "start_home"));
			    put("start_draw", getJSONStringValue(adds_info, "start_draw"));
			    put("start_away", getJSONStringValue(adds_info, "start_away"));
			    put("end_home", getJSONStringValue(adds_info, "end_home"));
			    put("end_draw", getJSONStringValue(adds_info, "end_draw"));
			    put("end_away", getJSONStringValue(adds_info, "end_away"));
			    put("radio_home", getJSONStringValue(adds_info, "radio_home"));
			    put("radio_draw", getJSONStringValue(adds_info, "radio_draw"));
			    put("radio_away", getJSONStringValue(adds_info, "radio_away"));
			    put("kelly_home", getJSONStringValue(adds_info, "kelly_home"));
			    put("kelly_draw", getJSONStringValue(adds_info, "kelly_draw"));
			    put("kelly_away", getJSONStringValue(adds_info, "kelly_away"));
			}
		    };
		    Map<String, Object> addsInfoInfo = mapper.queryAddsInfoOne(addsInfoMap);
		    if (null == addsInfoInfo || addsInfoInfo.size() == 0) {
			mapper.saveAddsInfo(addsInfoMap);
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private final static String getJSONStringValue(JSONObject json, String key) {
	if (json.containsKey(key)) {
	    return json.getString(key);
	} else {
	    return "";
	}
    }

    @Override
    public List<Map<String, Object>> getEndMatchList() {
	return mapper.getEndMatchList();

    }

    @Override
    public int queryZucaiNoCount(String no) {
	return mapper.queryZucaiNoCount(no);
    }

    @Override
    public Map<String, Object> queryZucaiMatchForOddsURI(String odds_info_uri) {
	return mapper.queryZucaiMatchForOddsURI(odds_info_uri);
    }

    @Override
    public void saveZucai14Match(Map<String, Object> hashMap) {
//	Map<String, Object> matchInfo = mapper.queryZucai14Match(hashMap);
//	if (null == matchInfo || matchInfo.size() == 0) {
//	    
//	}
	mapper.deleteZucai14Match(hashMap);
	mapper.saveZucai14Match(hashMap);
    }

    @Override
    public void updateLastUpdateTime() {
	mapper.updateLastUpdateTime();	
    }

    @Override
    public String queryMaxZucaiNo() {
	return mapper.queryMaxZucaiNo();	
    }

}
