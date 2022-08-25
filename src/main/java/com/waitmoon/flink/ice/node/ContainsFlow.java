package com.waitmoon.flink.ice.node;

import com.ice.common.enums.NodeRunStateEnum;
import com.ice.core.context.IceContext;
import com.ice.core.context.IceRoam;
import com.ice.core.leaf.roam.BaseLeafRoamFlow;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author waitmoon
 * 过滤性质节点
 * 判断值在不在集合中
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ContainsFlow extends BaseLeafRoamFlow {
    //默认input
    private String key = "input";

    private Set<String> set;

    @Override
    protected boolean doRoamFlow(IceRoam roam) {
        //判断roam中的key对应的值是否在集合中
        return set.contains(roam.<String>getMulti(key));
    }

    @Override
    public void afterPropertiesSet() {
        log.info("ContainsFlow init with key:{}, set:{} nodeId:{}", key, set, this.getIceNodeId());
    }

    public NodeRunStateEnum errorHandle(IceContext ctx, Throwable t) {
        log.error("error occur id:{} e:", this.findIceNodeId(), t);
        return super.errorHandle(ctx, t);
    }
}
