import java.util.concurrent.*;

public class Problem6_RateLimiter {
    private static class TokenBucket {
        int tokens, maxTokens;
        long lastRefillTime;

        TokenBucket(int max) {
            this.tokens = this.maxTokens = max;
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean consume() {
            long now = System.currentTimeMillis();
            long secondsElapsed = (now - lastRefillTime) / 1000;
            if (secondsElapsed > 0) {
                tokens = Math.min(maxTokens, (int)(tokens + secondsElapsed * (maxTokens / 3600.0)));
                lastRefillTime = now;
            }
            if (tokens > 0) { tokens--; return true; }
            return false;
        }

        long secondsUntilReset() {
            return 3600 - ((System.currentTimeMillis() - lastRefillTime) / 1000);
        }
    }

    private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();

    public String checkRateLimit(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(1000));
        TokenBucket bucket = clients.get(clientId);
        if (bucket.consume())
            return "Allowed (" + bucket.tokens + " requests remaining)";
        return "Denied (0 remaining, retry after " + bucket.secondsUntilReset() + "s)";
    }

    public String getRateLimitStatus(String clientId) {
        TokenBucket b = clients.getOrDefault(clientId, new TokenBucket(1000));
        return String.format("{used: %d, limit: %d, reset_in: %ds}",
                b.maxTokens - b.tokens, b.maxTokens, b.secondsUntilReset());
    }

    public static void main(String[] args) {
        Problem6_RateLimiter rl = new Problem6_RateLimiter();
        System.out.println(rl.checkRateLimit("abc123")); // Allowed 999
        System.out.println(rl.checkRateLimit("abc123")); // Allowed 998
        System.out.println(rl.getRateLimitStatus("abc123"));
    }
}