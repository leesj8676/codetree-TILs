import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
7 2 1
3 2 1 0 0 0 0
4 0 4 0 2 1 4
4 4 4 0 2 0 4
0 0 0 0 3 0 4
0 0 4 4 4 0 4
0 0 4 0 0 0 4
0 0 4 4 4 4 4
 * */

public class Main {
	static int n, m, k, arr[][];
	static int[][] teamPathArr; // 팀별 이동선
	static int[][] headerPos, tailPos;// 팀별 머리사람, 꼬리사람 위치
	static int[] dr = new int[] { 0, -1, 0, 1 }, dc = new int[] { 1, 0, -1, 0 }; // 우, 상, 좌, 하

	static int dist; // dfs 기록용 전역변수 (공 맞은 사람과 머리와의 거리)
	static boolean[][] visited; // dfs 기록용 전역변수 (dfs 방문 여부)

	public static void main(String[] args) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;

		split = br.readLine().split(" ");
		n = Integer.parseInt(split[0]);
		m = Integer.parseInt(split[1]);
		k = Integer.parseInt(split[2]);

		arr = new int[n][n];
		headerPos = new int[m + 1][2];
		tailPos = new int[m + 1][2];

		for (int i = 0; i < n; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < n; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}

		// DFS로 팀별 이동선 기록
		teamPathArr = new int[n][n];
		getTeamPathArr();

		// 팀별 머리, 꼬리 위치 기록
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (arr[i][j] == 1) {
					int teamNo = teamPathArr[i][j];
					headerPos[teamNo] = new int[] { i, j };
				}
				if (arr[i][j] == 3) {
					int teamNo = teamPathArr[i][j];
					tailPos[teamNo] = new int[] { i, j };
				}
			}
		}

		// 라운드별 공 시작 위치 선언
		int[][] ballPos = new int[4 * n][2]; // 라운드별 공 시작 위치

		for (int i = 0; i < n; i++) {
			int r = 0 + i, c = 0;
			ballPos[i] = new int[] { r, c };
		}

		for (int i = n; i < 2 * n; i++) {
			int r = n - 1, c = 0 + (i - n);
			ballPos[i] = new int[] { r, c };
		}
		for (int i = 2 * n; i < 3 * n; i++) {
			int r = n - 1 - (i - 2 * n), c = n - 1;
			ballPos[i] = new int[] { r, c };
		}
		for (int i = 3 * n; i < 4 * n; i++) {
			int r = 0, c = n - 1 - (i - 3 * n);
			ballPos[i] = new int[] { r, c };
		}

		long ans = 0;
		for (int round = 0; round < k; round++) {
			visited = new boolean[n][n];
			dist = Integer.MIN_VALUE;
			// 팀별 한칸씩 이동
			for (int i = 1; i <= m; i++) {

				// 현재 팀의 머리 사람 위치 구해서 이동
				int[] hPos = headerPos[i];
				int r = hPos[0];
				int c = hPos[1];
				for (int k2 = 0; k2 < 4; k2++) {
					int nr = r + dr[k2];
					int nc = c + dc[k2];
					if (nr < 0 || nr >= n || nc < 0 || nc >= n)
						continue;
					if (arr[nr][nc] == 4 || arr[nr][nc] == 3) {
						headerPos[i] = new int[] { nr, nc };
						arr[nr][nc] = 1;
						arr[r][c] = 2;
						break;
					}
				}

				// 현재 팀의 꼬리 사람 위치 구해서 이동
				int[] tPos = tailPos[i];
				r = tPos[0];
				c = tPos[1];
				for (int k2 = 0; k2 < 4; k2++) {
					int nr = r + dr[k2];
					int nc = c + dc[k2];
					if (nr < 0 || nr >= n || nc < 0 || nc >= n)
						continue;
					if (arr[nr][nc] == 2) {
						tailPos[i] = new int[] { nr, nc };
						arr[nr][nc] = 3;
						arr[r][c] = 4;
						break;
					}
				}
			}

			// 공 던지기
			// 현재 라운드의 공 던질 위치, 방향 구하기
			int ballIndex = round % (4 * n);
			int[] pos = ballPos[ballIndex];
			int ballDirection = ballIndex / n;

			// 현재 위치에서 공 던지기
			for (int k2 = 0; k2 < n; k2++) {
				// r, c로 + k(0~n-1) nr, nc 구하기
				int r = pos[0] + dr[ballDirection] * k2;
				int c = pos[1] + dc[ballDirection] * k2;
				// 사람이 있으면, 그 사람의 팀 번호 구하가ㅣ
				if (arr[r][c] >= 1 && arr[r][c] <= 3) {
					int teamNo = teamPathArr[r][c];
					// 해당 팀의 헤더 위치에서부터 DFS를 통해 거리 구하기
                    visited[headerPos[teamNo][0]][headerPos[teamNo][1]] = true;
					getDist(headerPos[teamNo][0], headerPos[teamNo][1], r, c, 1);
					ans += dist * dist;
					// 해당 팀의 header와 tail 위치 swap 이후 종료
					int headerR = headerPos[teamNo][0];
					int headerC = headerPos[teamNo][1];
					int tailR = tailPos[teamNo][0];
					int tailC = tailPos[teamNo][1];
					arr[headerR][headerC] = 3;
					arr[tailR][tailC] = 1;
					headerPos[teamNo] = new int[] { tailR, tailC };
					tailPos[teamNo] = new int[] { headerR, headerC };
					
					break;	// 최초 맞은 공만 계산
				}
			}
		}

		System.out.println(ans);
	}

	private static void getDist(int curR, int curC, int goalR, int goalC, int count) {
		if (curR == goalR && curC == goalC) {
			dist = count;
			return;
		}

		for (int k2 = 0; k2 < 4; k2++) {
			int nr = curR + dr[k2];
			int nc = curC + dc[k2];
			if (nr < 0 || nr >= n || nc < 0 || nc >= n)
				continue;
			if (1 <= arr[nr][nc] && arr[nr][nc] <= 3 && !visited[nr][nc]) {
				visited[nr][nc] = true;
				getDist(nr, nc, goalR, goalC, count + 1);
				break;
			}
		}
	}

	private static void getTeamPathArr() {
		int teamCnt = 1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (arr[i][j] != 0 && teamPathArr[i][j] == 0) {// 아직 찾지 않은 이동 경로인 경우
					dfs(i, j, teamCnt++);
				}
			}
		}
	}

	private static void dfs(int r, int c, int teamCnt) {
		teamPathArr[r][c] = teamCnt;

		int nr, nc;
		for (int k2 = 0; k2 < 4; k2++) {
			nr = r + dr[k2];
			nc = c + dc[k2];
			if (nr < 0 || nr >= n || nc < 0 || nc >= n)
				continue;
			if (arr[nr][nc] != 0 && teamPathArr[nr][nc] == 0) {
				dfs(nr, nc, teamCnt);
			}
		}
	}
}