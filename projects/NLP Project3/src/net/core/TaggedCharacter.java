package net.core;

/**
 * ��������ʵ���ע��Ϣ���ַ�
 */
public class TaggedCharacter {
    
    private String character;
    private String entityTag;

    public TaggedCharacter(String character, String entityTag) {
        this.character = character;
        this.entityTag = entityTag;
    }
    
    public String getCharacter() {
        return this.character;
    }

    public String getEntityTag() {
        return entityTag;
    }
    
    @Override
    public String toString() {
        return this.character + " " + this.entityTag;
    }

}
