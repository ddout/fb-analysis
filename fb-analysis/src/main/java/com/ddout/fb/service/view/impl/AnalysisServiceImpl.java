package com.ddout.fb.service.view.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.commons.utils.ParamsUtil;
import com.cdhy.commons.utils.exception.BizException;
import com.ddout.fb.dao.fb.IAnalysisMapper;
import com.ddout.fb.service.view.IAnalysisService;

@Service
public class AnalysisServiceImpl implements IAnalysisService {
    private static final Logger log = Logger.getLogger(AnalysisServiceImpl.class);
    @Autowired
    private IAnalysisMapper mapper;

    @Override
    public Map<String, Object> execAnalysis(String matchId) {
	// TODO 这里应该是把其他的分析结果综合起来，按照一定的权重，综合得出分析结果(可以按照百分比呈现)
	// 目前想到6种
	Map<String, Object> result = new HashMap<String, Object>();
	// result.put("analysis", execAnalysis(matchId));// 最终分析结果--包含实际结果的对比
	// result.put("his_battle", execHisBattle(matchId));// 历史交锋分析结果
	// result.put("home_state", execHomeState(matchId));// 最近比赛状态分析(主)
	// result.put("away_state", execAwayState(matchId));// 最近比赛状态分析(客)
	// result.put("idx_analysis", execIdxAnalysis(matchId));// 指数分析结果
	BigDecimal[] kiliy = Killy(matchId);
	int idx_analysis = killyResult(kiliy);
	log.info("凯利方差结果：" + idx_analysis);
	result.put("idx_analysis", idx_analysis);// 指数分析结果
	return result;
    }

    @Override
    public Object execHisBattle(String matchId) {
	// TODO 历史交锋分析结果
	// 1.分主客场???
	return null;
    }

    @Override
    public Object execHomeState(String matchId) {
	// TODO 最近比赛状态分析(主)
	return null;
    }

    @Override
    public Object execAwayState(String matchId) {
	// TODO 最近比赛状态分析(客)
	return null;
    }

    @Override
    public Object execIdxAnalysis(String matchId) {
	// TODO 指数分析结果
	// 获取凯利方差离散值
	BigDecimal[] kiliy = Killy(matchId);
	log.info("凯利方差结果：" + killyResult(kiliy));
	return kiliy;
    }

    @Override
    public Object execSameLeagueHome(String matchId) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Object execSameLeagueAway(String matchId) {
	// TODO Auto-generated method stub
	return null;
    }

    public static final List<String> makerIdArr = Arrays.asList(new String[] { "24", "99" });

    /**
     * 凯利方差
     * 
     * @param matchId
     */
    private BigDecimal[] Killy(String matchId) {

	List<Map<String, Object>> oddsList = mapper.queryOddsList(matchId);
	if (null == oddsList || oddsList.size() == 0) {
	    throw new BizException("[" + matchId + "]no odds list");
	}
	long b = System.currentTimeMillis();

	// 平均概率
	BigDecimal APH = new BigDecimal(0.0);
	BigDecimal APD = new BigDecimal(0.0);
	BigDecimal APA = new BigDecimal(0.0);
	//
	BigDecimal count = new BigDecimal(0);
	for (Map<String, Object> map : oddsList) {
	    String makerId = ParamsUtil.getString4Map(map, "makerID");
	    if (makerIdArr.contains(makerId)) {
		continue;
	    }
	    APH = APH.add(new BigDecimal(ParamsUtil.getFloat4Map(map, "radio_home")));
	    APD = APD.add(new BigDecimal(ParamsUtil.getFloat4Map(map, "radio_draw")));
	    APA = APA.add(new BigDecimal(ParamsUtil.getFloat4Map(map, "radio_away")));
	    count = count.add(new BigDecimal(1));
	}
	APH = APH.divide(count, 10, RoundingMode.HALF_UP).divide(new BigDecimal(100));
	APD = APD.divide(count, 10, RoundingMode.HALF_UP).divide(new BigDecimal(100));
	APA = APA.divide(count, 10, RoundingMode.HALF_UP).divide(new BigDecimal(100));
	log.info("平均概率：" + APH + "=" + APD + "=" + APA);
	// 平均凯利值
	BigDecimal AEH = new BigDecimal(0.0);
	BigDecimal AED = new BigDecimal(0.0);
	BigDecimal AEA = new BigDecimal(0.0);
	for (Map<String, Object> map : oddsList) {
	    String makerId = ParamsUtil.getString4Map(map, "makerID");
	    if (makerIdArr.contains(makerId)) {
		continue;
	    }
	    // 凯利值
	    BigDecimal EH = APH.multiply(new BigDecimal(ParamsUtil.getFloat4Map(map, "end_home")));
	    BigDecimal ED = APD.multiply(new BigDecimal(ParamsUtil.getFloat4Map(map, "end_draw")));
	    BigDecimal EA = APA.multiply(new BigDecimal(ParamsUtil.getFloat4Map(map, "end_away")));
	    // 累加凯利值
	    AEH = AEH.add(EH);
	    AED = AED.add(ED);
	    AEA = AEA.add(EA);
	}
	// 计算平均凯利值
	AEH = AEH.divide(count, 10, RoundingMode.HALF_UP);
	AED = AED.divide(count, 10, RoundingMode.HALF_UP);
	AEA = AEA.divide(count, 10, RoundingMode.HALF_UP);
	log.info("平均凯利：" + AEH + "=" + AED + "=" + AEA);
	// 凯利方差的离散值
	BigDecimal ADH = new BigDecimal(0.0);
	BigDecimal ADD = new BigDecimal(0.0);
	BigDecimal ADA = new BigDecimal(0.0);
	for (Map<String, Object> map : oddsList) {
	    String makerId = ParamsUtil.getString4Map(map, "makerID");
	    if (makerIdArr.contains(makerId)) {
		continue;
	    }
	    // 凯利值
	    BigDecimal EH = APH.multiply(new BigDecimal(ParamsUtil.getFloat4Map(map, "end_home")));
	    BigDecimal ED = APD.multiply(new BigDecimal(ParamsUtil.getFloat4Map(map, "end_draw")));
	    BigDecimal EA = APA.multiply(new BigDecimal(ParamsUtil.getFloat4Map(map, "end_away")));
	    // 凯利方差
	    BigDecimal DH = AEH.subtract(EH).abs().pow(2);
	    BigDecimal DD = AED.subtract(ED).abs().pow(2);
	    BigDecimal DA = AEA.subtract(EA).abs().pow(2);
	    // 累加凯利方差
	    ADH = ADH.add(DH);
	    ADD = ADD.add(DD);
	    ADA = ADA.add(DA);
	}
	// 计算凯利方差的离散值
	ADH = ADH.divide(count, 10, RoundingMode.HALF_UP);
	ADD = ADD.divide(count, 10, RoundingMode.HALF_UP);
	ADA = ADA.divide(count, 10, RoundingMode.HALF_UP);
	log.info("凯利方差的离散值：" + ADH + "=" + ADD + "=" + ADA);
	long e = System.currentTimeMillis();
	log.info("耗时：" + (e - b) + "ms");
	return new BigDecimal[] { ADH, ADD, ADA };
    }

    @Override
    public void analysisMatch() {
	int count = mapper.getEndMatchCount();
	final int limit = 100;
	for (int i = 0; i < count; i += limit) {
	    final int start = i;
	    List<Map<String, Object>> matchs = mapper.queryEndMatch(new HashMap<String, Object>() {
		{
		    put("start", start);
		    put("end", limit);
		}
	    });
	    for (Map<String, Object> match : matchs) {
		final String matchResult = ParamsUtil.getString4Map(match, "match_result");
		try {
		    final BigDecimal[] kiliy = Killy(ParamsUtil.getString4Map(match, "id"));
		    final int killyResult = killyResult(kiliy);
		    final String matchId = ParamsUtil.getString4Map(match, "id");
		    mapper.saveAnalysisView1(new HashMap<String, Object>() {
			private static final long serialVersionUID = -737490257722803154L;

			{
			    put("matchId", matchId);
			    put("killyResult", killyResult);
			    put("matchResult", matchResult);
			    //
			    put("killy_home", kiliy[0]);
			    put("killy_draw", kiliy[1]);
			    put("killy_away", kiliy[2]);
			}
		    });
		} catch (BizException e) {
		    log.debug(e.getMessage());
		} catch (Exception e) {
		    log.error(e);
		}

	    }
	}
    }

    private int killyResult(BigDecimal[] kiliy) {
	double home = -kiliy[0].doubleValue();
	double draw = -kiliy[1].doubleValue();
	double away = -kiliy[2].doubleValue();
	if (home > draw && home > away) {
	    return 3;
	}
	if (draw > home && draw > away) {
	    return 1;
	}
	if (away > home && away > draw) {
	    return 0;
	}
	return -1;
    }
}
