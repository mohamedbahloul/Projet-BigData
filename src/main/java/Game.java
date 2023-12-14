import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;

public class Game implements WritableComparable<Game> {

    public Instant date;
    public Integer round;
    public Integer win;

    public String player;
    public Double deck;
    public String clan;
    public List<Integer> cards;

    public String player2;
    public Double deck2;
    public String clan2;
    public List<Integer> cards2;

    public Game(Instant date, Integer round, Integer win, String player, Double deck, String clan, List<Integer> cards,
            String player2, Double deck2, String clan2, List<Integer> cards2) {
        this.date = date;
        this.round = round;

        if (player.compareTo(player2) < 0) {
            this.player = player;
            this.player2 = player2;
            this.deck = deck;
            this.deck2 = deck2;
            this.clan = clan;
            this.clan2 = clan2;
            this.cards = cards;
            this.cards2 = cards2;
            this.win = win;
        } else {
            this.player = player2;
            this.player2 = player;
            this.deck = deck2;
            this.deck2 = deck;
            this.clan = clan2;
            this.clan2 = clan;
            this.cards = cards2;
            this.cards2 = cards;
            this.win = win == 1 ? 0 : 1;
        }
    }

    public Game() {
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(date.toString());
        dataOutput.writeUTF(player);
        dataOutput.writeUTF(player2);
        dataOutput.writeInt(win);
        dataOutput.writeInt(round);
        dataOutput.writeDouble(deck);
        dataOutput.writeDouble(deck2);
        dataOutput.writeUTF(clan);
        dataOutput.writeUTF(clan2);
        dataOutput.writeUTF(cards.toString());
        dataOutput.writeUTF(cards2.toString());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        date = Instant.parse(dataInput.readUTF());
        player = dataInput.readUTF();
        player2 = dataInput.readUTF();
        win = dataInput.readInt();
        round = dataInput.readInt();
        deck = dataInput.readDouble();
        deck2 = dataInput.readDouble();
        clan = dataInput.readUTF();
        clan2 = dataInput.readUTF();
        cards = stringToList(dataInput.readUTF());
        cards2 = stringToList(dataInput.readUTF());
    }

    public static List<Integer> stringToList(String input) {
        List<Integer> result = new ArrayList<>();

        // Supprimez les crochets de début et de fin
        input = input.replaceAll("\\[|\\]", "");

        // Séparez la chaîne en sous-chaînes en utilisant la virgule comme délimiteur
        String[] elements = input.split(",");

        // Convertissez chaque sous-chaîne en entier et ajoutez-le à la liste
        for (String element : elements) {
            result.add(Integer.parseInt(element.trim()));
        }

        return result;
    }

    @Override
    public String toString() {
        return "player : " + player + "\t player2 : " + player2 + "\t win : " + win + "\t round : " + round
                + "\t deck : " + deck + "\t deck2 : " + deck2 + "\t clan : " + clan + "\t clan2 : " + clan2
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

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
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

    public String getClan() {
        return clan;
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
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

    public String getClan2() {
        return clan2;
    }

    public void setClan2(String clan2) {
        this.clan2 = clan2;
    }

    public List<Integer> getCards2() {
        return cards2;
    }

    public void setCards2(List<Integer> cards2) {
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
