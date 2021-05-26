import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;
import localsearch.selectors.MinMaxSelector;

public class QueenCBLS {
	int n;
	// modelling
	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem S;

	public QueenCBLS(int n){
		this.n = n;
	}
	private void stateModel(){
		mgr = new LocalSearchManager();
		x = new VarIntLS[n];
		for(int i = 0; i < n; i++){
			x[i] = new VarIntLS(mgr,0,n-1);
		}
		S = new ConstraintSystem(mgr);
		S.post(new AllDifferent(x));

		IFunction[] f1 = new IFunction[n];
		for(int i = 0; i < n; i++)
			f1[i] = new FuncPlus(x[i], i);
		S.post(new AllDifferent(f1));

		IFunction[] f2 = new IFunction[n];
		for(int i = 0; i < n; i++)
			f2[i] = new FuncPlus(x[i], -i);
		S.post(new AllDifferent(f2));

		mgr.close();// mandatory

	}
	private void localSearch(){
		MinMaxSelector mms = new MinMaxSelector(S);
		for(int it = 1; it <= 10000; it++){
			VarIntLS sel_x = mms.selectMostViolatingVariable();
			int v = mms.selectMostPromissingValue(sel_x);
			sel_x.setValuePropagate(v);// local move for generating new neighbor

			System.out.println("step " + it + " S = " + S.violations());
			if(S.violations() == 0){
				break;
			}
		}
	}
	public void solve(){
		stateModel();
		HillClimbing searcher = new HillClimbing(S);
		searcher.search(100, 10000);

		//localSearch();
		//for(int i = 0; i < n; i++)
		//System.out.println("x[" + i + "] = " + x[i].getValue());
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueenCBLS app = new QueenCBLS(10);
		app.solve();
	}

}
