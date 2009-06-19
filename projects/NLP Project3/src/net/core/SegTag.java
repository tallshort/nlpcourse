package net.core;

import ICTCLAS.I3S.AC.ICTCLAS30;

/**
 * ��װ���п�Ժ�ִ���
 */
public class SegTag {
    private ICTCLAS30 testICTCLAS30 = new ICTCLAS30();
    private int nPOSmap = 2;
    
    public SegTag() {
    }
    
    public SegTag(int nPOSmap) {
        this.nPOSmap = nPOSmap;
    }
    
    /**
     * ��ָ�����ı����зִʲ����ϴ���
     * 
     * @param text ������ı�
     * @return ���зִ�����Ա�ע���ı�
     */
    public String process(String text) {
        try{    
            
            String argu = ".";
            if (testICTCLAS30.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
            {
                throw new RuntimeException("Init Fail!");
            }

            /*
             * ���ô��Ա�ע�� ID ������Լ� 
             * 1 ������һ����ע�� 
             * 0 ������������ע�� 
             * 2 ���������ע�� 
             * 3 ����һ����ע��
             */
            testICTCLAS30.ICTCLAS_SetPOSmap(nPOSmap);

            // �����û��ʵ�ǰ
            byte nativeBytes[] = testICTCLAS30.ICTCLAS_ParagraphProcess(text.getBytes("GB2312"), 1);
            String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
            return nativeStr.substring(0, nativeStr.length() - 1);
        
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
