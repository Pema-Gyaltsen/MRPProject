package at.technikum.application.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;



public final class TokenService {
    private static final long TTL_MS = 24 * 60 * 60 * 1000L;

    // token -> record (thread-safe)
    private static final Map<String, Record> TOKENS = new ConcurrentHashMap<>();

    private TokenService() {}

    public static String issue(String username) {
        long exp = System.currentTimeMillis() + TTL_MS;
        String token = UUID.randomUUID().toString(); // opaque, unguessable enough for demo
        TOKENS.put(token, new Record(username, exp));
        return token; // return as-is (safe to send in header or body)
    }

    public static String verify(String token) {
        if (token == null || token.isBlank()) return null;

        Record r = TOKENS.get(token);
        if (r == null) return null;

        if (System.currentTimeMillis() > r.exp) {
            TOKENS.remove(token); // expire eagerly
            return null;
        }
        return r.username;
    }

    public static void revoke(String token) {
        if (token != null) TOKENS.remove(token);
    }

    // Optional: call this occasionally to trim expired entries (e.g., on a timer).
    public static int purgeExpired() {
        long now = System.currentTimeMillis();
        int removed = 0;
        for (Map.Entry<String, Record> e : TOKENS.entrySet()) {
            if (now > e.getValue().exp) {
                TOKENS.remove(e.getKey());
                removed++;
            }
        }
        return removed;
    }

    private static final class Record {
        final String username;
        final long exp;
        Record(String username, long exp) {
            this.username = username;
            this.exp = exp;
        }
    }
}

/*
  Opaque token service (no hashing):
  - issue(username):  creates a random token and stores {token -> (username, exp)} in memory
  - verify(token):    looks up the token, checks expiry, returns username or null
  - revoke(token):    optional logout

  NOTE: tokens live only in this JVM. On restart they’re gone.
  For multi-instance or persistence, back this with DB/Redis instead of a Map.
 */
