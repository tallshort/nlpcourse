package weps.test.namedentity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class runMain {

    /**
     * @param args
     */
    /*
     * �������������� 1.��ȡÿ���ļ����ݣ�Ϊÿά���鴴��WordInfo
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        /* �Ƚ������ļ�д��.m �� .clabel�ļ� Ȼ�����exe����ִ��CLUTO�������з����ļ� */

        int classNum = 30; // ���÷������
        File sourceDir = new File("clutoRawData"); // ���Դ���ݵĵط�
        File[] allFile = sourceDir.listFiles();
        String lastName = allFile[0].getName();
        lastName = lastName.substring(0, lastName.lastIndexOf("_"));// ȡ�õ�ǰ����
        int lastPos = 0;
        for (int i = 0; i < allFile.length; i++) {
            String currName = allFile[i].getName();
            currName = currName.substring(0, currName.lastIndexOf("_"));
            if (!currName.equals(lastName)) {
                System.out.println("���ڴ���:" + lastName);
                processAll(allFile, lastName, lastPos, i);// ��Ҫ������ļ����ش���
                lastPos = i;
                excuteClusty(classNum, lastName);
                lastName = currName; // ���¸��˽��о���
            }
        }
        System.out.println("���ڴ���:" + lastName);
        processAll(allFile, lastName, lastPos, allFile.length);// �������һ����
        excuteClusty(classNum, lastName);
    }

    private static void excuteClusty(int classNum, String lastName) {
        try // ִ�������� ����ǰ ���� ������Ϣ
        {
            String s = "vcluster -clmethod=rb "
                    + // ���ò�ξ���
                    "-clustfile=matrixFile\\" + lastName + classNum
                    + ".cluster "
                    + // �������ļ�
                    "-showtree " + "-showfeatures "
                    + "-clabelfile=matrixFile\\" + lastName + ".clabel " + // ����б��ļ�
                    "matrixFile\\" + lastName + ".m " + classNum;
            Process p = Runtime.getRuntime().exec(s); // Ҫ����ľ����ļ�
            BufferedReader in = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String result = "";
            String temp = in.readLine();
            while (temp != null) {
                result += temp;
                temp = in.readLine();
            }
            File f = new File("detail/" + lastName + ".detail"); // ������ľ�����Ϣд��detailĿ¼��
            OutputStream osDetail = new FileOutputStream(f, false);
            osDetail.write(result.getBytes());
            osDetail.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processAll(File[] files, String Name, int from, int end) {
        try {
            int totalNum = 0; // ��¼�ܹ���Ԫ�����
            File fMatrix = new File("matrixFile\\" + Name + ".m"); // ��¼�����ļ�
            OutputStream osMatrix = new FileOutputStream(fMatrix, false);
            File fColLabel = new File("matrixFile\\" + Name + ".clabel"); // ��¼����Ϣ
            OutputStream osCLabel = new FileOutputStream(fColLabel, false);
            String clabel = ""; // ��¼�������
            String result = ""; // ��¼����ľ����ַ���
            HashMap<String, WordInfo> map = new HashMap<String, WordInfo>();
            int lastIndex = 1;// ���ڼ�¼map�е��ʵ�������
            for (int i = from; i < end; i++) {
                // String person=GetName(file.getName());
                DataProcess d = new DataProcess(files[i], map, lastIndex,
                        totalNum);// ����ÿ��ʵ��
                result += d.Process() + "\n"; // ���б�ʾÿ�� ʵ��һ��
                lastIndex = d.GetIndex(); // �����б�ֵ
                totalNum = d.GetTotal(); // �����ܹ�Ԫ����
            }
            int rows = end - from;
            int cols = map.size();
            result = rows + " " + cols + " " + totalNum + "\n" + result;
            clabel = GetLabel(map);
            osMatrix.write(result.getBytes());
            osCLabel.write(clabel.getBytes());
            osMatrix.close(); // д���ļ�
            osCLabel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetLabel(HashMap<String, WordInfo> map)
    // ��Ҫ��map����������ȡ��������value�����е�position��������key�������
    {
        String label = "";
        for (int i = 0; i < map.size(); i++) {
            Set<Entry<String, WordInfo>> set = map.entrySet();
            Iterator<Entry<String, WordInfo>> it = set.iterator();
            while (it.hasNext()) {
                Entry<String, WordInfo> entry = it.next();
                if (entry.getValue().Get() == i) {
                    label += entry.getKey() + "\n";
                    break;
                }
            }
        }
        return label;
    }

    public static String GetName(String name) {
        int pos1 = name.lastIndexOf("_");// ��ȡǰ�沿��Ϊ��ǰ����
        return name.substring(0, pos1);
    }

}
