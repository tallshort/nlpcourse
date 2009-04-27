package weps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractExtractor {
    
    private final static String DESCRIPTION_FILES_DIR = "description_files";
    private final static String WEBPAGES_DIR = "web_pages";
    
    private String datasetDir = "D:/MLPROJECT2/weps2007/test";
    private String targetDir = ".";
    private String targetPerson = "";
    
    public List<String> getPeopleNameList() {
        List<String> nameList = new ArrayList<String>();
        File dir = new File(datasetDir + "/" + DESCRIPTION_FILES_DIR);
        for (String fileName : dir.list()) {
            if (fileName.endsWith(".xml")) {
                // System.out.println(fileName.replace(".xml", "").replace("_", " "));
                nameList.add(fileName.replace(".xml", ""));
            }
        }
        return nameList;
    }
    
    public void extractContents() {
        List<String> nameList = this.getPeopleNameList();
        for (String name : nameList) {
            if (!this.targetPerson.equals("") && !name.equals(this.targetPerson)) {
                continue;
            }
            String webPagesDir
                = this.datasetDir + "/" + WEBPAGES_DIR + "/" + name + "/" + "raw";
            File dir = new File(webPagesDir);
            for (String rank : dir.list()) {
                File webPageFile = new File(webPagesDir + "/" + rank + "/index.html");
                if (webPageFile.exists()) {
                    extractContent(webPageFile.getAbsolutePath(), name, rank);
                }
            }
        }
    }

    protected abstract void extractContent(String filePath, 
            String name, String rank);

    public String getDatasetDir() {
        return datasetDir;
    }

    public void setDatasetDir(String datasetDir) {
        this.datasetDir = datasetDir;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getTargetPerson() {
        return targetPerson;
    }

    public void setTargetPerson(String targetPerson) {
        this.targetPerson = targetPerson;
    }
    
}
