package chris.mcqueen.development.predictimo.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(chris.mcqueen.development.predictimo.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.Prediction.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.Prediction.class.getName() + ".usersPredictings", jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.Prediction.class.getName() + ".userProfileCreators", jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.PredictionType.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.PredictionType.class.getName() + ".predictionTitles", jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.PredictionPoll.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.PredictionPoll.class.getName() + ".userVotes", jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.UserPollVote.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.UserProfile.class.getName(), jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.UserProfile.class.getName() + ".userVotes", jcacheConfiguration);
            cm.createCache(chris.mcqueen.development.predictimo.domain.UserProfile.class.getName() + ".predictions", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
