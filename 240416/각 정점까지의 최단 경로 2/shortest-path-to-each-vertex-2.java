import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;

		split = br.readLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int m = Integer.parseInt(split[1]);

		// 최소거리 배열 선언 및 초기화
		// 자기 자신을 제외한 모든 점은 충분히 큰 값
		int[][] dist = new int[n + 1][n + 1];

		for (int i = 0; i <= n; i++) {
			Arrays.fill(dist[i], (int) 1e9);
		}

		for (int i = 1; i <= n; i++) {
			dist[i][i] = 0;
		}

		for (int i = 0; i < m; i++) {
			split = br.readLine().split(" ");
			int from = Integer.parseInt(split[0]);
			int to = Integer.parseInt(split[1]);
			int weight = Integer.parseInt(split[2]);

			dist[from][to] = dist[from][to] < weight ? dist[from][to] : weight; // from -> to 로 가는 경로 dist에 우선 기록
		}

		for (int k = 1; k <= n; k++) {
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
				}
			}
		}

		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (dist[i][j] == (int) 1e9)
					System.out.println(-1);
				else
					System.out.print(dist[i][j] + " ");
			}
			System.out.println();
		}
	}
}