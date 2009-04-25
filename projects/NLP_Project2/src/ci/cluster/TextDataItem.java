package ci.cluster;

import ci.textanalysis.TagMagnitudeVector;

public interface TextDataItem {
    
    public Object getData();
    
    public TagMagnitudeVector getTagMagnitudeVector();
    
    public Integer getClusterId();
    
    public void setClusterId(Integer clusterId);
    
}
