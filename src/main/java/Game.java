import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.WritableComparable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;

public class Game implements WritableComparable<Game> {

    private Instant date;
    private Integer round;
    private Boolean win;

    private String player;
    private Double deck;
    private Integer clanTr;
    private List<Byte> cards;

    private String player2;
    private Double deck2;
    private Integer clanTr2;
    private List<Byte> cards2;

    public Game(Instant date, Integer round, Boolean win, String player, Double deck, Integer clanTr, List<Byte> cards,
            String player2, Double deck2, Integer clanTr2, List<Byte> cards2) {
        this.date = date;
        this.round = round;

        if (player.compareTo(player2) < 0) {
            this.player = player;
            this.player2 = player2;
            this.deck = deck;
            this.deck2 = deck2;
            this.clanTr = clanTr;
            this.clanTr2 = clanTr2;
            this.cards = cards;
            this.cards2 = cards2;
            this.win = win;
        } else {
            this.player = player2;
            this.player2 = player;
            this.deck = deck2;
            this.deck2 = deck;
            this.clanTr = clanTr2;
            this.clanTr2 = clanTr;
            this.cards = cards2;
            this.cards2 = cards;
            this.win = !win;
        }
    }

    public Game() {
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(date.toString());
        dataOutput.writeUTF(player);
        dataOutput.writeUTF(player2);
        dataOutput.writeBoolean(win);
        dataOutput.writeInt(round);
        dataOutput.writeDouble(deck);
        dataOutput.writeDouble(deck2);
        dataOutput.writeInt(clanTr);
        dataOutput.writeInt(clanTr2);
        dataOutput.writeUTF(cards.toString());
        dataOutput.writeUTF(cards2.toString());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        date = Instant.parse(dataInput.readUTF());
        player = dataInput.readUTF();
        player2 = dataInput.readUTF();
        win = dataInput.readBoolean();
        round = dataInput.readInt();
        deck = dataInput.readDouble();
        deck2 = dataInput.readDouble();
        clanTr = dataInput.readInt();
        clanTr2 = dataInput.readInt();
        cards = stringToList(dataInput.readUTF());
        cards2 = stringToList(dataInput.readUTF());
    }

    public static List<Byte> stringToList(String input) {
        List<Byte> result = new ArrayList<>();

        // Supprimez les crochets de début et de fin
        input = input.replaceAll("\\[|\\]", "");

        // Séparez la chaîne en sous-chaînes en utilisant la virgule comme délimiteur
        String[] elements = input.split(",");

        // Convertissez chaque sous-chaîne en entier et ajoutez-le à la liste
        for (String element : elements) {
            result.add(Byte.parseByte(element.trim()));
        }

        return result;
    }

    @Override
    public String toString() {
        return "player : " + player + "\t player2 : " + player2 + "\t win : " + win + "\t round : " + round
                + "\t deck : " + deck + "\t deck2 : " + deck2 + "\t clanTr : " + clanTr + "\t clanTr2 : " + clanTr2
                + "\t cards : " + cards + "\t cards2 : " + cards2;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Boolean getWin() {
        return win;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Double getDeck() {
        return deck;
    }

    public void setDeck(Double deck) {
        this.deck = deck;
    }

    public Integer getClanTr() {
        return clanTr;
    }

    public void setClanTr(Integer clanTr) {
        this.clanTr = clanTr;
    }

    public List<Byte> getCards() {
        return cards;
    }

    public void setCards(List<Byte> cards) {
        this.cards = cards;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Double getDeck2() {
        return deck2;
    }

    public void setDeck2(Double deck2) {
        this.deck2 = deck2;
    }

    public Integer getClanTr2() {
        return clanTr2;
    }

    public void setClanTr2(Integer clanTr2) {
        this.clanTr2 = clanTr2;
    }

    public List<Byte> getCards2() {
        return cards2;
    }

    public void setCards2(List<Byte> cards2) {
        this.cards2 = cards2;
    }

    @Override
    public int compareTo(Game o) {
        int result = this.player.compareTo(o.player);
        if (result == 0) {
            result = this.player2.compareTo(o.player2);
        }
        if (result == 0) {
            result = this.date.compareTo(o.date);
        }
        if (result == 0) {
            result = this.round.compareTo(o.round);
        }
        return result;
    }

}
