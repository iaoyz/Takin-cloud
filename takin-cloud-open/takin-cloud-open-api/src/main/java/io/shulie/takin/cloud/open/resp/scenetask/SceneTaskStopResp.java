package io.shulie.takin.cloud.open.resp.scenetask;

import com.google.common.collect.Lists;
import io.shulie.takin.cloud.common.pojo.AbstractEntry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: liyuanba
 * @Date: 2021/11/26 2:18 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SceneTaskStopResp extends AbstractEntry {
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
