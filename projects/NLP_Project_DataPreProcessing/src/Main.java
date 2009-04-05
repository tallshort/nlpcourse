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
        String[] algorithms = { "NaiveBayes" };
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
        //classify(-1, 1, "SMO");
        compare(-1, 1, 1,1, "SMO");
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

    private static void compare(int startOffset, int endOffset, int includeTokenPreOffset, int includeTokenPostOffset, String modelName) {
        File f1 = new File("ChineseLS.test.key");
        File f2 = new File("result-" + modelName + startOffset + endOffset + includeTokenPreOffset + includeTokenPostOffset + ".txt");
        try {
        	double right=0;//��¼��ƽ����ȷ��
        	double rightNum=0; //�Ե����ʵ���ȷ��
        	double totalNum=0; //�����ʵ���������
        	String lastWord="";//��¼�ϸ�����
            int count = 0; // ��¼�ܽ������
            int equal = 0;
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            String s1 = br1.readLine();
            String s2 = br2.readLine();
            lastWord=s1.substring(0, s1.indexOf(" "));
            int cc = 0;
            while (s1 != null) {
            	if(lastWord.equals(s1.subSequence(0, s1.indexOf(" ")))) //��ͬһ����
            	{
            		totalNum++;
            		if (s1.equals(s2)) {
                        equal++;
                        count++;
                        rightNum++;
                    } else {
                        count++;
                    }
            	}
            	else
            	{
            		double temp=rightNum/totalNum;
            		System.out.println(lastWord+"  ��ȷ�ʣ�"+temp);
            		right += temp;
            		totalNum=1; //�¸��ʵ����� �� 0
            		lastWord=s1.substring(0, s1.indexOf(" ")); //��¼��ǰ��
            		rightNum=0;
	                if (s1.equals(s2)) {
	                    equal++;
	                    count++;
	                    rightNum++;
	                } else {
	                    count++;
	                }
	              
            	}
                s1 = br1.readLine();
                s2 = br2.readLine();
            }
            double x = (double) equal / count;
            System.out.println(lastWord+"  ��ȷ�ʣ�"+rightNum/totalNum);
            right +=rightNum/totalNum;
            System.out.println("ģ�ͣ�" + modelName + " ��Χ��" + startOffset
                    + endOffset + "\n΢ƽ����ȷ��Ϊ��" + x+"\n��ƽ����ȷ��Ϊ:"+right/40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
