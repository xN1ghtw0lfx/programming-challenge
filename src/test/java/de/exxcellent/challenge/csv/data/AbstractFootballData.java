package de.exxcellent.challenge.csv.data;

import de.exxcellent.challenge.csv.CsvProperty;

/**
 * Football data class.
 *
 * @author Kevin Degen
 */
public abstract class AbstractFootballData {

    @CsvProperty("Team")
    private String team;

    @CsvProperty("Games")
    private int games;

    @CsvProperty("Wins")
    private int wins;

    @CsvProperty("Losses")
    private int losses;

    @CsvProperty("Draws")
    private int draws;

    @CsvProperty("Goals")
    private int goals;

    @CsvProperty("Goals Allowed")
    private int goalsAllowed;

    @CsvProperty("Points")
    private int points;

    public String getTeam() {
        return team;
    }

    public int getGames() {
        return games;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public int getGoals() {
        return goals;
    }

    public int getGoalsAllowed() {
        return goalsAllowed;
    }

    public int getPoints() {
        return points;
    }

    public int getGoalSpread() {
        return Math.abs(getGoals() - getGoalsAllowed());
    }

}
