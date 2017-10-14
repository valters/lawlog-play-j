package services;

import java.util.List;

/** component is injected into a controller */
public interface TableOfContents {

    /** list of law keys available */
    List<String> keys();

    /** get full metadata */
    LawMetadata law(String key);

    /** for testing */
    String pamatlikums();

}
