package weps.main;

import java.util.List;
import java.util.Map;

import weps.AbstractExtractor;
import weps.ClusterNumberExtractor;
import weps.cluster.impl.WebPeopleDataSetCreator;
import weps.cluster.impl.WebPeopleKMeansClusterer;
import ci.cluster.Clusterer;
import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;

public class RunCluster {

    public static void main(String[] args) throws Exception {
        String dataSetDir = "merged_data_conll_all";
        List<String> peopleNameList = getWebPeopleNameList();
        
        ClusterNumberExtractor extractor = new ClusterNumberExtractor();
        extractor.setDatasetDir("weps2007/test");
        extractor.extractContentsPerName();
        Map<String, Integer> clusterNumberMap = extractor.getClusterNumberMap();
        
        for (String name : peopleNameList) {
            WebPeopleDataSetCreator dataSetCreator = new WebPeopleDataSetCreator();
            dataSetCreator.setDataSetDir(dataSetDir);
            dataSetCreator.setTargetPerson(name);
            List<TextDataItem> webPeopleData = dataSetCreator.createLearningData();
            int clusterNum = clusterNumberMap.get(name);
            System.out.println("" + clusterNum);
            Clusterer clusterer = createWebPeopleKMeansClusterer(webPeopleData, clusterNum, name);
            List<TextCluster> clusters = clusterer.cluster();
            System.out.println(clusterer);
        }
        
//        Clusterer clusterer = new TextHierarchialClusterer(webPeopleData) {
//            @Override
//            protected HierCluster createHierCluster(int clusterId,
//                    HierCluster c1, HierCluster c2, double similarity,
//                    TextDataItem textDataItem) {
//                return new WebPeopleHierCluster(clusterId, c1, c2, similarity, textDataItem);
//            }        
//        };
    }
    
    private static Clusterer createWebPeopleKMeansClusterer(
            List<TextDataItem> webPeopleData, int clusterNum, String targetPerson) {
        WebPeopleKMeansClusterer clusterer
            = new WebPeopleKMeansClusterer(webPeopleData, clusterNum);
        clusterer.setMaxIterations(200);
        clusterer.setTargetDir("cluster_results");
        clusterer.setTargetPerson(targetPerson);
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

}
