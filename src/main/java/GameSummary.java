import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class GameSummary implements Writable {
    private Boolean wins;
    private int uses;
    private int winsPartialCount;
    private int maxClanTr;

    public GameSummary() {
        this.wins = false;
        this.uses = 0;
        this.winsPartialCount = 0;
        this.maxClanTr = 0;
    }

    public GameSummary(Boolean wins, int uses, int maxClanTr) {
        this.wins = wins;
        this.uses = uses;
        this.winsPartialCount = 0;
        this.maxClanTr = maxClanTr;
    }

    public GameSummary(int winsPartialCount, int uses, int maxClanTr) {
        this.winsPartialCount = winsPartialCount;
        this.uses = uses;
        this.wins = false;
        this.maxClanTr = maxClanTr;
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

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(wins);
        dataOutput.writeInt(uses);
        dataOutput.writeByte(winsPartialCount);
        dataOutput.writeInt(maxClanTr);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        wins = dataInput.readBoolean();
        uses = dataInput.readInt();
        winsPartialCount = dataInput.readByte();
        maxClanTr = dataInput.readInt();
    }
}
