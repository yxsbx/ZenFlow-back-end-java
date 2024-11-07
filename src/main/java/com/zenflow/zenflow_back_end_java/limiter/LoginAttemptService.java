package com.zenflow.zenflow_back_end_java.limiter;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;

    @Autowired
    private Cache<String, Integer> loginAttemptCache;

    public void loginFailed(String key) {
        Integer attempts = loginAttemptCache.getIfPresent(key);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        loginAttemptCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        Integer attempts = loginAttemptCache.getIfPresent(key);
        return attempts != null && attempts >= MAX_ATTEMPTS;
    }

    public void loginSucceeded(String key) {
        loginAttemptCache.invalidate(key);
    }
}
