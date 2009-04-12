import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaxEntClassifier {

    private double globalRightNum;
    private double globalTotalSum;
    private ArrayList<Double> globalAccuarcyList = new ArrayList<Double>();

    private double g = 20;

    public static void main(String[] args) {
        int startOffset = -1;
        int endOffset = 2;
        int includeTokenPreOffset = -1;
        int includeTokenPostOffset = 3;
        double g = 20;
        
        MaxEntClassifier c = new MaxEntClassifier();
        c.setG(g);
        
        // Verbs
        List<String> wordList = getVerbWordList();
        c.runMaxEnt(wordList, startOffset, endOffset, includeTokenPreOffset,
                includeTokenPostOffset);
        // Nouns
        wordList = getNounWordList();
        startOffset = -1;
        endOffset = 1;
        includeTokenPreOffset = -1;
        includeTokenPostOffset = 2;
         c.runMaxEnt(wordList, startOffset, endOffset, includeTokenPreOffset,
                includeTokenPostOffset);

        c.summarize();
    }

    /**
     * ���΢ƽ���ͺ�ƽ����ͳ����Ϣ
     */
    public void summarize() {
        System.out.println("==============================================");
        double sum = 0;
        for (Double accuracy : this.globalAccuarcyList) {
            sum = sum + accuracy;
        }
        System.out.println("Global macro average: " + sum
                / globalAccuarcyList.size());
        System.out.println("Global right sum: " + this.globalRightNum + " "
                + "Global total sum: " + this.globalTotalSum);
        System.out.println("Global micro average: " + this.globalRightNum
                / this.globalTotalSum);
        System.out.println("==============================================");
    }

    /**
     * ���ո�����������MaxEnt������¼��ͳ����Ϣ
     * 
     * @param wordList
     *            ��������б�
     * @param startOffset
     *            ����ڴ�����ʵ�POS��ʼƫ����
     * @param endOffset
     *            ����ڴ�����ʵ�POS��βƫ����
     * @param includeTokenPreOffset
     *            �����POS��ʼƫ������TOKEN��ʼƫ����
     * @param includeTokenPostOffset
     *            �����POS��ʼƫ������TOKEN��βƫ����
     */
    public void runMaxEnt(List<String> wordList, int startOffset,
            int endOffset, int includeTokenPreOffset, int includeTokenPostOffset) {
        int iteration = 100;
        double rightNum = 0; // ΢ƽ�� ��¼�ض�����ȷ�ĸ���
        double totalNum = 0; // ΢ƽ�� ��¼�ض����ܹ��ĸ���
        double rightSum = 0; // ΢ƽ�� ��¼��ȷ�ĸ���
        double totalSum = 0; // ΢ƽ�� ��¼�ܹ��ĸ���
        ArrayList<Double> accuarcyList = new ArrayList<Double>();

        for (String word : wordList) {
            try {
                String postfix = startOffset + "_" + endOffset + "_"
                        + includeTokenPreOffset + "_" + includeTokenPostOffset
                        + "_" + word + ".txt";
                String train = "Chinese_train_pos.xml_" + postfix;
                String test = "Chinese_test_pos.xml_" + postfix;
                String model = "model_" + postfix;
                String out = "out_" + postfix;

                double maxAccuracy = -1;
                Process p = Runtime.getRuntime().exec(
                        "maxent -i " + iteration + " -g " + g + " -m " + model
                                + " -o " + out + " " + train + " " + test);
                BufferedReader in = new BufferedReader(new InputStreamReader(p
                        .getInputStream()));

                String strProc = null;
                Pattern pattern = Pattern
                        .compile("Accuracy:(.+)% [(](\\d+)/(\\d+)[)]");
                while ((strProc = in.readLine()) != null) {
                    Matcher matcher = pattern.matcher(strProc);
                    if (matcher.find()) {
                        double accuracy = Double.parseDouble(matcher.group(1));
                        rightNum = Double.parseDouble(matcher.group(2));
                        totalNum = Double.parseDouble(matcher.group(3));
                        if (accuracy > maxAccuracy) {
                            maxAccuracy = accuracy;
                        }
                        // System.out.println(strProc);
                    }
                }
                in.close();
                rightSum += rightNum;
                this.globalRightNum += rightNum;
                totalSum += totalNum;
                this.globalTotalSum += totalNum;
                accuarcyList.add(maxAccuracy);
                this.globalAccuarcyList.add(maxAccuracy);
                System.out.println(word + ": " + maxAccuracy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        double sum = 0;
        for (Double accuracy : accuarcyList) {
            sum = sum + accuracy;
        }
        System.out.println("==============================================");
        System.out.println("G: " + g); 
        System.out.println("Feature: " + startOffset + endOffset
                + includeTokenPreOffset + includeTokenPostOffset);
        System.out.println("Macro average: " + sum / wordList.size());
        System.out.println("Right sum: " + rightSum + " " + "Total sum: "
                + totalSum);
        System.out.println("Micro average: " + rightSum / totalSum);
        System.out.println("==============================================");
    }

    public static List<String> getVerbWordList() {
        List<String> wordList = new ArrayList<String>();
        wordList.add("��");
        wordList.add("����");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��ҡ");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��ͨ");
        wordList.add("��");
        wordList.add("ƽϢ");
        wordList.add("ʹ");
        wordList.add("˵��");
        wordList.add("��");
        wordList.add("�Ʒ�");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��");
        return wordList;
    }

    public static List<String> getNounWordList() {
        List<String> wordList = new ArrayList<String>();
        wordList.add("��");
        wordList.add("����");
        wordList.add("��");
        wordList.add("����");
        wordList.add("��λ");
        wordList.add("��");
        wordList.add("����");
        wordList.add("��Ů");
        wordList.add("����");
        wordList.add("��ͷ");
        wordList.add("��");
        wordList.add("����");
        wordList.add("����");
        wordList.add("��Ϣ");
        wordList.add("����");
        wordList.add("����");
        wordList.add("���");
        wordList.add("�۹�");
        wordList.add("��ҽ");
        return wordList;
    }

    public double getG() {
        return g;
    }

    /**
     * ����Gaussian prior��ȡֵ
     * 
     * @param g
     */
    public void setG(double g) {
        this.g = g;
    }

}
