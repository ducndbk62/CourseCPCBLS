package Baitap;

import localsearch.constraints.basic.AND;
import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.OR;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

/**
 *
 * @author hydon
 */

public class BinPacking2D {
    int W = 4;
    int H = 6;
    int N = 3;
    int[] w = {3 , 3 , 1};
    int[] h = {2 , 4 , 6};
    VarIntLS[] x;
    VarIntLS[] y;
    VarIntLS[] o;
    
    LocalSearchManager mgr;
    ConstraintSystem S;
    
    private void stateModel(){
        mgr = new LocalSearchManager();
        S = new ConstraintSystem(mgr);
        x = new VarIntLS[N];
        y = new VarIntLS[N];
        o = new VarIntLS[N];
        for(int i = 0 ; i < N;  i ++){
            x[i] = new VarIntLS(mgr , 0 , W);
            y[i] = new VarIntLS(mgr , 0 , H);
            o[i] = new VarIntLS(mgr , 0 , 1);
        }
        for (int i = 0; i < N; i++) {
            S.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(
                    new FuncPlus(x[i], w[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(
                    new FuncPlus(y[i], h[i]), H)));
            S.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(
                    new FuncPlus(x[i], h[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(
                    new FuncPlus(y[i], w[i]), H)));
        }
        for(int i = 0 ; i < N - 1 ; i ++){
            for(int j = i + 1 ; j < N ; j ++){
                IConstraint[] c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
                c[1] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
                c[2] = new LessOrEqual(new FuncPlus(y[i], h[i]), y[j]);
                c[3] = new LessOrEqual(new FuncPlus(y[j], h[j]), y[i]);
                S.post(new Implicate(new AND(new IsEqual(o[i], 0), new IsEqual(
						o[j], 0)), new OR(c)));
                c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[j], h[j]), x[i]);
                c[1] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
                c[2] = new LessOrEqual(new FuncPlus(y[i], h[i]), y[j]);
                c[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
                S.post(new Implicate(new AND(new IsEqual(o[i], 0), new IsEqual(
                        o[j], 1)), new OR(c)));
                c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
                c[1] = new LessOrEqual(new FuncPlus(x[i], h[i]), x[j]);
                c[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
                c[3] = new LessOrEqual(new FuncPlus(y[j], h[j]), y[i]);
                S.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(
                        o[j], 0)), new OR(c)));
                c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[j], h[j]), x[i]);
                c[1] = new LessOrEqual(new FuncPlus(x[i], h[i]), x[j]);
                c[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
                c[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
                S.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(
                        o[j], 1)), new OR(c)));
            }
        }
        mgr.close();
    }
    
    private void search(){
        //Hill hillSearch = new Hill();
        //hillSearch.LocalSearch(S , 1000);
        Tabu tabu = new Tabu(S);
        tabu.search(100000, 60, 100);
    }
    
    public void solve(){
        stateModel();
        search();
        for(int i = 0 ; i < N ; i ++){
            System.out.println("Item " + i + ": " + x[i].getValue() + " " + 
                    y[i].getValue() + " " + o[i].getValue());
        }
    }
    
    public static void main(String[] args) {
        BinPacking2D app = new BinPacking2D();
        app.solve();
    }
}
