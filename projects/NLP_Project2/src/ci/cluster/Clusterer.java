package ci.cluster;

import java.util.List;

public interface Clusterer {

    public static final double SIMILARITY_DELTA = 0.00000000001;
    public static final double STOPPING_SIMILARITY_THRESHOLD = 0.25;
    public List<TextCluster> cluster();
    
}
