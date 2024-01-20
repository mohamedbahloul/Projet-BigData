import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class TopK implements WritableComparable<TopK> {
    private String cards;
    private Byte month;
    private Byte week;
    private Short year;
    private Integer wins;
    private Integer uses;
    private Integer maxClanTrophies;
    private Double avgDeckDiff;
    private Integer distinctPlayersNumber;

    public TopK(String cards, Byte month, Byte week, Short year, Integer wins, Integer uses, Integer maxClanTrophies,
            Double avgDeckDiff, Integer distinctPlayersNumber) {
        this.cards = cards;
        this.month = month;
        this.week = week;
        this.year = year;
        this.wins = wins;
        this.uses = uses;
        this.maxClanTrophies = maxClanTrophies;
        this.avgDeckDiff = avgDeckDiff;
        this.distinctPlayersNumber = distinctPlayersNumber;
    }

    public TopK() {
    }

    public TopK(TopK topK) {
        this.cards = topK.cards;
        this.month = topK.month;
        this.week = topK.week;
        this.year = topK.year;
        this.wins = topK.wins;
        this.uses = topK.uses;
        this.maxClanTrophies = topK.maxClanTrophies;
        this.avgDeckDiff = topK.avgDeckDiff;
        this.distinctPlayersNumber = topK.distinctPlayersNumber;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(cards);
        dataOutput.writeByte(month);
        dataOutput.writeByte(week);
        dataOutput.writeShort(year);
        dataOutput.writeInt(wins);
        dataOutput.writeInt(uses);
        dataOutput.writeInt(maxClanTrophies);
        dataOutput.writeDouble(avgDeckDiff);
        dataOutput.writeInt(distinctPlayersNumber);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        cards = dataInput.readUTF();
        month = dataInput.readByte();
        week = dataInput.readByte();
        year = dataInput.readShort();
        wins = dataInput.readInt();
        uses = dataInput.readInt();
        maxClanTrophies = dataInput.readInt();
        avgDeckDiff = dataInput.readDouble();
        distinctPlayersNumber = dataInput.readInt();
    }

    @Override
    public int compareTo(TopK o) {
        int cmp = cards.compareTo(o.cards);
        if (cmp != 0) {
            return cmp;
        }
        cmp = month.compareTo(o.month);
        if (cmp != 0) {
            return cmp;
        }
        cmp = week.compareTo(o.week);
        if (cmp != 0) {
            return cmp;
        }
        return year.compareTo(o.year);
    }

    @Override
    public String toString() {
        return cards + "_" + month + "_" + week + "_" + year + "_" + wins + "_" + uses + "_" + maxClanTrophies + "_"
                + avgDeckDiff + "_" + distinctPlayersNumber;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public Byte getMonth() {
        return month;
    }

    public void setMonth(Byte month) {
        this.month = month;
    }

    public Byte getWeek() {
        return week;
    }

    public void setWeek(Byte week) {
        this.week = week;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getUses() {
        return uses;
    }

    public void setUses(Integer uses) {
        this.uses = uses;
    }

    public Integer getMaxClanTrophies() {
        return maxClanTrophies;
    }

    public void setMaxClanTrophies(Integer maxClanTrophies) {
        this.maxClanTrophies = maxClanTrophies;
    }

    public Double getAvgDeckDiff() {
        return avgDeckDiff;
    }

    public void setAvgDeckDiff(Double avgDeckDiff) {
        this.avgDeckDiff = avgDeckDiff;
    }

    public Integer getDistinctPlayersNumber() {
        return distinctPlayersNumber;
    }

    public void setDistinctPlayersNumber(Integer distinctPlayersNumber) {
        this.distinctPlayersNumber = distinctPlayersNumber;
    }

}
