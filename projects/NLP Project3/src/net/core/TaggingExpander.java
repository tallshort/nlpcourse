package net.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 标注信息扩展器，将原始数据扩展成具有分词、词性等信息
 */
public class TaggingExpander {

    private SegTag segTag = new SegTag();
    
    public List<Sentence> expand(List<String> dataLines) {
        List<Sentence> sentenceList = new ArrayList<Sentence>();
        
        int lineNumber = 1;
        Sentence s = new Sentence(lineNumber);
        for (String dataLine : dataLines) {
            if (dataLine.equals("")) {
                addSentence(sentenceList, s);
                lineNumber++;
                s = new Sentence(lineNumber);
                continue;
            }
            String[] charEntityTagEntry = dataLine.split(" ");
            s.addTaggedCharacter(charEntityTagEntry[0], charEntityTagEntry[1]);
            lineNumber++;
        }
        // Handle the last sentence
        if (s.getTaggedSegmentList().size() == 0) {
            addSentence(sentenceList, s);
        }
        return sentenceList;
    }

    private void addSentence(List<Sentence> sentenceList, Sentence s) {
        String segResult = segTag.process(s.getContent());
        String[] taggedSegments = segResult.split(" ");
        for (String taggedSegment : taggedSegments) {
            String[] segPos = taggedSegment.split("[/]");
            s.addTaggedSegment(segPos[0], segPos[1]);
        }
        sentenceList.add(s);
    }

}
