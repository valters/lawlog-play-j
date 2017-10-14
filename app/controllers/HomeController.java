package controllers;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.mvc.*;

import services.TableOfContents;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {

    private final TableOfContents appToc;

    @Inject
    public HomeController( final TableOfContents appToc ) {
        this.appToc = appToc;
    }

    public Result index() {
        return ok(index.render( appToc ));
    }

    public Result sandbox() {
        return ok("");
    }
}
