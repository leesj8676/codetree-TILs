import java.io.*;
import java.util.*;

public class Main {
 
	static class Knight {
		int r, c, h, w, k;

		public Knight(int r, int c, int h, int w, int k) {
			super();
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
		}

		@Override
		public String toString() {
			return "Knight [r=" + r + ", c=" + c + ", h=" + h + ", w=" + w + ", k=" + k + "]";
		}
	}

	static int dr[] = new int[] { -1, 0, 1, 0 }, dc[] = new int[] { 0, 1, 0, -1 };
	static int L, N, Q, arr[][], arr2[][];
	static List<Knight> knights;
	static int[] initHealthList;

	static boolean isMoved = false;
	static int currentKnightIndex = -1;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;

		split = br.readLine().split(" ");
		L = Integer.parseInt(split[0]);
		N = Integer.parseInt(split[1]);
		Q = Integer.parseInt(split[2]);

		arr = new int[L][L];
		arr2 = new int[L][L];

		for (int i = 0; i < L; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < L; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}

		knights = new ArrayList<>();
		knights.add(new Knight(0, 0, 0, 0, 0));

		initHealthList = new int[N + 1];
		for (int i = 1; i <= N; i++) {
			split = br.readLine().split(" ");
			int r = Integer.parseInt(split[0]) - 1;
			int c = Integer.parseInt(split[1]) - 1;
			int h = Integer.parseInt(split[2]);
			int w = Integer.parseInt(split[3]);
			int k = Integer.parseInt(split[4]);
			knights.add(new Knight(r, c, h, w, k));

			initHealthList[i] = k;
			// 각 기사 범위 표시
			for (int i2 = r; i2 < r + h; i2++) {
				for (int j2 = c; j2 < c + w; j2++) {
					arr2[i2][j2] = i;
				}
			}

		}

		for (int t = 0; t < Q; t++) {
			split = br.readLine().split(" ");
			int i = Integer.parseInt(split[0]);
			int d = Integer.parseInt(split[1]);

			// i번 기사 d로 이동 시도(이동 성공하면 체력 계산)
			move(i, d);
		}

		int ans = 0;
		for (int i = 1; i <= N; i++) {
			int health = knights.get(i).k;
			if (health > 0) {
				ans += (initHealthList[i] - health);
			}
		}
		System.out.println(ans);
	}

	private static void updateArr2(int idx, int d) {
		// idx번째 기사 한 줄 이동

		int[] m = getRange(idx, d); // d 에 고려해서 실제 이동이 일어나는 선분
		int r1 = m[0];
		int r2 = m[1];
		int c1 = m[2];
		int c2 = m[3];

		// 이동 하는 방향 한줄 먼저 칠하기
		for (int i = r1; i <= r2; i++) {
			for (int j = c1; j <= c2; j++) {
				arr2[i][j] = idx;
			}
		}

		m = getRange(idx, (d + 2) % 4); // d 에 고려해서 실제 이동이 일어나는 선분
		r1 = m[0] + dr[d];
		r2 = m[1] + dr[d];
		c1 = m[2] + dc[d];
		c2 = m[3] + dc[d];

		// 반대 방향 한줄 지우기
		for (int i = r1; i <= r2; i++) {
			for (int j = c1; j <= c2; j++) {
				arr2[i][j] = 0;
			}
		}
	}

	private static void move(int idx, int d) {
		Knight knight = knights.get(idx);

		if (knight.k == 0)
			return;

		// BFS 수행
		LinkedList<Integer> q = new LinkedList<>();
		q.add(idx);
		
		List<Integer> list = new ArrayList<>();
		list.add(idx);
		while (!q.isEmpty()) {
			int index = q.poll();

			int[] m = getRange(index, d); // d 에 고려해서 실제 이동이 일어나는 선분
			int r1 = m[0];
			int r2 = m[1];
			int c1 = m[2];
			int c2 = m[3];

			// 현재 기사가 갈 수 없으면 탐색 종료
			for (int i = r1; i <= r2; i++) {
				for (int j = c1; j <= c2; j++) {
					if (i < 0 || i >= L || j < 0 || j >= L || arr[i][j] == 2) {
						return;
					}
				}
			}

			// 충돌하면 리스트에 담고, 모두 이동 가능하면 나중에 이동
			for (int i = r1; i <= r2; i++) {
				for (int j = c1; j <= c2; j++) {
					int val = arr2[i][j];
					if (val != 0 && !list.contains(val)) {
						q.add(val);
						list.add(val);
					}
				}
			}

		}

		// 큐가 정상적으로 비워졌으면, 리스트에 있는 기사들 이동
		for (int i = list.size() - 1; i >= 0; i--) {
			int cur = list.get(i);
			updateArr2(cur, d);

			Knight k = knights.get(cur);
			int r = k.r;
			int c = k.c;
			int h = k.h;
			int w = k.w;

			k.r = r + dr[d];
			k.c = c + dc[d];

			// 체력 계산
			if (cur == idx) // 명령을 받은 기사는 제외
				continue;

			r = k.r;
			c = k.c;
			h = k.h;
			w = k.w;
			int health = k.k;
			for (int i2 = r; i2 < r + h; i2++) {
				for (int j2 = c; j2 < c + w; j2++) {
					health -= arr[i2][j2];
				}
			}

			if (health <= 0) {
				health = 0;
				for (int i2 = r; i2 < r + h; i2++) {
					for (int j2 = c; j2 < c + w; j2++) {
						arr2[i2][j2] = 0; // 기사 탈락
					}
				}
			}
			k.k = health;
		}
		return;
	}

	private static int[] getRange(int idx, int d) {
		Knight k = knights.get(idx);
		int r = k.r;
		int c = k.c;
		int h = k.h;
		int w = k.w;

		switch (d) {
		case 0:
			return new int[] { r - 1, r - 1, c, c + w - 1 };
		case 1:
			return new int[] { r, r + h - 1, c + w, c + w };
		case 2:
			return new int[] { r + h, r + h, c, c + w - 1 };
		default:
			return new int[] { r, r + h - 1, c - 1, c - 1 };
		}
	}
}