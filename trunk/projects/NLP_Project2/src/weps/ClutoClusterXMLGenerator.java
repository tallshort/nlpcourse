package weps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import weps.util.TextFile;

public class ClutoClusterXMLGenerator extends AbstractExtractor {

    private String clutoClusterResultsDir = ".";
    private int clusterNum;
    private String xmlResultsDir = ".";

    public void gererateClusterXMLs() throws Exception {
        
        ClusterPriorExtractor priorExtractor
            = new ClusterPriorExtractor();
        priorExtractor.setDatasetDir(this.getDatasetDir());
        priorExtractor.extractContentsPerName();
        Map<String, HashSet<String>> discardedMap = priorExtractor.getDiscardedMap();
        
        for (String name : this.getPeopleNameList()) {
            if (!this.getTargetPerson().equals("")
                    && !name.equals(this.getTargetPerson())) {
                continue;
            }
            String clusterResultPath = this.clutoClusterResultsDir + "/"
                    + name + this.clusterNum + ".cluster";
            String clusterXMLResultPath = xmlResultsDir + "/" + name
                    + ".clust.xml";
            List<String> clusterResults = new TextFile(clusterResultPath);

            // Build the cluster result map { clusterId -> doc list}
            Map<String, ArrayList<String>> clusterResultMap = new TreeMap<String, ArrayList<String>>();
            for (int i = 0; i < clusterResults.size(); i++) {
                String clusterId = clusterResults.get(i);
                ArrayList<String> docs = clusterResultMap.get(clusterId);
                if (docs == null) {
                    docs = new ArrayList<String>();
                    clusterResultMap.put(clusterId, docs);
                }
                docs.add("" + i);
            }

            // Generate XML cluster file
            Element root = new Element("clustering");
            Element discarded = new Element("discarded");
            for (Map.Entry<String, ArrayList<String>> clusterResult : clusterResultMap
                    .entrySet()) {
                String clusterId = clusterResult.getKey();
                // -1 -> discarded cluster
                if (clusterId.equals("-1")) {
                    for (String rank : clusterResult.getValue()) {
                        Element doc = new Element("doc");
                        doc.addAttribute(new Attribute("rank", rank));
                        discarded.appendChild(doc);              
                    }
                } else {
                    Element cluster = new Element("entity");
                    cluster.addAttribute(new Attribute("id", clusterId));
                    // Add doc elements
                    for (String rank : clusterResult.getValue()) {
                        Element doc = new Element("doc");
                        doc.addAttribute(new Attribute("rank", rank));
                        if (discardedMap.get(name).contains(rank)) {
                            discarded.appendChild(doc);
                        } else {
                            cluster.appendChild(doc);
                        }                
                    }
                    // Discard the empty entity element
                    if (cluster.getChildCount() != 0) {
                        root.appendChild(cluster);
                    }
                }
            }
            // Discard the empty discarded element
            if (discarded.getChildCount() != 0) {
                root.appendChild(discarded);
            }
            Document doc = new Document(root);
            TextFile.write(clusterXMLResultPath, doc.toXML());
        }
    }

    @Override
    protected void extractContent(String filePath, String name, String rank) {

    }

    public String getClutoClusterResultsDir() {
        return clutoClusterResultsDir;
    }

    public void setClutoClusterResultsDir(String clutoClusterResultsDir) {
        this.clutoClusterResultsDir = clutoClusterResultsDir;
    }

    public int getClusterNum() {
        return clusterNum;
    }

    public void setClusterNum(int clusterNum) {
        this.clusterNum = clusterNum;
    }

    public String getXmlResultsDir() {
        return xmlResultsDir;
    }

    public void setXmlResultsDir(String xmlResultsDir) {
        this.xmlResultsDir = xmlResultsDir;
    }

}
