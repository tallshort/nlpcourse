package net.core;

import ICTCLAS.I3S.AC.ICTCLAS30;

/**
 * 封装了中科院分词器
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
     * 将指定的文本进行分词并加上词性
     * 
     * @param text 输入的文本
     * @return 带有分词与词性标注的文本
     */
    public String process(String text) {
        try{    
            
            String argu = ".";
            if (testICTCLAS30.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
            {
                throw new RuntimeException("Init Fail!");
            }

            /*
             * 设置词性标注集 ID 代表词性集 
             * 1 计算所一级标注集 
             * 0 计算所二级标注集 
             * 2 北大二级标注集 
             * 3 北大一级标注集
             */
            testICTCLAS30.ICTCLAS_SetPOSmap(nPOSmap);

            // 导入用户词典前
            byte nativeBytes[] = testICTCLAS30.ICTCLAS_ParagraphProcess(text.getBytes("GB2312"), 1);
            String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
            return nativeStr.substring(0, nativeStr.length() - 1);
        
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
