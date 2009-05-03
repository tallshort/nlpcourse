package weps.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.crf.CRFClassifier;

public class StandfordNamedEntityRecognizer implements INamedEntityRecognizer {

private CRFClassifier classifer;
    
    public StandfordNamedEntityRecognizer() 
        throws ClassCastException, IOException, ClassNotFoundException {
        this.classifer
            = CRFClassifier.getClassifier("asset/ner-eng-ie.crf-3-all2008-distsim.ser.gz");
    }
    
    public List<String> recognizeNamedEntities(String text) {
        List<String> entityNameList = new ArrayList<String>();
        String labeledText = this.classifer.classifyToString(text);
        String[] res = labeledText.split(" ");
        for (String s : res) {
            // Format: <EntityName>/<EntityType>
            int index = s.indexOf("/");
            String p = s.substring(index + 1);
            if (p.equals("LOCATION") 
                    || p.equals("PERSON")
                    || p.equals("ORGANIZATION")) { // 如果被标记为命名实体
                String entityName = s.substring(0, index);
                entityNameList.add(entityName);
                // System.out.println(s);
            }
        }
        return entityNameList;
    }

}
