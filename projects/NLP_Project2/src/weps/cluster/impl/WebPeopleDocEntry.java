package weps.cluster.impl;

public class WebPeopleDocEntry {

    private String name;
    private String rank;
    
    public WebPeopleDocEntry(String name, String rank) {
        this.name = name;
        this.rank = rank;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
      return "[" + name + ", " + rank + "]";  
    }
}
