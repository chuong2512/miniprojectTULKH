package localsearch.search;

import java.util.ArrayList;
import java.util.Random;

import localsearch.model.IConstraint;
import localsearch.model.VarIntLS;

public class HillClimbing {
    IConstraint c;
    VarIntLS[] x;
    Random R= new Random();
    class Move{
        int i;// index of variable
        int val;// value to be assigned to the selected variable
        Move(int i, int val){
            this.i = i; this.val = val;
        }
    }
    public HillClimbing(IConstraint c){
        this.c = c;
    }
    private void exploreNeighborhood(ArrayList<Move> cand){
        // explore all the neighboring solutions
        // collect all best neighbors into the cand list
        cand.clear();
        int minDelta = Integer.MAX_VALUE;
        for(int i = 0; i < x.length; i++){
            for(int v = x[i].getMinValue(); v <= x[i].getMaxValue(); v++){
                int d = c.getAssignDelta(x[i], v);// query the variation of the violations
                // of the constraint c
                if(d < minDelta){
                    cand.clear();
                    cand.add(new Move(i,v));
                    minDelta = d;
                }else if(d == minDelta){
                    cand.add(new Move(i,v));
                }
            }
        }
    }
    private Move select(ArrayList<Move> cand){
        int idx = R.nextInt(cand.size());
        return cand.get(idx);
    }
    public void search(int maxIters, int maxTime){
        x = c.getVariables();// return all variables defining the constraint
        ArrayList<Move> cand = new ArrayList<Move>();
        double t0 = System.currentTimeMillis();
        for(int it = 1; it <= maxIters; it++){
            exploreNeighborhood(cand);
            Move m = select(cand);

            x[m.i].setValuePropagate(m.val);// local move for new solution
            System.out.println("x[" + m.i + "] = " + m.val + " c.violations = "
                    + c.violations());
            if(c.violations() == 0){
                break;
            }
            double t= System.currentTimeMillis() - t0;
            if(t > maxTime){
                System.out.println("time limit expired");
                break;
            }
        }
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
