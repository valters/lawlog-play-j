package services;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import play.Logger;
import play.libs.Json;
import utils.FileReader;

public class JsonTableOfContents implements TableOfContents {

    protected JsonNode json = FileReader.INSTANCE.readJson( LikumiDb.TocJsonAsset ).findPath( LikumiDb.JsonRootKey );

    private List<String> keys;


    @Override
    public List<String> keys() {

        if( keys == null ) {
            List<String> k = Lists.newArrayList();
            final Iterator<Map.Entry<String, JsonNode>> it = json.fields();
            while( it.hasNext() ) {
                final Map.Entry<String, JsonNode> e = it.next();
                k.add( e.getKey() );
            }

            keys = k;
        }

        return keys;
    }

    private Map<String, LawMetadataJson> laws = keys().stream().map( key -> Pair.of(key, asLaw(key) ) ).collect( Collectors.toMap( Pair::getKey, Pair::getValue ) );

    public LawMetadataJson asLaw( final String key ) {
        return Json.fromJson( json.get( key ), LawMetadataJson.class );
    }

    private Map<String, LawMetadata> lawCache = new ConcurrentHashMap<>();

    @Override
    public LawMetadata law( final String key ) {
        return lawCache.computeIfAbsent( key,  k -> new LawMetadata( key, laws.get( key ), versions( key ) ) );
    }

    private List<String> versions( final String key ) {
        return versionsMap.computeIfAbsent( key, this::readVersions );
    }

    private Map<String, List<String>> versionsMap = new ConcurrentHashMap<>();

    /** @return empty list if error occurred reading file */
    List<String> readVersions( String key ) {
        String versionFile = LikumiDb.VersionsRoot+"/"+key+".ver";
        try {
            Logger.info("Reading {}", versionFile );
            return FileReader.INSTANCE.readLines( versionFile );
        }
        catch(Exception e) {
            Logger.error( "Failed to read {}: ", versionFile, e );
            return Collections.emptyList();
        }
    }

    @Override
    public String pamatlikums() {
        return laws.get( LikumiDb.Pamatlikums ).desc;
    }
}
