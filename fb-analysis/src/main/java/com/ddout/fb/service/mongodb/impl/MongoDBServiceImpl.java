package com.ddout.fb.service.mongodb.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ddout.fb.service.analysis.ICrawlerService;
import com.ddout.fb.service.mongodb.IMongoDBService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class MongoDBServiceImpl implements IMongoDBService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ICrawlerService ce;

    @Override
    public void test() {
	// TODO Auto-generated method stub
	JSONArray ar = ce.parseCountry();
//	JSONObject json = new JSONObject();
//	json.put("test2", "test2");
	for (int i = 0; i < ar.size(); i++) {
	    JSONObject json = ar.getJSONObject(i);
	    mongoTemplate.save(json, "analysis");
	}
	System.out.println("ss");
    }

}
