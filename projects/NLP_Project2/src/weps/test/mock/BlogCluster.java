package weps.test.mock;

import ci.cluster.TextDataItem;
import ci.cluster.impl.TextClusterImpl;

public class BlogCluster extends TextClusterImpl {
    
    public BlogCluster(int clusterId) {
        super(clusterId);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id=" + this.getClusterId());
        for (TextDataItem item : this.getDataItems()) {
            BlogEntry blogEntry = (BlogEntry) item.getData();
            sb.append("\nTitle=" + blogEntry.getTitle());
            sb.append("\nAuthor=" + blogEntry.getAuthor());
            sb.append("\nExcerpt=" + blogEntry.getExcerpt());
        }
        return sb.toString();
    }

}
