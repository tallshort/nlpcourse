package weps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weps.util.TextFile;

public abstract class AbstractBodyExtractor implements IBodyExtractor {
    
    private final static String DESCRIPTION_FILES_DIR = "description_files";
    private final static String WEBPAGES_DIR = "web_pages";
    
    private String datasetDir = ".";
    private String bodiesTargetDir = ".";
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
    
    public void extractBodies() {
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
                    String body = this.extractBody(webPageFile.getAbsolutePath());
                    // System.out.println(body);
                    TextFile.write(this.getBodiesTargetDir() + "/" + name + "_" + rank + ".txt", body);
                }
            }
        }
    }

    public String getDatasetDir() {
        return datasetDir;
    }

    public void setDatasetDir(String datasetDir) {
        this.datasetDir = datasetDir;
    }

    public String getBodiesTargetDir() {
        return bodiesTargetDir;
    }

    public void setBodiesTargetDir(String bodiesTargetDir) {
        this.bodiesTargetDir = bodiesTargetDir;
    }

    public String getTargetPerson() {
        return targetPerson;
    }

    public void setTargetPerson(String targetPerson) {
        this.targetPerson = targetPerson;
    }
    
}
