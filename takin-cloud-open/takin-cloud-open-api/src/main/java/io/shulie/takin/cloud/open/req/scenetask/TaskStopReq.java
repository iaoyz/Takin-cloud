package io.shulie.takin.cloud.open.req.scenetask;

import io.shulie.takin.ext.content.user.CloudUserCommonRequestExt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @Author: liyuanba
 * @Date: 2021/11/26 11:00 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskStopReq extends CloudUserCommonRequestExt {
    @ApiModelProperty(value = "任务ID")
    @NotNull
    private Long reportId;

    @ApiModelProperty(value = "是否需要生成最终报告")
    private boolean finishReport = true;
}
