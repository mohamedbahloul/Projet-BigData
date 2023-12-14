
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;

public class GameKey implements WritableComparable<GameKey> {

    public Instant date;
    public Integer round;
    public String player;
    public String player2;

    public GameKey(Instant date, Integer round, String player, String player2) {
        this.date = date;
        this.round = round;
        if (player.compareTo(player2) < 0) {
            this.player = player;
            this.player2 = player2;
        } else {
            this.player = player2;
            this.player2 = player;
        }

    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(date.toString());
        dataOutput.writeUTF(player);
        dataOutput.writeUTF(player2);
        dataOutput.writeInt(round);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        date = Instant.parse(dataInput.readUTF());
        player = dataInput.readUTF();
        player2 = dataInput.readUTF();
        round = dataInput.readInt();
    }

    public List<Integer> hexStringToList(String hexString) {
        List<Integer> resultList = new ArrayList<>();

        // Assurez-vous que la longueur de la chaîne est paire
        if (hexString.length() != 16) {
            throw new IllegalArgumentException("La chaîne hexadécimale doit avoir une longueur paire.");
        }

        // Parcours la chaîne par paires de caractères
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexPair = hexString.substring(i, i + 2);
            // Convertit la paire hexadécimale en entier et l'ajoute à la liste
            int decimalValue = Integer.parseInt(hexPair, 16);
            resultList.add(decimalValue);
        }

        return resultList;
    }

    @Override
    public String toString() {
        return "date : " + date + "\t player : " + player + "\t player2 : " + player2 + "\t round : " + round;
    }

    @Override
    public int compareTo(GameKey other) {
        int result = this.date.compareTo(other.date);
        if (result == 0) {
            result = this.player.compareTo(other.player);
        }
        if (result == 0) {
            result = this.player2.compareTo(other.player2);
        }
        if (result == 0) {
            result = this.round.compareTo(other.round);
        }
        return result;
    }

}