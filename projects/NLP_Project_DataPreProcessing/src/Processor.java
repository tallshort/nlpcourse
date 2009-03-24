public class Processor {

    public static void main(String[] args) {
        DataProcessing dp = new DataProcessing();
        for (int i = -3; i <= 0; i++) {
            for (int j = 0; j <= 3; j++) {
                // At least two tokens as the features
                if (Math.abs(i - j) >= 2) {
                    generateData(dp, i, j);
                }
            }
        }
        // generateData(dp, -1, 1);
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
