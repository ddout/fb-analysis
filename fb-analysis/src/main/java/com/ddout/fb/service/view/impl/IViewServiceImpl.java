package com.ddout.fb.service.view.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.commons.utils.ParamsUtil;
import com.cdhy.commons.utils.exception.BizException;
import com.ddout.fb.dao.fb.ISystemInfoMapper;
import com.ddout.fb.dao.fb.IViewMapper;
import com.ddout.fb.service.view.IAnalysisService;
import com.ddout.fb.service.view.IViewService;

import net.sf.json.JSONObject;

@Service
public class IViewServiceImpl implements IViewService {
    @Autowired
    private ISystemInfoMapper systemInfoMapper;
    @Autowired
    private IViewMapper viewMapper;
    @Autowired
    private IAnalysisService analysisService;

    @Override
    public JSONObject viewServerInfo() {
	JSONObject result = new JSONObject();
	//
	JSONObject sysInfo = JSONObject.fromObject(systemInfoMapper.getSystemInfo());
	//
	Map<String, Object> sysView = viewMapper.getSystemView();
	//
	List<Map<String, Object>> zucai14s = viewMapper.getZucai14s();
	result.put("sysInfo", sysInfo);
	result.put("sysView", sysView);
	result.put("zucai14s", zucai14s);
	return result;
    }

    @Override
    public List<Map<String, Object>> queryMatchSreach(Map<String, Object> parm) {
	String sreachkey = ParamsUtil.getString4Map(parm, "sreachkey");
	if ("".equals(sreachkey)) {
	    throw new BizException("sreachKey is not null");
	}
	String sreachType = ParamsUtil.getString4Map(parm, "sreachType");
	if ("".equals(sreachType)) {
	    sreachType = "1";
	}
	final String sreachTypeStr = sreachType;
	final String[] keys = sreachkey.split(" ");
	return viewMapper.queryMatchSreach(new HashMap<String, Object>() {
	    private static final long serialVersionUID = 931377222549563303L;

	    {
		put("sreachType", sreachTypeStr);
		put("key1", keys[0]);
		if (keys.length > 1) {
		    put("key2", keys[1]);
		}
	    }
	});
    }

    @Override
    public Map<String, Object> queryMatchInfoView(Map<String, Object> parm) {
	String matchId = ParamsUtil.getString4Map(parm, "matchId");
	if ("".equals(matchId)) {
	    throw new BizException("matchId is not null");
	}
	final Map<String, Object> matchInfo = viewMapper.getMatchInfo(matchId);
	final List<Map<String, Object>> oddsInfo = viewMapper.getOddsInfo(matchId);
	return new HashMap<String, Object>() {
	    private static final long serialVersionUID = 3969222085933846348L;

	    {
		put("matchObj", matchInfo);
		put("oddsObj", oddsInfo);
	    }
	};
    }

    @Override
    public Map<String, Object> queryViewAnalysis(Map<String, Object> parm) {
	String matchId = ParamsUtil.getString4Map(parm, "matchId");
	if ("".equals(matchId)) {
	    throw new BizException("matchId is not null");
	}
	return analysisService.execAnalysis(matchId);
    }

    @Override
    public Map<String, Object> queryViewZucai14(Map<String, Object> parm) {
	// zucai_no:'',
	// endTime:'',
	// matchs:[match_analysis_result,match_result]
	String no = ParamsUtil.getString4Map(parm, "no");
	if ("".equals(no)) {
	    throw new BizException("no is not null");
	}

	List<Map<String, Object>> zucai14List = viewMapper.queryZucai14ForNo(no);
	String endTime = "";
	if (zucai14List.size() > 0) {
	    for (Map<String, Object> map : zucai14List) {
		endTime = ParamsUtil.getString4Map(map, "end_time");
		try {
		    Map<String, Object> analysis = analysisService
			    .execAnalysis(ParamsUtil.getString4Map(map, "match_id"));
		    map.put("analysis", analysis);
		} catch (Exception e) {
		}
	    }
	}
	Map<String, Object> result = new HashMap<String, Object>();
	result.put("zucai_no", no);
	result.put("endTime", endTime);
	result.put("matchs", zucai14List);
	return result;
    }

}
