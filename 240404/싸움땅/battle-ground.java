import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Main {
	static class Player {
		int x, y, d, s, gun;

		public Player(int x, int y, int d, int s, int gun) {
			super();
			this.x = x;
			this.y = y;
			this.d = d;
			this.s = s;
			this.gun = gun;
		}

		@Override
		public String toString() {
			return "Player [x=" + x + ", y=" + y + ", d=" + d + ", s=" + s + ", gun=" + gun + "]";
		}

	}

	static ArrayList<Player> players;

	static int n, m, k, dx[] = { -1, 0, 1, 0 }, dy[] = { 0, 1, 0, -1 };
	static int arr[][];

	// NxN 사이즈의 트리 맵 선언 (총들 관리)
	static ArrayList<PriorityQueue<Integer>> gunsInArr = new ArrayList<PriorityQueue<Integer>>();

	public static void main(String[] args) throws Exception {
		// 입력받기
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String split[];
		split = br.readLine().split(" ");
		n = Integer.parseInt(split[0]);
		m = Integer.parseInt(split[1]);
		k = Integer.parseInt(split[2]);

		arr = new int[n][n];
		for (int i = 0; i < n; i++) {
			split = br.readLine().split(" ");
			for (int j = 0; j < n; j++) {
				PriorityQueue<Integer> guns = new PriorityQueue<Integer>();
				gunsInArr.add(guns);
				int val = Integer.parseInt(split[j]);
				if (val != 0)
					guns.add(-val);
			}
		}

		players = new ArrayList<>();
		players.add(null);
		for (int index = 1; index <= m; index++) {
			split = br.readLine().split(" ");
			int x = Integer.parseInt(split[0]) - 1;
			int y = Integer.parseInt(split[1]) - 1;
			int d = Integer.parseInt(split[2]);
			int s = Integer.parseInt(split[3]);
			Player p = new Player(x, y, d, s, 0);
			players.add(p);
			arr[x][y] = index;
		}

		int scores[] = new int[m + 1];
		while (k-- > 0) {

			// m번만큼 플레이어 이동시키기
			for (int index = 1; index <= m; index++) {
				Player p = players.get(index);

				int x = p.x;
				int y = p.y;
				int d = p.d;

				// dx, dy로 구한 다음 위치가 격자 바깥이면, 방향 변경 후 새로 구하기 (클래스의 플레이어 방향 값 변경)
				int nx = x + dx[d];
				int ny = y + dy[d];
				if (nx < 0 || ny < 0 || nx >= n || ny >= n) {
					d = (d + 2) % 4;
					p.d = d;
					nx = x + dx[d];
					ny = y + dy[d];
				}

				// 현재 플레이어가 안싸우고 바로 이동가능 하다면,
				if (arr[nx][ny] == 0) {
					// 이동 (클래스의 플레이어 값 변경, arr도 변경)
					p.x = nx;
					p.y = ny;
					arr[x][y] = 0;
					arr[nx][ny] = index;

					// 총이 있다면, 총 공격력 비교 후 바꾸기 (클래스의 플레이어 위치 값 변경, guns 도 변경)
					PriorityQueue<Integer> guns = gunsInArr.get(nx * n + ny);
					if (!guns.isEmpty()) {
						int gun = guns.peek() * -1;
						if (p.gun < gun) {
							guns.poll();
							int current = p.gun;
							if (current != 0) { // guns에 플레이어가 떨어트린 총의 공격력이 0이면 더하지 않음
								guns.add(-current);
							}
							p.gun = gun;
						}
					}
				} else { // 싸움땅을 한다면,
					// 플레이어 최초 능력치 + 현재 총 공격력 비교해서 판정
					// 진 사람과 이긴 사람 인덱스 각각 가져오기
					int loserIndex;
					int winnerIndex;
					int otherIndex = arr[nx][ny];
					Player o = players.get(otherIndex);
					int diff = Math.abs((p.s + p.gun) - (o.s + o.gun));
					if (p.s + p.gun < o.s + o.gun) {
						loserIndex = index;
						winnerIndex = otherIndex;
					} else if (p.s + p.gun > o.s + o.gun) {
						loserIndex = otherIndex;
						winnerIndex = index;
					} else {
						diff = 0;
						if (p.s < o.s) {
							loserIndex = index;
							winnerIndex = otherIndex;
						} else {
							loserIndex = otherIndex;
							winnerIndex = index;
						}
					}

					// 진 플레이어는 총 떨어트리기
					PriorityQueue<Integer> guns = gunsInArr.get(nx * n + ny);
					int current = players.get(loserIndex).gun;
					players.get(loserIndex).gun = 0;
					if (current != 0) {
						guns.add(current * -1);
					}

					// 이긴 플레이어는 모든 총 을 비교해서 바꾸기 (클래스의 플레이어 총 값 변경, guns도 변경)
					int first = !guns.isEmpty() ? guns.peek() * -1 : -999999;
					current = players.get(winnerIndex).gun;
					if (first > current) {
						players.get(winnerIndex).gun = first;
						guns.poll();
						if (current != 0) {
							guns.add(current * -1);
						}
					}

					// 현재 클래스 플레이어 위치 값 우선 변경
					arr[x][y] = 0;
					p.x = nx;
					p.y = ny;

					// 현재 플레이어가 이긴 경우라면, arr 값도 변경
					if (index == winnerIndex) {
						arr[nx][ny] = index;
					}

					scores[winnerIndex] += diff; // 점수 업데이트

					// 진 플레이어는 현재 위치에서, 자신의 방향에서 오른쪽(+1 % 4) 해가면서 가능한 곳으로 이동 시도
					Player loser = players.get(loserIndex);
					x = nx;
					y = ny;
					d = loser.d;

					for (int k2 = 0; k2 < 4; k2++) {
						int dd = (d + k2) % 4;
						nx = x + dx[dd];
						ny = y + dy[dd];

						if (nx < 0 || ny < 0 || nx >= n || ny >= n || arr[nx][ny] != 0)
							continue;

						// 이동 가능한곳을 찾았으면, 이동 (클래스의 플레이어 값 변경, arr도 변경)
						loser.x = nx;
						loser.y = ny;
						loser.d = dd;
						arr[nx][ny] = loserIndex;

						// 총 공격력도 비교 후 바꾸기 (클래스의 플레이어 값 변경, guns도 변경)
						guns = gunsInArr.get(nx * n + ny);
						if (!guns.isEmpty()) {
							int gun = guns.poll() * -1;
							loser.gun = gun;
						}
						break;
					}
				}
			}

		}
		for (int i = 1; i <= m; i++) {
			System.out.print(scores[i] + " ");
		}
		System.out.println("\n");
	}
}