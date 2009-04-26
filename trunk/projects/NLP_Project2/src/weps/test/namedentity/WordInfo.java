package weps.test.namedentity;

//记录每个出现单词的信息
public final class WordInfo {
    int Position; // 记录当前单词出现次数

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
