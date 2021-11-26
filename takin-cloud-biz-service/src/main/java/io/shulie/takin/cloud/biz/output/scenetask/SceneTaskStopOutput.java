package io.shulie.takin.cloud.biz.output.scenetask;

import com.google.common.collect.Lists;
import io.shulie.takin.cloud.common.pojo.AbstractEntry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: liyuanba
 * @Date: 2021/11/26 11:40 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SceneTaskStopOutput extends AbstractEntry {
    @ApiModelProperty(value = "场景ID")
    private Long sceneId;

    @ApiModelProperty(value = "任务ID")
    private Long reportId;

    @ApiModelProperty(value = "错误信息")
    private List<String> msgs;

    public void addMsg(String msg) {
        if (null == msgs) {
            msgs = Lists.newArrayList();
        }
        msgs.add(msg);
    }
}
