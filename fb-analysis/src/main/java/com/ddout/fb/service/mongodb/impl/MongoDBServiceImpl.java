package com.ddout.fb.service.mongodb.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ddout.fb.model.TestModel;
import com.ddout.fb.service.mongodb.IMongoDBService;

@Service
public class MongoDBServiceImpl implements IMongoDBService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void test() {
	// TODO Auto-generated method stub
	TestModel model = new TestModel("test", "test1");
	mongoTemplate.save(model, "analysis");
	System.out.println("ss");
    }

}
