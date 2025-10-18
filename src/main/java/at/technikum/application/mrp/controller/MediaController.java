package at.technikum.application.mrp.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.model.MediaEntry;
import at.technikum.application.mrp.service.AuthService;
import at.technikum.application.mrp.service.MediaService;
import at.technikum.server.http.Method;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.List;
import java.util.Map;

//concrete controller for /api/media endpoints

public class MediaController extends Controller {
    private final AuthService auth = new AuthService(); //to validate bearer tokens
    private final MediaService media = new MediaService(); // business logic: (CRUD on media)

    @Override
    public Response handle(Request req) {
        String method = req.getMethod();
        String path   = req.getPath();

        // /api/media
        if (path.equals("/api/media")) {
            if (Method.GET.getVerb().equals(method))  return list(); //GET -> list all Media
            if (Method.POST.getVerb().equals(method)) return create(req); //POST -> create a new media (auth required inside create)
            return status(Status.METHOD_NOT_ALLOWED);
        }

        // /api/media/{id}
        //if path starts with the prefix, treat the rest as the {id} Segment
        if (path.startsWith("/api/media/")) { //used for individual media
            String idPart = path.substring("/api/media/".length()); //.substring(startIndex)
            int id;
            try { id = Integer.parseInt(idPart); } //concerts the String to integer (int)
            catch (NumberFormatException e) { return json(Map.of("error","bad id"), Status.BAD_REQUEST); }

            if (Method.GET.getVerb().equals(method))    return getById(id); //fetch one by id
            if (Method.PUT.getVerb().equals(method))    return update(req, id); //update(auth&ownership enforced in service)
            if (Method.DELETE.getVerb().equals(method)) return delete(req, id); //delete(auth&ownership enforced in service)
            return status(Status.METHOD_NOT_ALLOWED);
        }

        return status(Status.NOT_FOUND); //if none route matched-> 404
    }

    private Integer userIdFromBearer(String header) {
        if (header == null || header.isBlank()) return null;
        return auth.validateToken(header); //returns userId or null if invalid/missing
    }

    //GET/api/media
    private Response list() {
        List<MediaEntry> all = media.all();
        return json(all, Status.OK);
    } //no auth needed, returns all media as JSON

    //POST/api/media
    private Response create(Request req) {
        Integer uid = userIdFromBearer(req.getHeader("Authorization"));
        if (uid == null) return json(Map.of("error","auth required"), Status.UNAUTHORIZED);

        try {
            MediaEntry m = readJson(req, MediaEntry.class); //parses body into Media object
            var saved = media.create(uid, m); //var: type reference, type inferred from RHS of =
            return json(saved, Status.CREATED);
        } catch (Exception e) {
            return json(Map.of("error","invalid json"), Status.BAD_REQUEST);
        }
    } //requires auth, if token missing/invalid -> 401

    //GET/api/media/{id}
    private Response getById(int id) {
        MediaEntry m = media.get(id);
        if (m == null) return json(Map.of("error","not found"), Status.NOT_FOUND);
        return json(m, Status.OK);
    } //fetches by id; returns 404 if no such media

    //POT/api/media/{id}
    private Response update(Request req, int id) {
        Integer uid = userIdFromBearer(req.getHeader("Authorization")); //Authorization: name of HTTP header
        if (uid == null) return json(Map.of("error","auth required"), Status.UNAUTHORIZED);

        try { //requires auth
            MediaEntry patch = readJson(req, MediaEntry.class);
            patch.id = id; //patchid: assignee
            var updated = media.update(uid, patch);
            if (updated == null) return json(Map.of("error","not owner or not found"), Status.FORBIDDEN);
            return json(updated, Status.OK);
        } catch (Exception e) {
            return json(Map.of("error","invalid json"), Status.BAD_REQUEST);
        }
    }

    //DELETE/api/media/{id}
    private Response delete(Request req, int id) {
        Integer uid = userIdFromBearer(req.getHeader("Authorization"));
        if (uid == null) return json(Map.of("error","auth required"), Status.UNAUTHORIZED);

        boolean ok = media.delete(uid, id);
        if (!ok) return json(Map.of("error","not owner or not found"), Status.FORBIDDEN);
        return status(Status.NO_CONTENT);
    }
}
/*
    Routes implemented:
    GET /api/media → list all
    POST /api/media → create (auth)
    GET /api/media/{id} → fetch one
    PUT /api/media/{id} → update (auth, owner)
    DELETE /api/media/{id} → delete (auth, owner)

    secured CRUD API for media.

 */