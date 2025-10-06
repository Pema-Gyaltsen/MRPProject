package at.technikum.server.http;
import java.util.*;
public class Request {
    private Method method;
    private String path;
    private String query;
    private String body;
    private final Map<String,String> headers = new HashMap<>();
    public String getMethod(){ return method.getVerb(); }
    public void setMethod(Method method){ this.method = method; }
    public String getPath(){ return path; }
    public void setPath(String path){ this.path = path; }
    public String getQuery(){ return query; }
    public void setQuery(String query){ this.query = query; }
    public String getBody(){ return body; }              // <-- String body (needed by readJson)
    public void setBody(String body){ this.body = body; }
    public String getHeader(String name){ return headers.get(name); }
    public Map<String,String> getHeaders(){ return Collections.unmodifiableMap(headers); }
    public void setHeader(String name,String value){ if(value!=null) headers.put(name,value); }
}
