package edu.columbia.rdf.edb;

public enum SampleState {
	UNLOCKED,
	LOCKED;

	public static SampleState parse(boolean locked) {
		return locked ? LOCKED : UNLOCKED;
	}
	
	public static SampleState parse(String name) {
		if (name.equals("l")) {
			return LOCKED;
		} else {
			return UNLOCKED;
		}
	}

	/**
	 * Returns a one letter abbreviation of the state.
	 * 
	 * @param state
	 * @return
	 */
	public static String shortCode(SampleState state) {
		return state == LOCKED ? "l" : "u";
	}
}
