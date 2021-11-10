package io.shulie.takin.cloud.common.bean.scenemanage;


import java.math.BigDecimal;
import java.util.List;

import io.shulie.takin.cloud.common.pojo.AbstractEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author moriarty
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "报告节点数据对象")
public class ScriptNodeSummaryBean extends AbstractEntry {

    @ApiModelProperty(value = "业务活动ID")
    private Long businessActivityId;

    @ApiModelProperty(value = "节点类型名称")
    private String name;

    @ApiModelProperty(value = "节点名称")
    private String testName;

    @ApiModelProperty(value = "元素节点的md5值")
    private String md5;

    @ApiModelProperty(value = "节点类型")
    private String type;

    @ApiModelProperty(value = "元素的绝对路径")
    private String xpath;

    @ApiModelProperty(value = "xpath的md5")
    private String xpathMd5;

    @ApiModelProperty(value = "TPS")
    private DataBean tps;

    @ApiModelProperty(value = "平均RT")
    private DataBean avgRt;

    @ApiModelProperty(value = "请求成功率")
    private DataBean successRate;

    @ApiModelProperty(value = "SA")
    private DataBean sa;

    @ApiModelProperty(value = "最大tps")
    private BigDecimal maxTps;

    @ApiModelProperty(value = "最大rt")
    private BigDecimal maxRt;

    @ApiModelProperty(value = "最小rt")
    private BigDecimal minRt;

    @ApiModelProperty(value = "总请求")
    private Long totalRequest;

    @ApiModelProperty(value = "平均线程数")
    private BigDecimal avgConcurrenceNum;

    @ApiModelProperty(value = "分布")
    private List<DistributeBean> distribute;

    @ApiModelProperty(value = "通过标识")
    private Integer passFlag;


    private List<ScriptNodeSummaryBean> children;


}
