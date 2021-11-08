import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.*;


public class BotBot implements RoShamBot {
    
    public List<Action> myPrevMoves;
    public List<Action> opPrevMoves;


    private static final List<Action> MOVES =
        Collections.unmodifiableList(Arrays.asList(Action.values()));


    public BotBot() {
        myPrevMoves = new ArrayList<Action>();
        opPrevMoves = new ArrayList<Action>();
    }
    
    // Returns the bot's next move.
    public Action getNextMove(Action lastOpponentMove) {

        opPrevMoves.add(lastOpponentMove);

        Random randInt = new Random();
        // Action myMove = MOVES.get(randInt.nextInt(3));

        if (opPrevMoves.size() < 5) {
            return MOVES.get(randInt.nextInt(5));
        }

        Action myMove = lookBack(5, 500);//look back at max 500 rounds for now

        myPrevMoves.add(myMove);
        return myMove;
    }

    //Returns move based on opponent's most likely move historically.
    public Action lookBack(int steps, int maxLookBack) {
        List<Action> freqMove = new ArrayList<Action>();

        List<Action> recent;
        List<Action> recentHist;

        if (opPrevMoves.size() - steps >= 0) {//after 5 games
            recent = opPrevMoves.subList(opPrevMoves.size() - steps, opPrevMoves.size());
        } else {
            recent = opPrevMoves.subList(0, opPrevMoves.size());
        }

        if (opPrevMoves.size() - maxLookBack >= 0) {
            recentHist = opPrevMoves.subList(opPrevMoves.size() - maxLookBack, opPrevMoves.size());
        } else {
            recentHist = opPrevMoves.subList(0, opPrevMoves.size());
        }
        

        int index = Collections.indexOfSubList(recentHist, recent);

        // Check for this move sequence in previous moves.
        while (index >= 0) {

            freqMove.add(recentHist.get(index + recent.size() - 1));

            recentHist = recentHist.subList(index + recent.size(), recentHist.size());

            index = Collections.indexOfSubList(recentHist, recent);
        }

        Random randInt = new Random();



        if (freqMove.size() > 0) {

            return counter(freqMove.get(randInt.nextInt(freqMove.size())));
        } else {
            return MOVES.get(randInt.nextInt(5));
        }
    }


    public Action counter(Action move) {
        double coinFlip = Math.random();
        if (move.equals(Action.ROCK)) {
            if (coinFlip <= 0.5) {
                return Action.PAPER;
            } else {
                return Action.SPOCK;
            }
        } else if (move.equals(Action.PAPER)) {
            if (coinFlip <= 0.5) {
                return Action.SCISSORS;
            } else {
                return Action.LIZARD;
            }
        } else if (move.equals(Action.SCISSORS)) {
            if (coinFlip <= 0.5) {
                return Action.ROCK;
            } else {
                return Action.SPOCK;
            }
        } else if (move.equals(Action.LIZARD)) {
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
}