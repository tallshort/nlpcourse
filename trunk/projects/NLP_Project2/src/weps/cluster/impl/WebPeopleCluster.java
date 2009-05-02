package weps.cluster.impl;

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
            WebPeopleDocEntry docEntry = (WebPeopleDocEntry)item.getData();
            sb.append("\n\tName=" + docEntry.getName());
            sb.append("\tRank=" + docEntry.getRank());
        }
        return sb.toString();
    }

}
