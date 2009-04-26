package weps.test.namedentity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;

import edu.stanford.nlp.ie.crf.CRFClassifier;

public class MLProject2Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            HashSet<String> hs = new HashSet<String>();// Ϊ��ȥ���ظ����Ƚ�������� ����
            File resultFile = new File("����.txt"); // ����������д���ļ�
            OutputStream os = new FileOutputStream(resultFile, false);
            // TODO Auto-generated method stub
            /* Ҫ��������ַ���from��ֱ�Ӵ��ļ����������� */
            String from = "first service mortgagefirst service mortgage, u 1st service mortgage, tony bryant, debra tillman, college park, georgia mortgage, mortgage lenders, atlanta lenders, home banc, georgia lenders, union city, tucker, alabama, new orleans, equal housing lender, hud homes, refi, art huntley, lenders, namb, fha, va, remax, investor loans, u first realty, mortgage lenders, free, FREE, atlanta mortgage loans, georgia mortgage loans, louisiana mortgage loans, alabama mortgage loans, birmingham mortgage loans, tuscaloosa mortgage loans, new orleans mortgage loans, atlanta mortgage companies, georgia mortgage companies, louisiana mortgage companies, alabama mortgage companies, birmingham mortgage companies, hud homes, hud home loans, georgia foreclosures, alabama foreclosures, louisiana foreclosures, investor loans, first time home buyer loans, relocation loans, commercial loans, 80/20 loans, fha loans, va loans, immigrant loans, down payment assistance, construction loansofficial website of First Service Mortgage offering all types of mortgages";
            /*
             * �żᣬע���£�������������Ҫ��ȡһ��ģ�ͣ�ʱ��Ƚϳ����10-30�룬���ڴ����ļ���ʱ������
             * ���ô��ݽ�������˼����ֻ��Ӳ�̶�ȡһ�Σ�����Ҫ����
             */
            CRFClassifier c = CRFClassifier
                    .getClassifier("asset/ner-eng-ie.crf-3-all2008-distsim.ser.gz");
            String r = c.classifyToString(from);// rΪ�Ѿ�������е� �ַ���

            /* ���沿�������COPY����ĳ����result��ΪҪд����ַ��������������� "|" ���ŷָ��ÿ���� */
            String result = "";// ����ַ���
            String[] res = r.split(" ");// �ո�ָ�ÿ���ַ���
            for (String s : res) {
                int pos1 = s.indexOf("/");
                String p = s.substring(pos1 + 1);
                if (p.equals("LOCATION") || p.equals("PERSON")
                        || p.equals("ORGANIZATION"))// ��������Ϊ����ʵ��
                {
                    hs.add(s.substring(0, pos1));
                    System.out.println(s);
                }
            }
            // �Ӽ���hs�ж���ÿ������
            for (String temp : hs) {
                result += temp + "|";
            }
            result = result.substring(0, result.length() - 2);
            os.write(result.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(abc);
        // System.out.println(abc);

    }

}
