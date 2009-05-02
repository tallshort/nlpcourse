package ci.cluster;

import java.util.List;

public interface Clusterer {

    public static double SIMILARITY_DELTA = 0.00000000001;
    public List<TextCluster> cluster();
    
}
