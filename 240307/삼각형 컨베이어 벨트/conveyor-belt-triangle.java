import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n, t;
		String[] split;
		split = br.readLine().split(" ");

		n = Integer.parseInt(split[0]);
		t = Integer.parseInt(split[1]);
		t = t > (2 * n) ? t % (2 * n) : t;

		int[][] arr = new int[3][n];

		for (int i = 0; i < 3; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < n; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}
		while (t-- > 0) {
			int t2 = arr[0][n - 1];
			int t3 = arr[1][n - 1];
			// 좌ㅡ>우로 밀고 당기기
			for (int i = n - 1; i > 0; i--) {
				arr[0][i] = arr[0][i - 1];
			}
			arr[0][0] = arr[2][n - 1];

			// 좌->우로 밀고 당기기
			for (int i = n - 1; i > 0; i--) {
				arr[1][i] = arr[1][i - 1];
			}
			arr[1][0] = t2;

			// 좌->우로 밀고 당기기
			for (int i = n - 1; i > 0; i--) {
				arr[2][i] = arr[2][i - 1];
			}
			arr[2][0] = t3;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}
}