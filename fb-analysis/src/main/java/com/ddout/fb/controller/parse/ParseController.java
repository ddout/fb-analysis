package com.ddout.fb.controller.parse;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdhy.commons.utils.ParamsUtil;
import com.cdhy.commons.utils.exception.BizException;
import com.cdhy.commons.utils.framework.MD5;
import com.cdhy.commons.utils.model.Result;
import com.ddout.fb.service.view.IViewService;
import com.ddout.fb.utils.ContextHolderUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/view")
public class ParseController {

    private static final Logger log = Logger.getLogger(ParseController.class);

    private static final String USER = "ddout";
    private static final String PWD = "64535f299360084520d11ea537437539";
    public static final String SESSION_USER = "session_user";

    @Autowired
    private IViewService service;

    private boolean authUser(String user, String pwd) {
	String pwds = MD5.MD5Encode(user + "_" + pwd);
	if (USER.equals(user) && PWD.equals(pwds)) {
	    return true;
	}
	return false;
    }

    private boolean checkAuthUser() {
	Object user = ContextHolderUtils.getSession().getAttribute(SESSION_USER);
	if (null != user && !"".equals(user)) {
	    return true;
	}
	return false;
	// return true;
    }

    @RequestMapping("/auth")
    @ResponseBody
    public Object auth(@RequestParam Map<String, Object> parm) {
	Result obj = new Result();
	try {
	    String user = ParamsUtil.getString4Map(parm, "user");
	    if (true == authUser(user, ParamsUtil.getString4Map(parm, "pwd"))) {
		ContextHolderUtils.getSession().setAttribute(SESSION_USER, user);
	    } else {
		throw new BizException("auth not valid");
	    }
	} catch (BizException e) {
	    log.debug("获取失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	} catch (Exception e) {
	    log.error("获取异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	}
	return obj;
    }

    @RequestMapping("/viewServerInfo")
    @ResponseBody
    public Object viewServerInfo(@RequestParam Map<String, Object> parm) {
	Result obj = new Result();
	try {
	    if (true == checkAuthUser()) {
		JSONObject serverInfo = service.viewServerInfo();
		obj.setRows(serverInfo);
	    } else {
		throw new BizException("auth not valid");
	    }
	} catch (BizException e) {
	    log.debug("获取失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	} catch (Exception e) {
	    log.error("获取异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	}
	return obj;
    }
    
    @RequestMapping("/viewSeasonInfo")
    @ResponseBody
    public Object viewSeasonInfo(@RequestParam Map<String, Object> parm) {
	Result obj = new Result();
	try {
	    if (true == checkAuthUser()) {
		List<JSONObject> serverInfo = service.viewSeasonInfo(parm);
		obj.setRows(serverInfo);
	    } else {
		throw new BizException("auth not valid");
	    }
	} catch (BizException e) {
	    log.debug("获取失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	} catch (Exception e) {
	    log.error("获取异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	}
	return obj;
    }
}
