package com.waitmoon.flink.ice;

import com.ice.core.Ice;
import com.ice.core.client.IceNioClient;
import com.ice.core.context.IcePack;
import com.ice.core.context.IceRoam;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * ice算子
 */
public class IceProcessor extends KeyedProcessFunction<Integer, String, String> {

    //ice 客户端
    private static IceNioClient iceNioClient;

    static {
        //初始化ice客户端
        try {
            //配置远程server地址，app，以及节点扫描路径
            //此处使用了自己搭建的server，后台地址 http://eg.waitmoon.com/config/list/2
            iceNioClient = new IceNioClient(2, "waitmoon.com:18121", "com.waitmoon.flink.ice.node");
            //启动ice客户端
            iceNioClient.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processElement(String value, Context ctx, Collector<String> out) {
        //组装IcePack
        IcePack pack = new IcePack();
        //设置要触发的iceId(配置后台中需要触发的ID)
        pack.setIceId(1081);
        //初始化roam，将单词和长度放入roam中
        IceRoam roam = new IceRoam();
        roam.put("input", value);
        roam.put("length", ctx.getCurrentKey());
        pack.setRoam(roam);
        //同步执行
        Ice.syncProcess(pack);
        //执行完成后，获取roam中的result
        String result = roam.getMulti("result");
        if (result != null) {
            //result不为空，将结果放入下游算子
            out.collect(result);
        }
    }

    @Override
    public void close() {
        if (iceNioClient != null) {
            //清理ice 客户端
            iceNioClient.destroy();
            iceNioClient = null;
        }
    }
}
