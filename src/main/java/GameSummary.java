import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class GameSummary implements Writable {
    private int wins;
    private int uses;
    private int maxClanTr;
    private double totalDeckDiff;
    private double avgDeckDiff;
    private String players;

    public GameSummary() {
        this.wins = 0;
        this.uses = 0;
        this.maxClanTr = 0;
        this.totalDeckDiff = 0;
        this.avgDeckDiff = 0;
        this.players = "";
    }

    public GameSummary(int wins, int uses, int maxClanTr, double totalDeckDiff, String players) {
        this.wins = wins;
        this.uses = uses;
        this.maxClanTr = maxClanTr;
        this.totalDeckDiff = totalDeckDiff;
        this.players = players;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
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

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(wins);
        dataOutput.writeInt(uses);
        dataOutput.writeInt(maxClanTr);
        dataOutput.writeDouble(totalDeckDiff);
        dataOutput.writeDouble(avgDeckDiff);
        dataOutput.writeUTF(players);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        wins = dataInput.readInt();
        uses = dataInput.readInt();
        maxClanTr = dataInput.readInt();
        totalDeckDiff = dataInput.readDouble();
        avgDeckDiff = dataInput.readDouble();
        players = new String(dataInput.readUTF());
    }
}
