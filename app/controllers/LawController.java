package controllers;

import java.util.Objects;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.LawMetadata;
import services.TableOfContents;
import utils.DateParam;

public class LawController extends Controller {

    private final TableOfContents appToc;

    @Inject
    public LawController( final TableOfContents appToc ) {
        this.appToc = appToc;
    }

    public Result index( String id) {
        final LawMetadata law = appToc.law( id );
        if( law.getIsoVersions().isEmpty() ) {
            return notFound( String.format( "No diffs found for law: %s", id ) );
        }

        final Pair<String, String> head = law.getIsoVersions().get( 0 );
        return renderVersion( id, law, head.getKey(), head.getValue() );
    }

    Result renderVersion(String id, LawMetadata law, String currVer, String diffVer) {
        Html diffContent = Html.apply( law.diffContent( law.diffFileFor( diffVer ) ) );
        return ok( views.html.law.render( id, law, currVer, diffVer, diffContent ) );
    }

    public Result version( String id, String ver) {
        final LawMetadata law = Objects.requireNonNull( appToc.law( id ), "law [" + id + "] not found" );
        String currVer = DateParam.isoToEur(ver);
        String diffVer = DateParam.eurToIso(currVer);
        return renderVersion( id, law, currVer, diffVer );
    }

    public Result rawVersion( String id, String ver) {
        final LawMetadata law = appToc.law( id );
        String currVer = DateParam.isoToEur(ver);
        String diffVer = DateParam.eurToIso(currVer);
        String diffContent = law.diffContent( law.diffFileFor( diffVer ) );

        return ok(Html.apply( diffContent ));
    }

}
