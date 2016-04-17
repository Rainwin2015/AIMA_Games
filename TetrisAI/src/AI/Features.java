/**
 * This class contains all the features and corresponding default weight of game
 * state.
 */

package AI;

/**
 * @author Zhang Kai
 */
public class Features {

    /**
     * General game play features.
     *
     * @author Rainwin2015
     */
    public static enum Feature {

        NUM_HOLES(
                -5.7612558434178),
        SCORE(
                4.928759132408411),
        ROW_TRANSITION(
                -3.1336813234776466),
        COL_TRANSITION(
                -9.650833052922613),
        WELL_SUM(
                -3.5600160926209976),
        LANDING_HEIGHT(-3.7829683278806012);

        private int phase;
        private double defaultValue;

        Feature(double defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setDefaultValue(double value) {
            this.defaultValue = value;
        }

        public double getDefaultValue() {
            return defaultValue;
        }

    }

    /**
     * Returns a String representing the names and default values of all
     * features.
     *
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Feature feature : Feature.values()) {
            sb.append("[" + feature.name() + " , " + feature.getDefaultValue() + "]\n");
        }
        return sb.toString();
    }

}
