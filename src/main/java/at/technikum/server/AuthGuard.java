package at.technikum.server;

import at.technikum.util.TokenStore;

public final class AuthGuard {
    public static Integer fromAuthorizationHeader(String auth){
        if(auth == null) return null;
        String[] parts = auth.split(" ");
        String token = parts.length==2? parts[1] : auth;
        return TokenStore.getUserId(token);
    }
    private AuthGuard(){}
}
