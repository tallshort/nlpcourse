package net.test;

import java.util.List;

import junit.framework.TestCase;
import net.core.SegTag;
import net.core.Sentence;
import net.core.TaggingExpander;
import net.util.TextFile;


public class UnitTest extends TestCase {
    
    public void testSegTag() {
        SegTag segTag = new SegTag();
        String segResult = segTag.process("�ձ�ǰ�������µǣ��Ҷ�����ȫ���˴�ί�ḱίԱ������Ӣ����һ����������������־徣���һ���ڱ����������ùۿ��ձ�����ĸ����г�ģ�͡�");
        assertEquals("�ձ�/ns ǰ/f ����/n ���µ�/nr ��/w ��/f ��/m ��/w ��/w ȫ���˴�ί��/nt ��/b ίԱ��/n ����Ӣ/nr ��/w ��/f һ/m ��/w ��/w ������/nt ����/n ��־�/nr ��/w ��/f һ/m ��/w ��/p ����/ns ��������/ns �ۿ�/v �ձ�/ns ����/v ��/u ����/b �г�/n ģ��/n ��/w ",
                segResult);
    }
    
    public void testSentence() {
        Sentence s = new Sentence(1);
        s.addTaggedCharacter("��", "N");
        s.addTaggedCharacter("��", "N");
        s.addTaggedCharacter("��", "B-ORG");
        s.addTaggedCharacter("��", "I-ORG");
        s.addTaggedCharacter("Ժ", "I-ORG");
        s.addTaggedCharacter("��", "I-ORG");
        s.addTaggedCharacter("��", "I-ORG");
        s.addTaggedCharacter("��", "I-ORG");
        s.addTaggedSegment("���", "t");
        s.addTaggedSegment("�п�Ժ", "n");
        s.addTaggedSegment("����", "vn");
        s.addTaggedSegment("��", "n");
        
        assertEquals(1, s.getStartLineNumber());
        assertEquals("����п�Ժ������", s.getContent());
        
        assertEquals(1, s.getTaggedSegmentList().get(0).getStartLineNumber());
        assertEquals(2, s.getTaggedSegmentList().get(0).getEndLineNumber());
        assertEquals(3, s.getTaggedSegmentList().get(1).getStartLineNumber());
        assertEquals(5, s.getTaggedSegmentList().get(1).getEndLineNumber());
        assertEquals(6, s.getTaggedSegmentList().get(2).getStartLineNumber());
        assertEquals(7, s.getTaggedSegmentList().get(2).getEndLineNumber());
        assertEquals(8, s.getTaggedSegmentList().get(3).getStartLineNumber());
        assertEquals(8, s.getTaggedSegmentList().get(3).getEndLineNumber());
        
        assertEquals("t", s.getTaggedSegmentList().get(0).getPosTag());
        assertEquals("��", s.getTaggedSegmentList().get(0).getFirstChar());
        assertEquals("��", s.getTaggedSegmentList().get(0).getLastChar());
        assertEquals("N", s.getTaggedSegmentList().get(0).getEntityTag());
        assertEquals("n", s.getTaggedSegmentList().get(1).getPosTag());
        assertEquals("��", s.getTaggedSegmentList().get(1).getFirstChar());
        assertEquals("Ժ", s.getTaggedSegmentList().get(1).getLastChar());
        assertEquals("B-ORG", s.getTaggedSegmentList().get(1).getEntityTag());
        
        assertEquals("��� �� �� t N", s.getTaggedSegmentList().get(0).toString());
        assertEquals("�п�Ժ �� Ժ n B-ORG", s.getTaggedSegmentList().get(1).toString());
        assertEquals("���� �� �� vn I-ORG", s.getTaggedSegmentList().get(2).toString());
        assertEquals("�� �� �� n I-ORG", s.getTaggedSegmentList().get(3).toString());
        
        assertEquals("��� �� �� t N\n�п�Ժ �� Ժ n B-ORG\n���� �� �� vn I-ORG\n�� �� �� n I-ORG\n",
                s.toString());
    }
    
    public void testTaggingMerger() {
        String dataFile = "fixture/test.txt";
        TaggingExpander expander = new TaggingExpander();
        List<Sentence> result = expander.expand(new TextFile(dataFile));
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getStartLineNumber());
        assertEquals(10, result.get(1).getStartLineNumber());
        assertEquals(19, result.get(2).getStartLineNumber());
        assertEquals("[��� �� �� t N\n�п�Ժ �� Ժ n B-ORG\n���� �� �� vn I-ORG\n�� �� �� n I-ORG\n, " +
        		"��� �� �� t N\n�п�Ժ �� Ժ n B-ORG\n���� �� �� vn I-ORG\n�� �� �� n I-ORG\n, " +
        		"��� �� �� t N\n�п�Ժ �� Ժ n B-ORG\n���� �� �� vn I-ORG\n�� �� �� n I-ORG\n]",
                result.toString());
    }
}
