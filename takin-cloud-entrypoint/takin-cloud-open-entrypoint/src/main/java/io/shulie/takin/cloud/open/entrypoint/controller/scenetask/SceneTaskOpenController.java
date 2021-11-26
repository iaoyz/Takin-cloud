package io.shulie.takin.cloud.open.entrypoint.controller.scenetask;

import java.util.List;
import java.util.stream.Collectors;

import io.shulie.takin.cloud.biz.input.report.UpdateReportSlaDataInput;
import io.shulie.takin.cloud.biz.input.scenemanage.EnginePluginInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskQueryTpsInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskStartCheckInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskStartInput;
import io.shulie.takin.cloud.biz.input.scenemanage.SceneTaskUpdateTpsInput;
import io.shulie.takin.cloud.biz.output.report.SceneInspectTaskStartOutput;
import io.shulie.takin.cloud.biz.output.report.SceneInspectTaskStopOutput;
import io.shulie.takin.cloud.biz.output.scenetask.*;
import io.shulie.takin.cloud.biz.service.report.ReportService;
import io.shulie.takin.cloud.biz.service.scene.SceneTaskService;
import io.shulie.takin.cloud.common.constants.APIUrls;
import io.shulie.takin.cloud.open.entrypoint.convert.SceneTaskOpenConverter;
import io.shulie.takin.cloud.open.req.engine.EnginePluginsRefOpen;
import io.shulie.takin.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.takin.cloud.open.req.scenemanage.SceneTaskStartReq;
import io.shulie.takin.cloud.open.req.scenetask.*;
import io.shulie.takin.cloud.open.resp.scenemanage.SceneInspectTaskStartResp;
import io.shulie.takin.cloud.open.resp.scenemanage.SceneInspectTaskStopResp;
import io.shulie.takin.cloud.open.resp.scenemanage.SceneTryRunTaskStartResp;
import io.shulie.takin.cloud.open.resp.scenemanage.SceneTryRunTaskStatusResp;
import io.shulie.takin.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.takin.cloud.open.resp.scenetask.SceneJobStateResp;
import io.shulie.takin.cloud.open.resp.scenetask.SceneTaskAdjustTpsResp;
import io.shulie.takin.cloud.open.resp.scenetask.SceneTaskStopResp;
import io.shulie.takin.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL + "scene/task/")
@Api(tags = "场景任务OPEN")
@Slf4j
public class SceneTaskOpenController {

    @Autowired
    private SceneTaskService sceneTaskService;
    @Autowired
    private ReportService reportService;

    @PostMapping("/start")
    @ApiOperation(value = "开始场景测试")
    public ResponseResult<SceneActionResp> start(@RequestBody SceneTaskStartReq request) {

        SceneTaskStartInput input = new SceneTaskStartInput();
        BeanUtils.copyProperties(request, input);
        SceneActionOutput output = sceneTaskService.start(input);
        SceneActionResp resp = new SceneActionResp();
        BeanUtils.copyProperties(output, resp);
        return ResponseResult.success(resp);
    }


    @PostMapping("/startFlowDebugTask")
    @ApiOperation(value = "启动调试流量任务")
    ResponseResult<Long> startFlowDebugTask(@RequestBody TaskFlowDebugStartReq taskFlowDebugStartReq){
        SceneManageWrapperInput input = SceneTaskOpenConverter.INSTANCE.ofTaskDebugDataStartReq(taskFlowDebugStartReq);
        //压测引擎插件需要传入插件id和插件版本 modified by xr.l 20210712
        List<EnginePluginInput> enginePluginInputs = null;
        if (CollectionUtils.isNotEmpty(taskFlowDebugStartReq.getEnginePlugins())) {
            List<EnginePluginsRefOpen> enginePlugins = taskFlowDebugStartReq.getEnginePlugins();
            enginePluginInputs = enginePlugins.stream().map(plugin -> new EnginePluginInput() {{
                setPluginId(plugin.getPluginId());
                setPluginVersion(plugin.getVersion());
            }}).collect(Collectors.toList());
        }
        Long reportId = sceneTaskService.startFlowDebugTask(input, enginePluginInputs);
        return ResponseResult.success(reportId);
    }

    @PostMapping("/startInspectTask")
    @ApiOperation(value = "启动巡检任务")
    ResponseResult<SceneInspectTaskStartResp> startInspectTask(@RequestBody TaskInspectStartReq taskFlowDebugStartReq){
        SceneManageWrapperInput input = SceneTaskOpenConverter.INSTANCE.ofTaskInspectStartReq(taskFlowDebugStartReq);

        //压测引擎插件需要传入插件id和插件版本 modified by xr.l 20210712
        List<EnginePluginInput> enginePluginInputs = null;
        if (CollectionUtils.isNotEmpty(taskFlowDebugStartReq.getEnginePlugins())) {
            List<EnginePluginsRefOpen> enginePlugins = taskFlowDebugStartReq.getEnginePlugins();
            enginePluginInputs = enginePlugins.stream().map(plugin -> new EnginePluginInput() {{
                setPluginId(plugin.getPluginId());
                setPluginVersion(plugin.getVersion());
            }}).collect(Collectors.toList());
        }
        SceneInspectTaskStartOutput output = sceneTaskService.startInspectTask(input, enginePluginInputs);
        SceneInspectTaskStartResp startResp = new SceneInspectTaskStartResp();
        BeanUtils.copyProperties(output,startResp);
        return ResponseResult.success(startResp);
    }

    @PostMapping("/forceStopTask")
    @ApiOperation(value = "强制停止任务，提示：可能会造成压测数据丢失")
    ResponseResult<SceneTaskStopResp> forceStopTask(@RequestBody TaskStopReq req) {
        SceneTaskStopOutput output = sceneTaskService.forceStopTask(req.getReportId(), req.isFinishReport());
        SceneTaskStopResp stopResp = new SceneTaskStopResp();
        BeanUtils.copyProperties(output, stopResp);
        return ResponseResult.success(stopResp);
    }

    @PostMapping("/stopInspectTask")
    @ApiOperation(value = "停止巡检任务")
    ResponseResult<SceneInspectTaskStopResp> stopInspectTask(@RequestBody TaskInspectStopReq taskFlowDebugStopReq){
        SceneInspectTaskStopOutput output = sceneTaskService.stopInspectTask(taskFlowDebugStopReq.getSceneId());
        SceneInspectTaskStopResp stopResp = new SceneInspectTaskStopResp();
        BeanUtils.copyProperties(output,stopResp);
        return ResponseResult.success(stopResp);
    }

    @PostMapping("/updateSceneTaskTps")
    @ApiOperation(value = "调整压测任务tps")
    ResponseResult<String> updateSceneTaskTps(@RequestBody SceneTaskUpdateTpsReq sceneTaskUpdateTpsReq){

        SceneTaskUpdateTpsInput input = new SceneTaskUpdateTpsInput();
        input.setSceneId(sceneTaskUpdateTpsReq.getSceneId());
        input.setReportId(sceneTaskUpdateTpsReq.getReportId());
        input.setTpsNum(sceneTaskUpdateTpsReq.getTpsNum());
        sceneTaskService.updateSceneTaskTps(input);
        return ResponseResult.success("tps更新成功");
    }

    @GetMapping("/queryAdjustTaskTps")
    @ApiOperation(value = "获取调整的任务tps")
    ResponseResult<SceneTaskAdjustTpsResp> queryAdjustTaskTps(@RequestParam Long sceneId,@RequestParam Long reportId){

        SceneTaskQueryTpsInput input = new SceneTaskQueryTpsInput();
        input.setSceneId(sceneId);
        input.setReportId(reportId);
        SceneTaskQueryTpsOutput sceneTaskQueryTpsOutput = sceneTaskService.queryAdjustTaskTps(input);
        if (sceneTaskQueryTpsOutput != null){
            SceneTaskAdjustTpsResp sceneTaskAdjustTpsResp = new SceneTaskAdjustTpsResp();
            sceneTaskAdjustTpsResp.setTotalTps(sceneTaskQueryTpsOutput.getTotalTps());
            return ResponseResult.success(sceneTaskAdjustTpsResp);
        }
        return ResponseResult.success();
    }

    @PostMapping("/stop")
    @ApiOperation(value = "结束场景测试")
    public ResponseResult<String> stop(@RequestBody SceneManageIdReq req) {
        //记录下sla的数据
        if(req.getReportId() != null) {
            UpdateReportSlaDataInput slaDataInput = new UpdateReportSlaDataInput();
            slaDataInput.setReportId(req.getReportId());
            slaDataInput.setSlaBean(req.getSlaBean());
            reportService.updateReportSlaData(slaDataInput);
        }
        log.info("任务{}-{} ，原因：web 调 cloud 触发停止", req.getId(),req.getReportId());
        // 与sla操作是一致的
        sceneTaskService.stop(req.getId());
        return ResponseResult.success("停止场景成功");
    }



    @GetMapping("/checkStartStatus")
    @ApiOperation(value = "检查启动状态")
    public ResponseResult<SceneActionResp> checkStartStatus(@RequestParam("id") Long id,
        @RequestParam(value = "reportId",required = false) Long reportId) {
            SceneActionOutput sceneAction = sceneTaskService.checkSceneTaskStatus(id,reportId);
            SceneActionResp resp = new SceneActionResp();
            resp.setData(sceneAction.getData());
            resp.setMsg(sceneAction.getMsg());
            resp.setReportId(sceneAction.getReportId());
            return ResponseResult.success(resp);

    }

    @PostMapping("/startTryRunTask")
    @ApiOperation(value = "启动脚本试跑")
    public ResponseResult<SceneTryRunTaskStartResp> startTryRunTask(@RequestBody
        SceneTryRunTaskStartReq sceneTryRunTaskStartReq) {
        SceneManageWrapperInput input = SceneTaskOpenConverter.INSTANCE.ofSceneTryRunTaskReq(sceneTryRunTaskStartReq);
        //压测引擎插件需要传入插件id和插件版本 modified by xr.l 20210712
        List<EnginePluginInput> enginePluginInputs = null;
        if (CollectionUtils.isNotEmpty(sceneTryRunTaskStartReq.getEnginePlugins())) {
            List<EnginePluginsRefOpen> enginePlugins = sceneTryRunTaskStartReq.getEnginePlugins();
            enginePluginInputs = enginePlugins.stream().map(plugin -> new EnginePluginInput() {{
                setPluginId(plugin.getPluginId());
                setPluginVersion(plugin.getVersion());
            }}).collect(Collectors.toList());
        }
        SceneTryRunTaskStartOutput sceneTryRunTaskStartOutput = sceneTaskService.startTryRun(input,
            enginePluginInputs);
        SceneTryRunTaskStartResp sceneTryRunTaskStartResp = new SceneTryRunTaskStartResp();
        BeanUtils.copyProperties(sceneTryRunTaskStartOutput, sceneTryRunTaskStartResp);
        return ResponseResult.success(sceneTryRunTaskStartResp);
    }

    @GetMapping("/checkTaskStatus")
    @ApiOperation(value = "查询试跑状态")
    public ResponseResult<SceneTryRunTaskStatusResp> checkTaskStatus(@RequestParam Long sceneId, @RequestParam Long reportId) {
        SceneTryRunTaskStatusOutput sceneTryRunTaskStatusOutput = sceneTaskService.checkTaskStatus(sceneId,
            reportId);
        SceneTryRunTaskStatusResp resp = new SceneTryRunTaskStatusResp();
        BeanUtils.copyProperties(sceneTryRunTaskStatusOutput,resp);
        return ResponseResult.success(resp);
    }

    @GetMapping("/checkJobStatus")
    @ApiOperation(value = "检测巡检任务状态：压测引擎")
    public ResponseResult<SceneJobStateResp> checkJobStatus(Long id) {
        SceneJobStateOutput jobState = sceneTaskService.checkSceneJobStatus(id);
        SceneJobStateResp resp = new SceneJobStateResp();
        resp.setState(jobState.getState());
        resp.setMsg(jobState.getMsg());
        return ResponseResult.success(resp);
    }

    @GetMapping("/preCheck")
    @ApiOperation(value = "启动压测前检查位点")
    public ResponseResult<SceneStartCheckResp> sceneStartPreCheck(@RequestParam Long sceneId) {
        SceneTaskStartCheckInput input = new SceneTaskStartCheckInput();
        input.setSceneId(sceneId);
        SceneTaskStartCheckOutput output = sceneTaskService.sceneStartCsvPositionCheck(input);
        SceneStartCheckResp resp = new SceneStartCheckResp();
        BeanUtils.copyProperties(output, resp);
        return ResponseResult.success(resp);
    }

}

