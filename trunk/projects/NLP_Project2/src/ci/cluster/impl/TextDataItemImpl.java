package ci.cluster.impl;

import ci.cluster.TextDataItem;
import ci.textanalysis.TagMagnitudeVector;

public class TextDataItemImpl implements TextDataItem {

    private TagMagnitudeVector tagMagnitudeVector;
    private Integer clusterId;
    private Object data;
    
    public TextDataItemImpl(Object data, TagMagnitudeVector tagMagnitudeVector) {
        this.data = data;
        this.tagMagnitudeVector = tagMagnitudeVector;
    }
    
    public double distance(TagMagnitudeVector other) {
        return this.getTagMagnitudeVector().dotProduct(other);
    }
    
    public Integer getClusterId() {
        return this.clusterId;
    }

    public Object getData() {
        return this.data;
    }

    public TagMagnitudeVector getTagMagnitudeVector() {
        return this.tagMagnitudeVector;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

}
