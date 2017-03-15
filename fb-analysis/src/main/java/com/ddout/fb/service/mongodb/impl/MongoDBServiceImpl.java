package com.ddout.fb.service.mongodb.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ddout.fb.service.mongodb.IMongoDBService;

import net.sf.json.JSONObject;

@Service
public class MongoDBServiceImpl implements IMongoDBService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveObject(JSONObject obj, String collectionName) {
	mongoTemplate.save(obj, collectionName);
    }

    @Override
    public List<JSONObject> queryAllObject(String collectionName) {

	return mongoTemplate.findAll(JSONObject.class, collectionName);
    }

}
