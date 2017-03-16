package com.ddout.fb.controller.parse;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cdhy.commons.utils.ParamsUtil;
import com.cdhy.commons.utils.SysConfigUtil;
import com.ddout.fb.service.parse.ICrawlerService;

@Controller
@RequestMapping("/parser")
public class ParseController {

    private static final Logger log = Logger.getLogger(ParseController.class);

    private static final String USER = SysConfigUtil.getInstance().getProperites("user");
    private static final String PWD = SysConfigUtil.getInstance().getProperites("pwd");

    @Autowired
    private ICrawlerService service;

    private boolean auth(Map<String, Object> parm) {
	String user = ParamsUtil.getString4Map(parm, "user");
	String pwd = ParamsUtil.getString4Map(parm, "pwd");
	if (USER.equals(user) && PWD.equals(pwd)) {
	    return true;
	}
	return false;
    }

}
