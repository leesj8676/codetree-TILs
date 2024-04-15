import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/*
4 1
3 1
1 3 5
2 2 7
3 4 6
4 2 2
*/

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;

		split = br.readLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int m = Integer.parseInt(split[1]);

		int[][] arrMat = new int[n + 1][n + 1]; // 단방향 인접 행렬

		for (int i = 0; i < m; i++) {
			split = br.readLine().split(" ");
			int from = Integer.parseInt(split[0]);
			int to = Integer.parseInt(split[1]);
			int weight = Integer.parseInt(split[2]);

			arrMat[from][to] = weight;
		}

		int[] dist = new int[n + 1];
		boolean[] visited = new boolean[n + 1];

		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[1] = 0;

		// 다익스트라: n번 순회(한번의 탐색에서 최소 하나의 최단 경로 구하는게 보장)
		for (int i = 0; i < n; i++) {
			// 저번 시도에서 구해낸 경로들 중, 최단 경로로 확정된 지점
			// 최초 시도 start = 1번
			int start = -1;
			int min = Integer.MAX_VALUE;
			for (int j = 1; j <= n; j++) {
				if (visited[j])
					continue;
				if (dist[j] < min) {
					min = dist[j];
					start = j;
				}
			}

			if(start==-1) break; // 끊어져 더 이상 탐색이 불가능한 경우
			
			visited[start] = true;
			for (int j = 1; j <= n; j++) {
				// 현재 지점에서 연결되어 있으면서 아직 최단 경로를 모르는 지점인 경우
				if (arrMat[start][j] != 0 && !visited[j]) {
					// 현재 최단경로 + 연결된 경로와 이미 구한 다른 경로 비교 후 업데이트
					int curPath = dist[start] + arrMat[start][j];
					dist[j] = dist[j] > curPath ? curPath : dist[j];
				}
			}
		}

		// 결과 출력
		for (int i = 2; i <= n; i++) {
			int val = dist[i];
			if(val==Integer.MAX_VALUE) System.out.println(-1);
			else System.out.println(val);
		}
	}
}