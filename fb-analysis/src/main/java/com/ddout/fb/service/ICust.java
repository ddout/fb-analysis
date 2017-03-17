package com.ddout.fb.service;

public interface ICust {
    /*
     * system--confg
     */
    public static final String SYSTEM_CONFIG_ID = "system_id";
    public static final String SYSTEM_CONFIG_NAME = "fb-analysis";
    
    
    /*
     * website--confg
     */
    public static final String BASE_PATH = "http://www.okooo.com";
    /** 国家uri */
    public static final String BASE_PATH_COUNTRY = "/soccer/";

    /*
     * mongodb--confg
     */

    /** 系统--配置 */
    public static final String SYSTEM_INFO = "system_info";
    /** 国家/联赛 */
    public static final String COLNAME_COUNTRY = "country_league";
    /** 赛季 */
    public static final String COLNAME_SEASON = "country_season";
    /** 球队 */
    public static final String COLNAME_TEAM = "country_team";
    /** 比赛 */
    public static final String COLNAME_MATCH = "country_match";
}
