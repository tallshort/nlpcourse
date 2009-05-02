package weps;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class ClusterPriorExtractor extends AbstractExtractor {

    private Map<String, Integer> clusterNumberMap;
    private Map<String, HashSet<String>> discardedMap;
    
    public ClusterPriorExtractor() throws Exception {
        clusterNumberMap = new HashMap<String, Integer>();
        discardedMap = new HashMap<String, HashSet<String>>();
    }

    @Override
    protected void extractContent(String filePath, String name, String rank) {
        try {
            extractClusterPrior(name);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }     
    }

    private void extractClusterPrior(String name) throws Exception {
        String xmlFilePath = this.getDatasetDir()
            + "/truth_files/official_annotation/" + name + ".clust.xml"; 
        Builder builder = new Builder(false);
        Document doc = builder.build(new File(xmlFilePath));
        Elements entityElements = doc.getRootElement().getChildElements("entity");
        clusterNumberMap.put(name, entityElements.size());
        
        this.discardedMap.put(name, new HashSet<String>());
        Element discardElement = doc.getRootElement().getFirstChildElement("discarded");
        if (discardElement != null) {
            Elements discardDocElements = discardElement.getChildElements("doc");
            for (int i = 0; i < discardDocElements.size(); i++) {
                Element discardDocElement = discardDocElements.get(i);
                HashSet<String> discardDocSet = this.discardedMap.get(name);
                discardDocSet.add(discardDocElement.getAttributeValue("rank"));     
            }
        }
    }

    public Map<String, Integer> getClusterNumberMap() {
        return clusterNumberMap;
    }

    public Map<String, HashSet<String>> getDiscardedMap() {
        return discardedMap;
    }

}
