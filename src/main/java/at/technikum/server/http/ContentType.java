package at.technikum.server.http;

public enum ContentType {
    TEXT_PLAIN("text/plain"), //inside TEXT_PLAIN object, it does mimeType = "text/plain"
    TEXT_HTML("text/html"),
    APPLICATION_JSON("application/json");
    private final String mimeType;
    ContentType(String mimeType) { this.mimeType = mimeType; } //enum constructor: implicitly private (enum cons, cant be public)
    public String getMimeType() { return mimeType; }
}
//MIME type: multipurpsoe internet Mail Extensions
//Enums can have fields, constructors and methods
//each enum constants are instances of the enum Type, the constructor initializes the fields of each instance
    //
