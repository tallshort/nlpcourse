package weps.cluster.impl;

import java.util.List;

import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.cluster.impl.TextKMeansClusterer;

public class WebPeopleKMeansClusterer extends TextKMeansClusterer {

    public WebPeopleKMeansClusterer(List<TextDataItem> textDataSet,
            int numClusters) {
        super(textDataSet, numClusters);
    }

    @Override
    protected TextCluster createTextCluster(int clusterId) {
        return new WebPeopleCluster(clusterId);
    }

}
