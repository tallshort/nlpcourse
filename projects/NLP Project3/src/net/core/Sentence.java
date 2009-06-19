package net.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包含标注信息的句子
 */
public class Sentence {
    
    private int startLineNumber;
    private Map<Integer, TaggedCharacter> taggedCharacterMap
        = new HashMap<Integer, TaggedCharacter>();
    private List<TaggedSegment> taggedSegmentList
        = new ArrayList<TaggedSegment>();
    
    private StringBuilder content = new StringBuilder();
    
    public Sentence(int startLineNumber) {
        this.startLineNumber = startLineNumber;
    }
    
    public int getStartLineNumber() {
        return this.startLineNumber;
    }

    public void addTaggedCharacter(String character, String entityTag) {
        TaggedCharacter tc = new TaggedCharacter(character, entityTag);
        int offset = this.taggedCharacterMap.keySet().size();
        this.taggedCharacterMap.put(this.startLineNumber + offset, tc);
        this.content.append(character);
    }
    
    public String getContent() {
        return content.toString();
    }

    public void addTaggedSegment(String segment, String posTag) {
        int lineNumber = this.startLineNumber;
        if (this.taggedSegmentList.size() != 0) {
            int endLineNumber
                = this.taggedSegmentList.get(this.taggedSegmentList.size() - 1).getEndLineNumber();
            lineNumber =  endLineNumber + 1;
            // The first tagged segment in the sentence
            if (this.taggedCharacterMap.get(lineNumber) == null) {
                lineNumber = lineNumber + 1; 
            }
        }
        // System.out.println(this.taggedCharacterMap);
        // System.out.println("" + lineNumber + " " + this.taggedCharacterMap.get(lineNumber));
        String entityTag = this.taggedCharacterMap.get(lineNumber).getEntityTag();
        TaggedSegment ts = new TaggedSegment(lineNumber, segment, posTag, entityTag);
        this.taggedSegmentList.add(ts);
    }

    public List<TaggedSegment> getTaggedSegmentList() {
        return taggedSegmentList;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TaggedSegment ts : this.taggedSegmentList) {
            sb.append(ts.toString()).append("\n");
        }
        return sb.toString();
    }

}
