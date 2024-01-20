public class TopKKey implements Comparable<TopKKey> {
    private Integer wins;
    private Integer uses;
    private Double avgDeckDiff;

    public TopKKey(Integer wins, Integer uses, Double avgDeckDiff) {
        this.wins = wins;
        this.uses = uses;
        this.avgDeckDiff = avgDeckDiff;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getUses() {
        return uses;
    }

    public void setUses(Integer uses) {
        this.uses = uses;
    }

    public Double getAvgDeckDiff() {
        return avgDeckDiff;
    }

    public void setAvgDeckDiff(Double avgDeckDiff) {
        this.avgDeckDiff = avgDeckDiff;
    }

    @Override
    public int compareTo(TopKKey o) {
        int winsDiff = this.wins.compareTo(o.wins);
        if (winsDiff != 0) {
            return winsDiff;
        }

        int usesDiff = this.uses.compareTo(o.uses);
        if (usesDiff != 0) {
            return usesDiff * -1;
        }

        return this.avgDeckDiff.compareTo(o.avgDeckDiff) * -1;
    }
    
}
