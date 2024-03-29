import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");

		int N = Integer.parseInt(split[0]);
		int M = Integer.parseInt(split[1]);
		int Q = Integer.parseInt(split[2]);

		int[][] arr = new int[N][M];
		for (int i = 0; i < N; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < M; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}

		int dr[] = new int[] { 0, 1, 0, -1 };
		int dc[] = new int[] { 1, 0, -1, 0 };

		while (Q-- > 0) {
			split = br.readLine().split(" ");
			int r1 = Integer.parseInt(split[0]) - 1;
			int c1 = Integer.parseInt(split[1]) - 1;
			int r2 = Integer.parseInt(split[2]) - 1;
			int c2 = Integer.parseInt(split[3]) - 1;

			// r1, c1을 기점으로 4번 회전
			int t1 = arr[r1][c1]; // 위 왼쪽
			int t2 = arr[r1][c2]; // 위 오른쪽
			int t3 = arr[r2][c2]; // 아래 오른쪽
			int t4 = arr[r2][c1]; // 아래 왼쪽

			for (int j = c2 - 1; j >= c1; j--) { // 위쪽 가로 한 줄 이동
				arr[r1][j + 1] = arr[r1][j];
			}
			for (int i = r2 - 1; i > r1; i--) { // 오른쪽 세로 한 줄 이동
				arr[i + 1][c2] = arr[i][c2];
			}
			arr[r1 + 1][c2] = t2;
			for (int j = c1; j < c2 - 1; j++) { // 아래쪽 가로 한 줄 이동
				arr[r2][j] = arr[r2][j + 1];
			}
			arr[r2][c2 - 1] = t3;
			for (int i = r1; i < r2 - 1; i++) {
				arr[i][c1] = arr[i + 1][c1];
			}
			arr[r2 - 1][c1] = t4;

			// 평균 값 계산
			int[][] newArr = new int[N][M];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					newArr[i][j] = arr[i][j];
				}
			}

			for (int i = r1; i <= r2; i++) {
				for (int j = c1; j <= c2; j++) {
					int cnt = 1;
					int sum = arr[i][j];
					for (int k = 0; k < 4; k++) {
						int ni = dr[k] + i;
						int nj = dc[k] + j;
						if (0 <= ni && ni < N && 0 <= nj && nj < M) {
							cnt++;
							sum += arr[ni][nj];
						}
					}
					newArr[i][j] = (int) (sum / cnt);
				}
			}
			arr = newArr;
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}
}