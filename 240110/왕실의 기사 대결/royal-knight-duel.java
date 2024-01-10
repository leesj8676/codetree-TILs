import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	// Knight class
	static class Knight {
		int r0, c0; // (r0, c0) : 방패 왼쪽 위 좌표
		int r1, c1; // (r0, c0) : 오른쪽 아래 좌표
		int h0, h1; // h0 : 최초 체력, h1 : 기존 체력
//		int w,h;

		public String toString() {
			return "방패 (" + r0 + ", " + c0 + ") ~ (" + r1 + ", " + c1 + "), 체력 : " + h1;
		}

		public Knight(int r0, int c0, int r1, int c1, int h0, int h1) {
			this.r0 = r0;
			this.c0 = c0;
			this.r1 = r1;
			this.c1 = c1;
			this.h0 = h0;
			this.h1 = h1;
		}
	}

	// dr, dc
	static int dr[] = new int[] { -1, 0, 1, 0 };
	static int dc[] = new int[] { 0, 1, 0, -1 };
	static int L, N, Q;
	static int arr[][];
	static Knight knights[];
	static boolean canGo;
	static boolean[] moveKnights;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;

		split = br.readLine().split(" ");
		L = Integer.parseInt(split[0]);
		N = Integer.parseInt(split[1]);
		Q = Integer.parseInt(split[2]);
		arr = new int[L][L];
		knights = new Knight[N];

		// 격자 그대로 입력 받기
		for (int i = 0; i < L; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < L; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}

		for (int i = 0; i < N; i++) {
			split = br.readLine().split(" ");
			int r0 = Integer.parseInt(split[0]) - 1;
			int c0 = Integer.parseInt(split[1]) - 1;
			int h = Integer.parseInt(split[2]) - 1;
			int w = Integer.parseInt(split[3]) - 1;
			int h0 = Integer.parseInt(split[4]);
			knights[i] = new Knight(r0, c0, r0 + h, c0 + w, h0, h0);
		}

		// q 개수만큼 명령 받기
		for (int i = 0; i < Q; i++) {
			split = br.readLine().split(" ");
			int idx = Integer.parseInt(split[0]) - 1;
			int d = Integer.parseInt(split[1]);
			// 시뮬레이션(i 번째 기사, d 방향)
			simulate(idx, d);
		}

		getAns();
	}

	private static void getAns() {
		int ans = 0;
		for (int i = 0; i < N; i++) {
			Knight k = knights[i];
			if (k.h1 == 0)
				continue;
			ans += (k.h0 - k.h1);
		}
		System.out.println(ans);
	}

	private static void simulate(int idx, int d) {
		// 명령 한번마다 체크하는 변수 초기화
		canGo = true;
		moveKnights = new boolean[N];

		Knight k = knights[idx];
		if (k.h1 == 0)
			return;

		// 재귀를 사용해서 idx번째와 부딪히는 모든 기사 이동 가능한지 체크
		canGo(idx, d);

		// 모든 기사가 이동 가능하면, 체크한 배열을 통해 이동
		if (canGo) {
			for (int i = 0; i < N; i++) {
				if (moveKnights[i]) {
					move(i, d);
				}
			}
		}

		moveKnights[idx] = false;
		// 대결 이후 데미지 값 업데이트
		damageUpdate();
	}

	private static void damageUpdate() {
		for (int i = 0; i < N; i++) {
			if (!moveKnights[i])
				continue;
			Knight k = knights[i];
			int cnt = 0;
			for (int r = k.r0; r <= k.r1; r++) {
				for (int c = k.c0; c <= k.c1; c++) {
					if (arr[r][c] == 1)
						cnt++;
				}
			}
			// 현재 기사 체력 업데이트 (최소 0)
			k.h1 = k.h1 - cnt > 0 ? k.h1 - cnt : 0;
		}
	}

	private static boolean canGo(int idx, int d) {
		// 충돌하는 기사 찾기
		for (int i = 0; i < N; i++) {
			if (idx == i)
				continue;
			if (knights[i].h1 == 0)
				continue;
			if (!isColide(idx, d, i))
				continue;

			// 충돌하는 기사 중, 벽에 닿는 경우
			if (meetWall(i, d)) {
				canGo = false;
				return false;
			}

			// 재귀적으로 해당 기사가 이동 가능한지 확인
			if (!canGo(i, d)) {
				canGo = false;
				return false;
			}
		}

		// 현재 기사가 이동 가능하면 이동
		moveKnights[idx] = true;
		return true;
	}

	private static void move(int idx, int d) {
		Knight k = knights[idx];
		switch (d) {
		case 0: // 위쪽
			k.r0--;
			k.r1--;
			break;
		case 1: // 오른쪽
			k.c0++;
			k.c1++;
			break;
		case 2: // 아래쪽
			k.r0++;
			k.r1++;
			break;
		case 3: // 왼쪽
			k.c0--;
			k.c1--;
			break;
		}
	}

	private static boolean meetWall(int i, int d) {
		Knight k = knights[i];
		int r0 = k.r0;
		int r1 = k.r1;
		int c0 = k.c0;
		int c1 = k.c1;

		switch (d) {
		case 0: // 위쪽
			r0--;
			// 변경된 (r0, c0~c1)에서 벽이 있는지 탐색
			for (int c = c0; c <= c1; c++) {
				if (arr[r0][c] == 2) {
					return true;
				}
			}
			break;
		case 1: // 오른쪽
			c1++;
			// 변경된 (r0~r1, c1)에서 벽이 있는지 탐색
			for (int r = r0; r <= r1; r++) {
				if (arr[r][c1] == 2) {
					return true;
				}
			}
			break;
		case 2: // 아래쪽
			r1++;
			for (int c = c0; c <= c1; c++) {
				if (arr[r1][c] == 2) {
					return true;
				}
			}
			break;
		case 3: // 왼쪽
			c0--;
			// 변경된 (r0~r1, c1)에서 벽이 있는지 탐색
			for (int r = r0; r <= r1; r++) {
				if (arr[r][c0] == 2) {
					return true;
				}
			}
			break;
		}
		return false;
	}

	// idx기사가 d로 이동할 때, i기사가 밀려나는지 확인
	private static boolean isColide(int idx, int d, int i) {
		Knight k = knights[idx];
		int r0 = k.r0;
		int c0 = k.c0;
		int r1 = k.r1;
		int c1 = k.c1;

		Knight o = knights[i];
		// d에 맞게 idx 기사의 방패 이동
		// i기사의 방패와 겹치는지 확인
		switch (d) {
		case 0: // 위쪽
			r0--;
			// r0이 o.r1이랑 같으면서
			// o.c0 ~ o.c1이 c0또는 c1이 있어야 함
			if (r0 == o.r1) {
				for (int c = o.c0; c <= o.c1; c++) {
					if (c == c0 || c == c1)
						return true;
				}
			}
			break;
		case 1: // 오른쪽
			c1++;
			// c1이 o.c0이랑 같으면서
			// o.r0 ~ o.r1에서 r0또는 r1이 있어야 함
			if (c1 == o.c0) {
				for (int r = o.r0; r <= o.r1; r++) {
					if (r == r0 || r == r1)
						return true;
				}
			}
			break;
		case 2: // 아래쪽
			r1++;
			if (r1 == o.r0) {
				for (int c = o.c0; c <= o.c1; c++) {
					if (c == c0 || c == c1)
						return true;
				}
			}
			break;
		case 3: // 왼쪽
			c0--;
			if (c0 == o.c1) {
				for (int r = o.r0; r <= o.r1; r++) {
					if (r == r0 || r == r1)
						return true;
				}
			}
			break;
		}
		return false;
	}
}