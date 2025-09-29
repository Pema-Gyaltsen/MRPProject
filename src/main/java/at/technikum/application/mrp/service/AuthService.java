package at.technikum.application.mrp.service;

import at.technikum.application.mrp.model.User;
import at.technikum.application.mrp.repository.UserRepository;
import at.technikum.util.Hashing;
import at.technikum.util.TokenStore;

public class AuthService {
    private final UserRepository users = new UserRepository();

    public User register(String username, String password){
        String hash = Hashing.sha256(password);
        return users.create(username, hash);
    }

    public String login(String username, String password){
        User u = users.findByUsername(username);
        if(u == null) return null;
        if(!u.passwordHash.equals(Hashing.sha256(password))) return null;
        String token = username + "-mrpToken"; // spec example format is acceptable for now. :contentReference[oaicite:3]{index=3}
        TokenStore.put(token, u.id);
        return token;
    }

    public Integer validateToken(String bearer) {
        if(bearer == null || bearer.isBlank()) return null;
        String[] parts = bearer.split(" ");
        String token = parts.length == 2 ? parts[1] : bearer; // allow "Bearer xxx" or raw
        return TokenStore.getUserId(token);
    }

    public User get(int id){ return users.findById(id); }
}
