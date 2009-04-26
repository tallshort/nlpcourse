package ci.textanalysis.standford.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ci.textanalysis.Tag;
import ci.textanalysis.TagMagnitudeVector;
import ci.textanalysis.TextAnalyzer;
import ci.textanalysis.lucene.impl.TagImpl;
import edu.stanford.nlp.ie.crf.CRFClassifier;

public class StanfordTextAnalyzer implements TextAnalyzer {

    private CRFClassifier classifer;
    
    public StanfordTextAnalyzer() 
        throws ClassCastException, IOException, ClassNotFoundException {
        this.classifer
            = CRFClassifier.getClassifier("asset/ner-eng-ie.crf-3-all2008-distsim.ser.gz");
    }
    
    public List<Tag> analyzeText(String text) throws IOException {
        List<Tag> tagList = new ArrayList<Tag>();
        String labeledText = this.classifer.classifyToString(text);
        String[] res = labeledText.split(" ");
        for (String s : res) {
            // Format: <EntityName>/<EntityType>
            int index = s.indexOf("/");
            String p = s.substring(index + 1);
            if (p.equals("LOCATION") 
                    || p.equals("PERSON")
                    || p.equals("ORGANIZATION")) { // 如果被标记为命名实体
                String tag = s.substring(0, index);
                tagList.add(new TagImpl(tag, tag));
                System.out.println(s);
            }
        }
        return tagList;
    }

    public TagMagnitudeVector createTagMagnitudeVector(String text)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
