package weps.main;

import weps.ClutoRawDataExtractor;

public class DataProcessing {
    public static void main(String[] args) throws Exception {
        buildDataForCluto();
    }

    private static void buildDataForCluto() throws Exception {
        ClutoRawDataExtractor extractor = new ClutoRawDataExtractor();
        // extractor.setTargetPerson("Alvin_Cooper");
        extractor.setTargetDir("clutoRawData");//写到哪里
        extractor.setBodyDir("test_webpages_bodies_version_1");//源文件
        extractor.setUrlDir("url_data");  //URL文件
        extractor.extractContents();
    }
}
