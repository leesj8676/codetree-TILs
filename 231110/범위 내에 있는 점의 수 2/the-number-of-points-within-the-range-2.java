import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");
		int N = Integer.parseInt(split[0]);
		int Q = Integer.parseInt(split[1]);

		int arr[] = new int[1_000_002]; // 수평선 (있으면 1)
		int prefixSum[] = new int[1_000_002]; // 부분합

		split = br.readLine().split(" ");
		for (int i = 0; i < N; i++) {
			int p = Integer.parseInt(split[i]) + 1;
			arr[p] = 1;
		}

		// 부분합 구하기
		for (int i = 1; i <= 1_000_001; i++) {
			prefixSum[i] = prefixSum[i - 1] + arr[i];
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Q; i++) {
			split = br.readLine().split(" ");
			int L = Integer.parseInt(split[0]) + 1;
			int R = Integer.parseInt(split[1]) + 1;
			int val = prefixSum[R] - prefixSum[L - 1];
			sb.append(val + "\n");
		}
		System.out.println(sb.toString());
	}
}