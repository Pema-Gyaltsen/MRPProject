package at.technikum.server.http;
public class Response {
    private Status status;
    private ContentType contentType;
    private String body;
    public Response() {}
    public Response(Status s, ContentType ct, String b){ this.status=s; this.contentType=ct; this.body=b; }
    public void setStatus(Status status){ this.status=status; }
    public int getStatusCode(){ return status.getCode(); }
    public String getStatusMessage(){ return status.getMessage(); }
    public String getContentType(){ return (contentType!=null?contentType:ContentType.TEXT_PLAIN).getMimeType(); }
    public void setContentType(ContentType c){ this.contentType=c; }
    public String getBody(){ return body!=null? body : ""; }     // null-safe
    public void setBody(String body){ this.body=body; }
}
