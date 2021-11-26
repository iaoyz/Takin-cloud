package io.shulie.takin.cloud.biz.service.scene;

import java.util.List;

import com.pamirs.takin.entity.domain.vo.report.SceneTaskNotifyParam;
import io.shulie.takin.cloud.biz.input.scenemanage.EnginePluginInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskQueryTpsInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskStartCheckInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskStartInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskUpdateTpsInput;
import io.shulie.takin.cloud.biz.output.report.SceneInspectTaskStartOutput;
import io.shulie.takin.cloud.biz.output.report.SceneInspectTaskStopOutput;
import io.shulie.takin.cloud.biz.output.scenetask.*;
import io.shulie.takin.cloud.common.bean.task.TaskResult;

/**
 * @author 莫问
 * @date 2020-04-22
 */
public interface SceneTaskService {

    /**
     * 启动场景测试
     *
     * @param input 入参
     * @return -
     */
    SceneActionOutput start(SceneTaskStartInput input);

    /**
     * 停止场景测试
     *
     * @param sceneId 场景主键
     */
    void stop(Long sceneId);

    /**
     * 检查场景压测启动状态
     *
     * @param sceneId  场景主键
     * @param reportId 报告主键
     * @return -
     */
    SceneActionOutput checkSceneTaskStatus(Long sceneId, Long reportId);

    /**
     * 处理场景任务事件
     *
     * @param taskResult 任务执行结果
     */
    void handleSceneTaskEvent(TaskResult taskResult);

    /**
     * 结束标识，之后并不是pod生命周期结束，而是metric数据传输完毕，将状态回置成压测停止
     *
     * @param param 入参
     * @see io.shulie.takin.cloud.biz.collector.collector.CollectorService
     */

    String taskResultNotify(SceneTaskNotifyParam param);

    /**
     * 开始任务试跑
     *
     * @param input 入参
     * @return -
     */
    SceneTryRunTaskStartOutput startTryRun(SceneManageWrapperInput input, List<EnginePluginInput> enginePlugins);

    /**
     * 调整压测任务的tps
     *
     * @param input 入参
     */
    void updateSceneTaskTps(SceneTaskUpdateTpsInput input);

    /**
     * 查询当前调整压测任务的tps
     *
     * @param input 入参
     * @return -
     */
    SceneTaskQueryTpsOutput queryAdjustTaskTps(SceneTaskQueryTpsInput input);

    /**
     * 启动流量调试，返回报告id
     *
     * @param input 入参
     * @return -
     */
    Long startFlowDebugTask(SceneManageWrapperInput input, List<EnginePluginInput> enginePlugins);

    /**
     * 启动巡检场景
     *
     * @param input 入参
     * @return -
     */
    SceneInspectTaskStartOutput startInspectTask(SceneManageWrapperInput input, List<EnginePluginInput> enginePlugins);

    /**
     * 停止巡检场景
     *
     * @param sceneId 场景主键
     * @return -
     */
    SceneInspectTaskStopOutput stopInspectTask(Long sceneId);

    /**
     * 强制停止任务，不考虑数据的安全性，数据会丢失
     */
    SceneTaskStopOutput forceStopTask(Long reportId, boolean isNeedFinishReport);

    /**
     * 查询流量试跑状态
     *
     * @param sceneId  场景主键
     * @param reportId 报告主键
     * @return -
     */
    SceneTryRunTaskStatusOutput checkTaskStatus(Long sceneId, Long reportId);


    /**
     * 检查巡检任务状态：压测引擎
     *
     * @param sceneId 场景主键
     * @return -
     */
    SceneJobStateOutput checkSceneJobStatus(Long sceneId);

    /**
     * 开始压测前检查文件位点
     * @param input
     * @return
     */
    SceneTaskStartCheckOutput sceneStartCsvPositionCheck(SceneTaskStartCheckInput input);

    /**
     * 清除位点缓存
     * @param sceneId
     */
    void cleanCachedPosition(Long sceneId);
}
