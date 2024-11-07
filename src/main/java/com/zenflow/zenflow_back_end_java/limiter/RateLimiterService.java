package com.zenflow.zenflow_back_end_java.limiter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zenflow.zenflow_back_end_java.exception.RateLimitExceededException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private static final int MAX_ATTEMPTS = 10;
    private static final long EXPIRATION_TIME_SECONDS = 60;

    private final Cache<String, Integer> requestCache;

    public RateLimiterService() {
        this.requestCache = Caffeine.newBuilder()
                .expireAfterWrite(EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS)
                .maximumSize(10000)
                .build();
    }

    public boolean isAllowed(String key) {
        Integer attempts = requestCache.getIfPresent(key);
        if (attempts == null) {
            requestCache.put(key, 1);
            return true;
        } else {
            if (attempts >= MAX_ATTEMPTS) {
                return false;
            } else {
                requestCache.put(key, attempts + 1);
                return true;
            }
        }
    }

    public void resetLimit(String key) {
        requestCache.invalidate(key);
    }

    public void checkRateLimit(String key) {
        if (!isAllowed(key)) {
            throw new RateLimitExceededException("Too many requests. Please try again later.");
        }
    }
}
