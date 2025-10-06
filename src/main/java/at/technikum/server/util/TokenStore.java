package at.technikum.server.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStore {
    // token -> userId
    private static final Map<String,Integer> TOKENS = new ConcurrentHashMap<>();

    public static void put(String token, int userId){ TOKENS.put(token, userId); }
    public static Integer getUserId(String token){ return TOKENS.get(token); }
    public static void revoke(String token){ TOKENS.remove(token); }
    private TokenStore(){}
}
