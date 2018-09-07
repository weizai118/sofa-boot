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
package com.alipay.sofa.infra.env;

import com.alipay.sofa.infra.autoconfigure.SofaBootInfraAutoConfiguration;
import com.alipay.sofa.infra.constants.CommonMiddlewareConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.*;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author qilong.zql
 * @since 2.5.0
 */
public class EnvironmentCustomizer implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        PropertySource propertySource = new PropertiesPropertySource("version",
            getSofaBootVersionProperties());
        environment.getPropertySources().addLast(propertySource);
    }

    /**
     * {@link org.springframework.boot.ResourceBanner#getVersionsMap}
     * Get SOFABoot Version and print it on banner
     */
    private Properties getSofaBootVersionProperties() {
        Properties properties = new Properties();
        String sofaBootVersion = SofaBootInfraAutoConfiguration.class.getPackage()
            .getImplementationVersion();
        // generally, it would not be null and just for test.
        sofaBootVersion = StringUtils.isEmpty(sofaBootVersion) ? "" : sofaBootVersion;
        String sofaBootFormattedVersion = sofaBootVersion.isEmpty() ? "" : String.format(" (v%s)",
            sofaBootVersion);
        properties.setProperty(CommonMiddlewareConstants.SOFA_BOOT_VERSION, sofaBootVersion);
        properties.setProperty(CommonMiddlewareConstants.SOFA_BOOT_FORMATTED_VERSION,
            sofaBootFormattedVersion);
        return properties;
    }
}