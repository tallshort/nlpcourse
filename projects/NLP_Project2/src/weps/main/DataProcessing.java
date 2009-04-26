package weps.main;

import weps.ClutoRawDataExtractor;

public class DataProcessing {
    public static void main(String[] args) throws Exception {
        buildDataForCluto();
    }

    private static void buildDataForCluto() throws Exception {
        ClutoRawDataExtractor extractor = new ClutoRawDataExtractor();
        // extractor.setTargetPerson("Alvin_Cooper");
        extractor.setTargetDir("clutoRawData");
        extractor.setBodyDir("test_webpages_bodies_version_1");
        extractor.setUrlDir("url_data");
        extractor.extractContents();
    }
}
