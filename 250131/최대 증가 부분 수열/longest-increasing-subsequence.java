import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] split;
        
        int N = Integer.parseInt(br.readLine());
        int[] arr = new int[N];
        int[] streak = new int[N];  // 현재 돌까지 밟을 수 있는 돌의 최대 개수
        split = br.readLine().split(" ");
        for(int i =0; i<N;i++) {
            arr[i] = Integer.parseInt(split[i]);
        }

        streak[0] = 1;
        for(int i = 1; i<N;i++) {
            int maxVal = 1;
            for(int j = i-1; j>=0;j--) {
                int val = streak[j];
                if(arr[j] < arr[i]) val++;
                maxVal = Math.max(maxVal, val);
            }
            streak[i] = maxVal;
        }

        //for(int i =0; i<N;i++) {
        //    System.out.println(streak[i]);
        //}
        
        System.out.println(streak[N-1]);
    }
}
