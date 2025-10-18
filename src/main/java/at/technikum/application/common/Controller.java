// at/technikum/application/common/Controller.java
package at.technikum.application.common;

import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import com.fasterxml.jackson.databind.ObjectMapper; //converts Java <-> JSON
import com.fasterxml.jackson.databind.SerializationFeature; //enum of toggles that change how "ObjectMapper" reads/write
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; //teaches Jackson how to (de)serialize java.time.* (e.g., LocalDate).

public abstract class Controller {

    // ----- JSON codec kept at the edge (controller layer) -----
    private static final ObjectMapper MAPPER = new ObjectMapper() //creates a Jackson mapper instance (the thing that converts Java objects ⟷ JSON) and assigns it to MAPPER (a single shared field).
            .registerModule(new JavaTimeModule()) //teaches ObjectMapper how to read/write LocalDate, LocalDateTime, Instant,etc, returns the same mapper
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //tells Jackson not to write dates as numeric timestamps (e.g., 1654522..), but as ISO-8601 strings (e.g., "2023-.."
//method chaining/a fluent API using dot operator: the dot calls a method on the value to its left, each method returns the same ObjectMapper instance (they return this), so one can chain the next call immediately

    //creates one shared Jackson mapper for the whole app, adds java.time support, outputs ISO-8601 strings dates/time instead of numeric timestamps


    /*
           ObjectMapper tmp = new ObjectMapper();
           tmp.registerModule(new JavaTimeModule());
           tmp.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
           private static final ObjectMapper MAPPER = tmp;

     */

    protected <T> T readJson(Request req, Class<T> type) throws Exception { //throws Exception: this method might fail, whoever calls me must handle it (try/catch) or also declare throws
       return MAPPER.readValue(req.getBody(), type); //.readValue: parses the JSON text and construct an instance of valueType (your DTO).
    } //type: the Class<T> token telling Jackson what class to create


    protected Response json(Object body, Status status) { //Object: predefined class, primitive aren't objects, int->integer
        try {
            String s = MAPPER.writeValueAsString(body); //use shared Jackson ObjectMapper to convert body -> JSON text
            Response r = new Response(); //new HTTP response object
            r.setStatus(status);
            r.setContentType(ContentType.APPLICATION_JSON); //set the response Content-Type header to JSON (your enum mas to application/json
            r.setBody(s); //put the serialized JSON into the response body
            return r; //hand the fully built JSON response back to the caller
        } catch (Exception e) { //if anything in try fails (serialization, handle it here
            return status(Status.INTERNAL_SERVER_ERROR); // here status = this.status, method defined in Controller: built and return a 500 internal server error response
        }
    }

    //overload of other json method, if one parameter, then this, if two, then above
    protected Response json(Object body) { return json(body, Status.OK); } //here return json = this.json

    // ----- entry point of server call, base class must implement actual behaviour of controller -----
    public abstract Response handle(Request request);

    //helper method to quickly build a reponse
    protected Response ok() { return status(Status.OK); }

    protected Response status(Status status) {
        return text(status.getMessage(), status);
    }

    protected Response text(String text) { return text(text, Status.OK); }

    protected Response text(String text, Status status) {
        Response response = new Response();
        response.setStatus(status);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(text);
        return response;
    }
}

/*
    Controller: abstract base class that centralizes common HTTP helpers:
        Parsing a JSON body into a Java object (readJson)
        Returning JSON/Text responses (json(...), text(...), status(...), ok())
        Forcing subclass to implement one method: handle(Request) //the endpoint logic
    HTTP server will call handle(request), inside that, controller uses the helpers to read request JSON and built a Response
 */
