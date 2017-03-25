package com.ddout.fb.service.updata;

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

    public void updateLastUpdateTime();


}
