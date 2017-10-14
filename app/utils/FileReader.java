package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.valters.xml.TransformToString;
import io.github.valters.xml.XPathUtils;
import io.github.valters.xml.XmlDomUtils;
import play.libs.Json;

public class FileReader {
    public static final FileReader INSTANCE = new FileReader();

    protected TransformToString transform = new TransformToString();
    protected XPathUtils xpath = new XPathUtils();

    public boolean exists( String file ) {
        return new File( file ).exists();
    }

    public Document readXml( String file ) {
        try {
            InputStream stream = new java.io.FileInputStream( new File( file ) );
            Document xml = parseXml( stream );
            return xml;
        }
        catch(Exception e) {
            throw new RuntimeException( e );
        }
    }

    private Document parseXml( InputStream in ) throws IOException, ParserConfigurationException, SAXException {
        try {
            return XmlDomUtils.documentBuilder().parse( in );
        }
        finally {
            in.close();
        }
    }

    public String nodeText( Document document, String xpathExpr ) {
        String text = transform.nodesToString( xpath.findNode( document, xpathExpr ).getChildNodes() );
        return text;
    }

    public JsonNode readJson( String fileAsset ) {
        try {
            InputStream stream = new FileInputStream( new File( fileAsset ) );
            return parseJson( stream );
        }
        catch(Exception e) {
            throw new RuntimeException( e );
        }
    }

    /** load json file into memory */
    private JsonNode parseJson( InputStream in ) throws IOException {
        try {
            return Json.parse( in );
        }
        finally {
            in.close();
        }
    }

    public List<String> readLines( String file ) {
        try {
            try ( final Stream<String> stream = Files.lines( Paths.get( file ) ) ) {
                return stream.collect( Collectors.toList() );
            }
        }
        catch( Exception e ) {
            throw new RuntimeException( e );
        }
    }

}
