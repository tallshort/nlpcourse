package ci.cluster.impl;

import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.textanalysis.TagMagnitudeVector;

public class TextDataItemImpl implements TextDataItem {

    private TagMagnitudeVector tagMagnitudeVector;
    private TextCluster cluster;
    private Object data;
    
    public TextDataItemImpl(Object data, TagMagnitudeVector tagMagnitudeVector) {
        this.data = data;
        this.tagMagnitudeVector = tagMagnitudeVector;
    }
    
    public double distance(TagMagnitudeVector other) {
        return this.getTagMagnitudeVector().dotProduct(other);
    }

    public Object getData() {
        return this.data;
    }

    public TagMagnitudeVector getTagMagnitudeVector() {
        return this.tagMagnitudeVector;
    }

    public TextCluster getCluster() {
        return this.cluster;
    }

    public void setCluster(TextCluster cluster) {
        this.cluster = cluster;
    }


}
