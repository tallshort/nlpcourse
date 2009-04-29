package weps;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import weps.util.TextFile;

public class XMLDescriptionExtractor extends AbstractExtractor {

    public XMLDescriptionExtractor() throws Exception {
        
    }

    @Override
    protected void extractContent(String filePath, String name, String rank) {
        try {
            extractXMLDescriptions(name);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }     
    }

    private void extractXMLDescriptions(String name) throws Exception {
        String xmlFilePath = this.getDatasetDir()
            + "/" + DESCRIPTION_FILES_DIR + "/" + name + ".xml"; 
        Builder builder = new Builder(false);
        Document doc = builder.build(xmlFilePath);
        Element corpus = doc.getRootElement();
        Elements docElements = corpus.getChildElements("doc");
        for (int i = 0; i < docElements.size(); i++) {
            Element docElement = docElements.get(i);
            String rank = String.format("%03d",
                    Integer.parseInt(docElement.getAttributeValue("rank")));
            String title = docElement.getAttributeValue("title");
            String snippet = docElement.getFirstChildElement("snippet").getValue();
            String outputPath = this.getTargetDir() + "/" + name + "_" + rank + ".txt";
            System.out.println("Extract XML Description " + name + " " + rank);
            TextFile.write(outputPath, title + "\n" + snippet);
        }
    }

}
