package ci.cluster;

import java.util.List;

import ci.textanalysis.TagMagnitudeVector;

public interface TextCluster {
    
    public void clearItems();
    
    public void setCenter(TagMagnitudeVector center);
    
    public TagMagnitudeVector getCenter();
    
    public void computeCenter();
    
    public int getClusterId();
    
    public List<TextDataItem> getDataItems();
    
    public void addDataItem(TextDataItem item);
    
}
