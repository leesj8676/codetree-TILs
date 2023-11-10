import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");
		int N = Integer.parseInt(split[0]);
		int K = Integer.parseInt(split[1]);
		int B = Integer.parseInt(split[2]);

		int dat[] = new int[N + 1]; // 있는지를 기록하는 체크 배열
		int prefixSum[] = new int[N + 1]; // 부분합

		Arrays.fill(dat, 1);
		for (int i = 0; i < B; i++) {
			int idx = Integer.parseInt(br.readLine());
			dat[idx] = 0;
		}
		dat[0] = 0;

		for (int i = 1; i <= N; i++) {
			prefixSum[i] = prefixSum[i - 1] + dat[i];
		}
		
		int ans = N;
		// k개가 연속해서 존재하는지 여부 : k개만큼의 부분합이 k
		for (int i = 0; i + K <= N; i++) {
			int sum = prefixSum[i + K] - prefixSum[i];
			int diff = K - sum;
			ans = Math.min(ans, diff); 
		}
		System.out.println(ans);
	}
}