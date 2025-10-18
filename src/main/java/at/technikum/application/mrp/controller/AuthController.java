package at.technikum.application.mrp.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.dto.LoginDto; //DTO: Data Transfer Object: used to transfer data between different parts of an application
import at.technikum.application.mrp.dto.RegisterDto;
import at.technikum.application.mrp.service.AuthService; //business/service layer for auth logic (register users, check passwords, generate tokens, etc.)
import at.technikum.server.http.Method;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.Map; //build small JSON objects quickly (Map.of("token",token))

public class AuthController extends Controller {
    private final AuthService auth = new AuthService(); //creates the service object once

    @Override
    public Response handle(Request req) {
        String method = req.getMethod();
        String path   = req.getPath();

        if (Method.POST.getVerb().equals(method) && path.equals("/api/users/register")) {
            return register(req);
        } //if it's POST to /api/..., delegate to register(req)

        if (Method.POST.getVerb().equals(method) && path.equals("/api/users/login")) {
            return login(req);
        }
        return status(Status.NOT_FOUND);
    }

    private Response register(Request req) {
        try {
            RegisterDto dto = readJson(req, RegisterDto.class); //parses the request body JSON into a RegisterDto (fields like username, password)
            if (dto.username == null || dto.password == null) {
                return json(Map.of("error", "username & password required"), Status.BAD_REQUEST);
            }
            //Map.of: quickly creates a small, immutable MAP, java object, writing a whole class -> overkill

            var user = auth.register(dto.username, dto.password);
            return json(Map.of("id", user.id, "username", user.username), Status.CREATED);

        } catch (IllegalStateException e) { //will implement dedicated exception classes later
            if ("USERNAME_TAKEN".equals(e.getMessage())) {
                return json(Map.of("error", "username already exists"), Status.CONFLICT);
            }
            return status(Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { //if JSON parsing fails, any other unexpected thing during parsing happens: 400 Bad Request
            return json(Map.of("error", "invalid json"), Status.BAD_REQUEST);
        }
    }

    private Response login(Request req) {
        try {
            LoginDto dto = readJson(req, LoginDto.class);
            String token = auth.login(dto.username, dto.password); //ask the service to authenticate and return the token, NULL = wrong credentials
            if (token == null) {
                return json(Map.of("error", "invalid credentials"), Status.UNAUTHORIZED);
            }
            return json(Map.of("token", token), Status.OK);
        } catch (Exception e) {
            return json(Map.of("error", "invalid json"), Status.BAD_REQUEST);
        }
    }
}

/*  Summary:
        Tiny router + handler for two endpoints:
            POST/api/users/register -> creates a user, returns 201 or 409 if username already taken
            POST/api/users/login -> checks credentials; returns 200 + token or 401 if wrong

        Uses DTOs to parse JSON, a service (AuthService) to do real work, helper methods from base Controller
        to quickly build JSON responses and set HTTP status codes.

 */


