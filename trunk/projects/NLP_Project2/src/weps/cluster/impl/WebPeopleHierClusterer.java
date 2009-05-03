package weps.cluster.impl;

import java.util.List;

import ci.cluster.TextDataItem;
import ci.cluster.hiercluster.HierCluster;
import ci.cluster.impl.TextHierarchialClusterer;

public class WebPeopleHierClusterer extends TextHierarchialClusterer {
    
    public WebPeopleHierClusterer(List<TextDataItem> textDataSet,
            int numOfRootClusters) {
        super(textDataSet, numOfRootClusters);
    }

    @Override
    protected HierCluster createHierCluster(int clusterId, HierCluster c1,
            HierCluster c2, double similarity, TextDataItem textDataItem) {
        return new WebPeopleHierCluster(clusterId, c1, c2, similarity, textDataItem);
    }


}
