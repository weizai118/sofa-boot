/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.healthcheck.service;

import com.alipay.sofa.healthcheck.startup.ReadinessCheckListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The health check HTTP checker for start status.
 *
 * @author liangen
 * @author qilong.zql
 * @version 2.3.0
 */
@ConfigurationProperties(prefix = "com.alipay.sofa.healthcheck")
public class SofaBootReadinessCheckEndpoint extends AbstractEndpoint<Health> {

    private final HealthAggregator healthAggregator = new OrderedHealthAggregator();

    @Autowired
    private ReadinessCheckListener readinessCheckListener;

    public SofaBootReadinessCheckEndpoint(String id, boolean sensitive) {
        super(id, sensitive);
    }

    @Override
    public Health invoke() {
        boolean healthCheckerStatus = readinessCheckListener.getHealthCheckerStatus();
        Map<String, Health> healthCheckerDetails = readinessCheckListener.getHealthCheckerDetails();

        boolean healthIndicatorStatus = readinessCheckListener.getHealthIndicatorStatus();
        Map<String, Health> healthIndicatorDetails = readinessCheckListener
            .getHealthIndicatorDetails();

        boolean afterHealthCheckCallbackStatus = readinessCheckListener.getHealthCallbackStatus();
        Map<String, Health> afterHealthCheckCallbackDetails = readinessCheckListener
            .getHealthCallbackDetails();

        Builder builder;
        Map<String, Health> healths = new HashMap<>();
        if (healthCheckerStatus && healthIndicatorStatus && afterHealthCheckCallbackStatus) {
            builder = Health.up();
        } else {
            builder = Health.down();
        }
        if (!CollectionUtils.isEmpty(healthCheckerDetails)) {
            builder = builder.withDetail("HealthChecker", healthCheckerDetails);
        }
        if (!CollectionUtils.isEmpty(afterHealthCheckCallbackDetails)) {
            builder = builder.withDetail("ReadinessCheckCallback", afterHealthCheckCallbackDetails);
        }
        healths.put("SOFABootReadinessHealthCheckInfo", builder.build());
        healths.putAll(healthIndicatorDetails);
        return this.healthAggregator.aggregate(healths);
    }

}