package de.isemwaf.smartFridge.configuration;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration extends CachingConfigurerSupport implements CachingConfigurer {

    @Primary
    @Bean("cacheManager")
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager("cache") {

            @Override
            protected Cache createConcurrentMapCache(String name) {

                return new ConcurrentMapCache(name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(5, TimeUnit.SECONDS)
                                .maximumSize(4000)
                                .build()
                                .asMap(),
                        false);
            }
        };
    }
}