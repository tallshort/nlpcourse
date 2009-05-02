package weps.cluster.impl;

import java.util.List;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import weps.util.TextFile;
import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.cluster.impl.TextKMeansClusterer;

public class WebPeopleKMeansClusterer extends TextKMeansClusterer {

    private String targetDir = ".";
    private String targetPerson = "";

    public WebPeopleKMeansClusterer(List<TextDataItem> textDataSet,
            int numClusters) {
        super(textDataSet, numClusters);
    }

    @Override
    protected TextCluster createTextCluster(int clusterId) {
        return new WebPeopleCluster(clusterId);
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public List<TextCluster> cluster() {
        List<TextCluster> clusters = super.cluster();
        // Generate XML cluster file
        Element root = new Element("clustering");
        Element discarded = null;
        for (TextCluster cluster : clusters) {
            Element clusterElement = new Element("entity");
            String clusterId = "" + cluster.getClusterId();
            if (clusterId.equals("-1")) {
                discarded = clusterElement = new Element("discarded");
            } else {
                clusterElement.addAttribute(new Attribute("id", clusterId));
            }
            for (TextDataItem dataItem : cluster.getDataItems()) {
                WebPeopleDocEntry docEntry = (WebPeopleDocEntry)dataItem.getData();
                Element doc = new Element("doc");
                String rank = "" + Integer.parseInt(docEntry.getRank());
                doc.addAttribute(new Attribute("rank", rank));
                clusterElement.appendChild(doc);
            }
            if (!clusterElement.getLocalName().equals("discarded")) {
                root.appendChild(clusterElement);
            }
        }
        if (discarded != null) {
            root.appendChild(discarded);
        }
        Document doc = new Document(root);
        String clusterResultPath
            = this.targetDir + "/" + this.targetPerson + ".clust.xml";
        TextFile.write(clusterResultPath, doc.toXML());
        return clusters;
    }

    public String getTargetPerson() {
        return targetPerson;
    }

    public void setTargetPerson(String targetPerson) {
        this.targetPerson = targetPerson;
    }

}
