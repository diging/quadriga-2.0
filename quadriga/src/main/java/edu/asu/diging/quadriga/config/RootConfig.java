package edu.asu.diging.quadriga.config;

import java.net.URISyntaxException;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableCaching
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan({"edu.asu.diging.quadriga", "edu.asu.diging.simpleusers.core.*"})
public class RootConfig {

    @Bean
    public CacheManager cacheManager() throws URISyntaxException {
        CacheManagerBuilder.newCacheManagerBuilder().withCache("preConfigured", CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))).build();
        CachingProvider cachingProvider = Caching.getCachingProvider();
        javax.cache.CacheManager manager = cachingProvider
                .getCacheManager(getClass().getResource("/ehcache.xml").toURI(), getClass().getClassLoader());
        JCacheCacheManager cacheManager = new JCacheCacheManager(manager);
        return cacheManager;
    }
    
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:locale/messages");
        source.setFallbackToSystemLocale(false);
        return source;
    }
}