package weps.test.mock;

import weps.test.mock.BlogEntry;
import ci.cluster.TextDataItem;
import ci.textanalysis.TagMagnitudeVector;

public class BlogDataItem implements TextDataItem {

    private TagMagnitudeVector tagMagnitudeVector;
    private Integer clusterId;
    private BlogEntry blogEntry;
    
    public BlogDataItem(BlogEntry blogEntry, 
            TagMagnitudeVector tagMagnitudeVector) {
        this.blogEntry = blogEntry;
        this.tagMagnitudeVector = tagMagnitudeVector;
    }
    
    public double distance(TagMagnitudeVector other) {
        return this.getTagMagnitudeVector().dotProduct(other);
    }
    
    public Integer getClusterId() {
        return this.clusterId;
    }

    public Object getData() {
        return this.blogEntry;
    }

    public TagMagnitudeVector getTagMagnitudeVector() {
        return this.tagMagnitudeVector;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

}
