package com.ddout.fb.service.parse.impl;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.ddout.fb.service.parse.ICrawlerService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class CrawlerServiceImpl implements ICrawlerService {
    public static final String BASE_PATH = "http://www.okooo.com";
    /** 国家uri */
    public static final String BASE_PATH_COUNTRY = "/soccer/";

    @Override
    public JSONArray parseCountry() {
	JSONArray countrys = new JSONArray();
	Connection con = Jsoup.connect(BASE_PATH + BASE_PATH_COUNTRY);// 获取请求连接
	con.header("Referer", BASE_PATH);
	try {
	    Document doc = con.get();
	    Element match01 = doc.select("#Match01").get(0);// 欧洲
	    Element match02 = doc.select("#Match02").get(0);// 美洲
	    Element match03 = doc.select("#Match03").get(0);// 亚洲
	    Element match04 = doc.select("#Match04").get(0);// 洲际
	    //
	    parseLeague(countrys, match01, "欧洲");
	    parseLeague(countrys, match02, "美洲");
	    parseLeague(countrys, match03, "亚洲");
	    parseLeague(countrys, match04, "洲际杯赛");
	    //
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
	return countrys;
    }

    /**
     * 通过国家内的内容解析联赛信息
     * 
     * @param countrys
     * @param match01
     * @param region
     */
    private void parseLeague(JSONArray countrys, Element match01, String region) {
	Elements countrys01 = match01.select(".MatchInfoListPic_L");
	for (Element e : countrys01) {
	    //
	    JSONObject json = new JSONObject();
	    JSONArray leagues = new JSONArray();
	    //
	    String matchInfoName = e.getElementsByClass("MatchInfoLogoName").get(0).html().replace("&nbsp;", "").trim();
	    json.put("matchInfoName", matchInfoName);// 国家名称
	    String matchInfoURI = e.select("div.MatchInfoListLogo a").get(0).attr("href");
	    json.put("matchInfoURI", matchInfoURI);
	    json.put("region", region);
	    //
	    Element toolbox = e.getElementsByClass("Toolbox").get(0);
	    Elements leagueDiv = toolbox.select("div.MatchShowOff");
	    for (Element ld : leagueDiv) {
		JSONObject league = new JSONObject();
		String leagueName = ld.html().replace("&nbsp;", "").trim();
		String leagueURI = ld.attr("onclick").replace("window.open('", "").replace("');", "").trim();
		league.put("leagueName", leagueName);
		league.put("leagueURI", leagueURI);
		leagues.add(league);
	    }
	    //
	    json.put("league", leagues);
	    countrys.add(json);
	}
    }

    @Override
    public JSONArray parseSeasonAndTeam(List<JSONObject> countrys) {
	JSONArray seasonArr = new JSONArray();
	//
	for (JSONObject country : countrys) {
	    String region = country.getString("region");// 区域
	    String matchName = country.getString("matchInfoName");// 国家名称
	    JSONArray leagues = country.getJSONArray("league");
	    for (int i = 0; i < leagues.size(); i++) {
		JSONObject league = leagues.getJSONObject(i);
		String leagueName = league.getString("leagueName");// 联赛名称
		String baseURI = league.getString("leagueURI");// 联赛基准uri
		//
		Connection con = Jsoup.connect(BASE_PATH + baseURI);// 获取请求连接
		con.header("Referer", BASE_PATH + BASE_PATH_COUNTRY);
		try {
		    Document doc = con.get();
		    // 赛季
		    Elements seasonHtml = doc.select("div.LotteryList_Data").get(3).select("a.BlueWord_TxtL");
		    // System.out.println(seasonHtml);
		    for (Element ses : seasonHtml) {
			String href = ses.attr("href");
			String seasonName = ses.html().trim();
			JSONObject season = new JSONObject();
			season.put("region", region);
			season.put("match", matchName);
			season.put("league", leagueName);
			season.put("uri", href);
			season.put("seasonName", seasonName);
			JSONArray teams = parseSeason4Teams(season);
			season.put("teams", teams);
			seasonArr.add(season);
			System.out.println(season);
		    }

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	return seasonArr;
    }

    private JSONArray parseSeason4Teams(JSONObject season) {
	JSONArray teams = new JSONArray();
	String seasonURI = season.getString("uri");
	Connection con = Jsoup.connect(BASE_PATH + seasonURI);// 获取请求连接
	con.header("Referer", BASE_PATH + BASE_PATH_COUNTRY);
	try {
	    Document doc = con.get();
	    // 球队
	    Elements teamHtml = doc.select("div.LotteryList_Data").get(4).select("a.BlueWord_TxtL");
	    // System.out.println(teamHtml);
	    for (Element tm : teamHtml) {
		String href = tm.attr("href");
		String teamName = tm.html().trim();
		JSONObject team = new JSONObject();
		team.put("uri", href);
		team.put("teamName", teamName);
		teams.add(team);
	    }
	    //
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return teams;
    }

}
