import java.util.ArrayList;
import java.util.List;

public class Processor {

    public static void main(String[] args) {     
        DataProcessing dp = new DataProcessing();
        // For maxent
        dp.setClassLabelFirst(true);
        dp.setSeparator(" ");
        dp.setOnefilePerItem(true);
        dp.setIncludeTokenPreOffset(1);
        dp.setIncludeTokenPostOffset(3);
        dp.setIncludeToken(true);
        // dp.setTargetWord("³ö");
//        for (int i = -3; i <= 0; i++) {
//            for (int j = 0; j <= 3; j++) {
//                // At least one tokens as the features
//                if (Math.abs(i - j) >= 1) {
//                    generateData(dp, i, j);
//                }
//            }
//        }
        generateData(dp, -1, 2);
        // System.out.println(dp.getTotalWordSet());
    }

    /**
     * @param start
     *            the start offset relative to the target token
     * @param end
     *            the end offset relative to the target token
     */
    public static void generateData(DataProcessing dp, int start, int end) {
        dp.process("Chinese_train_pos.xml", start, end);
        dp.process("Chinese_test_pos.xml", start, end);
    }
    
  
}
