import localsearch.constraints.basic.IsEqual;
import localsearch.functions.sum.SumVar;
import localsearch.functions.sum.SumVarConstraints;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SSM {
    int n ;
    int[][] d;

    public SSM(String filename) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        Scanner scanner = new Scanner(fileInputStream);

        this.n = Integer.parseInt(scanner.nextLine());
        this.d = new int[this.n][this.n];

        for(int i = 0; i < n; i++ ){
            String line = scanner.nextLine();
            for(int j = 0; j< n; j++){
                this.d[i][j] = Integer.parseInt(line.split(" ")[j]);
            }
        }
    }

    LocalSearchManager mgr;// doi tuong quan ly
    VarIntLS[][][] X;// bien quyet dinh
    ConstraintSystem S;// he thong cac rang buoc

    public void stateModel(){
        mgr = new LocalSearchManager();
        X = new VarIntLS[n][n][2*n - 2];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int t = 0; t < 2*n - 2; t++){
                    X[i][j][t] = new VarIntLS(mgr, 0, 1);
                }
            }
        }
        for(int t = 0; t < 2*n - 2; t++){
            for(int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(i*n+j < n/2)
                        X[i][j][t].setValue(1);
                    else
                        X[i][j][t].setValue(0);
                }
            }
        }
        S = new ConstraintSystem(mgr);
        //define rang buoc
        // doi i khong the tu thi dau voi chinh minh
        for(int i = 0; i < n; i++){
            for(int t = 0; t < 2*n - 2; t++) {
                S.post(new IsEqual(X[i][i][t], 0));
            }
        }
        // hai doi khac nhau chac chan doi dau voi nhau trong 1 tuan nao do
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i != j){
                    VarIntLS[] doidau = new VarIntLS[2*n-2];
                    for(int t = 0; t< 2*n -2; t++){
                        doidau[t] = X[i][j][t];
                    }
                S.post(new IsEqual(new SumVar(doidau),1));
                }
            }
        }
//        //trong 1 tuan chi co n/2 tran
        for(int t = 0; t < 2*n - 2; t++){
            VarIntLS[] doidau = new VarIntLS[n*n];
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    doidau[i*n + j] = X[i][j][t];
                }
            }
            S.post(new IsEqual(new SumVar(doidau),n/2));
        }
//         doi i chi co the thi dau 1 lan trong 1 tuan
        for(int i = 0; i < n ; i++){
            for(int t = 0; t < 2*n - 2; t++){
                VarIntLS[] doidau = new VarIntLS[2*n];
                for(int j = 0; j < n; j++){
                        doidau[j] = X[i][j][t];
                        doidau[n+j] = X[j][i][t];
                }
                S.post(new IsEqual(new SumVar(doidau),1));
            }
        }
        mgr.close();
    }

    public void search(){
        class Move {
            int i1,i2,t;
            int j1, j2;

            public Move(int i1,int i2, int j1, int j2,int t) {
                this.i1 = i1;
                this.i2 = i2;
                this.j1 = j1;
                this.j2 = j2;
                this.t = t;
            }
        }
        Random R = new Random();
        ArrayList<Move> cand = new ArrayList<Move>();
        int it = 0;

        while(it <= 10000 && S.violations() > 6){
            cand.clear(); int minDelta = Integer.MAX_VALUE;
            for(int t = 0; t < 2*n - 2; t++){
                for(int i1 = 0; i1 < n; i1++){
                    for(int j1 = 0; j1 < n ; j1++){
                        for(int i2 = 0; i2 < n; i2++) {
                            for (int j2 = 0; j2 < n; j2++) {
                                int delta = S.getSwapDelta(X[i1][j1][t], X[i2][j2][t]);
                                if (delta < minDelta) {
                                    cand.clear();
                                    cand.add(new Move(i1, i2, j1, j2, t));
                                    minDelta = delta;
                                } else if (delta == minDelta)
                                    cand.add(new Move(i1, i2, j1, j2, t));
                            }
                        }
                    }
                }
            }
            int idx = R.nextInt(cand.size());
            Move m = cand.get(idx);
            int i1 = m.i1; int i2 = m.i2; int j1 = m.j1; int j2 = m.j2; int t = m.t;
            X[i1][j1][t].swapValuePropagate(X[i2][j2][t]);
            it++;
        }


        int[][] Y = new int[n][2*n - 2];
        int dis = 0;
        for(int t = 0; t < 2*n - 2; t++){
            if(t == 0) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (i != j) {
                            if(X[i][j][t].getValue() == 1) {
                                dis += 0;
                                Y[i][t] = i;
                            }
                            else if(X[j][i][t].getValue() == 1) {
                                dis += d[i][j];
                                Y[i][t] = j;
                            }
                        }
                    }
                }
            }
            else{
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (i != j) {
                            if(X[i][j][t].getValue() == 1) {
                                dis += d[Y[i][t -1]][i];
                                Y[i][t] = i;
                            }
                            else if(X[j][i][t].getValue() == 1) {
                                dis += d[Y[i][t -1]][j];
                                Y[i][t] = j;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Total distance: "+ dis);

        for(int t = 0; t < 2*n - 2; t++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(X[i][j][t].getValue() == 1){
                        System.out.println("Tuần " + (t + 1) + " :" + (i+ 1)+" - "+(j + 1));
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        SSM s = new SSM("D:\\Code\\Tối ưu lập kế hoạch\\CPModel\\data_gen.txt");
        s.stateModel();
        s.search();
    }
}
