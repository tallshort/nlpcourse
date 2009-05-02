package weps.cluster.impl;

import ci.cluster.TextDataItem;
import ci.cluster.hiercluster.HierCluster;
import ci.cluster.impl.HierClusterImpl;

public class WebPeopleHierCluster extends HierClusterImpl {

    public WebPeopleHierCluster(int clusterId, HierCluster child1,
            HierCluster child2, double similarity, TextDataItem dataItem) {
        super(clusterId, child1, child2, similarity, dataItem);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String WebPeopleDetails = this.getWebPeopleDetails();
        if (WebPeopleDetails != null) {
            sb.append("Id=" + this.getClusterId() 
                    + " " + WebPeopleDetails);
        } else {
            sb.append("Id=" + this.getClusterId() 
                    + " similarity=" + this.getSimilarity());
        }
        if (this.getChild1() != null) {
            sb.append( " C1=" + this.getChild1().getClusterId());
        }
        if (this.getChild2() != null) {
            sb.append( " C2=" + this.getChild2().getClusterId());
        }
        return sb.toString();
    }

    private String getWebPeopleDetails() {
        if ((this.getDataItems() != null) && (this.getDataItems().size() > 0)) {
            TextDataItem textDataItem = this.getDataItems().get(0);
            if (textDataItem != null) {
                WebPeopleDocEntry docEntry = (WebPeopleDocEntry)textDataItem.getData();
                return docEntry.getName() + " " + docEntry.getRank();
            }
        }
        return null;
    }
    
}
