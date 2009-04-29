package weps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import weps.util.TextFile;

import com.google.common.base.Join;

public class NamedEntitiesExtractor extends AbstractExtractor {

    private INamedEntityRecognizer recognizer;
    private List<String> dataDirs = new ArrayList<String>();

    public NamedEntitiesExtractor() throws Exception {
        recognizer = new StandfordNamedEntityRecognizer();
    }

    @Override
    protected void extractContent(String filePath, String name, String rank) {
        extractXMLDescriptions(name);
        extractNamedEntites(name, rank);      
    }

    private void extractXMLDescriptions(String name) {
        String xmlFilePath = this.getDatasetDir()
            + "/" + DESCRIPTION_FILES_DIR + "/" + ".xml"; 
    }

    private void extractNamedEntites(String name, String rank) {
        String postfix = "/" + name + "_" + rank + ".txt";
        List<String> namedEntities = new ArrayList<String>();
        for (String dataDir : this.dataDirs) {
            String dataFilePath = dataDir + postfix;
            namedEntities.addAll(recognizer.recognizeNamedEntities(TextFile
                    .read(dataFilePath)));
        }
        // Remove the target people name in the named entity list
        String[] nameParts = name.split("_");
        List<String> removedEntites = new ArrayList<String>();
        for (String namePart : nameParts) {
            removedEntites.add(namePart);
            removedEntites.add(namePart.toLowerCase());
        }
        // System.out.println(namedEntities);
        namedEntities.removeAll(removedEntites);
        System.out.println("Extract Named Entities " + name + " " + rank);
        TextFile.write(this.getTargetDir() + postfix, Join.join("|",
                namedEntities));
    }
    
    public void addDataDir(String dirPath) {
        this.dataDirs.add(dirPath);
    }

}