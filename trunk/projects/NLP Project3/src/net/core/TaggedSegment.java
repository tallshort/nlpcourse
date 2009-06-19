package net.core;

/**
 * 具有命名实体、词性标注信息的分词
 */
public class TaggedSegment {
    
    private int startLineNumber;
    private String segment;
    private String posTag;
    private String entityTag;

    public TaggedSegment(int startLineNumber, String segment, String posTag,
            String entityTag) {
        this.startLineNumber = startLineNumber;
        this.segment = segment;
        this.posTag = posTag;
        this.entityTag = entityTag;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }
    
    public int getEndLineNumber() {
        return startLineNumber + segment.length() - 1;
    }

    public String getSegment() {
        return segment;
    }

    public String getPosTag() {
        return posTag;
    }

    public String getEntityTag() {
        return entityTag;
    }

    public String getFirstChar() {
        return "" + this.segment.charAt(0);
    }
    
    public String getLastChar() {
        return "" + this.segment.charAt(this.segment.length() - 1);
    }
    
    @Override
    public String toString() {
        return this.segment
            + " " + this.getFirstChar() + " " + this.getLastChar() 
            + " " + this.getPosTag() + " " + this.getEntityTag();
    }

}
