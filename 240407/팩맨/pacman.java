import java.io.BufferedReader;
import java.io.InputStreamReader;

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

		int m, t, facR, facC;
		int[] dr = new int[] { -1, -1, 0, 1, 1, 1, 0, -1 }, dc = new int[] { 0, -1, -1, -1, 0, 1, 1, 1 };
		int[][][] monsters = new int[4][4][8];
		int[][] ghosts = new int[4][4];
		int[][] facDirections = new int[64][3];

		// 입력 받기
		split = br.readLine().split(" ");
		m = Integer.parseInt(split[0]);
		t = Integer.parseInt(split[1]);

		split = br.readLine().split(" ");
		facR = Integer.parseInt(split[0]) - 1;
		facC = Integer.parseInt(split[1]) - 1;

		for (int i = 0; i < m; i++) {
			split = br.readLine().split(" ");
			int r = Integer.parseInt(split[0]) - 1;
			int c = Integer.parseInt(split[1]) - 1;
			int d = Integer.parseInt(split[2]) - 1;
			monsters[r][c][d]++;
		}

		// facDirections 생성하기
		int cnt = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					facDirections[cnt++] = new int[] { i * 2, j * 2, k * 2 };
				}
			}
		}

		// 턴 횟수만큼
		for (int _t = 0; _t < t; _t++) {
			// 1. 몬스터 복제 시도
			// eggs 생성
			int[][][] eggs = new int[4][4][8];
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					for (int d = 0; d < 8; d++) {
						eggs[i][j][d] = monsters[i][j][d];
					}
				}
			}

			// 2. 몬스터 이동
			// 16개 위치에서 8개 방향을 가진 몬스터별 다음 위치 찾기
			// 현재 i, j위치에서 d 방향을 가진 몬스터가 갈 수 있는곳 nr, nj, nd 찾기

			int[][][] moveMonsters = new int[4][4][8]; // 몬스터 이동 반영
			for (int r = 0; r < 4; r++) {
				for (int c = 0; c < 4; c++) {
					outer: for (int d = 0; d < 8; d++) {
						if (monsters[r][c][d] == 0)
							continue;

						for (int k = 0; k < 8; k++) {
							int nd = (d + k) % 8;
							int nr = r + dr[nd];
							int nc = c + dc[nd];
							// 격자 안이면서 몬스터 시체 없고 팩맨 위치도 아니면
							if (0 <= nr && nr < 4 && 0 <= nc && nc < 4 && ghosts[nr][nc] == 0
									&& !(nr == facR && nc == facC)) {
								// 현재 위치의 같은 방향을 가진 몬스터들 함께 이동
								moveMonsters[nr][nc][nd] = monsters[r][c][d];
								continue outer;
							}
						}
						// 이동하지 않는 경우에도 복사 *** 확인 필요
						moveMonsters[r][c][d] = monsters[r][c][d];
					}
				}
			}

			// 복사 반영
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					for (int d = 0; d < 8; d++) {
						monsters[i][j][d] = moveMonsters[i][j][d];
					}
				}
			}

			// 3. 팩맨 이동
			int max = 0, maxIndex = 0;
			int r = facR, c = facC;
			for (int i = 0; i < 64; i++) {
				int[] direction = facDirections[i];
				int nr1 = r + dr[direction[0]], nc1 = c + dc[direction[0]];
				int nr2 = nr1 + dr[direction[1]], nc2 = nc1 + dc[direction[1]];
				int nr3 = nr2 + dr[direction[2]], nc3 = nc2 + dc[direction[2]];
				if (nr1 < 0 || nr1 >= 4 || nc1 < 0 || nc1 >= 4 || nr2 < 0 || nr2 >= 4 || nc2 < 0 || nc2 >= 4 || nr3 < 0
						|| nr3 >= 4 || nc3 < 0 || nc3 >= 4)
					continue;
				int sum = 0;
				for (int j = 0; j < 8; j++)
					sum += monsters[nr1][nc1][j];
				for (int j = 0; j < 8; j++)
					sum += monsters[nr2][nc2][j];
				if (nr1 == nr3 && nc1 == nc3) {
					sum += 0; // 재방문 예외처리 (재방문 고려해야 하는 경우: 첫번째와 세번째가 같을때)
				} else {
					for (int j = 0; j < 8; j++)
						sum += monsters[nr3][nc3][j];
				}
				if (max < sum) {
					max = sum;
					maxIndex = i;
				}
			}

			// 구한 방향으로 팩맨 이동하며 있는 몬스터 제거
			int[] direction = facDirections[maxIndex];
			int nr1 = r + dr[direction[0]], nc1 = c + dc[direction[0]];
			int nr2 = nr1 + dr[direction[1]], nc2 = nc1 + dc[direction[1]];
			int nr3 = nr2 + dr[direction[2]], nc3 = nc2 + dc[direction[2]];

			// nr1, nc1 에 몬스터 존재여부 (시체 기록용)
			boolean existMonster1 = (monsters[nr1][nc1][0] > 0) || (monsters[nr1][nc1][1] > 0)
					|| (monsters[nr1][nc1][2] > 0) || (monsters[nr1][nc1][3] > 0) || (monsters[nr1][nc1][4] > 0)
					|| (monsters[nr1][nc1][5] > 0) || (monsters[nr1][nc1][6] > 0) || (monsters[nr1][nc1][7] > 0);

			boolean existMonster2 = (monsters[nr2][nc2][0] > 0) || (monsters[nr2][nc2][1] > 0)
					|| (monsters[nr2][nc2][2] > 0) || (monsters[nr2][nc2][3] > 0) || (monsters[nr2][nc2][4] > 0)
					|| (monsters[nr2][nc2][5] > 0) || (monsters[nr2][nc2][6] > 0) || (monsters[nr2][nc2][7] > 0);

			boolean existMonster3 = (monsters[nr3][nc3][0] > 0) || (monsters[nr3][nc3][1] > 0)
					|| (monsters[nr3][nc3][2] > 0) || (monsters[nr3][nc3][3] > 0) || (monsters[nr3][nc3][4] > 0)
					|| (monsters[nr3][nc3][5] > 0) || (monsters[nr3][nc3][6] > 0) || (monsters[nr3][nc3][7] > 0);

			if (existMonster1) {
				for (int j = 0; j < 8; j++) {
					monsters[nr1][nc1][j] = 0;
				}
			}
			if (existMonster2) {
				for (int j = 0; j < 8; j++) {
					monsters[nr2][nc2][j] = 0;
				}
			}
			if (existMonster3) {
				for (int j = 0; j < 8; j++) {
					monsters[nr3][nc3][j] = 0;
				}
			}

			// 시체 업데이트
			if (existMonster1)
				ghosts[nr1][nc1] = -3;
			if (existMonster2)
				ghosts[nr2][nc2] = -3;
			if (existMonster3)
				ghosts[nr3][nc3] = -3;

			// 팩맨 위치 업데이트
			facR = nr3;
			facC = nc3;

			// 4. 몬스터 시체 소멸
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (ghosts[i][j] < 0) {
						ghosts[i][j]++;
					}
				}
			}

			// 5. 몬스터 복제 완성
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					for (int d = 0; d < 8; d++) {
						monsters[i][j][d] += eggs[i][j][d];
					}
				}
			}
		}
		// 정답 ans 구하기
		int ans = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int d = 0; d < 8; d++) {
					ans += monsters[i][j][d];
				}
			}
		}
		System.out.println(ans);
	}
}