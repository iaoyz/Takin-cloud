package io.shulie.takin.cloud.biz.service.engine;

/**
 * @Author: liyuanba
 * @Date: 2021/11/26 11:17 上午
 */
public interface EngineService {
    /**
     * 删除引擎jog
     */
    public boolean deleteJob(String jobName, String engineInstanceRedisKey);
}
