package at.technikum.server.http;

import com.sun.net.httpserver.HttpExchange;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class Request {
    private final HttpExchange ex;
    public Request(HttpExchange ex){ this.ex = ex; }

    public String method(){ return ex.getRequestMethod(); }
    public URI uri(){ return ex.getRequestURI(); }
    public String path(){ return uri().getPath(); }
    public Map<String,String> query(){
        Map<String,String> map=new HashMap<>();
        String q = uri().getQuery();
        if(q==null) return map;
        for(String p : q.split("&")){
            String[] kv=p.split("=",2);
            map.put(kv[0], kv.length>1? kv[1] : "");
        }
        return map;
    }
    public String header(String name){ return ex.getRequestHeaders().getFirst(name); }
    public InputStream body(){ return ex.getRequestBody(); }
    public HttpExchange raw(){ return ex; }
}
