package com.github.charlesvhe.springcloud.practice.core;

import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class LabelMetadataRule extends ZoneAvoidanceRule {

    public static final String META_DATA_KEY_LABEL_AND = "labelAnd";
    public static final String META_DATA_KEY_LABEL_OR = "labelOr";

    public static final String META_DATA_KEY_WEIGHT = "weight";

    private Random random = new Random();

    @Override
    public Server choose(Object key) {

        /*
        * 可以参考不同规则后，并添加 标签过滤规则
        *
        * AvailabilityFilteringRule | 过滤掉那些因为一直连接失败的被标记为circuit tripped的后端server，并过滤掉那些高并发的的后端server（active connections 超过配置的阈值） | 使用一个AvailabilityPredicate来包含过滤server的逻辑，其实就就是检查status里记录的各个server的运行状态
         * RandomRule  | 随机选择一个server
         * BestAvailabl eRule | 选择一个最小的并发请求的server | 逐个考察Server，如果Server被tripped了，则忽略，在选择其中
         * RoundRobinRule  |  roundRobin方式轮询选择  |  轮询index，选择index对应位置的server
         * WeightedResponseTimeRule  |  根据响应时间分配一个weight，响应时间越长，weight越小，被选中的可能性越低。  |  一 个后台线程定期的从status里面读取评价响应时间，为每个server计算一个weight。Weight的计算也比较简单responsetime 减去每个server自己平均的responsetime是server的权重。当刚开始运行，没有形成statas时，使用roubine策略选择 server。
         * RetryRule  |  对选定的负载均衡策略机上重试机制。 |  在一个配置时间段内当选择server不成功，则一直尝试使用subRule的方式选择一个可用的server
         * ZoneAvoidanceRule  |  复合判断server所在区域的性能和server的可用性选择server  |  使 用ZoneAvoidancePredicate和AvailabilityPredicate来判断是否选择某个server，前一个判断判定一个 zone的运行性能是否可用，剔除不可用的zone（的所有server），AvailabilityPredicate用于过滤掉连接数过多的 Server
        * */
        int count = 0;
        if (this.getLoadBalancer() == null) {
            return null;
        } else {
            Server server = null;

            while(server == null) {
                if (Thread.interrupted()) {
                    return null;
                }

                List<Server> upList = this.getLoadBalancer().getReachableServers();
                List<Server> allList = this.getLoadBalancer().getAllServers();
                int serverCount = allList.size();
                if (serverCount == 0) {
                    return null;
                }

                int index = this.random.nextInt(serverCount);
                server = (Server)upList.get(index);
                if (server == null) {
                    Thread.yield();
                } else {
                    if (server.isAlive() && (server.isReadyToServe()) ) {
                        Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
                        String labelOr = metadata.get(META_DATA_KEY_LABEL_OR);
                        if(!StringUtils.isEmpty(labelOr)){
                            List<String> metadataLabel = Arrays.asList(labelOr.split(CoreHeaderInterceptor.HEADER_LABEL_SPLIT));
                            for (String label : metadataLabel) {
                                if(CoreHeaderInterceptor.label.get().contains(label)){
                                    return server;
                                }
                            }
                        }
                    }

                    server = null;
                    Thread.yield();
                }
            }

            return server;
        }
    }
}
