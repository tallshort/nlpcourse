package ci.cluster.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ci.cluster.Clusterer;
import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.cluster.hiercluster.HierCluster;

public abstract class TextHierarchialClusterer implements Clusterer {

    private Map<Integer, HierCluster> availableClusters;
    private List<TextDataItem> textDataSet;

    private int idCount = 0;
    private Set<HierDistance> allDistances;
    
    private int numOfRootClusters = 1;
    
    private List<TextCluster> clusters;

    public TextHierarchialClusterer(List<TextDataItem> textDataSet, int numOfRootClusters) {
        this.textDataSet = textDataSet;
        this.availableClusters = new HashMap<Integer, HierCluster>();
        this.allDistances = new HashSet<HierDistance>();
        this.numOfRootClusters = numOfRootClusters;
    }

    public List<TextCluster> cluster() {
        this.createInitialClusters();
        // Determine the type of clustering (fixed or dynamic)
        if (this.numOfRootClusters > 0) { // Fixed 
            while (this.availableClusters.size() > this.numOfRootClusters) {
                this.addNextCluster();
            }
        } else { // Dynamic
            while (this.availableClusters.size() > 1) {
                List<HierDistance> sortDist = new ArrayList<HierDistance>();
                sortDist.addAll(this.allDistances);
                Collections.sort(sortDist);
                HierDistance hd = sortDist.get(0);
                if (hd.getSimilarity() > STOPPING_SIMILARITY_THRESHOLD) {
                    this.addNextCluster();
                } else {
                    break;
                }
            }
        }
        System.out.println("Num of clusters: " + this.availableClusters.size());
        return new ArrayList<TextCluster>(this.availableClusters.values());
    }

    private void createInitialClusters() {
        this.createInitialSingleItemClusters();
        this.computeInitialDistances();
    }

    private void createInitialSingleItemClusters() {
        for (TextDataItem dataItem : this.textDataSet) {
            HierCluster cluster = createHierCluster(this.idCount++, null,
                    null, 1.0, dataItem);
            cluster.setCenter(dataItem.getTagMagnitudeVector());
            this.availableClusters.put(cluster.getClusterId(), cluster);
        }
    }

    private void computeInitialDistances() {
        for (HierCluster cluster : this.availableClusters.values()) {
            for (HierCluster otherCluster : this.availableClusters.values()) {
                if (cluster.getClusterId() != otherCluster.getClusterId()) {
                    HierDistance hd = new HierDistance(cluster, otherCluster);
                    if (!this.allDistances.contains(hd)) {
                        double similarity = cluster.computeSimilarity(otherCluster);
                        hd.setSimilarity(similarity);
                        this.allDistances.add(hd);
                    }
                }
            }
        }
    }

    private void addNextCluster() {
        List<HierDistance> sortDist = new ArrayList<HierDistance>();
        sortDist.addAll(this.allDistances);
        Collections.sort(sortDist);
        HierDistance hd = sortDist.get(0);
        this.allDistances.remove(hd);
        
        HierCluster cluster = createNewCluster(hd);
        pruneDistances(hd.getC1(), hd.getC2(), sortDist);
        addNewClusterDistances(cluster);
    }

    private HierCluster createNewCluster(HierDistance hd) {
        HierCluster cluster = createHierCluster(this.idCount++,
                hd.getC1(), hd.getC2(), hd.getSimilarity(), null);
        cluster.setCenter(hd.getC1().getCenter().add(hd.getC2().getCenter()));
        this.availableClusters.put(cluster.getClusterId(), cluster);
        this.availableClusters.remove(hd.getC1().getClusterId());
        this.availableClusters.remove(hd.getC2().getClusterId());
        return cluster;
    }

    protected abstract HierCluster createHierCluster(int clusterId, HierCluster c1,
            HierCluster c2, double similarity, TextDataItem textDataItem);

    private void pruneDistances(HierCluster c1, HierCluster c2,
            List<HierDistance> sortDist) {
        for(HierDistance hierDistance : sortDist) {
            if (hierDistance.containsCluster(c1)
                    || hierDistance.containsCluster(c2)) {
                this.allDistances.remove(hierDistance);
            }
        }
    }

    private void addNewClusterDistances(HierCluster cluster) {
        for (HierCluster hc : this.availableClusters.values()) {
            if (hc.getClusterId() != cluster.getClusterId()) {
                HierDistance hierDistance = new HierDistance(cluster, hc);
                double similarity = cluster.getCenter().dotProduct(hc.getCenter());
                hierDistance.setSimilarity(similarity);
                this.allDistances.add(hierDistance);
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Num of clusters = " + this.idCount + "\n");
        // sb.append(this.printClusterDetails(this.rootCluster, ""));
        return sb.toString();
    }

    private String printClusterDetails(HierCluster cluster, String append) {
        StringBuilder sb = new StringBuilder();
        if (cluster != null) {
            sb.append(cluster.toString());
            String tab = "\t" + append;
            if (cluster.getChild1() != null) {
                sb.append("\n" + tab + "C1="
                        + this.printClusterDetails(cluster.getChild1(), tab));
            }
            if (cluster.getChild2() != null) {
                sb.append("\n" + tab + "C2="
                        + this.printClusterDetails(cluster.getChild2(), tab));
            }
        }
        return sb.toString();
    }

}
