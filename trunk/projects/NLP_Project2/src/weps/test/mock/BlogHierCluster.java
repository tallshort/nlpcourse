package weps.test.mock;

import ci.cluster.TextDataItem;
import ci.cluster.hiercluster.HierCluster;
import ci.cluster.impl.HierClusterImpl;

public class BlogHierCluster extends HierClusterImpl {

    public BlogHierCluster(int clusterId, HierCluster child1,
            HierCluster child2, double similarity, TextDataItem dataItem) {
        super(clusterId, child1, child2, similarity, dataItem);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String blogDetails = this.getBlogDetails();
        if (blogDetails != null) {
            sb.append("Id=" + this.getClusterId() 
                    + " " + blogDetails);
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

    private String getBlogDetails() {
        if ((this.getDataItems() != null) && (this.getDataItems().size() > 0)) {
            TextDataItem textDataItem = this.getDataItems().get(0);
            if (textDataItem != null) {
                BlogEntry blogEntry = (BlogEntry)textDataItem.getData();
                return blogEntry.getTitle();
            }
        }
        return null;
    }
    
}
