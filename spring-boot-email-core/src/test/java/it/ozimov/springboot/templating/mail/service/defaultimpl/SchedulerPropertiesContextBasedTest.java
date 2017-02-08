/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.ozimov.springboot.templating.mail.service.defaultimpl;

import it.ozimov.springboot.templating.mail.ContextBasedTest;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;


@RunWith(Enclosed.class)
public class SchedulerPropertiesContextBasedTest implements ContextBasedTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @TestPropertySource(locations = "classpath:base-test.properties",
            properties =
                    {
                            "spring.mail.persistence.enabled=false",
                            "spring.mail.persistence.redis.embedded=false",
                            "spring.mail.persistence.redis.enabled=false",
                            "spring.mail.scheduler.priorityLevels=123",
                            "spring.mail.scheduler.persistenceLayer.desiredBatchSize=1",
                            "spring.mail.scheduler.persistenceLayer.minKeptInMemory=1",
                            "spring.mail.scheduler.persistenceLayer.maxKeptInMemory=1"
                    })
    public static class SchedulerPropertiesWithoutPersistenceContextBasedTest implements ContextBasedTest {

        @Rule
        public Timeout timeout = new Timeout(10, TimeUnit.SECONDS);

        @Rule
        public final JUnitSoftAssertions assertions = new JUnitSoftAssertions();

        @Autowired
        private SchedulerProperties schedulerProperties;

        @Test
        public void shouldSchedulerPropertiesHaveExpectedValues() throws Exception {
            //Assert
            assertions.assertThat(schedulerProperties)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("priorityLevels", 123)
                    .hasFieldOrPropertyWithValue("persistenceLayer", SchedulerProperties.PersistenceLayer.builder()
                            .desiredBatchSize(1).minKeptInMemory(1).maxKeptInMemory(1).build());
        }

    }


    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @TestPropertySource(locations = "classpath:redis-test.properties",
            properties =
                    {
                            "spring.mail.persistence.enabled=true",
                            "spring.mail.persistence.redis.embedded=false",
                            "spring.mail.persistence.redis.enabled=false",
                            "spring.mail.scheduler.priorityLevels=321",
                            "spring.mail.scheduler.persistenceLayer.desiredBatchSize=125",
                            "spring.mail.scheduler.persistenceLayer.minKeptInMemory=25",
                            "spring.mail.scheduler.persistenceLayer.maxKeptInMemory=123456"
                    })
    public static class SchedulerPropertiesWithPeristenceContextBasedTest implements ContextBasedTest {

        @Rule
        public Timeout timeout = new Timeout(10, TimeUnit.SECONDS);

        @Rule
        public final JUnitSoftAssertions assertions = new JUnitSoftAssertions();

        @Autowired
        private SchedulerProperties schedulerProperties;

        @Test
        public void shouldSchedulerPropertiesHaveExpectedValues() throws Exception {
            //Assert
            assertions.assertThat(schedulerProperties)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("priorityLevels", 321)
                    .hasFieldOrPropertyWithValue("persistenceLayer", SchedulerProperties.PersistenceLayer.builder()
                            .desiredBatchSize(125).minKeptInMemory(25).maxKeptInMemory(123456).build());
        }

    }

}