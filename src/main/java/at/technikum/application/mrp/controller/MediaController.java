package at.technikum.application.mrp.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.common.Json;
import at.technikum.application.mrp.model.Media;
import at.technikum.application.mrp.service.AuthService;
import at.technikum.application.mrp.service.MediaService;
import at.technikum.server.http.Status;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MediaController extends Controller {
    private final AuthService auth = new AuthService();
    private final MediaService media = new MediaService();

    private Integer userIdFromBearer(){
        String bearer = req.header("Authorization");
        return auth.validateToken(bearer);
    }

    public void list(){
        List<Media> all = media.all();
        try { res.json(Status.OK, Json.MAPPER.writeValueAsString(all)); }
        catch (IOException e){ res.json(Status.INTERNAL_SERVER_ERROR, "{\"error\":\"json\"}"); }
    }

    /*public void create(){
        Integer uid = userIdFromBearer();
        if(uid == null){ res.json(Status.UNAUTHORIZED, "{\"error\":\"auth required\"}"); return; }

        try {
            Media m = Json.MAPPER.readValue(req.body(), Media.class);
            var saved = media.create(uid, m);
            res.json(Status.CREATED, Json.MAPPER.writeValueAsString(saved));
        } catch (IOException e){ res.json(Status.BAD_REQUEST, "{\"error\":\"invalid json\"}"); }
    }*/

    public void create() {
        Integer uid = userIdFromBearer();
        if(uid == null){
            res.json(Status.UNAUTHORIZED, "{\"error\":\"auth required\"}");
            return;
        }

        try {
            Media m = Json.MAPPER.readValue(req.body(), Media.class);
            var saved = media.create(uid, m);
            res.json(Status.CREATED, Json.MAPPER.writeValueAsString(saved));
        } catch (com.fasterxml.jackson.core.JsonParseException e) {
            e.printStackTrace();
            res.json(Status.BAD_REQUEST, "{\"error\":\"malformed JSON syntax\"}");
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            e.printStackTrace();
            res.json(Status.BAD_REQUEST, "{\"error\":\"field mapping issue\"}");
        } catch (IOException e) {
            e.printStackTrace();
            res.json(Status.BAD_REQUEST, "{\"error\":\"invalid json\"}");
        } catch (Exception e) {
            e.printStackTrace();
            res.json(Status.INTERNAL_SERVER_ERROR, "{\"error\":\"server error\"}");
        }
    }


    public void getById(int id){
        Media m = media.get(id);
        if(m == null){ res.json(Status.NOT_FOUND, "{\"error\":\"not found\"}"); return; }
        try { res.json(Status.OK, Json.MAPPER.writeValueAsString(m)); }
        catch (IOException e){ res.json(Status.INTERNAL_SERVER_ERROR, "{\"error\":\"json\"}"); }
    }

    public void update(int id){
        Integer uid = userIdFromBearer();
        if(uid == null){ res.json(Status.UNAUTHORIZED, "{\"error\":\"auth required\"}"); return; }

        try {
            Media patch = Json.MAPPER.readValue(req.body(), Media.class);
            patch.id = id;
            var updated = media.update(uid, patch);
            if(updated == null){
                res.json(Status.FORBIDDEN, "{\"error\":\"not owner or not found\"}");
                return;
            }
            res.json(Status.OK, Json.MAPPER.writeValueAsString(updated));
        } catch (IOException e){ res.json(Status.BAD_REQUEST, "{\"error\":\"invalid json\"}"); }
    }

    public void delete(int id){
        Integer uid = userIdFromBearer();
        if(uid == null){ res.json(Status.UNAUTHORIZED, "{\"error\":\"auth required\"}"); return; }

        boolean ok = media.delete(uid, id);
        if(!ok){ res.json(Status.FORBIDDEN, "{\"error\":\"not owner or not found\"}"); return; }
        res.json(Status.NO_CONTENT, "");
    }
}
