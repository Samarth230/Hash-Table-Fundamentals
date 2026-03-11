import java.util.*;
import java.util.stream.*;

public class Problem5_AnalyticsDashboard {
    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.merge(url, 1, Integer::sum);
        uniqueVisitors.computeIfAbsent(url, k -> new HashSet<>()).add(userId);
        trafficSources.merge(source, 1, Integer::sum);
    }

    public void getDashboard() {
        System.out.println("=== Top Pages ===");
        pageViews.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(e -> System.out.printf("  %s - %d views (%d unique)%n",
                e.getKey(), e.getValue(),
                uniqueVisitors.get(e.getKey()).size()));

        System.out.println("\n=== Traffic Sources ===");
        int total = trafficSources.values().stream().mapToInt(Integer::intValue).sum();
        trafficSources.forEach((src, count) ->
            System.out.printf("  %s: %.0f%%%n", src, count * 100.0 / total));
    }

    public static void main(String[] args) {
        Problem5_AnalyticsDashboard dash = new Problem5_AnalyticsDashboard();
        dash.processEvent("/article/breaking-news", "user_123", "google");
        dash.processEvent("/article/breaking-news", "user_456", "facebook");
        dash.processEvent("/article/breaking-news", "user_123", "google"); // repeat user
        dash.processEvent("/sports/championship", "user_789", "google");
        dash.processEvent("/sports/championship", "user_101", "direct");
        dash.getDashboard();
    }
}