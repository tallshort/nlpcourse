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
    
    /**
     * 构造句子
     * 
     * @param startLineNumber 指定的起始行号
     */
    public Sentence(int startLineNumber) {
        this.startLineNumber = startLineNumber;
    }
    
    /**
     * 获取句子的起始行号
     */
    public int getStartLineNumber() {
        return this.startLineNumber;
    }

    /**
     * 添加标注字
     * 
     * @param character 字
     * @param entityTag 命名实体标注
     */
    public void addTaggedCharacter(String character, String entityTag) {
        TaggedCharacter tc = new TaggedCharacter(character, entityTag);
        int offset = this.taggedCharacterMap.keySet().size();
        this.taggedCharacterMap.put(this.startLineNumber + offset, tc);
        this.content.append(character);
    }
    
    /**
     * 获取句子文本内容
     */
    public String getContent() {
        return content.toString();
    }

    /**
     * 添加标注词
     * 
     * @param segment 词
     * @param posTag  词性
     */
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

    /**
     * 获取带有分词和词性标注的词列表
     */
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
