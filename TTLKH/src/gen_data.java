import java.io.*;
import java.util.Random;

public class gen_data {
    Random generator = new Random();
    int n;
    int[][] d;

    public gen_data(){
        Random generator = new Random();
        n = (generator.nextInt(10)+1) * 2;
        d = new int[n][n];
        for(int i = 0; i<n; i++){
            for(int j = 0; j <n; j++){
                if(i <= j){
                    if (i == j)
                        d[i][j] = 0;
                    else
                        d[i][j] = generator.nextInt(9) + 1;
                        d[j][i] = d[i][j];
                }
            }
        }
    }
    public void save_gendata() throws IOException {

        File file = new File("D:\\Code\\Tối ưu lập kế hoạch\\CPModel\\data_gen.txt");
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

        outputStreamWriter.write(String.valueOf(n));
        // Dùng để xuống hàng
        outputStreamWriter.write("\n");

        for (int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                if(j != n-1)
                    outputStreamWriter.write(String.valueOf(d[i][j])+" ");
                else
                    outputStreamWriter.write(String.valueOf(d[i][j]));
            }
            // Dùng để xuống hàng
            outputStreamWriter.write("\n");
        }
        // Đây là phương thức quan trọng!
        // Nó sẽ bắt chương trình chờ ghi dữ liệu xong thì mới kết thúc chương trình.
        outputStreamWriter.flush();
        System.out.println("Success...");
    }

    public static void main(String[] args) throws IOException {
        gen_data g = new gen_data();
        g.save_gendata();
    }
}
