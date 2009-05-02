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

        int classNum = 40; // ���÷������
        int[] num={42,18,18,1,27,41,38,62,58,80,47,14,54,82,32,65,33,50,69,81,20,24,38,56,35,41,72,71,51,90};
        File sourceDir = new File("merged_data_all2008_all"); // ���Դ���ݵĵط�
        File[] allFile = sourceDir.listFiles();
        //processDiscard(allFile,"merged_data_all2008_all");   //����ʧ���ļ����Զ����б�
        String lastName = allFile[0].getName();
        lastName = lastName.substring(0, lastName.lastIndexOf("_"));// ȡ�õ�ǰ����
        int lastPos = 0;
        int c=0;
        for (int i = 0; i < allFile.length; i++) {
            String currName = allFile[i].getName();
            currName = currName.substring(0, currName.lastIndexOf("_"));
            if (!currName.equals(lastName)) {
                System.out.println("���ڴ���:" + lastName);
                processAll(allFile, lastName, lastPos, i);// ��Ҫ������ļ����ش���
                lastPos = i;
                excuteClusty(num[c], lastName);
                lastName = currName; // ���¸��˽��о���
                c++;
            }
        }
        System.out.println("���ڴ���:" + lastName);
        processAll(allFile, lastName, lastPos, allFile.length);// �������һ����
        excuteClusty(num[29], lastName);
    }
    private static void processDiscard(File[] fileList,String path)
    {
    	int no=0;
    	String temp =fileList[0].getName();
    	String lastName = fileList[0].getName().substring(0, temp.lastIndexOf("_"));
    	for(int i=0;i<fileList.length;i++)
    	{
    		String currentName=fileList[i].getName();
    		currentName = currentName.substring(0, currentName.lastIndexOf("_"));
    		String tempNo =fileList[i].getName();
        	tempNo=tempNo.substring(tempNo.lastIndexOf("_")+1,tempNo.indexOf(".")); //ȡ���
        	int currentNo=Integer.parseInt(tempNo);
        	if(currentName.equals(lastName) && currentNo!=no )//�����ļ���ʧ
        	{
        		while(no<currentNo)
        		{
        			String fileNewName="";
        			if(no<10)
        			{
        				fileNewName=currentName+"_00"+no;
        			}
        			else
        			{
        				fileNewName=currentName+"_0"+no;
        			}
        			File f =new File(path+"/"+fileNewName+".txt");
        			try
        			{
        				OutputStream os=new FileOutputStream(f,false);
        				os.write("".getBytes());
        				os.close();
        			}
        			catch(Exception e)
        			{
        				e.printStackTrace();
        			}
        			no++;
        		}
        		no++;
        	}
        	else if(!currentName.equals(lastName))
        	{
        		no=1;   //��ÿ����������Ϣ
        		lastName=currentName;
        	}
        	else 
        		no++;
    	}
    }
    private static void excuteClusty(int classNum, String lastName) {
        try // ִ�������� ����ǰ ���� ������Ϣ
        {
            String s = "vcluster -clmethod=agglo "
                    + // ���ò�ξ���
                    "-clustfile=matrixFile\\" + lastName + "0"
                    + ".cluster "
                    //+"-agglofrom=80 "
                    //+"-rowmodel=log "
                    +"-sim=cos "
                    //+"-colmodel=idf " 
                    //+"-mincomponent=8 "
                    //+"cstype=best "  //�����㷨Ϊrb rbr graph�ſ���
                    //+"-nnbrs=80 "
                    +"-colprune=1 "   //**ѡ������Ӱ���������������*ϵ��
                    + "-crfun=upgma "// �������ļ�
                    +"-showtree " + "-showfeatures "
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
