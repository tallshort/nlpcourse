package weps;

import java.util.ArrayList;
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

    public void gererateClusterXMLs() {
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
            Element discarded = null;
            for (Map.Entry<String, ArrayList<String>> clusterResult : clusterResultMap
                    .entrySet()) {
                Element cluster = new Element("entity");
                String clusterId = clusterResult.getKey();
                if (clusterId.equals("-1")) {
                    discarded = cluster = new Element("discarded");
                } else {
                    cluster = new Element("entity");
                    cluster.addAttribute(new Attribute("id", clusterId));
                }
                for (String rank : clusterResult.getValue()) {
                    Element doc = new Element("doc");
                    doc.addAttribute(new Attribute("rank", rank));
                    cluster.appendChild(doc);
                }
                if (!cluster.getLocalName().equals("discarded")) {
                    root.appendChild(cluster);
                }
            }
            if (discarded != null) {
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
