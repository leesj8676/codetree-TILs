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
		for (int i = 0; i < n; i++) {
			int key = Integer.parseInt(split[i]);
			if (hashMap.containsKey(key)) {
				int val = hashMap.get(key);
				hashMap.put(key, val + 1);
			} else {
				hashMap.put(key, 1);
			}
		}
		
		int ans = 0;
		for (Integer key1 : hashMap.keySet()) {
			int key2 = k - key1;
			if(!hashMap.containsKey(key2)) continue;
			
			int val1 = hashMap.get(key1);
			int val2 = hashMap.get(key2);
			ans += val1 * val2;
			hashMap.put(key1, 0);
			hashMap.put(key2, 0);
		}
		System.out.println(ans);
	}
}