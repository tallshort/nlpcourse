import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        String[] algorithms = { "SMO" };
//        for (String algorithm : algorithms) {
//            for (int i = -3; i <= 0; i++) {
//                for (int j = 0; j <= 3; j++) {
//                    if (Math.abs(i - j) >= 1) {
//                        classify(i, j, algorithm);
//                        compare(i, j, algorithm);
//                    }
//                }
//            }
//        }
        classify(-5, 5, "NaiveBayes");
        compare(-5, 5, "NaiveBayes");
    }

    private static void classify(int startOffset, int endOffset,
            String algorithm) {
        // TODO Auto-generated method stub
        Reader r = new Reader("Chinese_train_pos.xml"); // ���������Ƿ���һ��HASHMAP
        // ��¼
        // �����дʺ�����ֵ����
        HashMap<String, String> h = r.read();
        /* ���������ļ��ֱ�Ϊ �Ѿ��������� ѵ�����ļ� �� ���Լ��ļ������ż����õ���Щ�ļ� */
        File f = new File("Chinese_train_pos.xml_" + startOffset + "_"
                + endOffset + ".txt");
        File ft = new File("Chinese_test_pos.xml_" + startOffset + "_"
                + endOffset + ".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            BufferedReader br1 = new BufferedReader(new FileReader(ft));
            String s = br.readLine();
            String s1 = br1.readLine();
            int num = 0;
            while (s != null || s1 != null) // ѭ��ÿ������ ÿ��������һ��������
            {
                List<String> train = new ArrayList<String>();
                List<String> test = new ArrayList<String>();
                while (!s.equals("<END>") && s != null) // ����һ���ʵ�ѵ������
                {
                    // System.out.println("s="+s);
                    train.add(s);
                    s = br.readLine();
                }
                // System.out.println("\n");
                while (!s1.equals("<END>") && s1 != null) // ����һ���ʵĲ�������
                {
                    // System.out.println("s1="+s1);
                    test.add(s1);
                    s1 = br1.readLine();
                }
                // System.out.println("���ڴ���ĵ��ʣ�" + h.get(String.valueOf(num)));
                /*
                 * ��ÿ���ʹ���һ������������Ҫ4�������� 1 train ����������ļ������һ�����ʵ�ѵ���� LIST 2 test :
                 * ������ļ������һ�����ʵĲ��Լ� 3 ���£�"SMO" ��Ϊ����������4�ַ�������ѡ�� ��1�� "NaiveBayes"
                 * ��Ҷ˹������ ��2�� "MP" ���ǰ�������� ��ע�⣺��ѵ��ʱ���ܳ��� ��3�� "SMO" ֧�������� ��4��
                 * "RBF" ����������� 4 �ôʵ����� �� ��"��ҽ"�ȣ������õ���ǰ��� �Ǹ�HASHMAP
                 */
                Classifer c = new Classifer(train, test, algorithm, h
                        .get(String.valueOf(num)), startOffset, endOffset);
                num++;
                s = br.readLine();
                s1 = br1.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // public Classifer(List<String> wordList,List<String> testList,String
        // model,String name)
    }

    private static void compare(int startOffset, int endOffset, String modelName) {
        File f1 = new File("ChineseLS.test.key");
        File f2 = new File("result-" + modelName + startOffset + endOffset
                + ".txt");
        try {
            int count = 0; // ��¼�ܽ������
            int equal = 0;
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            String s1 = br1.readLine();
            String s2 = br2.readLine();
            int cc = 0;
            while (s1 != null) {
                if (s1.equals(s2)) {
                    equal++;
                    count++;
                    // System.out.println(s1+"\t"+s2);
                    // System.out.println("�����"+count+"�����...���");

                } else {
                    count++;
                    // System.out.println("�����"+count+"�����...�����");
                    // System.out.println(s1+"\t"+s2);
                    // Thread.sleep(300);
                }
                // System.out.println("�����"+count+"�����...");
                s1 = br1.readLine();
                s2 = br2.readLine();
            }
            double x = (double) equal / count;
            System.out.println("ģ�ͣ�" + modelName + " ��Χ��" + startOffset
                    + endOffset + "��ȷ��Ϊ��" + x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
