package weps.test.namedentity;

import java.io.File;
import java.io.FileOutputStream;
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
        try {
            int totalNum = 0; // ��¼�ܹ���Ԫ�����
            File f = new File("d:/mltest/source");// ԴĿ¼
            File fMatrix = new File("d:/mltest/result/" + "Chris_Brockett.m"); // ��¼�����ļ�
            OutputStream osMatrix = new FileOutputStream(fMatrix, false);
            File fColLabel = new File("d:/mltest/result/"
                    + "Chris_Brockett.clabel"); // ��¼����Ϣ
            OutputStream osCLabel = new FileOutputStream(fColLabel, false);
            File[] fileList = f.listFiles();
            String clabel = ""; // ��¼�������
            String result = ""; // ��¼����ľ����ַ���
            HashMap<String, WordInfo> map = new HashMap<String, WordInfo>();
            int lastIndex = 0;// ���ڼ�¼map�е��ʵ�������
            for (File file : fileList) {
                // String person=GetName(file.getName());
                if (file.isDirectory())
                    continue;
                DataProcess d = new DataProcess(file, map, lastIndex, totalNum);// ����ÿ��ʵ��
                result += d.Process() + "\n"; // ���б�ʾÿ�� ʵ��һ��
                lastIndex = d.GetIndex(); // �����б�ֵ
                totalNum = d.GetTotal(); // �����ܹ�Ԫ����
            }
            int rows = fileList.length;
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
