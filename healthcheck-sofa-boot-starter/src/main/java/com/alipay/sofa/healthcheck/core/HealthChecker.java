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
package com.alipay.sofa.healthcheck.core;

import org.springframework.boot.actuate.health.Health;

/**
 * @author liangen
 * @author qilong.zql
 * @since 2.3.0
 * @deprecated this class is not intended for use and will be removed the next major version.
 * {@link org.springframework.boot.actuate.health.HealthIndicator} is recommended to use instead.
 */
@Deprecated
public interface HealthChecker {

    /**
     * HealthCheck information.
     * @return
     */
    Health isHealthy();

    /**
     * HealthChecker name
     * @return
     */
    String getComponentName();

    /**
     * The number of retries after failure.
     * @return
     */
    int getRetryCount();

    /**
     * The time interval for each retry after failure.
     * @return
     */
    long getRetryTimeInterval();

    /**
     * Is it strictly checked?
     * If true, the final check result of isHealthy() is used as the result of the component's check.
     * If false, the final result of the component is considered to be healthy, but the exception log is printed.
     * @return
     */
    boolean isStrictCheck();
}
