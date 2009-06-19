package net.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ������ע��Ϣ�ľ���
 */
public class Sentence {
    
    private int startLineNumber;
    private Map<Integer, TaggedCharacter> taggedCharacterMap
        = new HashMap<Integer, TaggedCharacter>();
    private List<TaggedSegment> taggedSegmentList
        = new ArrayList<TaggedSegment>();
    
    private StringBuilder content = new StringBuilder();
    
    /**
     * �������
     * 
     * @param startLineNumber ָ������ʼ�к�
     */
    public Sentence(int startLineNumber) {
        this.startLineNumber = startLineNumber;
    }
    
    /**
     * ��ȡ���ӵ���ʼ�к�
     */
    public int getStartLineNumber() {
        return this.startLineNumber;
    }

    /**
     * ��ӱ�ע��
     * 
     * @param character ��
     * @param entityTag ����ʵ���ע
     */
    public void addTaggedCharacter(String character, String entityTag) {
        TaggedCharacter tc = new TaggedCharacter(character, entityTag);
        int offset = this.taggedCharacterMap.keySet().size();
        this.taggedCharacterMap.put(this.startLineNumber + offset, tc);
        this.content.append(character);
    }
    
    /**
     * ��ȡ�����ı�����
     */
    public String getContent() {
        return content.toString();
    }

    /**
     * ��ӱ�ע��
     * 
     * @param segment ��
     * @param posTag  ����
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
     * ��ȡ���зִʺʹ��Ա�ע�Ĵ��б�
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
