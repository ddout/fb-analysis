package com.ddout.fb.service.mongodb.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ddout.fb.service.mongodb.IMongoDBService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class MongoDBServiceImpl implements IMongoDBService {
    public static final Logger logger = LoggerFactory.getLogger(MongoDBServiceImpl.class);
    @Autowired
    private MongoTemplate mongoTemplate;

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
	JSONObject stoeObj = mongoTemplate.findOne(new Query(criatira), JSONObject.class,
		IMongoDBService.COLNAME_COUNTRY);
	if (null == stoeObj) {
	    saveObject(json, IMongoDBService.COLNAME_COUNTRY);
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
	JSONObject stoeObj = mongoTemplate.findOne(new Query(criatira), JSONObject.class,
		IMongoDBService.COLNAME_SEASON);
	if (null == stoeObj) {
	    saveObject(season, IMongoDBService.COLNAME_SEASON);
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
			IMongoDBService.COLNAME_TEAM);
		if (null == teamObj) {
		    saveObject(team, IMongoDBService.COLNAME_TEAM);
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
	JSONObject stoeObj = mongoTemplate.findOne(new Query(criatira), JSONObject.class,
		IMongoDBService.COLNAME_MATCH);
	if (null == stoeObj) {
	    saveObject(matchObj, IMongoDBService.COLNAME_MATCH);
	} else {
	    logger.debug("!!!!is exists=" + matchObj);
	}
    }

    @Override
    public Object queryTest() {
	Criteria criatiraTeam = new Criteria();
	criatiraTeam.andOperator(Criteria.where("round").is("1"), Criteria.where("home_team").is("伯恩利"),
		Criteria.where("away_team").is("斯旺西"), Criteria.where("title.leagueName").is("欧冠12"));
	//
	JSONObject teamObj = mongoTemplate.findOne(new Query(criatiraTeam), JSONObject.class,
		IMongoDBService.COLNAME_MATCH);
	return teamObj;
    }

}
