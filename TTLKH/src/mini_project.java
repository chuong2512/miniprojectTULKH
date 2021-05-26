//import localsearch.constraints.basic.IsEqual;
//import localsearch.functions.sum.SumVar;
//import localsearch.model.ConstraintSystem;
//import localsearch.model.LocalSearchManager;
//import localsearch.model.VarIntLS;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//public class mini_project {
//    int n = 4;
//    int[][] d;
//
//    LocalSearchManager mgr;// doi tuong quan ly
//    VarIntLS[][] X;// bien quyet dinh
//    ConstraintSystem S;// he thong cac rang buoc
//
//    public void stateModel(){
//        mgr = new LocalSearchManager();
//        X = new VarIntLS[n][n];
//        for(int i = 0; i < n; i++){
//            for(int j = 0; j < n; j++){
//                    X[i][j] = new VarIntLS(mgr, 1, 2*n - 2);
//            }
//        }
//        for(int t = 0; t < 2*n - 2; t++){
//            for(int i = 0; i < n; i++) {
//                for (int j = 0; j < n; j++) {
//                    if(i*n+j < n/2)
//                        X[i][j][t].setValue(1);
//                    else
//                        X[i][j][t].setValue(0);
//                }
//            }
//        }
//        S = new ConstraintSystem(mgr);
//        //define rang buoc
//        // doi i khong the tu thi dau voi chinh minh
//        for(int i = 0; i < n; i++){
//            for(int t = 0; t < 2*n - 2; t++) {
//                S.post(new IsEqual(X[i][i][t], 0));
//            }
//        }
//        // hai doi khac nhau chac chan doi dau voi nhau trong 1 tuan nao do
//        for(int i = 0; i < n; i++){
//            for(int j = 0; j < n; j++){
//                if(i != j){
//                    VarIntLS[] doidau = new VarIntLS[2*n-2];
//                    for(int t = 0; t< 2*n -2; t++){
//                        doidau[t] = X[i][j][t];
//                    }
//                S.post(new IsEqual(new SumVar(doidau),1));
//                }
//            }
//        }
//        //trong 1 tuan chi co n/2 tran
//        for(int t = 0; t < 2*n - 2; t++){
//            VarIntLS[] doidau = new VarIntLS[n*n];
//            for(int i = 0; i < n; i++){
//                for(int j = 0; j < n; j++){
//                    doidau[i*n + j] = X[i][j][t];
//                }
//            }
//            S.post(new IsEqual(new SumVar(doidau),n/2));
//        }
//        // doi i chi co the thi dau 1 lan trong 1 tuan
//        for(int i = 0; i < n ; i++){
//            for(int t = 0; t < 2*n - 2; t++){
//                VarIntLS[] doidau = new VarIntLS[2*n];
//                for(int j = 0; j < n; j++) {
//                    doidau[j] = X[i][j][t];
//                    doidau[n+j] = X[j][i][t];
//                }
//            S.post(new IsEqual(new SumVar(doidau),1));
//            }
//        }
//        // doi i da n - 1 tran o san nha
//        for(int i = 0; i < n; i++){
//            VarIntLS[] doidau = new VarIntLS[n*(2*n-2)];
//            for(int j = 0; j < n; j++){
//                for(int t = 0; t< 2*n -2; t++){
//                    doidau[j*(2*n -2) + t] = X[i][j][t];
//                }
//                S.post(new IsEqual(new SumVar(doidau),n - 1));
//                }
//            }
//        // doi i da n - 1 tran tren san khach
//        for(int i = 0; i < n; i++){
//            VarIntLS[] doidau = new VarIntLS[n*(2*n-2)];
//            for(int j = 0; j < n; j++){
//                for(int t = 0; t< 2*n -2; t++){
//                    doidau[j*(2*n -2) + t] = X[j][i][t];
//                }
//                S.post(new IsEqual(new SumVar(doidau),n - 1));
//            }
//        }
//
//        mgr.close();
//    }
//
//    public void search(){
//        class Move {
//            int i1,i2,t;
//            int j1, j2;
//
//            public Move(int i1,int i2, int j1, int j2,int t) {
//                this.i1 = i1;
//                this.i2 = i2;
//                this.j1 = j1;
//                this.j2 = j2;
//                this.t = t;
//            }
//        }
//        Random R = new Random();
//        ArrayList<Move> cand = new ArrayList<Move>();
//        int it = 0;
//
//        while(it <= 100000 && S.violations() > 0){
//            cand.clear(); int minDelta = Integer.MAX_VALUE;
//            for(int t = 0; t < 2*n - 2; t++){
//                for(int i1 = 0; i1 < n; i1++){
//                    for(int j1 = 0; j1 < n ; j1++){
//                        for(int i2 = 0; i2 < n; i2++) {
//                            for (int j2 = 0; j2 < n; j2++) {
//                                int delta = S.getSwapDelta(X[i1][j1][t], X[i2][j2][t]);
//                                if (delta < minDelta) {
//                                    cand.clear();
//                                    cand.add(new Move(i1, i2, j1, j2, t));
//                                    minDelta = delta;
//                                } else if (delta == minDelta)
//                                    cand.add(new Move(i1, i2, j1, j2, t));
//                            }
//                        }
//                    }
//                }
//            }
//            int idx = R.nextInt(cand.size());
//            Move m = cand.get(idx);
//            int i1 = m.i1; int i2 = m.i2; int j1 = m.j1; int j2 = m.j2; int t = m.t;
//            X[i1][j1][t].swapValuePropagate(X[i2][j2][t]);
//            it++;
//        }
//        System.out.println("violations :" +S.violations());
//        System.out.println("it : " + it);
//        for(int i = 0; i < n; i++){
//            for(int j = 0; j < n; j++){
//                for(int t = 0; t < 2*n - 2; t++){
//                    if(X[i][j][t].getValue() == 1){
//                        System.out.println(i + " " + j + " " + t);
//                    }
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        mini_project s = new mini_project();
//        s.stateModel();
//        s.search();
//    }
//}
