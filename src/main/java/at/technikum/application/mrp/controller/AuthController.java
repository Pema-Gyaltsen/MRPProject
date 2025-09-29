package at.technikum.application.mrp.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.common.Json;
import at.technikum.server.http.Status;
import at.technikum.application.mrp.service.AuthService;

import java.io.IOException;
import java.util.Map;

public class AuthController extends Controller {
    private final AuthService auth = new AuthService();

    public void register(){
        try {
            Map<?,?> dto = Json.MAPPER.readValue(req.body(), Map.class);
            String username = (String) dto.get("username");
            String password = (String) dto.get("password");
            if(username==null || password==null){
                res.json(Status.BAD_REQUEST, "{\"error\":\"username & password required\"}");
                return;
            }
            var user = auth.register(username, password);
            var out = Map.of("id", user.id, "username", user.username);
            res.json(Status.CREATED, Json.MAPPER.writeValueAsString(out));
        } catch (IOException e) {
            res.json(Status.BAD_REQUEST, "{\"error\":\"invalid json\"}");
        } catch (IllegalStateException e){
            if("USERNAME_TAKEN".equals(e.getMessage())){
                res.json(Status.CONFLICT, "{\"error\":\"username already exists\"}");
            } else throw e;
        }
    }

    public void login(){
        try {
            Map<?,?> dto = Json.MAPPER.readValue(req.body(), Map.class);
            String username = (String) dto.get("username");
            String password = (String) dto.get("password");
            String token = auth.login(username, password);
            if(token == null){
                res.json(Status.UNAUTHORIZED, "{\"error\":\"invalid credentials\"}");
                return;
            }
            res.json(Status.OK, Json.MAPPER.writeValueAsString(Map.of("token", token)));
        } catch (IOException e){
            res.json(Status.BAD_REQUEST, "{\"error\":\"invalid json\"}");
        }
    }
}
