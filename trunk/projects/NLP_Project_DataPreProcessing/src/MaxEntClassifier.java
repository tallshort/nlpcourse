import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaxEntClassifier {

    public static void main(String[] args) {
        List<String> wordList = getWordList();
        int iteration = 100;
        int startOffset = -1;
        int endOffset = 2;
        int includeTokenPreOffset = 1;
        int includeTokenPostOffset = 3;
        double rightNum = 0; // ΢ƽ�� ��¼��ȷ�ĸ���
        double totalNum = 0; // ΢ƽ�� ��¼�ܹ��ĸ���
        ArrayList<Double> accuarcyList = new ArrayList<Double>();

        for (String word : wordList) {
            try {
                String postfix = startOffset + "_" + endOffset + "_" + includeTokenPreOffset + "_" + includeTokenPostOffset + "_" + word
                        + ".txt";
                String train = "Chinese_train_pos.xml_" + postfix;
                String test = "Chinese_test_pos.xml_" + postfix;
                String model = "model_" + postfix;
                String out = "out_" + postfix;

                double maxAccuracy = -1;
                out: for (double g = 0.5; g <= 6; g += 0.5) {
                    Process p = Runtime.getRuntime().exec(
                            "maxent -i " + iteration + " -g " + g + " -m "
                                    + model + " -o " + out + " " + train + " "
                                    + test);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(p.getInputStream()));

                    String strProc = null;
                    Pattern pattern = Pattern
                            .compile("Accuracy:(.+)% [(](\\d+)/(\\d+)[)]");
                    while ((strProc = in.readLine()) != null) {
                        Matcher matcher = pattern.matcher(strProc);
                        if (matcher.find()) {
                            double accuracy = Double.parseDouble(matcher
                                    .group(1));
                            rightNum += Double.parseDouble(matcher.group(2));
                            totalNum += Double.parseDouble(matcher.group(3));
                            if (accuracy > maxAccuracy) {
                                maxAccuracy = accuracy;
                            }
                        }
                    }
                    in.close();
                }
                accuarcyList.add(maxAccuracy);
                System.out.println(word + ": " + maxAccuracy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        double sum = 0;
        for (Double accuracy : accuarcyList) {
            sum = sum + accuracy;
        }
        System.out.println("Macro average: " + sum / 40);
        System.out.println("Micro average: " + rightNum / totalNum);
    }

    private static List<String> getWordList() {
        List<String> wordList = new ArrayList<String>();
        wordList.add("��ҽ");
        wordList.add("ʹ");
        wordList.add("��Ů");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��ҡ");
        wordList.add("��λ");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��");
        wordList.add("���");
        wordList.add("��");
        wordList.add("ƽϢ");
        wordList.add("��ͨ");
        wordList.add("��");
        wordList.add("����");
        wordList.add("��");
        wordList.add("�Ʒ�");
        wordList.add("����");
        wordList.add("����");
        wordList.add("��");
        wordList.add("��");
        wordList.add("����");
        wordList.add("��Ϣ");
        wordList.add("����");
        wordList.add("����");
        wordList.add("��");
        wordList.add("�۹�");
        wordList.add("��");
        wordList.add("��");
        wordList.add("����");
        wordList.add("˵��");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��");
        wordList.add("��ͷ");
        wordList.add("����");
        wordList.add("����");
        wordList.add("��");
        wordList.add("��");
        return wordList;
    }

}
