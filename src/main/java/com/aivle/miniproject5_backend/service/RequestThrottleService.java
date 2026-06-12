package com.aivle.miniproject5_backend.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RequestThrottleService {

    private static final long LIKE_COOLDOWN_MILLIS = 30_000;
    private static final long VIEW_COOLDOWN_MILLIS = 5_000;

    private final Map<String, Long> requestStore = new ConcurrentHashMap<>();

    public boolean canIncreaseLike(Long bookId, String clientIp) {
        return canProceed("LIKE", bookId, clientIp, LIKE_COOLDOWN_MILLIS);
    }

    public boolean canIncreaseView(Long bookId, String clientIp) {
        return canProceed("VIEW", bookId, clientIp, VIEW_COOLDOWN_MILLIS);
    }

    public long getRemainingLikeSeconds(Long bookId, String clientIp) {
        return getRemainingSeconds("LIKE", bookId, clientIp, LIKE_COOLDOWN_MILLIS);
    }

    private boolean canProceed(String type, Long bookId, String clientIp, long cooldownMillis) {
        long now = System.currentTimeMillis();
        String key = type + ":" + bookId + ":" + clientIp;

        Long lastRequestedAt = requestStore.get(key);

        if (lastRequestedAt != null && (now - lastRequestedAt) < cooldownMillis) {
            return false;
        }

        requestStore.put(key, now);
        return true;
    }

    private long getRemainingSeconds(String type, Long bookId, String clientIp, long cooldownMillis) {
        long now = System.currentTimeMillis();
        String key = type + ":" + bookId + ":" + clientIp;

        Long lastRequestedAt = requestStore.get(key);
        if (lastRequestedAt == null) return 0;

        long remainingMillis = cooldownMillis - (now - lastRequestedAt);
        if (remainingMillis <= 0) return 0;

        return (long) Math.ceil(remainingMillis / 1000.0);
    }
}