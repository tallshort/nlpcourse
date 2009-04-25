package weps.test.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.textanalysis.TagMagnitude;
import ci.textanalysis.TagMagnitudeVector;
import ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;

public class BlogCluster implements TextCluster {

    private TagMagnitudeVector center;
    private List<TextDataItem> items;
    private int clusterId;
    
    public BlogCluster(int clusterId) {
        this.clusterId = clusterId;
        this.items = new ArrayList<TextDataItem>();
    }
    
    public void addDataItem(TextDataItem item) {
        items.add(item);
    }

    public void clearItems() {
        this.items.clear();
    }
    
    public List<TextDataItem> getItems() {
        return this.items;
    }

    public void computeCenter() {
        if (this.items.size() == 0) {
            return;
        }
        List<TagMagnitudeVector> tmList = new ArrayList<TagMagnitudeVector>();
        for (TextDataItem item : items) {
            tmList.add(item.getTagMagnitudeVector());
        }
        TagMagnitudeVector empty
            = new TagMagnitudeVectorImpl(Collections.<TagMagnitude>emptyList());
        this.center = empty.add(tmList);
    }

    public TagMagnitudeVector getCenter() {
       return this.center;
    }
    
    public void setCenter(TagMagnitudeVector center) {
        this.center = center;
    }

    public int getClusterId() {
        return this.clusterId;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id=" + this.clusterId);
        for (TextDataItem item : this.items) {
            BlogEntry blogEntry = (BlogEntry) item.getData();
            sb.append("\nTitle=" + blogEntry.getTitle());
            sb.append("\nAuthor=" + blogEntry.getAuthor());
            sb.append("\nExcerpt=" + blogEntry.getExcerpt());
        }
        return sb.toString();
    }

}
