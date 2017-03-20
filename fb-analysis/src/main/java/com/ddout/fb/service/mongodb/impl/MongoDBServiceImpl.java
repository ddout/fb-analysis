package com.ddout.fb.service.mongodb.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ddout.fb.service.ICust;
import com.ddout.fb.service.mongodb.IMongoDBService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class MongoDBServiceImpl implements IMongoDBService {
    public static final Logger logger = LoggerFactory.getLogger(MongoDBServiceImpl.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Object queryTest() {
	Criteria criatiraTeam = new Criteria();
	Map<String, Object> parm = new HashMap<>();
	parm.put("matchName", "英格兰");
	parm.put("region", "欧洲");
	List<Criteria> list = new ArrayList<>();
	for (Entry<String, Object> en : parm.entrySet()) {
	    list.add(Criteria.where(en.getKey()).is(en.getValue()));
	}
	criatiraTeam.andOperator(list.toArray(new Criteria[list.size()]));
	JSONObject teamObj = mongoTemplate.findOne(new Query(criatiraTeam), JSONObject.class, ICust.COLNAME_COUNTRY);
	Update update = new Update();
	JSONArray ar = new JSONArray();
	ar.add(new JSONObject());
	ar.add(new JSONObject());
	update.set("init_Games_thread_group", ar);
	mongoTemplate.updateMulti(new Query(criatiraTeam), update, ICust.COLNAME_COUNTRY);
	return teamObj;
    }

    @Override
    public void saveObject(JSONObject obj, String collectionName) {
	try {
	    mongoTemplate.save(obj, collectionName);
	    logger.debug(obj + " save to [" + collectionName + "]!");
	} catch (Exception e) {
	    logger.error("db error", e);
	}
    }

    @Override
    public List<JSONObject> queryAllObject(String collectionName) {
	return mongoTemplate.findAll(JSONObject.class, collectionName);
    }

    @Override
    public void saveCountry(JSONObject json) {
	Criteria criatira = new Criteria();
	criatira.andOperator(Criteria.where("region").is(json.getString("region")),
		Criteria.where("matchName").is(json.getString("matchName")));
	JSONObject stoeObj = mongoTemplate.findOne(new Query(criatira), JSONObject.class, ICust.COLNAME_COUNTRY);
	if (null == stoeObj) {
	    saveObject(json, ICust.COLNAME_COUNTRY);
	} else {
	    logger.debug(json + " is exists!");
	}
    }

    @Override
    public void saveSeasonAndTeam(JSONObject season) {
	Criteria criatira = new Criteria();
	criatira.andOperator(Criteria.where("region").is(season.getString("region")),
		Criteria.where("matchName").is(season.getString("matchName")),
		Criteria.where("leagueName").is(season.getString("leagueName")),
		Criteria.where("seasonName").is(season.getString("seasonName")));
	JSONObject stoeObj = mongoTemplate.findOne(new Query(criatira), JSONObject.class, ICust.COLNAME_SEASON);
	if (null == stoeObj) {
	    saveObject(season, ICust.COLNAME_SEASON);
	} else {
	    logger.debug(season + " is exists!");
	}
	// 存球队
	JSONArray teams = season.getJSONArray("teams");
	for (int i = 0; i < teams.size(); i++) {
	    try {
		JSONObject team = teams.getJSONObject(i);
		Criteria criatiraTeam = new Criteria();
		criatiraTeam.andOperator(Criteria.where("region").is(team.getString("region")),
			Criteria.where("matchName").is(team.getString("matchName")),
			Criteria.where("teamName").is(team.getString("teamName")));
		JSONObject teamObj = mongoTemplate.findOne(new Query(criatiraTeam), JSONObject.class,
			ICust.COLNAME_TEAM);
		if (null == teamObj) {
		    saveObject(team, ICust.COLNAME_TEAM);
		} else {
		    logger.debug(team + " is exists!");
		}
	    } catch (Exception e) {
		logger.error("db error", e);
	    }
	}

    }

    @Override
    public void saveMatch(JSONObject matchObj) {

	Criteria criatira = new Criteria();
	criatira.andOperator(Criteria.where("match_id").is(matchObj.getString("match_id")));
	JSONObject stoeObj = mongoTemplate.findOne(new Query(criatira), JSONObject.class, ICust.COLNAME_MATCH);
	if (null == stoeObj) {
	    saveObject(matchObj, ICust.COLNAME_MATCH);
	} else {
	    logger.debug("!!!!is exists=" + matchObj);
	}
    }

    @Override
    public void saveSystemInfo(JSONObject json) {
	saveObject(json, ICust.SYSTEM_INFO);
    }

    @Override
    public JSONObject getSystemInfo() {
	Criteria criatiraTeam = new Criteria();
	criatiraTeam.andOperator(Criteria.where("system_id").is("fb-analysis"));
	//
	JSONObject obj = mongoTemplate.findOne(new Query(criatiraTeam), JSONObject.class, ICust.SYSTEM_INFO);
	return obj;
    }

    @Override
    public JSONObject getOneObj(Map<String, Object> parm, String collectionName) {
	Criteria criatiraTeam = new Criteria();
	List<Criteria> list = new ArrayList<>();
	if (null != parm) {
	    for (Entry<String, Object> en : parm.entrySet()) {
		list.add(Criteria.where(en.getKey()).is(en.getValue()));
	    }
	}
	criatiraTeam.andOperator(list.toArray(new Criteria[list.size()]));
	JSONObject obj = mongoTemplate.findOne(new Query(criatiraTeam), JSONObject.class, collectionName);
	return obj;
    }

    @Override
    public List<JSONObject> getObjsForParm(Map<String, Object> parm, String collectionName) {
	if (null == parm || parm.size() == 0) {
	    throw new RuntimeException("parm is not null");
	}
	Criteria criatiraTeam = new Criteria();
	List<Criteria> list = new ArrayList<>();
	for (Entry<String, Object> en : parm.entrySet()) {
	    list.add(Criteria.where(en.getKey()).is(en.getValue()));
	}
	criatiraTeam.andOperator(list.toArray(new Criteria[list.size()]));
	return mongoTemplate.find(new Query(criatiraTeam), JSONObject.class, collectionName);
    }

    @Override
    public List<JSONObject> getObjsForCriteria(Criteria criatira, String collectionName) {
	if (null == criatira) {
	    throw new RuntimeException("criatira is not null");
	}
	return mongoTemplate.find(new Query(criatira), JSONObject.class, collectionName);
    }

    @Override
    public List<JSONObject> getObjsForCriteria(Criteria criatira, int skip, int limit, String collectionName) {
	if (null == criatira) {
	    throw new RuntimeException("criatira is not null");
	}
	Query query = new Query(criatira);
	query.skip(skip).limit(limit);
	return mongoTemplate.find(query, JSONObject.class, collectionName);
    }

    @Override
    public long getCount(Criteria criatira, String collectionName) {
	if (null == criatira) {
	    throw new RuntimeException("criatira is not null");
	}
	return mongoTemplate.count(new Query(criatira), collectionName);
    }

    @Override
    public void updateObj(Criteria criatiraTeam, Update update, String collectionName) {
	mongoTemplate.updateMulti(new Query(criatiraTeam), update, collectionName);
    }
}
