import java.util.*;

//import java.util.stream.*;
public class TestBot implements RoShamBot {
    int roundsCompleted;
    ArrayList<Action> prevMoves; 

    public TestBot() {
        roundsCompleted = 0;
        this.prevMoves = new ArrayList<Action>();
    }

    public Action randomMove() {
        double coinFlip = Math.random();

        if (coinFlip <= 1.0/5.0)
            return Action.ROCK;
        else if (coinFlip <= 2.0/5.0)
            return Action.PAPER;
        else if (coinFlip <= 3.0/5.0)
            return Action.SCISSORS;
        else if (coinFlip <= 4.0/5.0)
            return Action.LIZARD;
        else 
            return Action.SPOCK;
    }

    public Action counter(Action opponent) {
        double coinFlip = Math.random();
        
        if (opponent.equals(Action.ROCK)) {
            if (coinFlip <= 0.5) {
                return Action.PAPER;
            } else {
                return Action.SPOCK;
            }
        } else if (opponent.equals(Action.PAPER)) {
            if (coinFlip <= 0.5) {
                return Action.SCISSORS;
            } else {
                return Action.LIZARD;
            }
        } else if (opponent.equals(Action.SCISSORS)) {
            if (coinFlip <= 0.5) {
                return Action.ROCK;
            } else {
                return Action.SPOCK;
            }
        } else if (opponent.equals(Action.LIZARD)) {
            if (coinFlip <= 0.5) {
                return Action.ROCK;
            } else {
                return Action.SCISSORS;
            }
        } else {
            if (coinFlip <= 0.5) {
                return Action.PAPER;
            } else {
                return Action.LIZARD;
            }
        }
    }
    public Action getNextMove(Action lastOpponentMove) {
        this.prevMoves.add(lastOpponentMove);
        if (this.roundsCompleted == 0) {
            roundsCompleted++;
            return randomMove();
        } else {
            roundsCompleted++;

            //long rockCount = prevMoves.stream().filter(i -> i.equals(Action.ROCK)).count();
            //long paperCount = prevMoves.stream().filter(i -> i.equals(Action.ROCK)).count();

            int rockCount = 0;
            int paperCount = 0;
            int scissorsCount = 0;
            int lizardCount = 0;
            int spockCount = 0;

            for (int i = 0; i < prevMoves.size(); i++) {
                if (prevMoves.get(i).equals(Action.ROCK)) {
                    rockCount++;
                } else if (prevMoves.get(i).equals(Action.PAPER)) {
                    paperCount++;
                } else if (prevMoves.get(i).equals(Action.SCISSORS)) {
                    scissorsCount++;
                } else if (prevMoves.get(i).equals(Action.LIZARD)) {
                    lizardCount++;
                } else {
                    spockCount++;
                } 
            }

            int[] counts = {rockCount, paperCount, scissorsCount, lizardCount, spockCount};
            Action[] actions = {Action.ROCK, Action.PAPER, Action.SCISSORS, Action.LIZARD, Action.SPOCK};
            
            int max = Integer.MIN_VALUE;
            Action opponent_fav = Action.ROCK;
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > max) {
                    max = counts[i];
                    opponent_fav = actions[i];
                }
            }

            //System.out.println(opponent_fav);

            return counter(opponent_fav);
        } 
    } 
}
