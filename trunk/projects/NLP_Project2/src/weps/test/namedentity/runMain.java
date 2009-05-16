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
    	String dataDir="merged_data_body0.6_url_des_NE";
        int classNum = 40; // ���÷������
        int[] num={42,18,18,1,27,41,38,62,58,80,47,14,54,82,32,65,33,50,69,81,20,24,38,1,35,41,72,71,51,90};
        int[] numZhang={52,49,43,43,59,56,47,68,58,56,52,41,70,69,60,61,55,54,65,57,44,48,71,28,57,61,54,63,59,62};
        int[] numZhang2={49,43,33,38,55,51,44,62,49,49,41,32,60,62,44,50,46,47,53,48,42,42,62,21,50,51,42,57,52,52};
        int[] numZhang15={38,34,28,25,36,36,36,51,33,38,30,24,47,45,38,43,35,31,44,41,34,31,40,14,43,41,32,41,43,38};
        HashMap<String,String> name=new HashMap<String,String>();
        HashClass(name);
        File sourceDir = new File(dataDir); // ���Դ���ݵĵط�
        File[] all = sourceDir.listFiles();
        processDiscard(all,dataDir);   //����ʧ���ļ����Զ����б�
        File source=new File(dataDir);
        File[] allFile=source.listFiles();
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
                excuteClusty(numZhang15[c], lastName);
                lastName = currName; // ���¸��˽��о���
                c++;
            }
        }
        System.out.println("���ڴ���:" + lastName);
        processAll(allFile, lastName, lastPos, allFile.length);// �������һ����
        excuteClusty(numZhang15[29], lastName);
    }
	private static void HashClass(HashMap<String, String> name) {
		name.put("Alvin_Cooper","50");
		name.put("Harry_Hughes","50");
		name.put("Jonathan_Brooks","50");
		name.put("Jude_Brown", "50");
		name.put("Karen_Peterson", "50");
		name.put("Marcy_Jackson", "50");
		name.put("Martha_Edwards", "50");
		name.put("Neil_Clark", "50");
		name.put("Stephan_Johnson", "50");
		name.put("Violet_Howard", "50");
		name.put("Dekang_Lin", "30");
		name.put("Chris_Brockett", "30");
		name.put("James_Curran", "30");
		name.put("Mark_Johnson", "30");
		name.put("Jerry_Hobbs", "30");
		name.put("Frank_Keller", "30");
		name.put("Leon_Barrett", "30");
		name.put("Robert_Moore", "30");
		name.put("Sharon_Goldwater", "30");
		name.put("Stephen_Clark", "30");
		name.put("Arthur_Morgan", "50");
		name.put("James_Morehead", "50");
		name.put("James_Davidson", "50");
		name.put("Patrick_Killen", "50");
		name.put("William_Dickson", "50");
		name.put("George_Foster", "50");
		name.put("James_Hamilton", "50");
		name.put("John_Nelson", "50");
		name.put("Thomas_Fraser", "50");
		name.put("Thomas_Kirk", "50");
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
                    //+"-rowmodel=maxtf "
                    +"-sim=cos "
                    +"-colmodel=idf " 
                    //+"-mincomponent=8 "
                    //+"cstype=best "  //�����㷨Ϊrb rbr graph�ſ���
                    //+"-nnbrs=80 "
                    +"-colprune=1 "   //**ѡ������Ӱ���������������*ϵ��
                    + "-crfun=slink "// �������ļ�
                    //+"-showtree " + "-showfeatures "+"-labeltree "+"-showsummaries=cliques "
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
