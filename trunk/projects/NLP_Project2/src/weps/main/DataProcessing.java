package weps.main;

import weps.ClutoClusterXMLGenerator;
import weps.ClutoRawDataExtractor;

public class DataProcessing {
    public static void main(String[] args) throws Exception {
        // buildDataForCluto();
        gererateClusterXMLs();
    }

    private static void buildDataForCluto() throws Exception {
        ClutoRawDataExtractor extractor = new ClutoRawDataExtractor();
        extractor.setTargetPerson("William_Dickson");
        extractor.setTargetDir("clutoRawData");//写到哪里
        extractor.setBodyDir("test_webpages_bodies_version_1");//源文件
        extractor.setUrlDir("url_data");  //URL文件
        extractor.extractContents();
    }
    
    private static void gererateClusterXMLs() throws Exception {
        ClutoClusterXMLGenerator generator = new ClutoClusterXMLGenerator();
        generator.setClutoClusterResultsDir("cluto_result2/matrixFile");
        // generator.setTargetPerson("Alvin_Cooper");
        generator.setXmlResultsDir("cluto_xml_result/cluto_xml_result2");
        generator.setClusterNum(40);
        generator.gererateClusterXMLs();
    }
}
