public class TopKKey implements Comparable<TopKKey> {
    private Integer wins;
    private Integer uses;
    private Integer distinctPlayersNumber;
    private Double avgDeckDiff;
    private String filter;

    public TopKKey(Integer wins, Integer uses, Integer distinctPlayersNumber, Double avgDeckDiff, String filter) {
        this.wins = wins;
        this.uses = uses;
        this.distinctPlayersNumber = distinctPlayersNumber;
        this.avgDeckDiff = avgDeckDiff;
        this.filter = filter;
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

    public Integer getDistinctPlayersNumber() {
        return distinctPlayersNumber;
    }

    public void setDistinctPlayersNumber(Integer distinctPlayersNumber) {
        this.distinctPlayersNumber = distinctPlayersNumber;
    }

    public Double getAvgDeckDiff() {
        return avgDeckDiff;
    }

    public void setAvgDeckDiff(Double avgDeckDiff) {
        this.avgDeckDiff = avgDeckDiff;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public int compareTo(TopKKey o) {
        if (this.filter.equals("AVG")) {
            int avgDeckDiffDiff = this.avgDeckDiff.compareTo(o.avgDeckDiff);
            if (avgDeckDiffDiff != 0) {
                return avgDeckDiffDiff;
            }

            int winsDiff = this.wins.compareTo(o.wins);
            if (winsDiff != 0) {
                return winsDiff;
            }

            return this.uses.compareTo(o.uses) * -1;
        } else if (this.filter.equals("U")) {
            int usesDiff = this.uses.compareTo(o.uses);
            if (usesDiff != 0) {
                return usesDiff;
            }

            int winsDiff = this.wins.compareTo(o.wins);
            if (winsDiff != 0) {
                return winsDiff;
            }

            return this.avgDeckDiff.compareTo(o.avgDeckDiff) * -1;
        } else if(this.filter.equals("P")) {
            int distinctPlayersNumberDiff = this.distinctPlayersNumber.compareTo(o.distinctPlayersNumber);
            if (distinctPlayersNumberDiff != 0) {
                return distinctPlayersNumberDiff;
            }

            int winsDiff = this.wins.compareTo(o.wins);
            if (winsDiff != 0) {
                return winsDiff;
            }

            int usesDiff = this.uses.compareTo(o.uses) * -1;
            if (usesDiff != 0) {
                return usesDiff;
            }

            return this.avgDeckDiff.compareTo(o.avgDeckDiff) * -1;
        }

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
