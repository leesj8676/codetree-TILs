import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/*
 
3 2 2
0 0 0
0 2 0
1 0 0
4 4 
2 3 1 4
4 1 2 3
3 4 2 1
4 3 1 2
2 4 3 1
2 1 3 4
3 4 1 2
4 1 2 3


 * */

public class Main {
	static class Person implements Comparable<Person> {
		int r, c, index, d;

		public Person(int r, int c, int index, int d) {
			super();
			this.r = r;
			this.c = c;
			this.index = index;
			this.d = d;
		}

		@Override
		public String toString() {
			return "Person [r=" + r + ", c=" + c + ", index=" + index + ", d=" + d + "]";
		}

		@Override
		public int compareTo(Main.Person o) {
			return this.index - o.index;
		}

	}

	public static void main(String[] args) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int m = Integer.parseInt(split[1]);
		int k = Integer.parseInt(split[2]);

		int[] dr = new int[] { -999, -1, 1, 0, 0 };
		int[] dc = new int[] { -999, 0, 0, -1, 1 };
		ArrayList<Person> alivePeople = new ArrayList<>();
		int[][] arr = new int[n][n]; // 위치에 소유권 인정되는 남은 턴 입력
		int[][] arrPerson = new int[n][n]; // 위치에 그 땅 소유한 사람 인덱스 입력

		for (int i = 0; i < n; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < n; j++) {
				int index = Integer.parseInt(split[j]);
				if (index > 0) {
					arr[i][j] = k; // 최초 시작 위치 -> -> k턴까지 index 번째 사람이 소유권
					arrPerson[i][j] = index;
					alivePeople.add(new Person(i, j, index, -1));
				}
			}
		}

		Collections.sort(alivePeople);
		// 사람별 초기 방향
		split = br.readLine().split(" ");
		for (int i = 0; i < m; i++) {
			alivePeople.get(i).d = Integer.parseInt(split[i]);
		}

		// 사람별 각 방향에서 선호하는 다음 방향
		int[][][] directions = new int[m + 1][5][5];
		for (int index = 1; index <= m; index++) {
			int[][] mat = new int[5][5];
			for (int d = 0; d < 4; d++) {
				split = br.readLine().split(" ");
				for (int nd = 0; nd < 4; nd++) {
					mat[d + 1][nd + 1] = Integer.parseInt(split[nd]);
				}
			}
			directions[index] = mat;
		}

		for (int turn = 1;; turn++) {
			if (turn == 1000) {
				System.out.println(-1);
				break;
			}

			int[][] arrThisTurn = new int[n][n]; // 동시 이동시 겹치는지 확인

			// 남아있는 사람 1번부터 순회
			int size = alivePeople.size();
			for (int i = 0; i < size; i++) {
				// 현재위치 r, c 얻기
				// arrThisTurn = false 처리하기
				Person p = alivePeople.get(i);
				int r = p.r;
				int c = p.c;
				int d = p.d;
				int index = p.index;

				// 우선순위에 맞게 다음 갈 곳 탐색
				boolean goEmptyArea = false; // 비어있는 곳 가는지 여부
				int nr = -999, nc = -999, nd = 0; // 다음으로 갈 위치 및 방향
				int[] mat = directions[index][d]; // 현재 사람이 가진 방향에서 우선순위
				for (int dd = 1; dd <= 4; dd++) {
					nr = r + dr[mat[dd]];
					nc = c + dc[mat[dd]];
					if (nr < 0 || nr >= n || nc < 0 || nc >= n)
						continue;
					if (arr[nr][nc] == 0) { // 현재 빈 땅인 경우
						goEmptyArea = true;
						nd = mat[dd];
						break;
					}
				}

				// 빈땅을 못 찾았으면, 우선순위에서 맨 처음 나오는 자기 땅으로 다시 이동
				if (!goEmptyArea) {
					for (int dd = 1; dd <= 4; dd++) {
						nr = r + dr[mat[dd]];
						nc = c + dc[mat[dd]];
						if (nr < 0 || nr >= n || nc < 0 || nc >= n)
							continue;
						if (arrPerson[nr][nc] == index) { // 자기 땅
							nd = mat[dd];
							break;
						}
					}
				}

				// 다음 위치 일단 업데이트
				p.r = nr;
				p.c = nc;
				p.d = nd;

				// 구한 nr, nc가 비어있는 곳이면
				// arr에 업데이트
				if (goEmptyArea) {
					if (arrThisTurn[nr][nc] != 0) { // 더 앞번의 사람이 이미 왔으면, 현재 사람 삭제
						alivePeople.remove(i);
						i--;
						size--;
					} else {
						arrThisTurn[nr][nc] = index;
					}
				}
			}

			// 자리별 남은 소유 인정 턴 감소
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (arr[i][j] > 0) {
						arr[i][j]--;
						if (arr[i][j] == 0)
							arrPerson[i][j] = 0;
					}
				}
			}
			
			// 이번턴에 사람이 차지한 땅에 언제까지 유효한지 기록
			for (int i2 = 0; i2 < n; i2++) {
				for (int j2 = 0; j2 < n; j2++) {
					if (arrThisTurn[i2][j2] > 0) {
						arr[i2][j2] = k;
						arrPerson[i2][j2] = arrThisTurn[i2][j2]; // 인덱스도 기록
					}
				}
			}

			
			

			if (alivePeople.size() == 1) {
				System.out.println(turn);
				break;
			}
		}
	}
}