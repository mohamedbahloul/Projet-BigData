import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class GameSummary implements Writable{
    private int wins;
    private int uses;

    public GameSummary() {
    }

    public GameSummary(int wins, int uses) {
        this.wins = wins;
        this.uses = uses;
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
    
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(wins);
        dataOutput.writeInt(uses);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        wins = dataInput.readInt();
        uses = dataInput.readInt();
    }
    
}
