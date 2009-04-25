package ci.cluster.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.textanalysis.TagMagnitude;
import ci.textanalysis.TagMagnitudeVector;
import ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;

public class TextClusterImpl implements TextCluster {

    private TagMagnitudeVector center;
    private List<TextDataItem> items;
    private int clusterId;

    public TextClusterImpl(int clusterId) {
        this.clusterId = clusterId;
        this.items = new ArrayList<TextDataItem>();
    }

    public void addDataItem(TextDataItem item) {
        items.add(item);
    }

    public void clearItems() {
        this.items.clear();
    }

    public List<TextDataItem> getDataItems() {
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + clusterId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TextClusterImpl other = (TextClusterImpl) obj;
        if (clusterId != other.clusterId)
            return false;
        return true;
    }

}