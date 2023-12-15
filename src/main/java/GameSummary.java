import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class GameSummary implements Writable {
    private Boolean wins;
    private int uses;
    private byte winsPartialCount;

    public GameSummary() {
        this.wins = false;
        this.uses = 0;
        this.winsPartialCount = 0;
    }

    public GameSummary(Boolean wins, int uses) {
        this.wins = wins;
        this.uses = uses;
        this.winsPartialCount = 0;
    }

    public GameSummary(byte winsPartialCount, int uses) {
        this.winsPartialCount = winsPartialCount;
        this.uses = uses;
        this.wins = false;
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

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(wins);
        dataOutput.writeInt(uses);
        dataOutput.writeByte(winsPartialCount);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        wins = dataInput.readBoolean();
        uses = dataInput.readInt();
        winsPartialCount = dataInput.readByte();
    }

    public byte getWinsPartialCount() {
        return winsPartialCount;
    }

    public void setWinsPartialCount(byte winsPartialCount) {
        this.winsPartialCount = winsPartialCount;
    }

}
