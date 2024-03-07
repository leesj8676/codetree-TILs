import java.io.*;

public class Main {
public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n, t;
		String[] split;
		split = br.readLine().split(" ");

		n = Integer.parseInt(split[0]);
		t = Integer.parseInt(split[1]);
		t = t > (2 * n) ? t % (2 * n) : t;

		int[][] arr = new int[2][n];

		split = br.readLine().split(" ");
		for (int i = 0; i < n; i++) {
			arr[0][i] = Integer.parseInt(split[i]);
		}

		split = br.readLine().split(" ");
		for (int i = 0; i < n; i++) {
			arr[1][i] = Integer.parseInt(split[i]);
		}

		while (t-- > 0) {
			int t1 = arr[0][n-1];
			// 윗줄: 좌ㅡ>우로 밀고 당기기 (맨 왼쪽 값 == 아래쪽 맨 오른값 필요)
			for (int i = n - 1; i > 0; i--) {
				arr[0][i] = arr[0][i - 1];
			}
			arr[0][0] = arr[1][n-1];

			// 아랫줄: 좌->우로 밀고 당기기 (맨 왼쪽 값 == 위쪽 맨 오른쪽값 필요)
			for (int i = n - 1; i > 0; i--) {
				arr[1][i] = arr[1][i - 1];
			}
			arr[1][0] = t1;
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}
}