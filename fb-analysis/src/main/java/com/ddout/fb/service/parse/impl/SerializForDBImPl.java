package com.ddout.fb.service.parse.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddout.fb.service.mongodb.IMongoDBService;
import com.ddout.fb.service.parse.ICrawlerService;
import com.ddout.fb.service.parse.ISerializeForDB;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SerializForDBImPl implements ISerializeForDB {

    @Autowired
    private ICrawlerService crawlerService;
    @Autowired
    private IMongoDBService mongoDBService;

    @Override
    public void saveCountry() {
	JSONArray countrys = crawlerService.parseCountry();
	for (int i = 0; i < countrys.size(); i++) {
	    JSONObject country = countrys.getJSONObject(i);
	    mongoDBService.saveObject(country, IMongoDBService.COLNAME_COUNTRY);
	}
    }

    @Override
    public List<JSONObject> queryCountry() {
	return mongoDBService.queryAllObject(IMongoDBService.COLNAME_COUNTRY);
    }

}
