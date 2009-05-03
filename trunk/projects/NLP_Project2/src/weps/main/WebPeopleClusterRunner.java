package weps.main;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import weps.cluster.impl.WebPeopleDataSetCreator;
import weps.cluster.impl.WebPeopleDocEntry;
import weps.cluster.impl.WebPeopleHierClusterer;
import weps.cluster.impl.WebPeopleKMeansClusterer;
import weps.extractor.AbstractExtractor;
import weps.extractor.ClusterPriorExtractor;
import weps.util.TextFile;
import ci.cluster.Clusterer;
import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;

public class WebPeopleClusterRunner {
    
    private String dataSetDir = "merged_data";
    private String targetDir = ".";
    
    public static void main(String[] args) throws Exception {
        WebPeopleClusterRunner runner = new WebPeopleClusterRunner();
        runner.setDataSetDir("merged_data_conll_all");
        runner.setTargetDir("cluster_results");
        runner.runClustering();
    }

    private void runClustering() throws Exception {            
        ClusterPriorExtractor priorExtractor = new ClusterPriorExtractor();
        priorExtractor.setDatasetDir("weps2007/test");
        priorExtractor.extractContentsPerName();
        Map<String, Integer> clusterNumberMap = priorExtractor.getClusterNumberMap();
        Map<String, HashSet<String>> discardedMap = priorExtractor.getDiscardedMap();
        
        List<String> peopleNameList = getWebPeopleNameList();
        for (String name : peopleNameList) {
            // Create learning data
            WebPeopleDataSetCreator dataSetCreator = new WebPeopleDataSetCreator();
            dataSetCreator.setDataSetDir(this.dataSetDir);
            dataSetCreator.setTargetPerson(name);
            List<TextDataItem> webPeopleData = dataSetCreator.createLearningData();
            // Clustering
            int clusterNum = clusterNumberMap.get(name);
            
            Clusterer clusterer = createWebPeopleHierClusterer(webPeopleData, clusterNum);
            // Clusterer clusterer = createWebPeopleKMeansClusterer(webPeopleData, clusterNum);
            
            List<TextCluster> clusters = clusterer.cluster();
            System.out.println(clusterer);
            // Gold standard XML cluster files
            generateXMLClusterResults(name, clusters, discardedMap);
        }
    }

    private void generateXMLClusterResults(String targetPerson, 
            List<TextCluster> clusters, Map<String, HashSet<String>> discardedMap) {
        Element root = new Element("clustering");
        Element discarded = new Element("discarded");
        for (TextCluster cluster : clusters) {
            Element clusterElement = new Element("entity");
            String clusterId = "" + cluster.getClusterId();            
            clusterElement.addAttribute(new Attribute("id", clusterId));
            
            // Add doc elements
            for (TextDataItem dataItem : cluster.getDataItems()) {
                WebPeopleDocEntry docEntry = (WebPeopleDocEntry)dataItem.getData();
                Element doc = new Element("doc");
                String rank = "" + Integer.parseInt(docEntry.getRank());
                doc.addAttribute(new Attribute("rank", rank));
                // Handle the discarded docs
                if (discardedMap.get(docEntry.getName()).contains(rank)) {
                    discarded.appendChild(doc);
                } else {
                    clusterElement.appendChild(doc);
                }
            }
            // Discard the empty entity element
            if (clusterElement.getChildCount() != 0) {
                root.appendChild(clusterElement);
            }
        }
        // Discard the empty discarded element
        if (discarded.getChildCount() != 0) {
            root.appendChild(discarded);
        }
        Document doc = new Document(root);
        String clusterResultPath
            = this.targetDir + "/" + targetPerson + ".clust.xml";
        TextFile.write(clusterResultPath, doc.toXML());
    }
 
    private Clusterer createWebPeopleHierClusterer(
            List<TextDataItem> webPeopleData, int clusterNum) {
        WebPeopleHierClusterer clusterer
            = new WebPeopleHierClusterer(webPeopleData, clusterNum);
        return clusterer;
    }
    
    private Clusterer createWebPeopleKMeansClusterer(
            List<TextDataItem> webPeopleData, int clusterNum) {
        WebPeopleKMeansClusterer clusterer
            = new WebPeopleKMeansClusterer(webPeopleData, clusterNum);
        clusterer.setMaxIterations(200);
        return clusterer;
    }

    private static List<String> getWebPeopleNameList() {
        AbstractExtractor extractor = new AbstractExtractor() {
            @Override
            protected void extractContent(String absolutePath, String name,
                    String rank) {
            }
        };
        extractor.setDatasetDir("weps2007/test");
        return extractor.getPeopleNameList();
    }

    public String getDataSetDir() {
        return dataSetDir;
    }

    public void setDataSetDir(String dataSetDir) {
        this.dataSetDir = dataSetDir;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

}
