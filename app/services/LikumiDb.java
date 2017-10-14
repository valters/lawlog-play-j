package services;

public class LikumiDb {
    /** actual content is provided by likumi-db project: https://github.com/valters/likumi-db */
    public static final String AssetRoot = "app/likumi-db";

    public static final String TocJsonAsset = AssetRoot + "/toc.json";

    public static final String JsonRootKey = "likumi";
    public static final String Pamatlikums = "satversme";

    /** for each law, known versions list is stored here, in .ver file */
    public static final String VersionsRoot = AssetRoot + "/version";

    public static final String DiffsRoot = AssetRoot + "/diff";
    public static final String DiffReportSuffix = ".html.txt-diff.xml";

    /** we take content from this root node */
    public static final String DiffElementXpath = "/diffreport/diff";

}
