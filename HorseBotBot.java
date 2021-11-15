import java.util.*;
import java.util.stream.*;

import javax.lang.model.util.ElementScanner6;

public class HorseBotBot implements RoShamBot{
    
    int roundsCompleted;
    ArrayList<Action> prevMoves;
    ArrayList<Action> ourMoves;

    ArrayList<Action> horseBotMoves;
    ArrayList<Action> botBotMoves;
    ArrayList<Action> randMoves;

    public static Action[] actions = {Action.ROCK, Action.PAPER, Action.SCISSORS, Action.LIZARD, Action.SPOCK}; 
    Integer WinLoseCountH;
    Integer WinLoseCountBB;
    Integer WinLoseCountR;

    public HorseBotBot() {
        roundsCompleted = 0;
        prevMoves = new ArrayList<Action>();
        ourMoves = new ArrayList<Action>();
        ourMoves.add(Action.ROCK);

        horseBotMoves = new ArrayList<Action>();
        botBotMoves = new ArrayList<Action>();
        randMoves = new ArrayList<Action>();

        WinLoseCountH = 0;
        WinLoseCountBB = 0;
        WinLoseCountR = 0;
    }

    public Action randomMove(char bot) {
        double coinFlip = Math.random();

        if (coinFlip <= 1.0/5.0) {
            if (bot == 'h')
                horseBotMoves.add(Action.ROCK);
            else if (bot == 'b')
                botBotMoves.add(Action.ROCK);
            else
                randMoves.add(Action.ROCK);

            return Action.ROCK;
        }
        else if (coinFlip <= 2.0/5.0) {
            if (bot == 'h')
                horseBotMoves.add(Action.PAPER);
            else if (bot == 'b')
                botBotMoves.add(Action.PAPER);
            else
                randMoves.add(Action.PAPER);

            return Action.PAPER;
        }
        else if (coinFlip <= 3.0/5.0) {
            if (bot == 'h')
                horseBotMoves.add(Action.SCISSORS);
            else if (bot == 'b')
                botBotMoves.add(Action.SCISSORS);
            else
                randMoves.add(Action.SCISSORS);

            return Action.SCISSORS;
        }
        else if (coinFlip <= 4.0/5.0) {
            if (bot == 'h')
                horseBotMoves.add(Action.LIZARD);
            else if (bot == 'b')
                botBotMoves.add(Action.LIZARD);
            else
                randMoves.add(Action.LIZARD);

            return Action.LIZARD;
        }
        else {
            if (bot == 'h')
                horseBotMoves.add(Action.SPOCK);
            else if (bot == 'b')
                botBotMoves.add(Action.SPOCK);
            else
                randMoves.add(Action.SPOCK);

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

    public static <A, B> List<Map.Entry<Action, Action>> zipJava8(ArrayList<Action> as, ArrayList<Action> bs) {
        return IntStream.range(0, Math.min(as.size(), bs.size()))
                .mapToObj(i -> Map.entry(as.get(i), bs.get(i)))
                .collect(Collectors.toList());
    }

    public Action HorseBot(Action lastOpponentMove) {
        if (this.roundsCompleted < 2) {
            //this.prevMoves.add(lastOpponentMove);
            //roundsCompleted++;
            return randomMove('h');
        } else {
            //roundsCompleted++;
            List<Map.Entry<Action, Action>> pairs = zipJava8(prevMoves, ourMoves);
            List<Integer> candidates = new ArrayList<Integer>();

            for (int i = 0; i < roundsCompleted - 1; i++) {
                candidates.add(i);
            }
            

            for (int l = 1; l < roundsCompleted; l++) {
                List<Integer> new_list = new ArrayList<Integer>();
                for (int c : candidates) {
                    if ((c >= l) && (pairs.get(c - l + 1).equals(pairs.get(pairs.size() - l)))) {
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

        
            Action predictedNext = pairs.get(candidates.get(0) + 1).getValue();
            Action ourMove = counter(predictedNext);

            horseBotMoves.add(ourMove);
            return ourMove;
        } 
    } 
    public Action BotBot(Action lastOpponentMove) {
        Action myMove;
        //prevMoves.add(lastOpponentMove);
        if (prevMoves.size() < 5) {
            return randomMove('b');
        }

        
        List<Action> moveSequence = lookBack(1, 500);//look back at max 500 rounds for now

        int i = 2;

        while(i < prevMoves.size()) {
            List<Action> maxSequence = lookBack(i, 500);
            if (maxSequence.size() > 1) {
                moveSequence = maxSequence;
                i++;
                //System.out.println(i);
            } else {
                break;
            }
        }

    

        Random randInt = new Random();
        
        if (moveSequence.size() > 0) {
            myMove = (counter(moveSequence.get(randInt.nextInt(moveSequence.size()))));
            
        } else {
            myMove = randomMove('b');
        }

        botBotMoves.add(myMove);
        return myMove;
    }

    public Action ImitatorBot(Action lastOpponentMove) {
        return counter(prevMoves.get(prevMoves.size() - 1));
    }


    public List<Action> lookBack(int steps, int maxLookBack) {
        List<Action> freqMove = new ArrayList<Action>();
        List<Action> recentMoves;
        List<Action> lookBackMoves;

        if (prevMoves.size() - steps >= 0) {//after 5 games
            recentMoves = prevMoves.subList(prevMoves.size() - steps, prevMoves.size());
        } else {
            recentMoves = prevMoves.subList(0, prevMoves.size());
        }

        if (prevMoves.size() - maxLookBack >= 0) {
            lookBackMoves = prevMoves.subList(prevMoves.size() - maxLookBack, prevMoves.size());
        } else {
            lookBackMoves = prevMoves.subList(0, prevMoves.size());
        }
        
        int index = Collections.indexOfSubList(lookBackMoves, recentMoves);

        // Check for this move sequence in previous moves.
        //Returns counter to final move in most recent sequence? So basically, to most recent move
        while (index >= 0) {
            //if we can safely return the next move in the sequence without going out of bounds
            if (lookBackMoves.size() - index - recentMoves.size() > 0) {
                freqMove.add(lookBackMoves.get(index + recentMoves.size()));
            //otherwise just return the counter to the last move in the sequence
            } else {
                freqMove.add(lookBackMoves.get(index + recentMoves.size() - 1));
            }
            lookBackMoves = lookBackMoves.subList(index + recentMoves.size(), lookBackMoves.size());
            index = Collections.indexOfSubList(lookBackMoves, recentMoves);
        }

        return freqMove;
    }

    public int runRound(Action a1, Action a2, int winLoseCounts) {
        // Determine winner, update scores and record the player actions.
        boolean p1Win = (((a1 == Action.SCISSORS) && (a2 == Action.PAPER)) ||
                         ((a1 == Action.PAPER) && (a2 == Action.ROCK)) ||
                         ((a1 == Action.ROCK) && (a2 == Action.LIZARD)) ||
                         ((a1 == Action.LIZARD) && (a2 == Action.SPOCK)) ||
                         ((a1 == Action.SPOCK) && (a2 == Action.SCISSORS)) ||
                         ((a1 == Action.SCISSORS) && (a2 == Action.LIZARD)) ||
                         ((a1 == Action.LIZARD) && (a2 == Action.PAPER)) ||
                         ((a1 == Action.PAPER) && (a2 == Action.SPOCK)) ||
                         ((a1 == Action.SPOCK) && (a2 == Action.ROCK)) ||
                         ((a1 == Action.ROCK) && (a2 == Action.SCISSORS)));
        
        boolean p2Win = (((a2 == Action.SCISSORS) && (a1 == Action.PAPER)) ||
                         ((a2 == Action.PAPER) && (a1 == Action.ROCK)) ||
                         ((a2 == Action.ROCK) && (a1 == Action.LIZARD)) ||
                         ((a2 == Action.LIZARD) && (a1 == Action.SPOCK)) ||
                         ((a2 == Action.SPOCK) && (a1 == Action.SCISSORS)) ||
                         ((a2 == Action.SCISSORS) && (a1 == Action.LIZARD)) ||
                         ((a2 == Action.LIZARD) && (a1 == Action.PAPER)) ||
                         ((a2 == Action.PAPER) && (a1 == Action.SPOCK)) ||
                         ((a2 == Action.SPOCK) && (a1 == Action.ROCK)) ||
                         ((a2 == Action.ROCK) && (a1 == Action.SCISSORS)));
        
        if (p1Win)
            winLoseCounts -= 1;
        else if (p2Win)
            winLoseCounts += 1;
        else // tie
            winLoseCounts += 0;

        return winLoseCounts;
    }

    public Action getNextMove(Action lastOpponentMove){
        this.prevMoves.add(lastOpponentMove);
        Action actionH = HorseBot(lastOpponentMove);
        Action actionBB = BotBot(lastOpponentMove);
        Action actionRand = randomMove('r');
        Action actionIB = ImitatorBot(lastOpponentMove);
        

        //System.out.println("actions:");
        //System.out.println(actionH);
        //System.out.println(actionBB);
        //System.out.println(actionIB);
        //System.out.println(actionRand);

        roundsCompleted++;

        //System.out.println("prev moves:");
        for (int i = 0; i < prevMoves.size(); i++) {
            //System.out.println(prevMoves.get(i));
            WinLoseCountH = runRound(prevMoves.get(i), horseBotMoves.get(i), WinLoseCountH);
            WinLoseCountBB = runRound(prevMoves.get(i), botBotMoves.get(i), WinLoseCountBB);
            WinLoseCountR = runRound(prevMoves.get(i), randMoves.get(i), WinLoseCountR);
        }
        Action ourAction = actionRand;
        if (WinLoseCountH >= WinLoseCountBB && WinLoseCountH >= WinLoseCountR) {
            ourAction = actionH;
        } else if (WinLoseCountBB >= WinLoseCountH && WinLoseCountBB >= WinLoseCountR) {
            ourAction = actionBB;
        } else {
            ourAction = actionRand;
        }

       
        //System.out.println("our move");
        //System.out.println(ourAction);
        ourMoves.add(ourAction);
        return ourAction;
    }
}

