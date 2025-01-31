import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] split;
        
        int N = Integer.parseInt(br.readLine());
        int[] arr = new int[N];
        int[] dp = new int[N];  // 현재 위치를 밟을때 최대 증가수열 값
        split = br.readLine().split(" ");
        for(int i =0; i<N;i++) {
            arr[i] = Integer.parseInt(split[i]);
        }

        int ans = 1;
        Arrays.fill(dp, 1);
        for(int i = 1; i<N;i++) {
            for(int j = i-1; j>=0;j--) {
                if(arr[j] < arr[i]) {
                   dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(dp[i], ans);
        }
        
        System.out.println(ans);
    }
}
