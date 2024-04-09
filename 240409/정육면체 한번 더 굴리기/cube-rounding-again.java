import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;

		int n, m, arr[][], scoresArr[][], ans = 0;
		int dices[][] = new int[][] { {}, { 3, 2, 4, 5 }, { 3, 6, 4, 1 }, { 6, 2, 1, 5 }, { 1, 2, 6, 5 },
				{ 3, 1, 4, 6 }, { 3, 5, 4, 2 } };
		int dr[] = new int[] { 0, 1, 0, -1 }; // 우 하 좌 상
		int dc[] = new int[] { 1, 0, -1, 0 };

		// 입력 받기
		split = br.readLine().split(" ");
		n = Integer.parseInt(split[0]);
		m = Integer.parseInt(split[1]);
		arr = new int[n][n];
		scoresArr = new int[n][n];
		for (int i = 0; i < n; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < n; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}

		// 각 위치에 얻을 수 있는 점수 BFS 최대 N*N 번 수행해서 구하기
		boolean[][] visited = new boolean[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				LinkedList<Integer[]> queue = new LinkedList<>();
				if (visited[i][j])
					continue;

				ArrayList<Integer[]> li = new ArrayList<>(); // 현재 BFS에서 방문한 위치 기록용
				visited[i][j] = true;
				queue.add(new Integer[] { i, j });
				li.add(new Integer[] { i, j });
				int val = arr[i][j];

				while (!queue.isEmpty()) {
					Integer[] mat = queue.poll();
					int r = mat[0];
					int c = mat[1];

					for (int k = 0; k < 4; k++) {
						int nr = r + dr[k];
						int nc = c + dc[k];

						if (nr < 0 || nr >= n || nc < 0 || nc >= n || visited[nr][nc])
							continue;
						if (val != arr[nr][nc])
							continue;

						visited[nr][nc] = true;
						queue.add(new Integer[] { nr, nc });
						li.add(new Integer[] { nr, nc });
					}
				}

				int score = li.size() * val;
				for (Integer[] mat : li) {
					int r = mat[0];
					int c = mat[1];
					scoresArr[r][c] = score;
				}
			}
		}

		// 주사위 위치 , 방향 선언
		int diceR = 0, diceC = 0, diceD = 0, diceEye = 1;
		int oldEye = -1;
		int rotOffset = 0; // rotation 보정 값
		int directionDiff = 0; // 방향 변화 값 (왼쪽 90도 == 1)

		for (int _t = 0; _t < m; _t++) {
			// 주사위 이동
			diceR = diceR + dr[diceD % 4];
			diceC = diceC + dc[diceD % 4];

			// 값 계산
			ans += scoresArr[diceR][diceC];

			// 주사위가 최초 기준 z축 회전한 정도 구하기
			for (int k = 0; k < 4; k++) {
				if (dices[diceEye][k] == oldEye) {
					rotOffset = k;
					break;
				}
			}

			// 바닥에 깔린 값(oppo)과 주사위의 눈(diceEye) 얻기
			int oppo = dices[diceEye][(directionDiff + rotOffset) % 4];
			oldEye = diceEye;
			diceEye = 7 - oppo;
			directionDiff = 0;

			// 주사위 방향 계산
			int val = arr[diceR][diceC];

			if (oppo > val) { // 주사위의 눈이 arr의 값보다 크면, 90도 회전
				directionDiff = 1;
				diceD = (diceD + 1) % 4;
			} else if (oppo < val) { // 주사위의 눈이 arr의 값보다 크면, 270도 회전
				directionDiff = 3;
				diceD = (diceD + 3) % 4;
			}

			// 다음턴에 이 방향으로 진행 가능한지 조사
			int nnr = diceR + dr[diceD % 4];
			int nnc = diceC + dc[diceD % 4];
			if (nnr < 0 || nnr >= n || nnc < 0 || nnc >= n) { // 불가능하면, 반대 방향으로 전환
				directionDiff += 2;
				diceD = (diceD + 2) % 4;
			}
		}
		System.out.println(ans);
	}
}