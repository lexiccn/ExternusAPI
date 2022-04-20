#Display Library

Bukkits default library for displaying things like 
    
<ul>
<li>Action Bars</li>
<li>Scoreboards</li>
<li>BossBars</li>
</ul>

is often clunky and unintuitive at best. That is where the display library comes into play

##Common

Among all of the display objects the following hold true

- All display objects will be removed when the player disconnects
- You should always add display objects to the BukkitAPIPlayer
- You should not store instances of the BukkitAPIPlayer just as regular Player instances
- All display objects fully support the locale library

## Scoreboards

### Creating a scoreboard

To create a scoreboard simply grab the BukkitAPIPlayer from the plugin. Then use `#setScoreboard` to make a scoreboard. The name of the scoreboard must be unique.

```java
BukkitPlugin plugin = this;
BukkitApiPlayer player = plugin.getBukkitPlayerManager().getPlayer(UUID.randomUUID());
//This makes a scoreboard with 12 lines. Note the name MUST be unique.
EScoreboard scoreboard = player.setScoreboard("MyScoreboard",12);
```

### Editing Lines 
Lines can be edited live at anytime using the following logic. Lines can be referenced by the position in which they sit on the scoreboard, or by name.

```java
//set the title of the scoreboard to my scoreboard
scoreboard.setTitle("My Scoreboard");
//set line 1 to the players health.
scoreboard.setLine("Health "+player.getHealth(),1,"health line");
```

Scoreboard lines fully support the locale library

```java
//ALTERNATIVELY it can be done as this for easier updating!
//make line 1 Health {0}, remember {0} is a placeholder as shown in the Locale Library
scoreboard.setLine("Health {0}",1,"health line");
//change the content of the {0} to the players health
scoreboard.setLineArgs("health line",player.getHealth());
```

### Removing The Scoreboard

```java
BukkitPlugin plugin = this;
BukkitApiPlayer player = plugin.getBukkitPlayerManager().getPlayer(UUID.randomUUID());
player.removeScoreboard();
```

## BossBars

The bossbar API functions extremely similary to the bukkit bossbar API. However it is still very useful as it fully supports the Locale Library and works somewhat well on 1.8

### Creating a BossBar

```java
BukkitApiPlayer player = plugin.getBukkitPlayerManager().getPlayer(bukkitPlayer);
EBossBar bossBar = player.setBossBar("healthBar");
bossBar.setColor(BarColor.BLUE);
bossBar.setMessage("Health {0}");
bossBar.setArgs(bukkitPlayer.getHealth());
```

### Removing a BossBar
```java
BukkitApiPlayer player = plugin.getBukkitPlayerManager().getPlayer(bukkitPlayer);
player.removeBossBar();
```

## Action Bars

### Sending an Action Bar

When sending an action bar it should be sent to a player for a given duration. 

```java
BukkitApiPlayer player = plugin.getBukkitPlayerManager().getPlayer(UUID.randomUUID());
//send an action bar that lasts for 5 seconds
ActionBar bar = new ActionBar("My Action Bar",Duration.of(5,ChronoUnit.MILLIS));
player.getActionBarManager().send(bar);
```

### Rejection Policy

When sending an action bar there may be an action bar that is already present on the players screen. Sometimes we may want to queue action bars, or just replace the existing one if it is sent. By default the rejection policy is overwrite. 

```java
//queue the action bar. This one will be sent after the current one has finished playing.
player.getActionBarManager().send(bar, RejectionPolicy.QUEUE());
//replace the current action bar with this one
player.getActionBarManager().send(bar, RejectionPolicy.OVERWRITE());
//keep the current action bar if it exists
player.getActionBarManager().send(bar,RejectionPolicy.SILENT_REJECT());
//throw an exception if an action bar exists
player.getActionBarManager().send(bar,RejectionPolicy.FAIL());
```
### Removing an Action Bar

```java
player.getActionBarManager().remove();
```