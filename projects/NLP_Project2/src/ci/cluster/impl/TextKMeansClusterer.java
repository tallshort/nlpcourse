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
            TextDataItem item = this.getDataItemAtRandom(usedIndexes);
            item.setCluster(cluster);
            // System.out.println(item.getCluster().getClusterId());
            cluster.setCenter(item.getTagMagnitudeVector());
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
            // System.out.println("New cluster: " + newCluster.getClusterId());
            if ((item.getCluster() == null) 
                    || (!item.getCluster().equals(newCluster))) {
                numChanges++;
                item.setCluster(newCluster);
            }
            newCluster.addDataItem(item);
        }
        return (numChanges > 0);
    }

    private TextCluster getClosestCluster(TextDataItem item) {
        TextCluster closestCluster = item.getCluster();
        Double maxSimilarity = null;
        // The init maxSimilarity
        if (closestCluster != null) {
            maxSimilarity
                = closestCluster.getCenter().dotProduct(item.getTagMagnitudeVector());
        }
        for (TextCluster cluster : this.clusters) {
            double similarity
                = cluster.getCenter().dotProduct(item.getTagMagnitudeVector());
            if (maxSimilarity == null
                    || ((similarity - maxSimilarity.doubleValue()) > SIMILARITY_DELTA)) {
                maxSimilarity = similarity;
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
