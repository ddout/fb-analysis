package com.ddout.fb.service.updata;

import java.io.IOException;

import net.sf.json.JSONObject;

public interface IUpDataService {
    /**
     * 新增加的赛季
     */
    public void updateNewSeason();

    /**
     * 新增加的比赛
     */
    public void updateNewMatch();

    /**
     * 更新已有的比赛数据
     */
    public void updateOldMatch();

    /**
     * 解析已有的比赛数据
     */
    public void parseOldMatch(JSONObject match) throws IOException;

}
