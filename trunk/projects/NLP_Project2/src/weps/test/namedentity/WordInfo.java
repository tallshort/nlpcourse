package weps.test.namedentity;

//��¼ÿ�����ֵ��ʵ���Ϣ
public final class WordInfo {
    int Position; // ��¼��ǰ���ʳ��ִ���

    public WordInfo(int Position) {
        super();
        this.Position = Position;
    }

    public int Get() {
        return Position;
    }

    public void Set(int p) {
        Position = p;
    }

    public void SetPlus() {
        Position++;
    }
}
