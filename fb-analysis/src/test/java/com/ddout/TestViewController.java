package com.ddout;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:conf/spring*.xml" })
public class TestViewController {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
	this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testAuth() throws Exception {
	//
	ResultActions ra = mockMvc.perform((post("/view/auth.do").characterEncoding("UTF-8")
		.contentType(MediaType.APPLICATION_JSON).param("user", "ddout").param("pwd", "xxxx")))
		.andExpect(status().isOk()).andDo(print());
	JSONObject result = JSONObject.fromObject(ra.andReturn().getResponse().getContentAsString());
	System.out.println(result);
    }

    @Test
    public void testViewServerInfo() throws Exception {
	//
	ResultActions ra = mockMvc.perform(
		(post("/view/viewServerInfo.do").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON)))
		.andExpect(status().isOk()).andDo(print());
	JSONObject result = JSONObject.fromObject(ra.andReturn().getResponse().getContentAsString());
	System.out.println(result);
    }

    @Test
    public void testViewSeasonInfo() throws Exception {
	//
	ResultActions ra = mockMvc.perform(
		(post("/view/viewSeasonInfo.do").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON)
			.param("region", "欧洲").param("matchName", "英格兰").param("leagueName", "英超")))
		.andExpect(status().isOk()).andDo(print());
	JSONObject result = JSONObject.fromObject(ra.andReturn().getResponse().getContentAsString());
	System.out.println(result);
    }
}
