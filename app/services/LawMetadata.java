package services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Document;

import utils.DateParam;
import utils.FileReader;

public class LawMetadata {
    public String key;
    public LawMetadataJson meta;
    public List<String> versions;

    public LawMetadata( final String key, final LawMetadataJson meta, final List<String> versions ) {
        this.key = key;
        this.meta = meta;
        this.versions = versions;
    }

    /**
     * convert full euro date into abbreviated iso date, keep both together because we need them to print the version list.
     *  sample entry: ('01.02.2017', '20170201')
     */
    List<Pair<String,String>> isoVersions = versions.stream().map( s -> Pair.of ( s, DateParam.eurToIso( s ) ) ).collect( Collectors.toList());

    /** @param version iso date */
    public String diffFileFor( String version ) {
        return LikumiDb.DiffsRoot+"/"+key+"/"+version + LikumiDb.DiffReportSuffix;
    }

    public String diffContent( String file ) {
        if ( ! FileReader.INSTANCE.exists( file ) ) {
            return String.format( "(sorry, '%s' is not available)", file );
        }
        else {
            return diffText( FileReader.INSTANCE.readXml( file ) );
        }
    }

    /** take just the node text */
    public String diffText( Document doc ) {
        return FileReader.INSTANCE.nodeText( doc, LikumiDb.DiffElementXpath );
    }

}
