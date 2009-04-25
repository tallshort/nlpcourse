package weps.cluster.impl;

import weps.test.mock.BlogEntry;
import ci.cluster.TextDataItem;
import ci.textanalysis.TagMagnitudeVector;

public class WebPeopleDataItem implements TextDataItem {

    private TagMagnitudeVector tagMagnitudeVector;
    private Integer clusterId;
    
    public WebPeopleDataItem(TagMagnitudeVector tagMagnitudeVector) {
        this.tagMagnitudeVector = tagMagnitudeVector;
    }
    
    public double distance(TagMagnitudeVector other) {
        return this.getTagMagnitudeVector().dotProduct(other);
    }
    
    public Integer getClusterId() {
        return this.clusterId;
    }

    public Object getData() {
        return null;
    }

    public TagMagnitudeVector getTagMagnitudeVector() {
        return this.tagMagnitudeVector;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

}
