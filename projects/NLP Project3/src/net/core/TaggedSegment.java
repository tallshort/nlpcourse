package net.core;

/**
 * 具有命名实体、词性标注信息的分词
 */
public class TaggedSegment {
    
    private int startLineNumber;
    private String segment;
    private String posTag;
    private String entityTag;

    /**
     * 构造分词
     * 
     * @param startLineNumber 起始行号
     * @param segment 词文本
     * @param posTag 词性
     * @param entityTag  实体标注
     */
    public TaggedSegment(int startLineNumber, String segment, String posTag,
            String entityTag) {
        this.startLineNumber = startLineNumber;
        this.segment = segment;
        this.posTag = posTag;
        this.entityTag = entityTag;
    }

    /**
     * 获取起始行号
     */
    public int getStartLineNumber() {
        return startLineNumber;
    }
    
    /**
     * 获取终止行号
     */
    public int getEndLineNumber() {
        return startLineNumber + segment.length() - 1;
    }

    /**
     * 获取词文本
     */
    public String getSegment() {
        return segment;
    }

    /**
     * 获取词性
     */
    public String getPosTag() {
        return posTag;
    }

    /**
     * 获取实体标注
     */
    public String getEntityTag() {
        return entityTag;
    }

    /**
     * 获取第一个字文本
     */
    public String getFirstChar() {
        return "" + this.segment.charAt(0);
    }
    
    /**
     * 获取最后一个字文本
     */
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
