import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int t = Integer.parseInt(split[1]);

		int arr[] = new int[n + 1];
		int prefixSum[] = new int[n + 1];

		split = br.readLine().split(" ");
		for (int i = 1; i <= n; i++) {
			arr[i] = Integer.parseInt(split[i - 1]);
		}

		for (int i = 1; i <= n; i++) {
			prefixSum[i] = prefixSum[i - 1] + arr[i];
		}

		int cnt = 0;
		// 모든 연속된 구간의 합
		// 연속된 구간(k)이 1~n개까지 모두 구하기 -> O(n^2)
		for (int k = 1; k <= n; k++) {
			for (int i = 0; i + k <= n; i++) {
				int sum = prefixSum[i + k] - prefixSum[i];
				if (sum == t)
					cnt++;
			}
		}
		System.out.println(cnt);
	}
}