package com.ddout.fb.service.view.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.cdhy.commons.utils.ParamsUtil;
import com.cdhy.commons.utils.exception.BizException;
import com.ddout.fb.service.ICust;
import com.ddout.fb.service.mongodb.IMongoDBService;
import com.ddout.fb.service.view.IViewService;

import net.sf.json.JSONObject;

@SuppressWarnings("serial")
@Service
public class IViewServiceImpl implements IViewService {
    @Autowired
    private IMongoDBService dbService;

    @Override
    public JSONObject viewServerInfo() {
	JSONObject result = new JSONObject();
	//
	JSONObject sysInfo = dbService.getOneObj(new HashMap<String, Object>() {
	    {
		put("system_id", "fb-analysis");
	    }
	}, ICust.SYSTEM_INFO);
	//
	// 4个集合count
	List<JSONObject> contrys = dbService.queryAllObject(ICust.COLNAME_COUNTRY);

	//
	long seasonCount = dbService.getCount(new Criteria(), ICust.COLNAME_SEASON);
	long teamCount = dbService.getCount(new Criteria(), ICust.COLNAME_TEAM);
	long matchCount = dbService.getCount(new Criteria(), ICust.COLNAME_MATCH);

	//
	Criteria endC = new Criteria();
	endC.andOperator(Criteria.where("score_result").is("end"));
	long match_end_Count = dbService.getCount(endC, ICust.COLNAME_MATCH);
	Criteria endNEC = new Criteria();
	endNEC.andOperator(Criteria.where("score_result").ne("end"));
	long match_ne_end_Count = dbService.getCount(endNEC, ICust.COLNAME_MATCH);
	//
	result.put("sysInfo", sysInfo);
	result.put("contrys", contrys);
	result.put("seasonCount", seasonCount);
	result.put("teamCount", teamCount);
	result.put("matchCount", matchCount);
	//
	result.put("match_end_Count", match_end_Count);
	result.put("match_ne_end_Count", match_ne_end_Count);
	return result;
    }

    @Override
    public List<JSONObject> viewSeasonInfo(Map<String, Object> parm) {
	final String region = ParamsUtil.getString4Map(parm, "region");
	final String matchName = ParamsUtil.getString4Map(parm, "matchName");
	final String leagueName = ParamsUtil.getString4Map(parm, "leagueName");
	if ("".equals(region) || "".equals(matchName) || "".equals(leagueName)) {
	    throw new BizException("params is validata!");
	}
	List<JSONObject> seasons = dbService.getObjsForParm(new HashMap<String, Object>() {
	    {
		put("region", region);
		put("matchName", matchName);
		put("leagueName", leagueName);
	    }
	}, ICust.COLNAME_SEASON);
	return seasons;
    }

}
