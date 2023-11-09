import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int k = Integer.parseInt(split[1]);

		int arr[][] = new int[n + 1][n + 1];
		int prefixSum[][] = new int[n + 1][n + 1];

		for (int i = 1; i <= n; i++) {
			split = br.readLine().split(" ");
			for (int j = 1; j <= n; j++) {
				arr[i][j] = Integer.parseInt(split[j-1]);
			}
		}

		// 2차원 부분합구하기
		// S[i][j] = S[i-1][j] + S[i-1][j-1] - S[i-1][j] + A[i][j]
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				prefixSum[i][j] = prefixSum[i - 1][j] + prefixSum[i][j - 1] - prefixSum[i - 1][j - 1] + arr[i][j];
			}
		}

		// (a1, b1) ~ (a2, b2) 범위의 직사각형의 합 구하기
		// sum = S[a2][b2] - S[a1][b2] - S[a2][b1] + S[a1][b1]
		int max = 0;
		for (int i = 0; i + k <= n; i++) {
			for (int j = 0; j + k <= n; j++) {
				int val = prefixSum[i + k][j + k] - prefixSum[i + k][j] - prefixSum[i][j + k] + prefixSum[i][j];
				max = Math.max(max, val);
			}
		}
		
		System.out.println(max);
	}
}