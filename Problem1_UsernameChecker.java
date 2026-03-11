import java.util.*;

public class Problem1_UsernameChecker {
    private HashMap<String, Integer> users = new HashMap<>();
    private HashMap<String, Integer> attempts = new HashMap<>();

    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    public boolean checkAvailability(String username) {
        attempts.merge(username, 1, Integer::sum);
        return !users.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) suggestions.add(username + i);
        suggestions.add(username.replace("_", "."));
        suggestions.removeIf(s -> users.containsKey(s));
        return suggestions;
    }

    public String getMostAttempted() {
        return Collections.max(attempts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public static void main(String[] args) {
        Problem1_UsernameChecker uc = new Problem1_UsernameChecker();
        uc.registerUser("john_doe", 1);
        uc.registerUser("admin", 2);

        System.out.println(uc.checkAvailability("john_doe"));  
        System.out.println(uc.checkAvailability("jane_smith")); 
        System.out.println(uc.suggestAlternatives("john_doe")); 
        uc.checkAvailability("admin");
        uc.checkAvailability("admin");
        uc.checkAvailability("admin");
        System.out.println(uc.getMostAttempted());
}}