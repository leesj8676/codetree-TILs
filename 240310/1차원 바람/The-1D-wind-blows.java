import java.io.*;

public class Main {
    static int arr[][];
	static int N, M;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");

		N = Integer.parseInt(split[0]);
		M = Integer.parseInt(split[1]);
		int Q = Integer.parseInt(split[2]);

		arr = new int[N][M];

		for (int i = 0; i < N; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < M; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}

		while (Q-- > 0) {
			split = br.readLine().split(" ");
			int row = Integer.parseInt(split[0]) - 1;
			int d = split[1].charAt(0) == 'L' ? 1 : 0;

			// 시뮬레이션

			// 1. 최초 바람 shift
			wind(row, d);

			// 2. 전파 재귀적으로 확인 후 shift
			recurUp(row - 1, 1 - d);
			recurDown(row + 1, 1 - d);
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}

	private static void wind(int row, int d) {
		if (d == 1) { // 오른쪽으로 shift
			int t = arr[row][M - 1];
			for (int j = M - 1; j > 0; j--) {
				arr[row][j] = arr[row][j - 1];
			}
			arr[row][0] = t;
		}

		else { // 왼쪽으로 shift
			int t = arr[row][0];
			for (int j = 0; j < M - 1; j++) {
				arr[row][j] = arr[row][j + 1];
			}
			arr[row][M - 1] = t;
		}
	}

	private static void recurUp(int row, int d) {
		if (row < 0)
			return;
		if (!isPropagtaion(row, row + 1))
			return; // 위아래 row가 전파 조건인지 확인

		wind(row, d);
		recurUp(row - 1, 1 - d);
	}

	private static void recurDown(int row, int d) {
		if (row >= N)
			return;
		if (!isPropagtaion(row - 1, row))
			return; // 위아래 row가 전파 조건인지 확인

		wind(row, d);
		recurDown(row + 1, 1 - d);
	}

	private static boolean isPropagtaion(int row1, int row2) {
		for (int i = 0; i < M; i++) {
			if (arr[row1][i] == arr[row2][i])
				return true;
		}
		return false;
	}
}