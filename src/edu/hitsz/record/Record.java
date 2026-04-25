package edu.hitsz.record;

public class Record implements Comparable<Record> {
    private String userName;
    private int score;
    private String time;

    public Record(String userName, int score, String time) {
        this.userName = userName;
        this.score = score;
        this.time = time;
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    @Override
    public int compareTo(Record o) {
        // 按分数降序排列
        return Integer.compare(o.score, this.score);
    }

    @Override
    public String toString() {
        // 采用简单的 CSV 格式，方便存取和调试
        return userName + "," + score + "," + time;
    }
}