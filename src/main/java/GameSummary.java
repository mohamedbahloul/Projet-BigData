import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.Writable;

public class GameSummary implements Writable {
    private Boolean wins;
    private int uses;
    private int winsPartialCount;
    private int maxClanTr;
    private double totalDeckDiff;
    private double avgDeckDiff;
    private Set<String> players;

    public GameSummary() {
        this.wins = false;
        this.uses = 0;
        this.winsPartialCount = 0;
        this.maxClanTr = 0;
        this.totalDeckDiff = 0;
        this.avgDeckDiff = 0;
        this.players = new HashSet<>();
    }

    public GameSummary(Boolean wins, int uses, int maxClanTr, double totalDeckDiff, Set<String> players) {
        this.wins = wins;
        this.uses = uses;
        this.winsPartialCount = 0;
        this.maxClanTr = maxClanTr;
        this.totalDeckDiff = totalDeckDiff;
        this.players = players;
    }

    public GameSummary(int winsPartialCount, int uses, int maxClanTr, double avgDeckDiff, Set<String> players) {
        this.winsPartialCount = winsPartialCount;
        this.uses = uses;
        this.wins = false;
        this.maxClanTr = maxClanTr;
        this.avgDeckDiff = avgDeckDiff;
        this.players = players;
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

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(wins);
        dataOutput.writeInt(uses);
        dataOutput.writeByte(winsPartialCount);
        dataOutput.writeInt(maxClanTr);
        dataOutput.writeDouble(totalDeckDiff);
        dataOutput.writeDouble(avgDeckDiff);
        dataOutput.writeUTF(players.toString());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        wins = dataInput.readBoolean();
        uses = dataInput.readInt();
        winsPartialCount = dataInput.readInt();
        maxClanTr = dataInput.readInt();
        totalDeckDiff = dataInput.readDouble();
        avgDeckDiff = dataInput.readDouble();
        String p = dataInput.readUTF();
        players = stringToSet(p);
    }

    private Set<String> stringToSet(String p) {
        // Vérification des cas limites
        if (p == null || p.isEmpty() || p.length() < 2 || p.charAt(0) != '{' || p.charAt(p.length() - 1) != '}') {
            // Gérer le cas d'une chaîne invalide
            return new HashSet<>();
        }

        // Suppression des accolades et séparation des éléments
        String[] elements = p.substring(1, p.length() - 1).split(", ");

        // Création de l'ensemble
        Set<String> resultSet = new HashSet<>(Arrays.asList(elements));

        return resultSet;
    }
}
