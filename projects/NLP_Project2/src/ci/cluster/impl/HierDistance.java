package ci.cluster.impl;

import ci.cluster.hiercluster.HierCluster;

public class HierDistance implements Comparable<HierDistance> {

    private HierCluster c1;
    private HierCluster c2;
    private double similarity;
    
    public HierDistance(HierCluster c1, HierCluster c2) {
        this.c1 = c1;
        this.c2 = c2;
    }
    
    public HierCluster getC1() {
        return this.c1;
    }
    
    public HierCluster getC2() {
        return this.c2;
    }
    
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
    
    public double getSimilarity() {
        return this.similarity;
    }
    
    public boolean containsCluster(HierCluster cluster) {
        if (this.getC1() == null || this.getC2() == null) {
            return false;
        }
        if ((cluster.getClusterId() == this.getC1().getClusterId())
                || (cluster.getClusterId() == this.getC2().getClusterId())) {
            return true;
        }
        return false;
    }
    
    public int compareTo(HierDistance o) {
        double diff = o.getSimilarity() - this.getSimilarity();
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((c1 == null) ? 0 : c1.hashCode());
        result = prime * result + ((c2 == null) ? 0 : c2.hashCode());
        long temp;
        temp = Double.doubleToLongBits(similarity);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        final HierDistance other = (HierDistance) obj;
        if (c1 == null) {
            if (other.c1 != null)
                return false;
        } else if (!c1.equals(other.c1))
            return false;
        if (c2 == null) {
            if (other.c2 != null)
                return false;
        } else if (!c2.equals(other.c2))
            return false;
        if (Double.doubleToLongBits(similarity) != Double
                .doubleToLongBits(other.similarity))
            return false;
        return true;
    }

}
