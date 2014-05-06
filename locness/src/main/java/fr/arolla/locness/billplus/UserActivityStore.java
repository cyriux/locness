package fr.arolla.locness.billplus;

import java.util.HashMap;
import java.util.Map;

//anemic
public class UserActivityStore {

	private final Map<Long, Integer> calls = new HashMap<Long, Integer>();
	private final Map<Long, Integer> texts = new HashMap<Long, Integer>();

	public void addCallTime(Long userId, int callTime) {
		calls.put(userId, callTime);
	}

	public void addTextCount(Long userId, int textCount) {
		texts.put(userId, textCount);
	}

}
