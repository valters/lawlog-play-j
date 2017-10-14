package services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private List<Pair<String,String>> isoVersions;

    /**
     * We convert full euro date into abbreviated iso date, to keep both together because we need them both when printing the version list.
     *  sample entry: ('01.02.2017', '20170201')
     */
    public List<Pair<String,String>> getIsoVersions() {
        if( isoVersions == null ) {
            isoVersions = Optional.ofNullable(versions).
                    map( v -> v.stream().map( s -> Pair.of ( s, DateParam.eurToIso( s ) ) ).collect( Collectors.toList()) )
                    .orElse( Collections.emptyList() );
        }


        return isoVersions;
    }

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
