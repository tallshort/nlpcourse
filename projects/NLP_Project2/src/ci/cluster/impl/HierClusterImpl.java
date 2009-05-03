package ci.cluster.impl;

import java.util.List;

import ci.cluster.TextDataItem;
import ci.cluster.hiercluster.HierCluster;

public class HierClusterImpl extends TextClusterImpl implements HierCluster {

    private HierCluster child1;
    private HierCluster child2;
    private double similarity;
    
    public HierClusterImpl(int clusterId, HierCluster child1, HierCluster child2,
            double similarity, TextDataItem dataItem) {
        super(clusterId);
        this.child1 = child1;
        this.child2 = child2;
        this.similarity = similarity;
        if (dataItem != null) {
            this.addDataItem(dataItem);
        }
    }

    public double computeSimilarity(HierCluster o) {
        return this.getCenter().dotProduct(o.getCenter());
    }

    public HierCluster getChild1() {
        return this.child1;
    }

    public HierCluster getChild2() {
        return this.child2;
    }

    public double getSimilarity() {
        return this.similarity;
    }
    
    @Override
    public List<TextDataItem> getDataItems() {
        List<TextDataItem> dataItems = super.getDataItems();
        if (this.getChild1() != null) {
            dataItems.addAll(this.getChild1().getDataItems());
        }
        if (this.getChild2() != null) {
            dataItems.addAll(this.getChild2().getDataItems());
        }
        return dataItems;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((child1 == null) ? 0 : child1.hashCode());
        result = prime * result + ((child2 == null) ? 0 : child2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final HierClusterImpl other = (HierClusterImpl) obj;
        if (child1 == null) {
            if (other.child1 != null)
                return false;
        } else if (!child1.equals(other.child1))
            return false;
        if (child2 == null) {
            if (other.child2 != null)
                return false;
        } else if (!child2.equals(other.child2))
            return false;
        return true;
    }

}
