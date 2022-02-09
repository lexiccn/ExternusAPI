package me.deltaorion.extapi.display.scoreboard;

import com.google.common.base.Preconditions;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.locale.message.Message;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author RienBijl, DeltaOrion
 * @website https://github.com/RienBijl
 *
 * Some work on this class was taken from
 * https://github.com/RienBijl/Scoreboard-revision/blob/53c30cfd740eda28a80710dc9fd69f2d0955337f/src/main/java/rien/bijl/Scoreboard/r/Board/Implementations/Drivers/V1/ScoreboardDriverV1.java#L124
 *
 * This scoreboard takes a {@link Scoreboard} and wraps around it.
 */

public class WrapperScoreboard implements EScoreboard {

    @NotNull private Message title;
    @NotNull private final Map<Integer,ScoreboardLine> scoreboardLines;
    @NotNull private final String name;
    private final int lines;

    private final BukkitPlugin plugin;

    @NotNull private final BukkitApiPlayer player;
    @NotNull private final Objective objective;
    @NotNull private final Scoreboard scoreboard;

    private boolean visible = true;


    private final int CHARACTER_LIMIT; //the amount of characters per line is double this

    /**
     * Creates a new scoreboard with the maximum amount of permitted lines.
     *
     * @param name The name of the scoreboard
     * @param plugin The plugin which the scoreboard is hosted on.
     */
    public WrapperScoreboard(@NotNull Player player, @NotNull String name, @NotNull BukkitPlugin plugin) {
        this(player,name,plugin,LINE_LIMIT);
    }

    /**
     * Creates a new scoreboard. The scoreboard will be tied to the player. Doing this will replace whatever scoreboard the player has
     * with this new scoreboard. You can retrieve the scoreboard using {@link BukkitApiPlayer#getScoreboard()}
     *
     * @param name The name of the scoreboard
     * @param plugin The plugin which this scoreboard is hosted on
     * @param lines The amount of lines for the scoreboard
     * @throws IllegalStateException If the amount of lines is less than 0 or greater than {@link #LINE_LIMIT}
     */
    public WrapperScoreboard(@NotNull Player player, @NotNull String name, @NotNull BukkitPlugin plugin, int lines) {
        Preconditions.checkState(lines >=0 && lines<=LINE_LIMIT,"A scoreboard can only have '"+LINE_LIMIT+"' lines");
        Objects.requireNonNull(player);
        Objects.requireNonNull(name);
        Objects.requireNonNull(plugin);

        this.lines = lines;
        this.scoreboardLines = new HashMap<>();
        this.title = Message.valueOf("");
        CHARACTER_LIMIT = getCharacterLimit(plugin);
        this.name = name;

        this.plugin = Objects.requireNonNull(plugin);

        this.player = plugin.getBukkitPlayerManager().getPlayer(player);
        this.scoreboard = Objects.requireNonNull(plugin.getServer().getScoreboardManager().getNewScoreboard());
        this.objective = Objects.requireNonNull(scoreboard.registerNewObjective(name,"dummy"));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(title.toString(this.player.getLocale()));

        this.createTeams();
        this.setBoard();
    }

    //returns the maximum amount of characters for this server version.
    private int getCharacterLimit(BukkitPlugin plugin) {
        if(plugin.getEServer().getServerVersion().getMajor()>=13) {
            return 64;
        } else {
            return 16;
        }
    }

    public void setLine(@NotNull Message content, int line, Object... args) {
        setLine(content,line,null,args);
    }
    
    public void setLine(@NotNull String content, int line) {
        setLine(Message.valueOf(content),line);
    }

    public void setLine(@NotNull String content, int line, @Nullable String lineName) {
        setLine(Message.valueOf(content),line,lineName);
    }

    public void setLineByName(@NotNull String content, @NotNull String lineName) {
        setLineByName(Message.valueOf(content),lineName);
    }

    public void setLineByName(@NotNull Message content, @NotNull String lineName, Object... args) {
        ScoreboardLine line = getLineByName(lineName);
        if(line!=null)
            setLine(content,line.getLine(),lineName,args);
    }

    public void setLine(@NotNull Message content, int line, @Nullable String lineName, Object... args) {
        lineValid(line);
        if(lineName!=null) {
            ScoreboardLine l = getLineByName(lineName);
            if(l!=null) {
                if(l.getLine()!=line)
                    throw new IllegalArgumentException("Two lines cannot have the same line name. The line name '"+lineName+"' already exists!");
            }
        }
        ScoreboardLine l = new ScoreboardLine(Objects.requireNonNull(content),lineName,line);
        scoreboardLines.put(line,l);
        //if should update
        updateLine(l,args);
    }

    public void setLineArgs(int line, Object... args) {
        lineValid(line);
        if(this.scoreboardLines.containsKey(line)) {
            ScoreboardLine sLine = this.scoreboardLines.get(line);
            if(this.player==null) {
                sLine.getMessage().setDefaults(line);
            } else {
                updateLine(sLine,args);
            }
        }
    }

    @Override
    public void setLineArgs(@NotNull String lineName, Object... args) {
        ScoreboardLine sLine = getLineByName(lineName);
        if(sLine==null)
            return;

        updateLine(sLine, args);
    }

    @Override
    public void setTitle(@NotNull Message title, Object... args) {
        this.title = title;
        this.objective.setDisplayName(title.toString(player.getLocale(),args));
    }

    @Override
    public void setTitle(@NotNull String title) {
        this.setTitle(Message.valueOf(title));
    }

    private void updateLine(ScoreboardLine line, Object... args) {
        Objects.requireNonNull(player);
        updateLine(line,line.getMessage().toString(player.getLocale(),args));
    }

    private void updateLine(ScoreboardLine line, String content) {
        Objects.requireNonNull(scoreboard,"This should not be called unless the scoreboard exists!");
        Team team = scoreboard.getTeam(line.getTeamName());
        String[] split = split(content);

        assert team != null;

        team.setPrefix(split[0]);
        team.setSuffix(split[1]);

        line.setAsDisplayed(split[0] + split[1]);
    }

    private String[] split(String line) {
        if (line.length() < CHARACTER_LIMIT) {
            return new String[]{line, ""};
        }

        String prefix = line.substring(0, CHARACTER_LIMIT);
        String suffix = line.substring(CHARACTER_LIMIT);

        if (prefix.endsWith("§")) { // Check if we accidentally cut off a color
            prefix = removeLastCharacter(prefix);
            suffix = "§" + suffix;
        } else if(prefix.contains("§")) { // Are there any colors we need to continue?
            suffix = ChatColor.getLastColors(prefix) + suffix;
        } else { // Just make sure the team color doesn't mess up anything
            suffix = "§f" + suffix;
        }

        if (suffix.length() > CHARACTER_LIMIT) {
            suffix = suffix.substring(0, CHARACTER_LIMIT);
        }

        return new String[]{prefix, suffix};
    }

    private String removeLastCharacter(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

    private void setBoard() {
        Player p = this.player.getPlayer();
        if(p!=null)
            p.setScoreboard(scoreboard);
    }

    private void createTeams() {
        int score = this.lines;

        for (int i = 0; i < this.lines; i++) {
            Team t = this.scoreboard.registerNewTeam(ScoreboardLine.getTeamName(i));
            t.addEntry(ChatColor.values()[i] + "");
            this.objective.getScore(ChatColor.values()[i] + "").setScore(score);
            score--;
        }

        for(ScoreboardLine scoreboardLine : scoreboardLines.values()) {
            updateLine(scoreboardLine);
        }
    }

    @Nullable
    private ScoreboardLine getLineByName(@NotNull String lineName) {
        Objects.requireNonNull(lineName);
        for(ScoreboardLine scoreboardLine : scoreboardLines.values()) {
            if(Objects.equals(lineName,scoreboardLine.getName())) {
                return scoreboardLine;
            }
        }
        return null;
    }

    @NotNull @Override
    public Message getLineAt(int index) {
        lineValid(index);
        if(scoreboardLines.get(index)==null) {
            return Message.valueOf("");
        } else {
            return scoreboardLines.get(index).getMessage();
        }
    }

    @Nullable @Override
    public Message getLineFromName(@NotNull String name) {
        Objects.requireNonNull(name);
        ScoreboardLine line = getLineByName(name);
        if(line==null) {
            return null;
        } else {
            return line.getMessage();
        }
    }

    @NotNull
    @Override
    public String getDisplayedAt(int index) {
        lineValid(index);
        return scoreboardLines.get(index).getAsDisplayed();
    }

    @Nullable
    @Override
    public String getDisplayedAt(@NotNull String name) {
        ScoreboardLine line = getLineByName(Objects.requireNonNull(name));
        if(line==null)
            return null;

        return line.getAsDisplayed();
    }

    @NotNull @Override
    public Message getTitle() {
        return this.title;
    }

    @Override
    public int getSize() {
        return this.lines;
    }

    private void lineValid(int line) {
        if(line >= 0 && line <= this.lines-1)
            return;

        throw new ArrayIndexOutOfBoundsException("A scoreboard line number must be >= '0' AND < '"+this.lines+"'");
    }

    @NotNull @Override
    public String getName() {
        return name;
    }

    @NotNull @Override
    public BukkitApiPlayer getPlayer() {
        return player;
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible==this.visible)
            return;

        this.visible = visible;
        if(visible) {
            player.getPlayer().setScoreboard(scoreboard);
        } else {
            player.getPlayer().setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }


    @NotNull
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("name",name)
                .add("title",title).toString();
    }

}
