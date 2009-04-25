package weps.cluster.impl;

import java.util.ArrayList;

import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.cluster.impl.TextClusterImpl;

public class WebPeopleCluster extends TextClusterImpl {

    public WebPeopleCluster(int clusterId) {
        super(clusterId);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id=" + this.getClusterId());
        for (TextDataItem item : this.getDataItems()) {
            // TODO the content of the item
        }
        return sb.toString();
    }

}
