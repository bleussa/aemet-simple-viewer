package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class AemetCacheConfig {

    static final String MUNICIPIOS = "municipios";
    static final String PREDICCION = "prediccion";

    @Bean
    CacheManager cacheManager(AemetProperties properties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache(MUNICIPIOS, Caffeine.newBuilder()
                .expireAfterWrite(properties.cache().municipiosTtl())
                .build());
        cacheManager.registerCustomCache(PREDICCION, Caffeine.newBuilder()
                .expireAfterWrite(properties.cache().prediccionTtl())
                .maximumSize(10_000)
                .build());
        return cacheManager;
    }
}
