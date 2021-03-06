package cbls115676khmt61.phamquangdung;

import localsearch.constraints.basic.AND;
import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.constraints.basic.OR;
import localsearch.functions.basic.FuncMinus;
import localsearch.functions.basic.FuncPlus;
import localsearch.functions.sum.Sum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Container {
	
	int W = 4;
	int H = 6;
	int N = 6;
	int [] w = {1, 3, 2, 3, 1, 2};
	int [] h = {4, 1, 2, 1, 4, 3};
	
	LocalSearchManager mgr;
	ConstraintSystem S;
	VarIntLS[] x;
	VarIntLS[] y;
	VarIntLS[] o;
	
	private void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[N];
		y = new VarIntLS[N];
		o = new VarIntLS[N];
		
		for(int i = 0; i < N; i++) {
			x[i] = new VarIntLS(mgr, 0, N);
			y[i] = new VarIntLS(mgr, 0, N);
			o[i] = new VarIntLS(mgr, 0, 1);

		}
		
		S = new ConstraintSystem(mgr);
		
//		// gioi han trong container
//		for (int i = 0; i < N; i++) {
//			S.post(new LessOrEqual(new FuncPlus(x[i], w[i]), W));
//			S.post(new LessOrEqual(new FuncPlus(y[i], l[i]), L));
//		}
//		
//		// khong de len nhau
//		for(int i = 0; i < N-1; i++)
//		
//		// khong lam dich chuyen 
		
		for(int i = 0; i < N-1; i++){
			for(int j = i+1; j < N; j++){
				// items i and j cannot overlap
				IConstraint[] c1 = new IConstraint[2];
				c1[0] = new IsEqual(o[i], 0);
				c1[1] = new IsEqual(o[j], 0);
				IConstraint c2 = new AND(c1);
				IConstraint[] c3 = new IConstraint[4];
				c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]),x[j]);
				c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]),x[i]);
				c3[2] = new LessOrEqual(new FuncPlus(y[i], h[i]),y[j]);
				c3[3] = new LessOrEqual(new FuncPlus(y[j], h[j]),y[i]);
				IConstraint c4 = new OR(c3);
				S.post(new Implicate(c2, c4));
				
				
				c1 = new IConstraint[2];
				c1[0] = new IsEqual(o[i], 0);
				c1[1] = new IsEqual(o[j], 1);
				c2 = new AND(c1);
				c3 = new IConstraint[4];
				c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]),x[j]);
				c3[1] = new LessOrEqual(new FuncPlus(x[j], h[j]),x[i]);
				c3[2] = new LessOrEqual(new FuncPlus(y[i], h[i]),y[j]);
				c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]),y[i]);
				c4 = new OR(c3);
				S.post(new Implicate(c2, c4));
				
				
				c1 = new IConstraint[2];
				c1[0] = new IsEqual(o[i], 1);
				c1[1] = new IsEqual(o[j], 0);
				c2 = new AND(c1);
				c3 = new IConstraint[4];
				c3[0] = new LessOrEqual(new FuncPlus(x[i], h[i]),x[j]);
				c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]),x[i]);
				c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]),y[j]);
				c3[3] = new LessOrEqual(new FuncPlus(y[j], h[j]),y[i]);
				c4 = new OR(c3);
				S.post(new Implicate(c2, c4));
				
				c1 = new IConstraint[2];
				c1[0] = new IsEqual(o[i], 1);
				c1[1] = new IsEqual(o[j], 1);
				c2 = new AND(c1);
				c3 = new IConstraint[4];
				c3[0] = new LessOrEqual(new FuncPlus(x[i], h[i]),x[j]);
				c3[1] = new LessOrEqual(new FuncPlus(x[j], h[j]),x[i]);
				c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]),y[j]);
				c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]),y[i]);
				c4 = new OR(c3);
				S.post(new Implicate(c2, c4));
				
				
			}
		}
		
		for(int i = 0; i < N; i++){
			S.post(new Implicate(new IsEqual(o[i], 0), 
					new LessOrEqual(new FuncPlus(x[i], w[i]), W)));
			S.post(new Implicate(new IsEqual(o[i], 0), 
					new LessOrEqual(new FuncPlus(y[i], h[i]), H)));
			
			S.post(new Implicate(new IsEqual(o[i], 1), 
					new LessOrEqual(new FuncPlus(x[i], h[i]), W)));
			S.post(new Implicate(new IsEqual(o[i], 1), 
					new LessOrEqual(new FuncPlus(y[i], w[i]), H)));
		}
		
		// lay hang ma khong dich chuyen hang
		for(int i = 0; i < N-1; i++) {
			for(int j = i+1; j < N; j++) {
				// y[j] - y[i] >= h[j] vs i la vat duoc lay truoc vat j
				IConstraint[] ic = new IConstraint[3];
				ic[0] = new LessOrEqual(new FuncMinus(y[j], y[i]), -h[j]);
				ic[1] = new NotEqual(new FuncMinus(x[j], x[i]), w[j]);
				ic[2] = new NotEqual(new FuncMinus(x[i], x[j]), w[j]);
				IConstraint ic3 = new OR(ic[1], ic[2]);
				S.post(new OR(ic[0], ic3));
				
				
			}
		}
		
		
		
		mgr.close();
	}
	
	public void search() {
		HillClimbingSearch searcher = new HillClimbingSearch();
		searcher.search(S,  100000);
		for(int i = 0; i < N; i++){
			System.out.println("item " + i + ": " + x[i].getValue() + "," + y[i].getValue() + "," + o[i].getValue());
		}
		
	}
	
	public void solve(){
		stateModel();
		search();
	}
	
	public static void main(String[] args) {
		Container con = new Container();
		con.solve();
	}
}
