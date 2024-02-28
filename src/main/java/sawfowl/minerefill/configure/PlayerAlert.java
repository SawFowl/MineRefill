package sawfowl.minerefill.configure;

public class PlayerAlert {

	private final int time;
	private final String[] path;
	public PlayerAlert(int time, String[] path) {
		this.time = time;
		this.path = path;
	}

	public int getTime() {
		return time;
	}

	public String[] getPath() {
		return path;
	}

}
