package weps.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import weps.ClutoClusterXMLGenerator;
import weps.NamedEntitiesExtractor;
import weps.RawDataMerger;
import weps.XMLDescriptionExtractor;

public class DataProcessing {
    
    private String datasetDir = ".";
    private String targePerson = "";
    
    public static void main(String[] args) throws Exception {
        DataProcessing dp = new DataProcessing();
        dp.setDatasetDir("F:/Work/NLP_Project2/weps2007/test");
        // dp.setTargePerson("Alvin_Cooper");
        // dp.extractXMLDescriptions();
        //dp.extractNamedEntities(); 
        //dp.mergeRawData();
        dp.runScorer();
    }
    
    private void mergeRawData() throws Exception {
        RawDataMerger merger = new RawDataMerger();
        merger.setDatasetDir(this.datasetDir);
        merger.addMergeDir("xml_descriptions");
        merger.addMergeDir("named_entities_data");
        merger.setTargetDir("merged_data");
        merger.setTargetPerson(this.targePerson);
        merger.extractContentsPerDoc();
    }
    
    private void extractXMLDescriptions() throws Exception {
        XMLDescriptionExtractor extractor = new XMLDescriptionExtractor();
        extractor.setDatasetDir(this.datasetDir);
        extractor.setTargetPerson(this.targePerson);
        extractor.setTargetDir("xml_descriptions");
        extractor.extractContentsPerName();
    }

    private void extractNamedEntities() throws Exception {
        NamedEntitiesExtractor extractor = new NamedEntitiesExtractor();
        extractor.setDatasetDir(this.datasetDir);
        extractor.setTargetPerson(this.targePerson);
        extractor.setTargetDir("named_entities_data"); // Ð´µ½ÄÄÀï
        extractor.addDataDir("test_webpages_bodies_version_1");
        extractor.addDataDir("xml_descriptions");
        extractor.extractContentsPerDoc();
    }

    private void gererateClusterXMLs() throws Exception {
        ClutoClusterXMLGenerator generator = new ClutoClusterXMLGenerator();
        generator.setDatasetDir(this.datasetDir);
        generator.setClutoClusterResultsDir("cluto_result2/matrixFile");
        // generator.setTargetPerson(this.targetPerson);
        generator.setXmlResultsDir("cluto_xml_result/cluto_xml_result2");
        generator.setClusterNum(40);
        generator.gererateClusterXMLs();
    }
    
    private void runScorer() {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        try {
            p = rn.exec("python asset/scorer_1.1/scorer.py");
            BufferedReader in = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String strProc = null;
            while ((strProc = in.readLine()) != null) {
                System.out.println(strProc);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDatasetDir() {
        return datasetDir;
    }

    public void setDatasetDir(String datasetDir) {
        this.datasetDir = datasetDir;
    }

    public String getTargePerson() {
        return targePerson;
    }

    public void setTargePerson(String targePerson) {
        this.targePerson = targePerson;
    }
}
