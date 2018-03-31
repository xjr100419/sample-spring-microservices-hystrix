package com.github.charlesvhe.springcloud.practice.core;

import com.google.common.base.Optional;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class LabelAndWeightMetadataRule extends ZoneAvoidanceRule {

    private static final Random random = new Random();
    private CompositePredicate compositePredicate;


    @Override
    public Server choose(Object key) {
        System.out.println("********  choose  start.... ");
        ILoadBalancer lb = this.getLoadBalancer();
        Optional<Server> server = this.getPredicate().chooseRoundRobinAfterFiltering(lb.getAllServers(), key);

        Server server1 = server.isPresent() ? (Server)server.get() : null;
        if(server1!=null){
            Map<String, String> metadata = ((DiscoveryEnabledServer) server1).getInstanceInfo().getMetadata();
            if(!MetadataUtil.checkLabelOr(metadata)){
                System.out.println("********  discover not found.... ");
                return null;
            }
        }

        return server1;
    }

    public LabelAndWeightMetadataRule() {
        //ZoneAvoidancePredicate zonePredicate = new ZoneAvoidancePredicate(this,null);
        AvailabilityPredicate availabilityPredicate = new AvailabilityPredicate(this,null);
        MetadataAwarePredicate metadataAwarePredicate = new MetadataAwarePredicate();
        this.compositePredicate = this.createCompositePredicate( availabilityPredicate,metadataAwarePredicate);
    }

    private CompositePredicate createCompositePredicate(ZoneAvoidancePredicate p1, AvailabilityPredicate p2,MetadataAwarePredicate p3) {
        return CompositePredicate.withPredicates(new AbstractServerPredicate[]{p3,p1, p2}).addFallbackPredicate(p2).addFallbackPredicate(AbstractServerPredicate.alwaysTrue()).build();
    }

    private CompositePredicate createCompositePredicate( AvailabilityPredicate p2,MetadataAwarePredicate p3) {
        return CompositePredicate.withPredicates(new AbstractServerPredicate[]{p3, p2}).addFallbackPredicate(p2).addFallbackPredicate(AbstractServerPredicate.alwaysTrue()).build();
    }


    @Override
    public AbstractServerPredicate getPredicate() {
        return this.compositePredicate;
    }
}
