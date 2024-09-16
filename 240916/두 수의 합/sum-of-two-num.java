import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] split;
		
		split = br.readLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int k = Integer.parseInt(split[1]);
		HashMap<Integer, Integer> hashMap = new HashMap<>();
		split = br.readLine().split(" ");
		
		int ans = 0;
		for (int i = 0; i < n; i++) {
			int key1 = Integer.parseInt(split[i]);
			int key2 = k - key1;
			if (hashMap.containsKey(key2)) {
				ans += hashMap.get(key2);
			}

			if (hashMap.containsKey(key1)) {
				int val1 = hashMap.get(key1);
				hashMap.put(key1, val1 + 1);
			} else {
				hashMap.put(key1, 1);
			}
		}
		
		System.out.println(ans);
	}
}