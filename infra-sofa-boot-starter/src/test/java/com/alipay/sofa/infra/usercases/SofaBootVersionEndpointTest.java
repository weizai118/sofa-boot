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
package com.alipay.sofa.infra.usercases;

import com.alipay.sofa.infra.endpoint.SofaBootVersionEndpoint;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author qilong.zql
 * @since 2.5.0
 */
public class SofaBootVersionEndpointTest {

    @Test
    @SuppressWarnings("unchecked")
    public void test() {
        SofaBootVersionEndpoint sofaBootVersionEndpoint = new SofaBootVersionEndpoint();
        List<Object> result = (List<Object>) sofaBootVersionEndpoint.invoke();
        List<Object> cacheResult = (List<Object>) sofaBootVersionEndpoint.invoke();

        Assert.assertNotNull(result);
        Assert.assertNotNull(cacheResult);
        Assert.assertTrue(result.equals(cacheResult));

        Properties versionInfo = (Properties) cacheResult.get(0);

        Assert.assertTrue("com.alipay.sofa".equals(versionInfo.getProperty("GroupId")));
        Assert.assertTrue("infra-sofa-boot-starter".equals(versionInfo.getProperty("ArtifactId")));
        Assert.assertTrue("https://github.com/alipay/sofa-boot".equals(versionInfo
            .getProperty("Doc-Url")));
    }

    @Test
    public void testException() {
        new MockUp<SofaBootVersionEndpoint>() {
            @Mock
            private List<Resource> getSofaVersionsPropertiesResources() throws IOException {
                return Collections.singletonList((Resource) new ByteArrayResource(new byte[] {}));
            }
        };
        new SofaBootVersionEndpoint().invoke();

        new MockUp<SofaBootVersionEndpoint>() {
            @Mock
            private void generateGavResult(List<Properties> gavResult) throws IOException {
                throw new RuntimeException("mock");
            }
        };
        new SofaBootVersionEndpoint().invoke();
    }
}