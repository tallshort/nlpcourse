import junit.framework.TestCase;

public class DataProcessingUnitTest extends TestCase {
    public void testDataProcessing() {
        DataProcessing dp = new DataProcessing();
//      dp.setClassLabelFirst(true);
//      dp.setSeparator(" ");
//      dp.setOnefilePerItem(true);
//      dp.setIncludeToken(true);
        generateData(dp, -2, 3);
        assertData(-2, 3);
    }
    
    private void assertData(int startOffset, int endOffset) {
        String postfix = "_" + startOffset + "_" + endOffset + ".txt";
        assertEquals(TextFile.read("Chinese_train_pos.xml" + postfix), 
                TextFile.read("baseline/Chinese_train_pos.xml" + postfix));
        assertEquals(TextFile.read("Chinese_test_pos.xml" + postfix), 
                TextFile.read("baseline/Chinese_test_pos.xml" + postfix));
    }
    
    private void generateData(DataProcessing dp, int startOffset, int endOffset) {
        dp.process("Chinese_train_pos.xml", startOffset, endOffset);
        dp.process("Chinese_test_pos.xml", startOffset, endOffset);
    }
}
