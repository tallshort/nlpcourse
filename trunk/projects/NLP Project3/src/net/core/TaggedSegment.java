package net.core;

/**
 * ��������ʵ�塢���Ա�ע��Ϣ�ķִ�
 */
public class TaggedSegment {
    
    private int startLineNumber;
    private String segment;
    private String posTag;
    private String entityTag;

    /**
     * ����ִ�
     * 
     * @param startLineNumber ��ʼ�к�
     * @param segment ���ı�
     * @param posTag ����
     * @param entityTag  ʵ���ע
     */
    public TaggedSegment(int startLineNumber, String segment, String posTag,
            String entityTag) {
        this.startLineNumber = startLineNumber;
        this.segment = segment;
        this.posTag = posTag;
        this.entityTag = entityTag;
    }

    /**
     * ��ȡ��ʼ�к�
     */
    public int getStartLineNumber() {
        return startLineNumber;
    }
    
    /**
     * ��ȡ��ֹ�к�
     */
    public int getEndLineNumber() {
        return startLineNumber + segment.length() - 1;
    }

    /**
     * ��ȡ���ı�
     */
    public String getSegment() {
        return segment;
    }

    /**
     * ��ȡ����
     */
    public String getPosTag() {
        return posTag;
    }

    /**
     * ��ȡʵ���ע
     */
    public String getEntityTag() {
        return entityTag;
    }

    /**
     * ��ȡ��һ�����ı�
     */
    public String getFirstChar() {
        return "" + this.segment.charAt(0);
    }
    
    /**
     * ��ȡ���һ�����ı�
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
