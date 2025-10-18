package at.technikum.application.common;

public class Route {

    private final String path;

    private final Controller controller; //immutable field

    public Route(String path, Controller controller) { //constructor
        this.path = path; //this: refers to "this object's field"
        this.controller = controller;
    }

    public String getPath() {
        return path;
    } //"getter" method

    public Controller getController() {
        return controller;
    } //same
}
