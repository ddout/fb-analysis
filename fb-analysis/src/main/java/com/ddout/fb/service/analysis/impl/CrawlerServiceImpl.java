package com.ddout.fb.service.analysis.impl;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.ddout.fb.service.analysis.ICrawlerService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class CrawlerServiceImpl implements ICrawlerService {
    public static final String BASE_PATH = "http://www.okooo.com/";

    @Override
    public JSONArray parseCountry() {
	JSONArray countrys = new JSONArray();
	Connection con = Jsoup.connect(BASE_PATH + "soccer/");// 获取请求连接
	con.header("Referer", "http://www.okooo.com/");
	try {
	    Document doc = con.get();
	    Element match01 = doc.select("#Match01").get(0);// 欧洲
	    Element match02 = doc.select("#Match02").get(0);// 美洲
	    Element match03 = doc.select("#Match03").get(0);// 亚洲
	    Element match04 = doc.select("#Match04").get(0);// 洲际
	    //
	    parseItem(countrys, match01, "欧洲");
	    parseItem(countrys, match02, "美洲");
	    parseItem(countrys, match03, "亚洲");
	    parseItem(countrys, match04, "洲际杯赛");
	    //
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
	return countrys;
    }

    private void parseItem(JSONArray countrys, Element match01, String region) {
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

}
