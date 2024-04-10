import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		int n, arr[][], ans = 0;
		int[] dr = new int[] { 0, 1, 0, -1 }; // 좌 하 우 상
		int[] dc = new int[] { -1, 0, 1, 0 };

		n = Integer.parseInt(br.readLine());
		arr = new int[n][n];
		for (int i = 0; i < n; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < n; j++) {
				arr[i][j] = Integer.parseInt(split[j]);
			}
		}

		// 달팽이 순회 방식 지정

		// 달팽이 배열 방향별 이동 횟수 (예): n:5 -> 1, 1, 2, 2, 3, 3, 4, 4, 4
		// 규칙: 좌, 하, 우 상, 좌 .. 방향으로 반복해가며 1, 1, 2, ... 만큼씩 이동
		int snails[] = new int[2*n -1];

		int ccnt = 0;
		for (int i = 0; i < 2*n -1; i++) {
			if (i % 2 == 0 && i != 2*n - 2)
				++ccnt;
			snails[i] = ccnt;
		}

		int snailCount = 0, snailIndex = 0; // 같은 방향으로 몇번 이동했는지 카운트 / snails의 인덱스

		int r = n / 2;
		int c = n / 2;

		for (int i = 0; i < n * n - 1; i++) {
			int d = snailIndex % 4;
			int nr = r + dr[d];
			int nc = c + dc[d];

			r = nr;
			c = nc;

			// 이동 완료한 (r,c) => curr
			// d => 이동한 방향

			// d 방향 기준으로 먼지 이동 규칙 적용(하드코딩)

			ArrayList<Integer[]> changes = new ArrayList<>(); // 업데이트해야 하는 격자와 먼지량 리스트
			int moveSum = 0; // 총 먼지 이동량

			// curR, curC에서 nR, nC 구하기
			int mass = 0;

			// d와 같은 두칸 방향 (5%)
			mass = (int) (arr[r][c] * 0.05);
			nr = r + dr[d] * 2;
			nc = c + dc[d] * 2;
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d와 같은 방향 한칸 + 왼쪽 방향 한칸 (10%)
			mass = (int) (arr[r][c] * 0.1);
			nr = r + dr[d] + dr[(d + 1) % 4];
			nc = c + dc[d] + dc[(d + 1) % 4];
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d와 같은 방향 한칸 + 오른쪽 방향 한칸 (10%)
			mass = (int) (arr[r][c] * 0.1);
			nr = r + dr[d] + dr[(d + 3) % 4];
			nc = c + dc[d] + dc[(d + 3) % 4];
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d오른쪽 방향 한칸 (7%)
			mass = (int) (arr[r][c] * 0.07);
			nr = r + dr[(d + 3) % 4];
			nc = c + dc[(d + 3) % 4];
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d오른쪽 방향 두칸 (2%)
			mass = (int) (arr[r][c] * 0.02);
			nr = r + dr[(d + 3) % 4] * 2;
			nc = c + dc[(d + 3) % 4] * 2;
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d왼쪽 방향 한칸 (7%)
			mass = (int) (arr[r][c] * 0.07);
			nr = r + dr[(d + 1) % 4];
			nc = c + dc[(d + 1) % 4];
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d왼쪽 방향 두칸 (2%)
			mass = (int) (arr[r][c] * 0.02);
			nr = r + dr[(d + 1) % 4] * 2;
			nc = c + dc[(d + 1) % 4] * 2;
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d 방향과 반대 방향 + 왼쪽 방향 한칸 (1%)
			mass = (int) (arr[r][c] * 0.01);
			nr = r + dr[(d + 2) % 4] + dr[(d + 1) % 4];
			nc = c + dc[(d + 2) % 4] + dc[(d + 1) % 4];
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// d 방향과 반대 방향 + 오른쪽 방향 한칸 (1%)
			mass = (int) (arr[r][c] * 0.01);
			nr = r + dr[(d + 2) % 4] + dr[(d + 3) % 4];
			nc = c + dc[(d + 2) % 4] + dc[(d + 3) % 4];
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// 모두 구했으면, d방향으로 한칸에 a%를 구하기
			mass = arr[r][c] - moveSum;
			nr = r + dr[d];
			nc = c + dc[d];
			moveSum += mass;
			if (isInRange(n, nr, nc)) {
				changes.add(new Integer[] { nr, nc, mass });
			} else
				ans += mass;

			// 리스트 꺼내서 먼지 값 업데이트하기
			arr[r][c] = 0;
			for (Integer[] m : changes) {
				int r2 = m[0];
				int c2 = m[1];
				int mass2 = m[2];
				arr[r2][c2] += mass2;
			}

			// 달팽이 이동 규칙에서, 다음 이동 방향을 90도 반시계 회전해야 하는지 조사
			snailCount++;
			if (snailCount == snails[snailIndex]) {
				snailCount = 0;
				snailIndex++;
			}
		}

		// ans 출력
		System.out.println(ans);
	}

	private static boolean isInRange(int n, int nr, int nc) {
		if (nr < 0 || nr >= n || nc < 0 || nc >= n)
			return false;
		return true;
	}
}