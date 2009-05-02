package ci.cluster;

import ci.textanalysis.TagMagnitudeVector;

public interface TextDataItem {
    
    public Object getData();
    
    public TagMagnitudeVector getTagMagnitudeVector();
    
    public TextCluster getCluster();
    
    public void setCluster(TextCluster cluster);
    
}
