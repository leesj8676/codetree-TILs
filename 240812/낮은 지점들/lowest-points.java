import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int n = Integer.parseInt(br.readLine());
		String[] split;

		HashMap<Integer, Integer> hashMap = new HashMap<>();
		for (int i = 0; i < n; i++) {
			split = br.readLine().split(" ");
			int x = Integer.parseInt(split[0]);
			int y = Integer.parseInt(split[1]);

			if (hashMap.get(x) == null) {
				hashMap.put(x, y);
			} else {
				int val = hashMap.get(x);
				if (y < val) {
					hashMap.put(x, y);
				}
			}
		}

		int ans = 0;
		for (int val : hashMap.values()) {
			ans += val;
		}
		System.out.println(ans);
	}
}