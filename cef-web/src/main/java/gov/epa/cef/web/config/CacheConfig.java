/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@EnableCaching
@Configuration
public class CacheConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Environment environment;

    @Autowired
    CacheConfig(Environment environment) {

        this.environment = environment;
    }

    @Bean
    CacheManager cacheManager() {

        return new HazelcastCacheManager(hazelcastInstance());
    }

    @Bean(destroyMethod = "shutdown")
    HazelcastInstance hazelcastInstance() {

//        Config config = new Config()
//            .setInstanceName("cef-hazelcast-cache")
//            .addMapConfig(new MapConfig()
//                .setName("default")
//                .setMaxSizeConfig(new MaxSizeConfig(2000, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
//                .setEvictionPolicy(EvictionPolicy.LRU)
//                .setTimeToLiveSeconds(120));

        String configLocation = this.environment.getRequiredProperty("spring.hazelcast.config");
        logger.info("Using cache configuration: {}", configLocation);

        Config config = null;
        try {

            config = new XmlConfigBuilder(ResourceUtils.getURL(configLocation)).build();

        } catch (IOException e) {

            throw new IllegalStateException(e);
        }

        return Hazelcast.getOrCreateHazelcastInstance(config);
    }
}
