package ci.cluster;

import ci.textanalysis.TagMagnitudeVector;

public interface TextCluster {
    
    public void clearItems();
    
    public void setCenter(TagMagnitudeVector center);
    
    public TagMagnitudeVector getCenter();
    
    public void computeCenter();
    
    public int getClusterId();
    
    public void addDataItem(TextDataItem item);
    
}
