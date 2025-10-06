package at.technikum.application.mrp.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.dto.LoginDto;
import at.technikum.application.mrp.dto.RegisterDto;
import at.technikum.application.mrp.service.AuthService;
import at.technikum.server.http.Method;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.Map;

public class AuthController extends Controller {
    private final AuthService auth = new AuthService();

    @Override
    public Response handle(Request req) {
        String method = req.getMethod();
        String path   = req.getPath();

        if (Method.POST.getVerb().equals(method) && path.equals("/api/auth/register")) {
            return register(req);
        }
        if (Method.POST.getVerb().equals(method) && path.equals("/api/auth/login")) {
            return login(req);
        }
        return status(Status.NOT_FOUND);
    }

    private Response register(Request req) {
        try {
            RegisterDto dto = readJson(req, RegisterDto.class);
            if (dto.username == null || dto.password == null) {
                return json(Map.of("error", "username & password required"), Status.BAD_REQUEST);
            }
            var user = auth.register(dto.username, dto.password);
            return json(Map.of("id", user.id, "username", user.username), Status.CREATED);
        } catch (IllegalStateException e) {
            if ("USERNAME_TAKEN".equals(e.getMessage())) {
                return json(Map.of("error", "username already exists"), Status.CONFLICT);
            }
            return status(Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return json(Map.of("error", "invalid json"), Status.BAD_REQUEST);
        }
    }

    private Response login(Request req) {
        try {
            LoginDto dto = readJson(req, LoginDto.class);
            String token = auth.login(dto.username, dto.password);
            if (token == null) {
                return json(Map.of("error", "invalid credentials"), Status.UNAUTHORIZED);
            }
            return json(Map.of("token", token), Status.OK);
        } catch (Exception e) {
            return json(Map.of("error", "invalid json"), Status.BAD_REQUEST);
        }
    }
}
