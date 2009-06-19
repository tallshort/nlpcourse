package net.core;

/**
 * ��������ʵ���ע��Ϣ����
 */
public class TaggedCharacter {
    
    private String character;
    private String entityTag;

    /**
     * �����ע��
     * 
     * @param character ������
     * @param entityTag ʵ���ע
     */
    public TaggedCharacter(String character, String entityTag) {
        this.character = character;
        this.entityTag = entityTag;
    }
    
    /**
     * ��ȡ�ֵ��ı���ʾ
     */
    public String getCharacter() {
        return this.character;
    }

    /**
     * ��ȡʵ���ע
     */
    public String getEntityTag() {
        return entityTag;
    }
    
    @Override
    public String toString() {
        return this.character + " " + this.entityTag;
    }

}
