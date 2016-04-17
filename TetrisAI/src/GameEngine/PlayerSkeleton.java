package GameEngine;

import AI.AIPlayer;

public class PlayerSkeleton {
    AIPlayer player;

    public PlayerSkeleton() {
        this.player = new AIPlayer();
    }

    //implement this function to have a working system
    public int pickMove(State s, int[][] legalMoves) {
        return player.pickMove(s);
    }

    public static void main(String[] args) {
        State s = new State();
        new TFrame(s);
        PlayerSkeleton p = new PlayerSkeleton();
        while (!s.hasLost()) {
            s.makeMove(p.pickMove(s, s.getLegalMoves()));
            s.draw();
            s.drawNext(0,0);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("You have completed " + s.getRowsCleared() + " rows.");
    }

}
