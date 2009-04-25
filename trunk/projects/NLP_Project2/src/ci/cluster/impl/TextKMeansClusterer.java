package ci.cluster.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ci.cluster.Clusterer;
import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;

public abstract class TextKMeansClusterer implements Clusterer {

    private List<TextDataItem> textDataSet;
    private List<TextCluster> clusters;
    private int numClusters = 3;
    private int maxIterations = 100;
    private Random random = new Random();

    public TextKMeansClusterer(List<TextDataItem> textDataSet, int numClusters) {
        this.textDataSet = textDataSet;
        this.numClusters = numClusters;
    }

    public List<TextCluster> cluster() {
        if (this.textDataSet.size() == 0) {
            return Collections.emptyList();
        }
        this.initializeClusters();
        boolean change = true;
        int count = 0;
        while ((count++ < this.maxIterations) && (change)) {
            this.clearClusterItems();
            change = this.reassignClusters();
            this.computeClusterCenters();
        }
        return this.clusters;
    }

    protected abstract TextCluster createTextCluster(int clusterId);

    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    private void initializeClusters() {
        this.clusters = new ArrayList<TextCluster>();
        Set<Integer> usedIndexes = new HashSet<Integer>();
        for (int i = 0; i < this.numClusters; i++) {
            TextCluster cluster = createTextCluster(i);
            cluster.setCenter(this.getDataItemAtRandom(usedIndexes)
                    .getTagMagnitudeVector());
            this.clusters.add(cluster);
        }
    }

    private TextDataItem getDataItemAtRandom(Set<Integer> usedIndexes) {
        boolean found = false;
        while (!found) {
            int index = random.nextInt(this.textDataSet.size());
            if (!usedIndexes.contains(index)) {
                usedIndexes.add(index);
                return this.textDataSet.get(index);
            }
        }
        return null;
    }

    private void clearClusterItems() {
        for (TextCluster cluster : this.clusters) {
            cluster.clearItems();
        }
    }

    private boolean reassignClusters() {
        int numChanges = 0;
        for (TextDataItem item : this.textDataSet) {
            TextCluster newCluster = this.getClosestCluster(item);
            if ((item.getClusterId() == null) 
                    || (item.getClusterId().intValue() 
                            != newCluster.getClusterId())) {
                numChanges++;
                item.setClusterId(newCluster.getClusterId());
            }
            newCluster.addDataItem(item);
        }
        return (numChanges > 0);
    }

    private TextCluster getClosestCluster(TextDataItem item) {
        TextCluster closestCluster = null;
        Double hightSimilarity = null;
        for (TextCluster cluster : this.clusters) {
            double similarity
                = cluster.getCenter().dotProduct(item.getTagMagnitudeVector());
            if ((hightSimilarity) == null
                    || (hightSimilarity.doubleValue() < similarity)) {
                hightSimilarity = similarity;
                closestCluster = cluster;
            }
        }
        return closestCluster;
    }

    private void computeClusterCenters() {
        for (TextCluster cluster : this.clusters) {
            cluster.computeCenter();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TextCluster cluster : this.clusters) {
            sb.append("\n\n");
            sb.append(cluster.toString());
        }
        return sb.toString();
    }
}
