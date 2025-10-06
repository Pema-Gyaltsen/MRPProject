package at.technikum.application.mrp.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.model.Media;
import at.technikum.application.mrp.service.AuthService;
import at.technikum.application.mrp.service.MediaService;
import at.technikum.server.http.Method;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.List;
import java.util.Map;

public class MediaController extends Controller {
    private final AuthService auth = new AuthService();
    private final MediaService media = new MediaService();

    @Override
    public Response handle(Request req) {
        String method = req.getMethod();
        String path   = req.getPath();

        // /api/media
        if (path.equals("/api/media")) {
            if (Method.GET.getVerb().equals(method))  return list();
            if (Method.POST.getVerb().equals(method)) return create(req);
            return status(Status.METHOD_NOT_ALLOWED);
        }

        // /api/media/{id}
        if (path.startsWith("/api/media/")) {
            String idPart = path.substring("/api/media/".length());
            int id;
            try { id = Integer.parseInt(idPart); }
            catch (NumberFormatException e) { return json(Map.of("error","bad id"), Status.BAD_REQUEST); }

            if (Method.GET.getVerb().equals(method))    return getById(id);
            if (Method.PUT.getVerb().equals(method))    return update(req, id);
            if (Method.DELETE.getVerb().equals(method)) return delete(req, id);
            return status(Status.METHOD_NOT_ALLOWED);
        }

        return status(Status.NOT_FOUND);
    }

    private Integer userIdFromBearer(String header) {
        if (header == null || header.isBlank()) return null;
        return auth.validateToken(header);
    }

    private Response list() {
        List<Media> all = media.all();
        return json(all, Status.OK);
    }

    private Response create(Request req) {
        Integer uid = userIdFromBearer(req.getHeader("Authorization"));
        if (uid == null) return json(Map.of("error","auth required"), Status.UNAUTHORIZED);

        try {
            Media m = readJson(req, Media.class);
            var saved = media.create(uid, m);
            return json(saved, Status.CREATED);
        } catch (Exception e) {
            return json(Map.of("error","invalid json"), Status.BAD_REQUEST);
        }
    }

    private Response getById(int id) {
        Media m = media.get(id);
        if (m == null) return json(Map.of("error","not found"), Status.NOT_FOUND);
        return json(m, Status.OK);
    }

    private Response update(Request req, int id) {
        Integer uid = userIdFromBearer(req.getHeader("Authorization"));
        if (uid == null) return json(Map.of("error","auth required"), Status.UNAUTHORIZED);

        try {
            Media patch = readJson(req, Media.class);
            patch.id = id;
            var updated = media.update(uid, patch);
            if (updated == null) return json(Map.of("error","not owner or not found"), Status.FORBIDDEN);
            return json(updated, Status.OK);
        } catch (Exception e) {
            return json(Map.of("error","invalid json"), Status.BAD_REQUEST);
        }
    }

    private Response delete(Request req, int id) {
        Integer uid = userIdFromBearer(req.getHeader("Authorization"));
        if (uid == null) return json(Map.of("error","auth required"), Status.UNAUTHORIZED);

        boolean ok = media.delete(uid, id);
        if (!ok) return json(Map.of("error","not owner or not found"), Status.FORBIDDEN);
        return status(Status.NO_CONTENT);
    }
}
