package webservice.Model;

public class Game {
	private int id;
	private String name;
	private String range;
	private String dest;
	private int score;
	private String time;
	private int round;
	private int playerNum;
	private String gamePassword;
	public Game(int id, String name, String range, String dest, int score, String time, int round, int playerNum,
			String gamePassword) 
	{
		this.id = id;
		this.name = name;
		this.range = range;
		this.dest = dest;
		this.score = score;
		this.time = time;
		this.round = round;
		this.playerNum = playerNum;
		this.gamePassword = gamePassword;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public int getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	public String getGamePassword() {
		return gamePassword;
	}
	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}
	
	
	

}
