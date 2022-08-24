package com.waitmoon.flink.ice.node;

import com.ice.core.context.IceRoam;
import com.ice.core.leaf.roam.BaseLeafRoamNone;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author waitmoon
 * put something to roam
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PutNone extends BaseLeafRoamNone {

    private String key;

    private Object value;

    @Override
    protected void doRoamNone(IceRoam roam) {
        roam.putMulti(key, value);
    }
}
