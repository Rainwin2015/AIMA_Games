package GA;
import AI.Features;
import AI.FeaturesMatrix;


import java.util.Vector;

/**
 * Created by RayZK on 05/04/16.
 */
public class FitnessFunction {
	public static final int POPULATION = 100;
	FeaturesMatrix fmatrix; 
	
	Vector<FeaturesMatrix> fitfunction = new Vector<FeaturesMatrix>();
	
	
	public Vector<FeaturesMatrix> getRandomize()
	{
		for(int i=0; i<POPULATION; i++)
		{
			
			fmatrix = new FeaturesMatrix();
			
			for (Features.Feature feature : Features.Feature.values()) {
				
				switch (feature) {
	            case NUM_HOLES:
	                fmatrix.set(feature, Math.random()*-1);
	                break;
	            case SCORE:
	            	fmatrix.set(feature, Math.random());
	                break;

	            case ROW_TRANSITION:
	            	fmatrix.set(feature, Math.random()*-1);
	                break;

	            case COL_TRANSITION:
	            	fmatrix.set(feature, Math.random()*-1);
	                break;

	            case WELL_SUM:
	            	fmatrix.set(feature, Math.random()*-1);
	                break;
	            case LANDING_HEIGHT:
	            	fmatrix.set(feature, Math.random()*-1);
	                break;

	            default:
	                return null;
	        }
			}
			
			fitfunction.add(fmatrix);
		}
		return fitfunction;
	}

	public Vector<FeaturesMatrix> getFullyRandomize()
	{
		for(int i=0; i<POPULATION; i++)
		{

			fmatrix = new FeaturesMatrix();

			for (Features.Feature feature : Features.Feature.values()) {

				switch (feature) {
					case NUM_HOLES:
						fmatrix.set(feature, Math.random()*2-1);
						break;
					case SCORE:
						fmatrix.set(feature, Math.random()*2-1);
						break;

					case ROW_TRANSITION:
						fmatrix.set(feature, Math.random()*2-1);
						break;

					case COL_TRANSITION:
						fmatrix.set(feature, Math.random()*2-1);
						break;

					case WELL_SUM:
						fmatrix.set(feature, Math.random()*2-1);
						break;
					case LANDING_HEIGHT:
						fmatrix.set(feature, Math.random()*2-1);
						break;

					default:
						return null;
				}
			}

			fitfunction.add(fmatrix);
		}
		return fitfunction;
	}
}
