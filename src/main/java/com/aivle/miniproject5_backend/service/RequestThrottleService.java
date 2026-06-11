package com.aivle.miniproject5_backend.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
// 같은 IP 기준으로 도서별 좋아요/조회수 요청 시간을 기록하고,
// 일정 시간(cooldown) 안의 중복 요청을 차단하는 서비스
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
}