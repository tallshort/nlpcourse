package weps.extractor;

import java.io.File;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import weps.util.TextFile;

import com.google.common.base.Join;

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
        Document doc = builder.build(new File(xmlFilePath));
        Element corpus = doc.getRootElement();
        Elements docElements = corpus.getChildElements("doc");
        for (int i = 0; i < docElements.size(); i++) {
            Element docElement = docElements.get(i);
            String rank = String.format("%03d",
                    Integer.parseInt(docElement.getAttributeValue("rank")));
            // e.g. Alvin_Copper_alvin_copper_ALVIN_COOPER
            String[] nameParts
                = (name + "_" + name.toLowerCase() + "_" + name.toUpperCase()).split("_");
            String title
                = docElement.getAttributeValue("title").replaceAll(Join.join("|", nameParts), "");
            String snippet
                = docElement.getFirstChildElement("snippet").getValue().replaceAll(Join.join("|", nameParts), "");;
            String outputPath = this.getTargetDir() + "/" + name + "_" + rank + ".txt";
            System.out.println("Extract XML Description " + name + " " + rank);
            TextFile.write(outputPath, title + "\n" + snippet);
        }
    }

}
