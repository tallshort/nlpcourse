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
        String segResult = segTag.process("日本前首相竹下登（右二）、全国人大常委会副委员长王光英（左一）、铁道部部长傅志寰（右一）在北京人民大会堂观看日本制造的高速列车模型。");
        assertEquals("日本/ns 前/f 首相/n 竹下登/nr （/w 右/f 二/m ）/w 、/w 全国人大常委会/nt 副/b 委员长/n 王光英/nr （/w 左/f 一/m ）/w 、/w 铁道部/nt 部长/n 傅志寰/nr （/w 右/f 一/m ）/w 在/p 北京/ns 人民大会堂/ns 观看/v 日本/ns 制造/v 的/u 高速/b 列车/n 模型/n 。/w ",
                segResult);
    }
    
    public void testSentence() {
        Sentence s = new Sentence(1);
        s.addTaggedCharacter("如", "N");
        s.addTaggedCharacter("今", "N");
        s.addTaggedCharacter("中", "B-ORG");
        s.addTaggedCharacter("科", "I-ORG");
        s.addTaggedCharacter("院", "I-ORG");
        s.addTaggedCharacter("计", "I-ORG");
        s.addTaggedCharacter("算", "I-ORG");
        s.addTaggedCharacter("所", "I-ORG");
        s.addTaggedSegment("如今", "t");
        s.addTaggedSegment("中科院", "n");
        s.addTaggedSegment("计算", "vn");
        s.addTaggedSegment("所", "n");
        
        assertEquals(1, s.getStartLineNumber());
        assertEquals("如今中科院计算所", s.getContent());
        
        assertEquals(1, s.getTaggedSegmentList().get(0).getStartLineNumber());
        assertEquals(2, s.getTaggedSegmentList().get(0).getEndLineNumber());
        assertEquals(3, s.getTaggedSegmentList().get(1).getStartLineNumber());
        assertEquals(5, s.getTaggedSegmentList().get(1).getEndLineNumber());
        assertEquals(6, s.getTaggedSegmentList().get(2).getStartLineNumber());
        assertEquals(7, s.getTaggedSegmentList().get(2).getEndLineNumber());
        assertEquals(8, s.getTaggedSegmentList().get(3).getStartLineNumber());
        assertEquals(8, s.getTaggedSegmentList().get(3).getEndLineNumber());
        
        assertEquals("t", s.getTaggedSegmentList().get(0).getPosTag());
        assertEquals("如", s.getTaggedSegmentList().get(0).getFirstChar());
        assertEquals("今", s.getTaggedSegmentList().get(0).getLastChar());
        assertEquals("N", s.getTaggedSegmentList().get(0).getEntityTag());
        assertEquals("n", s.getTaggedSegmentList().get(1).getPosTag());
        assertEquals("中", s.getTaggedSegmentList().get(1).getFirstChar());
        assertEquals("院", s.getTaggedSegmentList().get(1).getLastChar());
        assertEquals("B-ORG", s.getTaggedSegmentList().get(1).getEntityTag());
        
        assertEquals("如今 如 今 t N", s.getTaggedSegmentList().get(0).toString());
        assertEquals("中科院 中 院 n B-ORG", s.getTaggedSegmentList().get(1).toString());
        assertEquals("计算 计 算 vn I-ORG", s.getTaggedSegmentList().get(2).toString());
        assertEquals("所 所 所 n I-ORG", s.getTaggedSegmentList().get(3).toString());
        
        assertEquals("如今 如 今 t N\n中科院 中 院 n B-ORG\n计算 计 算 vn I-ORG\n所 所 所 n I-ORG\n",
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
        assertEquals("[如今 如 今 t N\n中科院 中 院 n B-ORG\n计算 计 算 vn I-ORG\n所 所 所 n I-ORG\n, " +
        		"如今 如 今 t N\n中科院 中 院 n B-ORG\n计算 计 算 vn I-ORG\n所 所 所 n I-ORG\n, " +
        		"如今 如 今 t N\n中科院 中 院 n B-ORG\n计算 计 算 vn I-ORG\n所 所 所 n I-ORG\n]",
                result.toString());
    }
}
