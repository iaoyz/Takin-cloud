

package io.shulie.takin.cloud.open.entrypoint.controller.report;

import java.util.List;

import io.shulie.takin.cloud.biz.input.report.UpdateReportConclusionInput;
import io.shulie.takin.cloud.biz.input.report.WarnCreateInput;
import io.shulie.takin.cloud.biz.output.report.ReportDetailOutput;
import io.shulie.takin.cloud.biz.service.report.ReportService;
import io.shulie.takin.cloud.common.bean.scenemanage.ScriptNodeSummaryBean;
import io.shulie.takin.cloud.common.constants.APIUrls;
import io.shulie.takin.cloud.common.exception.TakinCloudException;
import io.shulie.takin.cloud.common.exception.TakinCloudExceptionEnum;
import io.shulie.takin.cloud.open.req.report.ReportDetailByIdReq;
import io.shulie.takin.cloud.open.req.report.ReportTrendQueryReq;
import io.shulie.takin.cloud.open.req.report.ScriptNodeTreeQueryReq;
import io.shulie.takin.cloud.open.req.report.UpdateReportConclusionReq;
import io.shulie.takin.cloud.open.req.report.WarnCreateReq;
import io.shulie.takin.cloud.open.resp.report.NodeTreeSummaryResp;
import io.shulie.takin.cloud.open.resp.report.ReportDetailResp;
import io.shulie.takin.cloud.open.resp.report.ReportTrendResp;
import io.shulie.takin.cloud.open.resp.report.ScriptNodeTreeResp;
import io.shulie.takin.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 莫问
 * @date 2020-04-17
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL)
@Api(tags = "场景报告模块", value = "场景报告")
public class ReportOpenController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/report/warn")
    @ApiOperation("创建告警")
    public ResponseResult<String> addWarn(@RequestBody WarnCreateReq req) {
        WarnCreateInput input = new WarnCreateInput();
        BeanUtils.copyProperties(req, input);
        reportService.addWarn(input);
        return ResponseResult.success("创建告警成功");
    }

    /**
     * 更新报告
     */
    @PutMapping("/report/updateReportConclusion")
    @ApiOperation("更新报告-漏数检查使用")
    public ResponseResult<String> updateReportConclusion(@RequestBody UpdateReportConclusionReq req) {
        UpdateReportConclusionInput input = new UpdateReportConclusionInput();
        BeanUtils.copyProperties(req, input);
        reportService.updateReportConclusion(input);
        return ResponseResult.success("更新成功");
    }

    @GetMapping(value = "report/getReportByReportId")
    @ApiOperation("报告详情")
    @ApiImplicitParam(name = "reportId", value = "报告ID")
    public ResponseResult<ReportDetailResp> getReportByReportId(Long reportId) {
        ReportDetailOutput detailOutput = reportService.getReportByReportId(reportId);
        if (detailOutput == null) {
            throw new TakinCloudException(TakinCloudExceptionEnum.REPORT_GET_ERROR, "报告不存在Id:"+reportId);
        }
        ReportDetailResp resp = new ReportDetailResp();
        BeanUtils.copyProperties(detailOutput, resp);
        return ResponseResult.success(resp);
    }

    /**
     * 实况报表
     */
    @GetMapping("report/tempReportDetail")
    @ApiOperation("实况报告")
    @ApiImplicitParam(name = "sceneId", value = "场景ID")
    public ResponseResult<ReportDetailResp> tempReportDetail(Long sceneId) {
        ReportDetailOutput detailOutput = reportService.tempReportDetail(sceneId);
        if (detailOutput == null) {
            throw new TakinCloudException(TakinCloudExceptionEnum.REPORT_GET_ERROR, "报告不存在");
        }
        ReportDetailResp resp = new ReportDetailResp();
        BeanUtils.copyProperties(detailOutput, resp);
        return ResponseResult.success(resp);
    }


    /**
     * 节点树
     */
    @GetMapping("report/nodeTree")
    @ApiOperation("节点树")
    public ResponseResult<List<ScriptNodeTreeResp>> queryScriptNodeTree(ScriptNodeTreeQueryReq req){
        List<ScriptNodeTreeResp> nodeTree = reportService.getNodeTree(req);
        if (CollectionUtils.isEmpty(nodeTree)){
            throw new TakinCloudException(TakinCloudExceptionEnum.REPORT_GET_ERROR, "报告不存在");
        }
        return ResponseResult.success(nodeTree);
    }

    /**
     * 实况报告链路趋势
     */
    @GetMapping("report/tempReportTrend")
    @ApiOperation("实况报告链路趋势")
    public ResponseResult<ReportTrendResp> queryTempReportTrend(ReportTrendQueryReq req){
        return ResponseResult.success(reportService.queryTempReportTrend(req));
    }

    /**
     * 报告链路趋势
     */
    @GetMapping("report/reportTrend")
    @ApiOperation("报告链路趋势")
    public ResponseResult<ReportTrendResp> queryReportTrend(ReportTrendQueryReq req){
        return ResponseResult.success(reportService.queryReportTrend(req));
    }

    /**
     * 压测明细
     */
    @GetMapping("/report/businessActivity/summary/list")
    @ApiOperation("压测明细")
    public ResponseResult<NodeTreeSummaryResp> queryActivitiesSummaryList(ReportDetailByIdReq req){
        return ResponseResult.success(reportService.getBusinessActivitySummaryList(req.getReportId()));
    }



}
