/**
 * Entry Method
 */
package AI;

import framework.Util;

public class Main {

    public static void main(String[] args) {

        AIPlayer player = new AIPlayer();

        int numThreads = 30;
        Util.setDemoMode(true);
        Util.setDebugMode(true);
        for (int j = 1; j < 3; j++) {
            for (int i = 0; i < numThreads; i++) {
                player.setForwarding(j);
                player.run();
                System.out.print(", ");
            }
            System.out.println();
        }

        return;
    }
}
