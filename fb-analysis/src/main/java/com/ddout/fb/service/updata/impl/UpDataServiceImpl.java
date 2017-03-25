package com.ddout.fb.service.updata.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.commons.utils.ParamsUtil;
import com.ddout.fb.dao.fb.ISystemInfoMapper;
import com.ddout.fb.service.ICust;
import com.ddout.fb.service.mysql.ISaveDataService;
import com.ddout.fb.service.updata.IUpDataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class UpDataServiceImpl implements IUpDataService {
    public static final Logger logger = LoggerFactory.getLogger(UpDataServiceImpl.class);
    @Autowired
    private ISystemInfoMapper systemInfoMapper;
    @Autowired
    private ISaveDataService dataService;

    private static Connection getConnect(String url) {
	Connection conn = Jsoup.connect(url);// 获取请求连接
	conn.timeout(1000 * 15);
	return conn;
    }

    @Override
    public void updateNewSeason() {
	Map<String, Object> systemInfo = systemInfoMapper.getSystemInfo();
	if (null == systemInfo || !"finally".equals(ParamsUtil.getString4Map(systemInfo, "init_SeasonAndTeam"))) {
	    logger.info("System is not inited, updateNewSeason is continue;");
	    return;
	}

	List<Map<String, Object>> leagues = systemInfoMapper.queryAllLeague();
	for (Map<String, Object> league : leagues) {
	    String region = ParamsUtil.getString4Map(league, "region");// 联赛名称
	    String matchName = ParamsUtil.getString4Map(league, "matchName");// 联赛名称
	    String leagueName = ParamsUtil.getString4Map(league, "leagueName");// 联赛名称
	    String baseURI = ParamsUtil.getString4Map(league, "leagueURI");// 联赛基准uri
	    //
	    Connection con = getConnect(ICust.BASE_PATH + baseURI);// 获取请求连接
	    con.header("Referer", ICust.BASE_PATH + ICust.BASE_PATH_COUNTRY);
	    try {
		Document doc = con.get();
		// 赛季
		Elements seasonHtml = doc.select("div.LotteryList_Data").get(3).select("a.BlueWord_TxtL");
		int seasonSize = seasonHtml.size();
		for (Element ses : seasonHtml) {
		    String href = ses.attr("href");// 赛季uri
		    String seasonName = ses.html().trim();// 赛季名称
		    JSONObject season = new JSONObject();
		    season.put("region", region);
		    season.put("matchName", matchName);
		    season.put("leagueName", leagueName);
		    season.put("seasonURI", href);
		    season.put("seasonName", seasonName);
		    season.put("seasonOrder", seasonSize + "");
		    seasonSize = seasonSize - 1;
		    JSONArray teams = parseSeason4Teams(season);// 解析当前赛季的team
		    season.put("teams", teams);
		    // 存储-db
		    dataService.saveSeasonAndTeam(season);
		    logger.info("parseSeasonAndTeam end:" + season);
		}
	    } catch (IOException e) {
		logger.error("parse SeasonAndTeam is errot", e);
	    }
	}

    }

    private JSONArray parseSeason4Teams(JSONObject season) {
	JSONArray teams = new JSONArray();
	String seasonURI = season.getString("seasonURI");
	String region = season.getString("region");
	String matchName = season.getString("matchName");
	Connection con = getConnect(ICust.BASE_PATH + seasonURI);// 获取请求连接
	con.header("Referer", ICust.BASE_PATH + ICust.BASE_PATH_COUNTRY);
	try {
	    Document doc = con.get();
	    // 球队
	    Elements teamHtml = doc.select("div.LotteryList_Data").get(4).select("a.BlueWord_TxtL");
	    for (Element tm : teamHtml) {
		String href = tm.attr("href");
		String teamName = tm.html().trim();
		JSONObject team = new JSONObject();
		team.put("teamURI", href);
		team.put("region", region);
		team.put("matchName", matchName);
		team.put("teamName", teamName);// team名称
		teams.add(team);
	    }
	    //
	} catch (IOException e) {
	    logger.error("parse team is errot", e);
	}
	return teams;
    }

    @Override
    public void updateNewMatch() {
	Map<String, Object> systemInfo = systemInfoMapper.getSystemInfo();
	if (null == systemInfo || !"finally".equals(ParamsUtil.getString4Map(systemInfo, "init_Games"))) {
	    logger.info("System is not inited, updateNewMatch is continue;");
	    return;
	}
	//
	List<Map<String, Object>> leagues = systemInfoMapper.queryAllLeague();
	for (Map<String, Object> league : leagues) {
	    String region = ParamsUtil.getString4Map(league, "region");// 联赛名称
	    String matchName = ParamsUtil.getString4Map(league, "matchName");// 联赛名称
	    String leagueName = ParamsUtil.getString4Map(league, "leagueName");// 联赛名称
	    String baseURI = ParamsUtil.getString4Map(league, "leagueURI");// 联赛基准uri
	    //
	    Connection conBase = getConnect(ICust.BASE_PATH + baseURI);// 获取请求连接
	    conBase.header("Referer", ICust.BASE_PATH + ICust.BASE_PATH_COUNTRY);
	    try {
		Document docBase = conBase.get();
		// 最新赛季
		Element ses = docBase.select("div.LotteryList_Data").get(3).select("a.BlueWord_TxtL").get(0);
		String uri = ses.attr("href");// 赛季uri
		String seasonName = ses.html().trim();// 赛季名称
		//
		//
		parseSeasonMatch(region, matchName, leagueName, uri, seasonName);
		//
	    } catch (IOException e) {
		logger.error("parse SeasonAndTeam is errot", e);
	    }
	}
    }

    private void parseSeasonMatch(String region, String matchName, String leagueName, String uri, String seasonName) {
	Connection con = getConnect(ICust.BASE_PATH + uri);// 获取请求连接
	con.header("Referer", ICust.BASE_PATH + uri);
	try {
	    Document doc = con.get();
	    // 资格赛-小组赛-淘汰赛
	    Elements jsqtitlebox = doc.select("div.jsqtitlebox");
	    if (!"".equals(jsqtitlebox.html())) {// 不为空就是杯赛
		Elements atags = jsqtitlebox.select("a");
		for (Element atag : atags) {
		    try {
			String boxUri = atag.attr("href");
			String boxName = atag.html().trim();
			Connection boxcon = getConnect(ICust.BASE_PATH + boxUri);// 获取请求连接
			boxcon.header("Referer", ICust.BASE_PATH + uri);
			Document boxdoc = boxcon.get();
			// 分组--A组，B组
			Elements buttonBg04_Off = boxdoc.select("div.ButtonBg04_Off");//
			if (!"".equals(buttonBg04_Off.html())) {// 有分组
			    Elements groups = buttonBg04_Off.select("a");
			    for (Element group : groups) {
				try {
				    String groupUri = group.attr("href");
				    String groupName = group.html().trim();
				    Connection groupcon = getConnect(ICust.BASE_PATH + groupUri);// 获取请求连接
				    groupcon.header("Referer", ICust.BASE_PATH + boxUri);
				    Document groupdoc = groupcon.get();
				    //
				    // 第一轮-第二轮，全部,如果是联赛就是
				    // 1,2,3,4...38
				    // region, matchName,
				    // leagueName,
				    // seasonName, boxName,
				    // groupName
				    JSONObject names = new JSONObject();
				    names.put("region", region);
				    names.put("matchName", matchName);
				    names.put("leagueName", leagueName);
				    names.put("seasonName", seasonName);
				    names.put("boxName", boxName);
				    names.put("groupName", groupName);
				    parseRound(names, groupdoc, groupUri, boxUri);
				} catch (Exception e) {
				    logger.error("parse groups is errot", e);
				}
			    }
			} else {
			    // 第一轮-第二轮，全部,如果是联赛就是 1,2,3,4...38
			    // region, matchName, leagueName,
			    // seasonName,
			    // boxName
			    JSONObject names = new JSONObject();
			    names.put("region", region);
			    names.put("matchName", matchName);
			    names.put("leagueName", leagueName);
			    names.put("seasonName", seasonName);
			    names.put("boxName", boxName.trim());
			    parseRound(names, boxdoc, boxUri, uri);
			}
		    } catch (Exception e) {
			logger.error("parse jsqtitlebox is errot", e);
		    }

		}
	    } else {// 联赛
		// 第一轮-第二轮，全部,如果是联赛就是 1,2,3,4...38
		// new String[] { region, matchName, leagueName,
		// seasonName
		JSONObject names = new JSONObject();
		names.put("region", region);
		names.put("matchName", matchName);
		names.put("leagueName", leagueName);
		names.put("seasonName", seasonName);
		parseRound(names, doc, uri, uri);
	    }

	} catch (IOException e) {
	    logger.error("parse games is errot", e);
	}
    }

    private void parseRound(JSONObject names, Document groupdoc, String infoURI, String refererURI) {
	// 第一轮-第二轮，全部,如果是联赛就是 1,2,3,4...38
	Elements linkblock = groupdoc.select("table.SmallFont.linkblock a");
	//
	if ("".equals(linkblock)) {// 没有轮次的情况
	    // 可以直接解析比赛数据了
	    JSONObject matchObj = parseGameInfo(names, groupdoc, infoURI, refererURI);
	    if (null != matchObj) {
		dataService.saveMatch(matchObj);
	    }
	} else {// 排除全部
	    for (Element block : linkblock) {
		String blockUri = block.attr("href");
		String blockName = block.html().replaceAll("<b>", "").replaceAll("</b>", "").trim();
		if (!"全部".equals(blockName)) {
		    try {
			Connection conn = getConnect(ICust.BASE_PATH + blockUri);// 获取请求连接
			conn.header("Referer", ICust.BASE_PATH + blockUri);
			Document doc = conn.get();
			// 可以直接解析比赛数据了
			names.put("roundName", blockName);
			JSONObject matchObj = parseGameInfo(names, doc, blockUri, blockUri);
			if (null != matchObj) {
			    dataService.saveMatch(matchObj);
			}
		    } catch (Exception e) {
			logger.error("parse groups is errot", e);
		    }
		}
	    }
	}
    };

    private JSONObject parseGameInfo(JSONObject names, Document doc, String infoURI, String refererURI) {
	// #team_fight_table
	JSONObject matchResult = null;
	Elements ddtable = doc.select("#team_fight_table");
	if (!"".equals(ddtable.toString())) {
	    // 开始解析比赛
	    Element table = ddtable.get(0);
	    // System.out.println(names);
	    // System.out.println(table);
	    Elements matchs = table.select("tr.BlackWords");
	    for (Element match : matchs) {
		String matchid = match.attr("matchid");
		if (null == matchid || "".equals(matchid)) {
		    continue;
		}
		try {
		    JSONObject matchObj = new JSONObject();
		    matchObj.put("title", names);
		    matchObj.put("infoURI", infoURI);
		    matchObj.put("refererURI", refererURI);
		    //
		    Elements tds = match.select("td");// size=8
		    matchObj.put("match_id", matchid);
		    matchObj.put("gameTime", tds.get(0).html().trim());
		    matchObj.put("round", tds.get(1).html().trim());
		    matchObj.put("home_team", tds.get(2).html().trim());
		    matchObj.put("away_team", tds.get(4).html().trim());
		    // 比分
		    String bf = tds.get(3).html().trim();
		    if ("延期".equals(bf) || "VS".equals(bf)) {// 比赛未开始或延期
			matchObj.put("score_result", bf);
			matchObj.put("match_site", "");
			matchObj.put("home_score", "");
			matchObj.put("away_score", "");
		    } else {// 已经完赛的
			Element atag = tds.get(3).select("a").get(0);
			String scoreStr = atag.select("strong").get(0).html().trim();
			matchObj.put("score_result", "end");
			matchObj.put("match_site", atag.attr("href"));
			String[] scoreArr = scoreStr.split("-");
			matchObj.put("home_score", scoreArr[0]);
			matchObj.put("away_score", scoreArr[1]);
		    }
		    // 赔率相关
		    // 平均初赔
		    String odds_init_3 = tds.get(5).html().trim();
		    String odds_init_1 = tds.get(6).html().trim();
		    String odds_init_0 = tds.get(7).html().trim();
		    matchObj.put("odds_info_uri", "/soccer/match/" + matchid + "/odds/");
		    if ("-".equals(odds_init_1) || "-".equals(odds_init_0) || "-".equals(odds_init_3)) {
			matchObj.put("odds_info_3", null);
			matchObj.put("odds_info_1", null);
			matchObj.put("odds_info_0", null);
			matchObj.put("odds_info", null);
		    } else {
			matchObj.put("odds_info_3", odds_init_3);
			matchObj.put("odds_info_1", odds_init_1);
			matchObj.put("odds_info_0", odds_init_0);
			matchObj.put("odds_info", parseMatchOddsInfo(matchObj));
		    }
		    //
		    matchResult = matchObj;
		    //
		    logger.info("parseGameInfo end:" + matchObj);
		} catch (Exception e) {
		    logger.error("parse match error", e);
		}
	    }
	}
	return matchResult;
    }

    private static final String ODDS_AJAX_URI = "ajax/?page={0}&companytype=BaijiaBooks&type=1";

    private JSONArray parseMatchOddsInfo(JSONObject matchObj) {
	JSONArray arr = new JSONArray();
	String odds_info_uri = matchObj.getString("odds_info_uri");
	//
	for (int i = 0; i < 6; i++) {
	    String ajaxUri = java.text.MessageFormat.format(ODDS_AJAX_URI, i);
	    try {
		Connection conn = getConnect(ICust.BASE_PATH + odds_info_uri + ajaxUri);// 获取请求连接
		conn.header("Referer", ICust.BASE_PATH + odds_info_uri);
		Document doc = conn.get();
		/// soccer/match/877269/odds/ajax/?page=0&companytype=BaijiaBooks&type=1
		// Referer:http://www.okooo.com/soccer/match/877269/odds/
		Elements script = doc.select("script");
		String scriptHtml = script.get(0).html();
		String tempStr = scriptHtml.substring(scriptHtml.indexOf("'") + 1, scriptHtml.indexOf("';"));
		JSONArray dataJSONArr = JSONArray.fromObject(tempStr);
		for (int j = 0; j < dataJSONArr.size(); j++) {
		    JSONObject jsonObj = dataJSONArr.getJSONObject(j);
		    JSONObject data = new JSONObject();
		    data.put("makerID", jsonObj.getString("MakerID"));
		    data.put("companyName", jsonObj.getString("CompanyName"));// 公司名称
		    data.put("providerName", jsonObj.getString("ProviderName"));// 供应商名称
		    //
		    data.put("payoff", jsonObj.getString("Payoff"));// 赔付率
		    JSONObject start = jsonObj.getJSONObject("Start");// 初始指数
		    data.put("start_home", start.getString("home"));
		    data.put("start_draw", start.getString("draw"));
		    data.put("start_away", start.getString("away"));
		    JSONObject end = jsonObj.getJSONObject("End");// 最新指数
		    data.put("end_home", end.getString("home"));
		    data.put("end_draw", end.getString("draw"));
		    data.put("end_away", end.getString("away"));
		    JSONObject radio = jsonObj.getJSONObject("Radio");// 最新概率
		    data.put("radio_home", radio.getString("home"));
		    data.put("radio_draw", radio.getString("draw"));
		    data.put("radio_away", radio.getString("away"));
		    JSONObject kelly = jsonObj.getJSONObject("Kelly");// 凯利指数
		    data.put("kelly_home", kelly.getString("home"));
		    data.put("kelly_draw", kelly.getString("draw"));
		    data.put("kelly_away", kelly.getString("away"));
		    //
		    arr.add(data);
		}
	    } catch (Exception e) {
		logger.error("parse MatchOddsInfo error", e);
	    }
	}
	return arr;
    }

    @Override
    public void updateOldMatch() {
	Map<String, Object> systemInfo = systemInfoMapper.getSystemInfo();
	if (null == systemInfo || !"finally".equals(ParamsUtil.getString4Map(systemInfo, "init_Games"))) {
	    logger.info("System is not inited, updateNewMatch is continue;");
	    return;
	}
	// 预估1W的数据
	final List<Map<String, Object>> maths = dataService.getEndMatchList();
	logger.debug("=" + maths.size());
	for (Map<String, Object> match : maths) {
	    try {
		parseOldMatch(JSONObject.fromObject(match));
	    } catch (Exception e) {
		logger.error("parse match is errot", e);
	    }
	}

    }

    private void parseOldMatch(JSONObject match) throws IOException {
	String infoURI = match.getString("infoURI");
	String refererURI = match.getString("refererURI");
	String match_id = match.getString("id");
	//
	JSONObject names = new JSONObject();
	names.put("region", match.getString("region"));
	names.put("matchName", match.getString("matchName"));
	names.put("leagueName", match.getString("leagueName"));
	names.put("seasonName", match.getString("seasonName"));
	names.put("boxName", match.getString("boxName"));
	names.put("groupName", match.getString("groupName"));
	//
	Connection conn = getConnect(ICust.BASE_PATH + infoURI);// 获取请求连接
	conn.header("Referer", ICust.BASE_PATH + refererURI);
	Document groupdoc = conn.get();
	// 可以直接解析比赛数据了
	JSONObject matchObj = parseGameInfoForEnd(match_id, names, groupdoc, infoURI, refererURI);
	if (null != matchObj) {
	    String score_result = matchObj.getString("score_result");
	    if ("end".equals(score_result)) {
		dataService.updateOldMatch(matchObj);
	    }
	}
    }

    private JSONObject parseGameInfoForEnd(String match_id, JSONObject names, Document doc, String infoURI,
	    String refererURI) {
	// #team_fight_table
	JSONObject matchResult = null;
	Elements ddtable = doc.select("#team_fight_table");
	if (!"".equals(ddtable.toString())) {
	    // 开始解析比赛
	    Element table = ddtable.get(0);
	    // System.out.println(names);
	    // System.out.println(table);
	    Elements matchs = table.select("tr.BlackWords");
	    for (Element match : matchs) {
		String matchid = match.attr("matchid");
		if (null == matchid || "".equals(matchid)) {
		    continue;
		}
		if (!matchid.equals(match_id)) {
		    continue;
		}
		try {
		    JSONObject matchObj = new JSONObject();
		    matchObj.put("title", names);
		    matchObj.put("infoURI", infoURI);
		    matchObj.put("refererURI", refererURI);
		    //
		    Elements tds = match.select("td");// size=8
		    matchObj.put("match_id", matchid);
		    matchObj.put("gameTime", tds.get(0).html().trim());
		    matchObj.put("round", tds.get(1).html().trim());
		    matchObj.put("home_team", tds.get(2).html().trim());
		    matchObj.put("away_team", tds.get(4).html().trim());
		    // 比分
		    String bf = tds.get(3).html().trim();
		    if ("延期".equals(bf) || "VS".equals(bf)) {// 比赛未开始或延期
			matchObj.put("score_result", bf);
			matchObj.put("match_site", "");
			matchObj.put("home_score", "");
			matchObj.put("away_score", "");
		    } else {// 已经完赛的
			Element atag = tds.get(3).select("a").get(0);
			String scoreStr = atag.select("strong").get(0).html().trim();
			matchObj.put("score_result", "end");
			matchObj.put("match_site", atag.attr("href"));
			String[] scoreArr = scoreStr.split("-");
			matchObj.put("home_score", scoreArr[0]);
			matchObj.put("away_score", scoreArr[1]);
		    }
		    // 赔率相关
		    // 平均初赔
		    String odds_init_3 = tds.get(5).html().trim();
		    String odds_init_1 = tds.get(6).html().trim();
		    String odds_init_0 = tds.get(7).html().trim();
		    matchObj.put("odds_info_uri", "/soccer/match/" + matchid + "/odds/");
		    if ("-".equals(odds_init_1) || "-".equals(odds_init_0) || "-".equals(odds_init_3)) {
			matchObj.put("odds_info_3", null);
			matchObj.put("odds_info_1", null);
			matchObj.put("odds_info_0", null);
			matchObj.put("odds_info", null);
		    } else {
			matchObj.put("odds_info_3", odds_init_3);
			matchObj.put("odds_info_1", odds_init_1);
			matchObj.put("odds_info_0", odds_init_0);
			matchObj.put("odds_info", parseMatchOddsInfo(matchObj));
		    }
		    //
		    matchResult = matchObj;
		    //
		    logger.info("parseGameInfo end:" + matchObj);
		    break;
		} catch (Exception e) {
		    logger.error("parse match error", e);
		}
	    }
	}
	return matchResult;
    }

    @Override
    public void updateLastUpdateTime() {
	dataService.updateLastUpdateTime();
	
    }
}
