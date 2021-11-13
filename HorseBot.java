import java.util.*;
import java.util.stream.*;

public class HorseBot implements RoShamBot {
    int roundsCompleted;
    ArrayList<Action> prevMoves;
    ArrayList<Action> ourMoves;
    public static Action[] actions = {Action.ROCK, Action.PAPER, Action.SCISSORS, Action.LIZARD, Action.SPOCK}; 

    
    public HorseBot() {
        roundsCompleted = 0;
        prevMoves = new ArrayList<Action>();
        ourMoves = new ArrayList<Action>();
    }

    
    public Action randomMove() {
        double coinFlip = Math.random();

        if (coinFlip <= 1.0/5.0) {
            ourMoves.add(Action.ROCK);
            return Action.ROCK;
        }
        else if (coinFlip <= 2.0/5.0) {
            ourMoves.add(Action.PAPER);
            return Action.PAPER;
        }
        else if (coinFlip <= 3.0/5.0) {
            ourMoves.add(Action.SCISSORS);
            return Action.SCISSORS;
        }
        else if (coinFlip <= 4.0/5.0) {
            ourMoves.add(Action.LIZARD);
            return Action.LIZARD;
        }
        else {
            ourMoves.add(Action.SPOCK);
            return Action.SPOCK;
        }
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

    public Action[][] zip(ArrayList<Action> prevMoves, ArrayList<Action> ourMoves) {
        Action[][] pairs = new Action[prevMoves.size()][prevMoves.size()];
        for (int i = 0; i < prevMoves.size(); i++) {
            Action theirMove = prevMoves.get(i);
            Action ourMove = ourMoves.get(i);
            pairs[i] = new Action[]{theirMove, ourMove};
        }
        return pairs;
    }
    public Action getNextMove(Action lastOpponentMove) {
        if (this.roundsCompleted < 2) {
            this.prevMoves.add(lastOpponentMove);
            roundsCompleted++;
            return randomMove();
        } else {
            roundsCompleted++;

            Action[][] pairs = zip(prevMoves, ourMoves);
            
            //System.out.println(pairs[0][1]);
            List<Integer> candidates = new ArrayList<Integer>();
            for (int i = 0; i < roundsCompleted - 2; i++) {
                candidates.add(i);
            }

            //System.out.println(pairs.length);
            

            for (int l = 1; l < roundsCompleted; l++) {
                List<Integer> new_list = new ArrayList<Integer>();
                for (int c : candidates) {
                    if ((c >= l) && (pairs[c - l + 1].equals(pairs[pairs.length - l]))) {
                        new_list.add(c);
                    }
                }
                if (new_list.isEmpty()) {
                    new_list.add(candidates.get(candidates.size() - 1));
                    candidates = new_list;
                } else {
                    candidates = new_list;
                }
            }
            
            Action predictedNext = pairs[candidates.get(0) + 1][0];
            Action ourMove = counter(predictedNext);
            this.prevMoves.add(lastOpponentMove);
            ourMoves.add(ourMove);
            return ourMove;
        } 
    } 
}
