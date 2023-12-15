import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class GameSummary implements Writable {
    private Boolean wins;
    private int uses;

    public GameSummary() {
    }

    public GameSummary(Boolean wins, int uses) {
        this.wins = wins;
        this.uses = uses;
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
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        wins = dataInput.readBoolean();
        uses = dataInput.readInt();
    }

}
