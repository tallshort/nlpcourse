package weps;

import java.util.Arrays;
import java.util.List;

import weps.util.TextFile;

import com.google.common.base.Join;

public class ClutoRawDataExtractor extends AbstractExtractor {

    private INamedEntityRecognizer recognizer;
    private String bodyDir = "body_data";
    private String urlDir = "url_data";
    
    public ClutoRawDataExtractor() throws Exception {
        recognizer = new StandfordNamedEntityRecognizer();
    }
    
    @Override
    protected void extractContent(String filePath, String name, String rank) {
        String postfix = "/" + name + "_" + rank + ".txt";
        String bodyFilePath = 
            this.bodyDir  + postfix;
        String urlFilePath = this.urlDir + postfix;
        try {
            List<String> namedEntities
                = recognizer.recognizeNamedEntities(TextFile.read(bodyFilePath));
            String urlData = TextFile.read(urlFilePath).trim();
            if (!urlData.equals("")) {
                namedEntities.addAll(Arrays.asList(urlData.split("[|]")));
            }
            System.out.println(namedEntities);
            TextFile.write(this.getTargetDir() + postfix, 
                    Join.join("|", namedEntities));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBodyDir() {
        return bodyDir;
    }

    public void setBodyDir(String bodyDir) {
        this.bodyDir = bodyDir;
    }

    public String getUrlDir() {
        return urlDir;
    }

    public void setUrlDir(String urlDir) {
        this.urlDir = urlDir;
    }

}
