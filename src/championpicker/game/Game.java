package championpicker.game;

import championpicker.champ.ChampList;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Date;

public class Game {

	private long id;
	private long date;
	// Team0 is the blue team and always has first ban and first pick
	private Team team0, team1;
	// true if team0 won
	private boolean winner;

	// public Game(int id, double weight) {
	// 	this.id = id;
	// }

	public Game(JSONObject json) throws Exception {
		id = json.getLong("matchId");
		date = json.getLong("matchCreation");
		JSONArray participants = json.getJSONArray("participants");
		int teamSize = participants.length() / 2;
		team0 = new Team();
		team1 = new Team();
		for(int i = 0; i < teamSize; i++) {
			JSONObject player = participants.getJSONObject(i);
			team0.addPick(ChampList.master.byId(player.getInt("championId")));

			player = participants.getJSONObject(i + teamSize);
			team1.addPick(ChampList.master.byId(player.getInt("championId")));
		}
		JSONArray teams = json.getJSONArray("teams");
		JSONObject team = teams.getJSONObject(0);
		winner = team.getBoolean("winner");
		JSONArray bans = team.getJSONArray("bans");
		team0.addBan(ChampList.master.byId(bans.getJSONObject(0).getInt("championId")));
		team0.addBan(ChampList.master.byId(bans.getJSONObject(1).getInt("championId")));
		team0.addBan(ChampList.master.byId(bans.getJSONObject(2).getInt("championId")));
		team = teams.getJSONObject(1);
		bans = team.getJSONArray("bans");
		team1.addBan(ChampList.master.byId(bans.getJSONObject(0).getInt("championId")));
		team1.addBan(ChampList.master.byId(bans.getJSONObject(1).getInt("championId")));
		team1.addBan(ChampList.master.byId(bans.getJSONObject(2).getInt("championId")));
	}

	public long getId() {
		return id;
	}

	public String toString() {
		return team0 + " vs " + team1;
		//return String.valueOf(ge)
	}

	public long getDateDiff(long date) {
		return date - this.date;
	}
}
