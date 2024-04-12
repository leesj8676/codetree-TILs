import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
3 1
2 2 2 2 2 2

3
 * */
public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;

		split = br.readLine().split(" ");
		// n, k 입력 받기
		int n = Integer.parseInt(split[0]);
		int k = Integer.parseInt(split[1]);

		int[][] indicesOnConveyer = new int[2][n];// 컨베이어 벨트 칸별 인덱스
		int[] durabilities = new int[2 * n]; // (칸별 내구도)
		boolean[] isPeopleOnConveyor = new boolean[2 * n]; // (칸별 사람 있는지 여부)
		int zeroDurabCount = 0;// 내구도가 0인 칸 카운트

		// 내구도 입력 받기
		split = br.readLine().split(" ");
		for (int i = 0; i < 2 * n; i++) {
			durabilities[i] = Integer.parseInt(split[i]);
		}

		for (int i = 0; i < n; i++) {
			indicesOnConveyer[0][i] = i;
		}
		for (int i = n; i < 2 * n; i++) {
			indicesOnConveyer[1][2 * n - i - 1] = i;
		}

		for (int t = 1;; t++) {// 무한루프
			// 1. 컨베이어 벨트 시계방향 회전
			int t1 = indicesOnConveyer[1][0];
			int t2 = indicesOnConveyer[0][n - 1];

			for (int i = n - 1; i > 0; i--) {
				indicesOnConveyer[0][i] = indicesOnConveyer[0][i - 1];
			}
			indicesOnConveyer[0][0] = t1;

			for (int i = 0; i < n - 1; i++) {
				indicesOnConveyer[1][i] = indicesOnConveyer[1][i + 1];
			}
			indicesOnConveyer[1][n - 1] = t2;

			// 첫번째 칸 인덱스 firstIndex -> indicesOnConveyer[0][0]
			// n 번째 칸 인덱스 nthIndex -> indicesOnConveyer[0][n-1]을 구한다.
			int firstIndex = indicesOnConveyer[0][0];
			int nthIndex = indicesOnConveyer[0][n - 1];

			// n번째 칸에 사람이 있는 경우, 해당 사람은 내린다.
			if (isPeopleOnConveyor[nthIndex])
				isPeopleOnConveyor[nthIndex] = false;

			// 2. 사람별 이동 가능하면 이동
			// n-1번째 칸부터 첫번째 칸까지 순회하며 다음칸 탐색
			for (int i = n - 2; i >= 0; i--) {
				int cur = indicesOnConveyer[0][i];
				if (!isPeopleOnConveyor[cur])
					continue;
				int next = indicesOnConveyer[0][i + 1];

				// 다음 칸은 +1을 하면 됨, (현재 칸이 n-1 번째 칸이면 그다음칸은 0)
				// 다음 칸이 내구도가 0 초과 && 사람이 없으면 다음칸으로 사람 이동
				if (durabilities[next] > 0 && !isPeopleOnConveyor[next]) {
					// isPeopleOnConveyor 업데이트(현재칸 0, 다음칸 1)
					isPeopleOnConveyor[cur] = false;
					isPeopleOnConveyor[next] = true;

					// 다음 칸의 내구도를 1 감소시킴
					// 다음 칸의 내구도가 0으로 바뀌었으면, zeroDurabCount ++
					durabilities[next]--;
					if (durabilities[next] == 0) {
						zeroDurabCount++;
					}
					// 다음 칸이 n번째 칸이었으면, 그 사람은 내린다.
					if (nthIndex == next) {
						isPeopleOnConveyor[next] = false;
					}
				}
			}

			// 3. 사람이 탑승 가능하면 추가 탑승
			// 첫번째 칸이 내구도가 0초과하면 탑승
			if (durabilities[firstIndex] > 0) {
				isPeopleOnConveyor[firstIndex] = true;

				// 해당 칸의 내구도를 1 감소시킴
				// 해당 칸의 내구도가 0으로 바뀌었으면, zeroDurabCount ++
				durabilities[firstIndex]--;
				if (durabilities[firstIndex] == 0) {
					zeroDurabCount++;
				}
			}

			// 4. 종료 조건 조사
			if (zeroDurabCount >= k) {
				System.out.println(t);
				break;
			}
		}
	}
}