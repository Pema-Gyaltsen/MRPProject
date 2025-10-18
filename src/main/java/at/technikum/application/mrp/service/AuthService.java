package at.technikum.application.mrp.service;

import at.technikum.application.mrp.model.User;
import at.technikum.application.mrp.repository.UserRepository;
import at.technikum.application.security.TokenService;

public class AuthService {
    private final UserRepository users = new UserRepository(); // service depends on the repo to talk to the DB

    public User register(String username, String password) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("USERNAME_EMPTY");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("PASSWORD_EMPTY");

        if (users.findByUsername(username) != null) {
            throw new IllegalStateException("USERNAME_TAKEN");
        }

        // Store raw password (rename field later if you want)
        return users.create(username, password);
    }

    /*
      Login: validates credentials and returns a stateless signed token.
     */
    public String login(String username, String password) {
        if (username == null || password == null) return null;

        User u = users.findByUsername(username);
        if (u == null) return null;


        if (!password.equals(u.passwordHash)) return null;
        //passwordHash stores raw password, I will rename it later

        // Issue stateless token (no TokenStore anywhere)
        return TokenService.issue(u.username);
    }

    /**
     * Validate a Bearer token from the Authorization header and return the userId, or null if invalid.
     * Accepts either "Bearer <token>" or the raw token string.
     */
    public Integer validateToken(String bearer) {
        if (bearer == null || bearer.isBlank()) return null;

        String token = bearer;
        int sp = bearer.indexOf(' ');
        if (sp > 0 && bearer.regionMatches(true, 0, "Bearer", 0, "Bearer".length())) {
            token = bearer.substring(sp + 1).trim();
        }

        String username = TokenService.verify(token);
        if (username == null) return null;

        User u = users.findByUsername(username);
        return (u != null) ? u.id : null;
    }

    public User get(int id) {
        return users.findById(id);
    }
}
