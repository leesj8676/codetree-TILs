import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*

5 3 1 1
2 4 1
1 4 2
4 2 1
2 4

output: 1

*/
// ※ 필요시 이동한 사람들의 위치 정보 arr에 저장 (술래 이동시 확인용) 사용하기
public class Main {
	public static void main(String[] args) throws Exception {
		// trees[n][n] : boolean
		// rs[m+1], rc[m+1] // 도망자 위치
		// arr[][]arrayList // 이번 턴에 이동한 도망자들 위치 배열
		// directions[m+1] // 도망자 방향 (0~3)
		// alives[m+1] : boolean (도망자 생존여부)
		// sr, sc, sd // 술래 위치 및 방향
		// dr, dc
		// snailCounts[2*n-1] (달팽이 이동 크기)
		// snailDirections[2*n-1] (달팽이 이동 방향)
		// isSnailClockwise = true (현재 달팽이 이동 방향 시계)
		// sdc, sdi (술래 현재 방향 달팽이 이동 카운트 및 인덱스)

		// 입력 받기
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		split = br.readLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int m = Integer.parseInt(split[1]);
		int h = Integer.parseInt(split[2]);
		int k = Integer.parseInt(split[3]);
		int ans = 0;

		boolean[][] trees = new boolean[n][n]; // 나무 존재 여부
		int[] rs = new int[m + 1], cs = new int[m + 1]; // 도망자 위치
		int[] directions = new int[m + 1]; // 도망자 방향
		boolean[] alives = new boolean[m + 1]; // 도망자 생존 여부

		int sr = n / 2, sc = n / 2, sd = 0; // 술래 위치 및 방향

		int[] dr = new int[] { -1, 0, 1, 0 }, dc = new int[] { 0, 1, 0, -1 };

		int snailCounts[] = new int[2 * n - 1]; // 달팽이 이동 카운트 (예: n =5 -> 1, 1, 2, 2, 3, 3, 4, 4, 4)
		boolean isSnailClockwise = true; // 달팽이 이동 방향 시계 or 반시계
		int sdi = 0, sdc = 0; // 현재 술래 달팽이 이동 방향 인덱스, 해당 인덱스로 몇 번 이동했는지 카운트

		/* 디버깅용: 술래, 도망자 위치 저장 */
		ArrayList<ArrayList<Integer>> peopleInArr = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				ArrayList<Integer> people = new ArrayList<>();
				peopleInArr.add(people);
			}
		}

		peopleInArr.get(n / 2 * n + n / 2).add(0);

		for (int i = 1; i <= m; i++) {
			split = br.readLine().split(" ");
			int x = Integer.parseInt(split[0]) - 1;
			int y = Integer.parseInt(split[1]) - 1;
			int d = Integer.parseInt(split[2]);
			rs[i] = x;
			cs[i] = y;
			directions[i] = d;

			ArrayList<Integer> people = peopleInArr.get(x * n + y);
			people.add(i);

			alives[i] = true;
		}

		for (int i = 0; i < h; i++) {
			split = br.readLine().split(" ");
			int x = Integer.parseInt(split[0]) - 1;
			int y = Integer.parseInt(split[1]) - 1;
			trees[x][y] = true;

			/* 디버깅용: 나무 위치 저장 */
			ArrayList<Integer> people = peopleInArr.get(x * n + y);
			people.add(-1);
		}

		// n 값에 맞게 달팽이 이동 규칙 설정
		int cnt = 1;
		for (int i = 0; i < 2 * n - 1; i++) {
			snailCounts[i] = cnt;
			if (i % 2 == 1 && i != 2 * n - 3)
				cnt++;
		}

		// k만큼 라운드 진행
		round: for (int t = 1; t <= k; t++) {

			// 도망자 이동
			for (int idx = 1; idx <= m; idx++) {
				// 현재 도망자가 생존해 있는 경우, 맨해튼 거리가 3이하인지 확인
				if (!alives[idx])
					continue;
				int r = rs[idx];
				int c = cs[idx];
				int d = directions[idx];
				int dist = Math.abs(sr - r) + Math.abs(sc - c);
				if (dist <= 3) {
					int nr = r + dr[d];
					int nc = c + dc[d];
					// 현재 방향으로 이동해도 격자를 벗어나지 않으면
					if (0 <= nr && nr < n && 0 <= nc && nc < n) {
						// 움직이는 칸에 술래가 있는 경우 (암것도 안함)
						if (nr == sr && nc == sc)
							;
						else {
							// 아니라면, 해당 칸으로 이동
							rs[idx] = nr;
							cs[idx] = nc;

							/* 디버깅용: 도망자 위치 확인 */
							ArrayList<Integer> people = peopleInArr.get(r * n + c);
							for (int i = 0; i < people.size(); i++) {
								if (people.get(i) == idx) {
									people.remove(i);
									break;
								}
							}
							ArrayList<Integer> nPeople = peopleInArr.get(nr * n + nc);
							nPeople.add(idx);
						}
					} else {
						// 격자를 벗어나면, 방향 전환
						// 술래가 없다면, 1칸 이동
						d = (d + 2) % 4;
						directions[idx] = d;
						nr = r + dr[d];
						nc = c + dc[d];

						if (nr != sr || nc != sc) {
							rs[idx] = nr;
							cs[idx] = nc;
						}

						/* 디버깅용: 도망자 위치 확인 */
						ArrayList<Integer> people = peopleInArr.get(r * n + c);
						for (int i = 0; i < people.size(); i++) {
							if (people.get(i) == idx) {
								people.remove(i);
								break;
							}
						}
						ArrayList<Integer> nPeople = peopleInArr.get(nr * n + nc);
						nPeople.add(idx);
					}

				}
			}

			// i 번째 술래 / (r, c) / 방향 : d

			// 술래 이동

			/* 디버깅용: 술래 위치 확인 */
			ArrayList<Integer> people = peopleInArr.get(sr * n + sc);
			for (int i = 0; i < people.size(); i++) {
				if (people.get(i) == 0) {
					people.remove(i);
					break;
				}
			}

			sr += dr[sd]; // 술래 다음 위치
			sc += dc[sd];

			// 현재 달팽이 인덱스(sdi)에 해당하는 방향으로 1칸 이동
			// 현재 방향 달팽이 카운트(sdc) 1 증가
			sdc++;

			// 현재 방향으로 더이상 이동하지 못한다면
			if (snailCounts[sdi] == sdc) {
				sdc = 0;
				if (isSnailClockwise) { // 시계 방향이라면, 인덱스(sdi) 한칸 증가, 방향(sd) 오른쪽으로 한칸 이동
					// 예외처리: (0,0) 도달시 방향 바꾸기
					if (sr == 0 && sc == 0) {
						sd = 2;
						isSnailClockwise = !isSnailClockwise;
					} else {
						sdi++;
						sd = (sd + 1) % 4;
					}
				} else { // 반시계 방향이라면, 인덱스 한칸 감소, 방향 왼쪽으로 한칸 이동
					// 예외처리: 정중앙 도달 시 방향 바꾸기
					if (sr == n / 2 && sc == n / 2) {
						sd = 0;
						isSnailClockwise = !isSnailClockwise;
					} else {
						sdi--;
						sd = (sd + 3) % 4;
					}
				}
			}

			ArrayList<Integer> nPeople = peopleInArr.get(sr * n + sc);
			nPeople.add(0);

			// 현재 방향에서 3칸 순회 하며 (0~2) arr 조사
			for (int i = 0; i < 3; i++) {
				int nr = sr + dr[sd] * i;
				int nc = sc + dc[sd] * i;
				// 격자를 벗어나거나 나무가 있으면, continue
				if (nr < 0 || nr >= n || nc < 0 || nc >= n || trees[nr][nc])
					continue;

				for (int j = 1; j <= m; j++) {
					if (!alives[j])
						continue;
					int r = rs[j];
					int c = cs[j];
					// 도망자들이 있으면, 해당 도망자들 제거, 점수 추가
					if (nr == r && nc == c) {
						alives[j] = false;
						ans += t;

						/* 디버깅용: 도망자 위치 확인 */
						people = peopleInArr.get(r * n + c);
						for (int i2 = 0; i2 < people.size(); i2++) {
							if (people.get(i2) == j) {
								people.remove(i2);
								break;
							}
						}
					}
				}
			}

			// 한 술래라도 살아있다면, continue (게임 계속)
			for (int j = 1; j <= m; j++) {
				if (alives[j])
					continue round;
			}

			break; // 다 탈락한 경우
		}
		// 점수 출력
		System.out.println(ans);
	}
}