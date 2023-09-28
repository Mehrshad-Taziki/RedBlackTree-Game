package Bot;

import DataStructures.Node;
import DataStructures.Pair;
import DataStructures.RedBlackTree;
import Enums.Player;

import java.util.ArrayList;
import java.util.Random;

public class AutoPlayer {
    RedBlackTree tree;
    ArrayList<Integer> bestCases;

    public AutoPlayer(RedBlackTree tree) {
        this.tree = tree;
        this.bestCases = new ArrayList<>();
    }


    public int choose(RedBlackTree tree) {
        this.tree = tree;
        bestCases.clear();
        for (int i = 1; i <= 32; i++) {
            if (isValid(i, Player.PLAYER_TWO, tree)) bestCases.add(i);
        }
        ArrayList<Pair> choices = new ArrayList<>();
        for (int pickedNumber :
                bestCases) {
            int score = getScore(pickedNumber);
            if (score == Integer.MAX_VALUE) return pickedNumber;
            score = score + 25;
            if (score >= 1) {
                choices.add(new Pair(pickedNumber, score));
            }
        }
        int totalCost = 0;
        for (Pair goodPick :
                choices) {
            totalCost += goodPick.getScore();
        }
        if (tree.height(tree.getRoot()) >= 4) {
            bestCases = getGoodPicks(tree, Player.PLAYER_TWO);
            int bestScore = Integer.MIN_VALUE;
            int bestPick = bestCases.get(0);
            for (Integer pick :
                    bestCases) {
                RedBlackTree cloneTree = tree.getClone();
                cloneTree.bstInsert(pick, Player.PLAYER_TWO);
                int thisPickScore = min(cloneTree, 3);
                if (thisPickScore > bestScore) {
                    bestScore = thisPickScore;
                    bestPick = pick;
                }
            }
            return bestPick;
        }
        if (totalCost != 0) {
            int pickOne = weightedRandom(choices);
            int pickTwo = weightedRandom(choices);
            int pickThree = weightedRandom(choices);
            int pickFour = weightedRandom(choices);
            int pickFive = weightedRandom(choices);
            Pair pairOne = new Pair(pickOne, calculateFinalScore(pickOne));
            Pair pairTwo;
            Pair pairThree;
            Pair pairFour;
            Pair pairFive;
            if (pickTwo != pickOne) pairTwo = new Pair(pickTwo, calculateFinalScore(pickTwo));
            else pairTwo = new Pair(pickTwo, -100000);
            if (pickThree != pickTwo && pickThree != pickOne)
                pairThree = new Pair(pickThree, calculateFinalScore(pickThree));
            else pairThree = new Pair(pickThree, -100000);
            if (pickFour != pickThree && pickFour != pickTwo && pickFour != pickOne)
                pairFour = new Pair(pickFour, calculateFinalScore(pickFour));
            else pairFour = new Pair(pickFour, -100000);
            if (pickFive != pickFour && pickFive != pickThree && pickFive != pickTwo && pickFive != pickOne)
                pairFive = new Pair(pickFive, calculateFinalScore(pickFive));
            else pairFive = new Pair(pickFive, -100000);
            Pair max = pairOne;
            if (max.getScore() < pairTwo.getScore()) max = pairTwo;
            if (max.getScore() < pairThree.getScore()) max = pairThree;
            if (max.getScore() < pairFour.getScore()) max = pairFour;
            if (max.getScore() < pairFive.getScore()) max = pairFive;
            return max.getValue();
        } else {
            bestCases = getGoodPicks(tree, Player.PLAYER_TWO);
            int bestScore = Integer.MIN_VALUE;
            int bestPick = bestCases.get(0);
            for (Integer pick :
                    bestCases) {
                RedBlackTree cloneTree = tree.getClone();
                cloneTree.bstInsert(pick, Player.PLAYER_TWO);
                int thisPickScore = min(cloneTree, 5);
                if (thisPickScore > bestScore) {
                    bestScore = thisPickScore;
                    bestPick = pick;
                }
            }
            return bestPick;
        }
    }

    public int calculateFinalScore(int pick) {
        RedBlackTree cloneTree = tree.getClone();
        cloneTree.bstInsert(pick, Player.PLAYER_TWO);
        if (cloneTree.isEnded()) {
            if (cloneTree.getDifference(Player.PLAYER_TWO) > 0) return Integer.MAX_VALUE;
            else if (cloneTree.getDifference(Player.PLAYER_TWO) == 0) return 0;
            else return Integer.MIN_VALUE;
        }
        ArrayList<Integer> validPicks = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            if (isValid(i, Player.PLAYER_ONE, cloneTree)) validPicks.add(i);
        }
        int totalScore = 0;
        for (int playerOnePick :
                validPicks) {
            RedBlackTree testTree = cloneTree.getClone();
            testTree.bstInsert(playerOnePick, Player.PLAYER_ONE);
            if (testTree.isEnded()) {
                if (testTree.getDifference(Player.PLAYER_TWO) > 0) totalScore += 200; //check numbers
                else if (testTree.getDifference(Player.PLAYER_TWO) == 0) totalScore += 0;
                else totalScore -= 200;
            } else totalScore += testTree.getDifference(Player.PLAYER_TWO);
        }
        return totalScore;
    }

    public int weightedRandom(ArrayList<Pair> choices) {
        int totalCost = 0;
        for (Pair goodPick :
                choices) {
            totalCost += goodPick.getScore();
        }
        Random random = new Random();
        int randomNumber = random.nextInt(totalCost);
        for (Pair goodPick :
                choices) {
            if (randomNumber > goodPick.getScore()) randomNumber = randomNumber - goodPick.getScore();
            else {
                return goodPick.getValue();
            }
        }
        return 0;
    }

    public int getScore(int pickedNumber) {
        RedBlackTree cloneTree = tree.getClone();
        int adjustment = 0;
        if (cloneTree.contains(pickedNumber)) adjustment = pickedNumber + 32;
        cloneTree.bstInsert(pickedNumber, Player.PLAYER_TWO);
        if (cloneTree.isEnded()) {
            if (cloneTree.getDifference(Player.PLAYER_TWO) > 0) return Integer.MAX_VALUE;
            else if (cloneTree.getDifference(Player.PLAYER_TWO) == 0) return 0;
            else return Integer.MIN_VALUE;
        } else return cloneTree.getDifference(Player.PLAYER_TWO) - adjustment;
    }

    public boolean isValid(int pick, Player inserter, RedBlackTree tree) {
        if (pick < 1) return false;
        if (pick > 32) return false;
        if (tree.contains(pick + 32)) return false;
        if (tree.contains(pick)) {
            Node node = tree.get(pick);
            if (node.getPlayer() != inserter) {
                return false;
            }
        }
        return true;
    }

    private int min(RedBlackTree tree, int count) {
        if (tree.isEnded()) {
            int score = tree.getDifference(Player.PLAYER_TWO);
            if (score > 0) return 2000 * (count + 1);
            if (score == 0) return 0;
            return -2000 * (count + 1);
        }
        if (count == 0) return giveScore(tree);
        ArrayList<Integer> picks = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            if (isValid(i, Player.PLAYER_ONE, tree)) picks.add(i);
        }
        int bestScore = Integer.MAX_VALUE;
        for (Integer pick :
                picks) {
            RedBlackTree cloneTree = tree.getClone();
            cloneTree.bstInsert(pick, Player.PLAYER_ONE);
            int thisPickScore = max(cloneTree, count - 1);
            if (thisPickScore < bestScore) {
                bestScore = thisPickScore;
            }
        }
        return bestScore;
    }

    private int max(RedBlackTree tree, int count) {
        if (tree.isEnded()) {
            int score = tree.getDifference(Player.PLAYER_TWO);
            if (score > 0) return 2000 * (count + 1);
            if (score == 0) return 0;
            return -2000 * (count + 1);
        }
        if (count == 0) return giveScore(tree);
        ArrayList<Integer> picks = getGoodPicks(tree, Player.PLAYER_TWO);
        int bestScore = Integer.MIN_VALUE;
        for (Integer pick :
                picks) {
            RedBlackTree cloneTree = tree.getClone();
            cloneTree.bstInsert(pick, Player.PLAYER_TWO);
            int thisPickScore = min(cloneTree, count - 1);
            if (thisPickScore > bestScore) {
                bestScore = thisPickScore;
            }
        }
        return bestScore;
    }

    public int getScore(int pickedNumber, RedBlackTree tree, Player turn) {
        RedBlackTree cloneTree = tree.getClone();
        cloneTree.bstInsert(pickedNumber, turn);
        if (cloneTree.isEnded()) {
            if (cloneTree.getDifference(Player.PLAYER_TWO) > 0) return Integer.MAX_VALUE;
            else if (cloneTree.getDifference(Player.PLAYER_TWO) == 0) return 0;
            else return Integer.MIN_VALUE;
        } else return cloneTree.getDifference(Player.PLAYER_TWO);
    }

    public ArrayList<Integer> getGoodPicks(RedBlackTree tree, Player turn) {
        ArrayList<Integer> ans;
        ArrayList<Integer> picks = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            if (isValid(i, turn, tree)) picks.add(i);
        }
        if (picks.size() <= 20) return picks;
        ArrayList<Pair> picksPairs = new ArrayList();
        if (turn == Player.PLAYER_TWO) {
            for (Integer pick :
                    picks) {
                int score = getScore(pick, tree, Player.PLAYER_TWO);
                picksPairs.add(new Pair(pick, score));
            }
        }
        if (turn == Player.PLAYER_ONE) {
            for (Integer pick :
                    picks) {
                int score = -getScore(pick, tree, Player.PLAYER_ONE);
                picksPairs.add(new Pair(pick, score));
            }
        }
        ans = topTwenty(picksPairs);
        return ans;
    }

    public ArrayList<Integer> topTwenty(ArrayList<Pair> pick) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int maxInt = Integer.MIN_VALUE;
            Pair maxPair = pick.get(0);
            for (Pair pickPair :
                    pick) {
                if (maxInt < pickPair.getScore()) {
                    maxPair = pickPair;
                    maxInt = pickPair.getScore();
                }
            }
            ans.add(maxPair.getValue());
            pick.remove(maxPair);
        }
        return ans;
    }

    public int giveScore(RedBlackTree tree) {
        return tree.getDifference(Player.PLAYER_TWO);
    }
}
