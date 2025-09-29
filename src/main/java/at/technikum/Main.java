package at.technikum;

import at.technikum.application.common.Router;
import at.technikum.application.common.Route;
import at.technikum.application.mrp.controller.AuthController;
import at.technikum.application.mrp.controller.MediaController;
import at.technikum.server.Server;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import com.sun.net.httpserver.HttpExchange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Router router = new Router();

        // --- Controllers
        AuthController auth = new AuthController();
        MediaController media = new MediaController();

        // --- Routes (Auth)
        router.add(new Route("POST", "^/api/users/register$", (HttpExchange ex) -> {
            Request req = new Request(ex); Response res = new Response(ex);
            auth.bind(req,res); auth.register();
        }));
        router.add(new Route("POST", "^/api/users/login$", (HttpExchange ex) -> {
            Request req = new Request(ex); Response res = new Response(ex);
            auth.bind(req,res); auth.login();
        }));

        // --- Routes (Media)
        router.add(new Route("GET", "^/api/media$", (HttpExchange ex) -> {
            Request req = new Request(ex); Response res = new Response(ex);
            media.bind(req,res); media.list();
        }));
        router.add(new Route("POST", "^/api/media$", (HttpExchange ex) -> {
            Request req = new Request(ex); Response res = new Response(ex);
            media.bind(req,res); media.create();
        }));
        router.add(new Route("GET", "^/api/media/(\\d+)$", (HttpExchange ex) -> {
            Request req = new Request(ex); Response res = new Response(ex);
            Matcher m = Pattern.compile("^/api/media/(\\d+)$").matcher(req.path());
            m.find();
            media.bind(req,res); media.getById(Integer.parseInt(m.group(1)));
        }));
        router.add(new Route("PUT", "^/api/media/(\\d+)$", (HttpExchange ex) -> {
            Request req = new Request(ex); Response res = new Response(ex);
            Matcher m = Pattern.compile("^/api/media/(\\d+)$").matcher(req.path());
            m.find();
            media.bind(req,res); media.update(Integer.parseInt(m.group(1)));
        }));
        router.add(new Route("DELETE", "^/api/media/(\\d+)$", (HttpExchange ex) -> {
            Request req = new Request(ex); Response res = new Response(ex);
            Matcher m = Pattern.compile("^/api/media/(\\d+)$").matcher(req.path());
            m.find();
            media.bind(req,res); media.delete(Integer.parseInt(m.group(1)));
        }));

        new Server(router).start();
        System.out.println("MRP server running at http://localhost:8080");
    }
}
