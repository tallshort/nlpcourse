package net.core;

/**
 * 具有命名实体标注信息的字
 */
public class TaggedCharacter {
    
    private String character;
    private String entityTag;

    /**
     * 构造标注字
     * 
     * @param character 字内容
     * @param entityTag 实体标注
     */
    public TaggedCharacter(String character, String entityTag) {
        this.character = character;
        this.entityTag = entityTag;
    }
    
    /**
     * 获取字的文本表示
     */
    public String getCharacter() {
        return this.character;
    }

    /**
     * 获取实体标注
     */
    public String getEntityTag() {
        return entityTag;
    }
    
    @Override
    public String toString() {
        return this.character + " " + this.entityTag;
    }

}
