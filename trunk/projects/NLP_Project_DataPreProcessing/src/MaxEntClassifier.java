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
        double rightNum = 0; // 微平均 记录正确的个数
        double totalNum = 0; // 微平均 记录总共的个数
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
        wordList.add("中医");
        wordList.add("使");
        wordList.add("儿女");
        wordList.add("出");
        wordList.add("动");
        wordList.add("动摇");
        wordList.add("单位");
        wordList.add("发");
        wordList.add("叫");
        wordList.add("吃");
        wordList.add("天地");
        wordList.add("带");
        wordList.add("平息");
        wordList.add("开通");
        wordList.add("想");
        wordList.add("成立");
        wordList.add("挑");
        wordList.add("推翻");
        wordList.add("旗帜");
        wordList.add("日子");
        wordList.add("望");
        wordList.add("本");
        wordList.add("机组");
        wordList.add("气息");
        wordList.add("气象");
        wordList.add("牌子");
        wordList.add("看");
        wordList.add("眼光");
        wordList.add("菜");
        wordList.add("补");
        wordList.add("表面");
        wordList.add("说明");
        wordList.add("赶");
        wordList.add("进");
        wordList.add("道");
        wordList.add("镜头");
        wordList.add("长城");
        wordList.add("队伍");
        wordList.add("震惊");
        wordList.add("面");
        return wordList;
    }

}
