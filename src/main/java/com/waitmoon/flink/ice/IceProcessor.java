package com.waitmoon.flink.ice;

import com.ice.core.Ice;
import com.ice.core.client.IceNioClient;
import com.ice.core.context.IcePack;
import com.ice.core.context.IceRoam;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class IceProcessor extends KeyedProcessFunction<Integer, String, String> {


    private static IceNioClient iceNioClient;

    static {
        try {
            iceNioClient = new IceNioClient(2, "www.waitmoon.com:18121", "com.waitmoon.flink.ice.node");
            iceNioClient.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processElement(String value, Context ctx, Collector<String> out) {
        IceRoam roam = new IceRoam();
        roam.put("input", value);
        roam.put("key", ctx.getCurrentKey());
        IcePack pack = new IcePack();
        pack.setIceId(1081);
        pack.setRoam(roam);
        Ice.syncProcess(pack);
        String result = roam.getMulti("result");
        if (result != null) {
            out.collect(result);
        }
    }

    @Override
    public void close() {
        if (iceNioClient != null) {
            iceNioClient.destroy();
            iceNioClient = null;
        }
    }
}
