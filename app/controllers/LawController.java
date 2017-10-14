package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class LawController extends Controller {
    public Result index( String id) {
        return ok(id);
    }

    public Result version( String id, String ver) {
        return ok(id + ver);
    }

    public Result rawVersion( String id, String ver) {
        return ok(id + ver);
    }

}
