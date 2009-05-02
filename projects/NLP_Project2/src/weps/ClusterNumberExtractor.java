package weps;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Elements;

public class ClusterNumberExtractor extends AbstractExtractor {

    private Map<String, Integer> clusterNumberMap;
    
    public ClusterNumberExtractor() throws Exception {
        clusterNumberMap = new HashMap<String, Integer>();
    }

    @Override
    protected void extractContent(String filePath, String name, String rank) {
        try {
            extractClusterNumber(name);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }     
    }

    private void extractClusterNumber(String name) throws Exception {
        String xmlFilePath = this.getDatasetDir()
            + "/truth_files/official_annotation/" + name + ".clust.xml"; 
        Builder builder = new Builder(false);
        Document doc = builder.build(new File(xmlFilePath));
        Elements entityElements = doc.getRootElement().getChildElements("entity");
        clusterNumberMap.put(name, entityElements.size());
    }

    public Map<String, Integer> getClusterNumberMap() {
        return clusterNumberMap;
    }

}
