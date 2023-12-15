import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class GameSummary implements Writable {
    private Boolean wins;
    private int uses;
    private int winsPartialCount;
    private int maxClanTr;
    private double totalDeckDiff;
    private double avgDeckDiff;

    public GameSummary() {
        this.wins = false;
        this.uses = 0;
        this.winsPartialCount = 0;
        this.maxClanTr = 0;
        this.totalDeckDiff = 0;
        this.avgDeckDiff = 0;
    }

    public GameSummary(Boolean wins, int uses, int maxClanTr, double totalDeckDiff) {
        this.wins = wins;
        this.uses = uses;
        this.winsPartialCount = 0;
        this.maxClanTr = maxClanTr;
        this.totalDeckDiff = totalDeckDiff;
    }

    public GameSummary(int winsPartialCount, int uses, int maxClanTr, double avgDeckDiff) {
        this.winsPartialCount = winsPartialCount;
        this.uses = uses;
        this.wins = false;
        this.maxClanTr = maxClanTr;
        this.avgDeckDiff = avgDeckDiff;
    }

    public Boolean getWins() {
        return wins;
    }

    public void setWins(Boolean wins) {
        this.wins = wins;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int getWinsPartialCount() {
        return winsPartialCount;
    }

    public void setWinsPartialCount(int winsPartialCount) {
        this.winsPartialCount = winsPartialCount;
    }

    public int getMaxClanTr() {
        return maxClanTr;
    }

    public void setMaxClanTr(int maxClanTr) {
        this.maxClanTr = maxClanTr;
    }

    public double getTotalDeckDiff() {
        return totalDeckDiff;
    }

    public void setTotalDeckDiff(double totalDeckDiff) {
        this.totalDeckDiff = totalDeckDiff;
    }

    public double getAvgDeckDiff() {
        return avgDeckDiff;
    }

    public void setAvgDeckDiff(double avgDeckDiff) {
        this.avgDeckDiff = avgDeckDiff;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(wins);
        dataOutput.writeInt(uses);
        dataOutput.writeByte(winsPartialCount);
        dataOutput.writeInt(maxClanTr);
        dataOutput.writeDouble(totalDeckDiff);
        dataOutput.writeDouble(avgDeckDiff);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        wins = dataInput.readBoolean();
        uses = dataInput.readInt();
        winsPartialCount = dataInput.readByte();
        maxClanTr = dataInput.readInt();
        totalDeckDiff = dataInput.readDouble();
        avgDeckDiff = dataInput.readDouble();
    }
}
