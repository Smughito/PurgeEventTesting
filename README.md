# PurgeEvent Plugin

A dynamic PvP event system for Minecraft Paper servers with phase-based gameplay.

## Features

- **Phase System**: Inactive (default) → Phase 1 → Phase 2 → Back to Inactive
- **No Cooldown Phase**: Removed from the system
- **Automatic Player Enrollment**: All non-OP players automatically join the event when it starts
- **Kill Tracking**: Tracks player kills and displays them on the scoreboard
- **Custom Death Messages**: Fully customizable death messages in config.yml
- **Phase 1 PvP Control**: PvP is disabled in Phase 1 except in designated WorldGuard regions
- **Spectator Mode**: Players become spectators upon death (except in Inactive phase)
- **Win Condition**: Last player alive wins
- **Live Stats**: Shows kills and alive player count on scoreboard
- **Final Statistics**: Displays winner and top killers when the event ends
- **Phase Announcements**: Automatic announcements when each phase starts

## Requirements

- Paper/Spigot 1.19.4+
- WorldGuard plugin
- Java 17+

## Installation

1. Download the plugin JAR file
2. Place it in your server's `plugins` folder
3. Ensure WorldGuard is installed
4. Restart the server
5. Configure the plugin in `plugins/PurgeEvent/config.yml`

## Commands

All commands require `purge.admin` permission (default: OP)

- `/purge start` - Start the event (automatically begins with Phase 1)
- `/purge stop` - Stop the event immediately
- `/purge phase <1|2>` - Manually change to a specific phase
- `/purge autophase [on|off]` - Toggle automatic phase progression (disabled by default)
- `/purge reload` - Reload the configuration file

## Configuration

Edit `plugins/PurgeEvent/config.yml` to customize:

### Phase Settings
- **duration**: Phase duration in seconds (only used when auto-phase is enabled)
- **pvp-regions**: List of WorldGuard regions where PvP is allowed in Phase 1

### Messages
- **phase-start**: Customizable messages for each phase start
- **phase-subtitle**: Subtitle displayed in title screen
- **pvp-disabled**: Message shown when PvP is blocked in Phase 1
- **death-messages**: Customizable death messages (random selection)
  - `{victim}` - Replaced with victim's name
  - `{killer}` - Replaced with killer's name
  - Use `&` for color codes

## How It Works

### Default Behavior
1. Server starts with **Inactive** phase (no event running)
2. Admin runs `/purge start` to begin the event
3. **Phase 1** starts immediately:
   - All non-OP players enter Survival mode
   - PvP is disabled except in configured WorldGuard regions
   - Players can prepare and fight in designated zones
4. Admin manually changes to **Phase 2** (or it progresses automatically if auto-phase is enabled):
   - PvP is enabled everywhere
   - All-out battle begins
5. Event continues until only one player remains alive
6. Winner and top killers are announced
7. Phase returns to **Inactive**

### Auto-Phase (Optional)
When enabled with `/purge autophase on`, phases will automatically progress after their configured duration.

### Player Behavior
- **Joining**: Non-OP players automatically join the event when they connect
- **Death**: Players become spectators immediately upon death
- **Quitting**: Players who log out are marked as eliminated
- **OPs**: Operators are excluded from the event and can spectate/manage freely

## Building from Source

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Gradle 7.0+

### Build Steps

1. Clone the repository:
```bash
git clone https://github.com/Smughito/PurgeEvent.git
cd PurgeEvent
```

2. Build the plugin:
```bash
./gradlew clean build
```

3. Find the compiled JAR in `build/libs/PurgeEvent-1.0.0.jar`

## File Structure

```
src/main/
├── java/me/smughito/purgeevent/
│   ├── PurgeEvent.java (Main plugin class)
│   ├── commands/
│   │   └── PurgeCommands.java (Command handler)
│   ├── listeners/
│   │   ├── PlayerDeathListener.java (Death events)
│   │   ├── PlayerJoinListener.java (Join events)
│   │   ├── PlayerQuitListener.java (Quit events)
│   │   └── PvpListener.java (PvP protection in Phase 1)
│   ├── managers/
│   │   ├── PhaseManager.java (Phase control)
│   │   ├── PlayerDataManager.java (Player data tracking)
│   │   └── ScoreboardManager.java (Scoreboard display)
│   ├── phases/
│   │   └── GamePhase.java (Phase enum)
│   └── utils/
│       └── DeathMessageUtil.java (Death message handler)
└── resources/
    ├── config.yml (Configuration file)
    └── plugin.yml (Plugin metadata)
```

## Support

For issues or questions, please create an issue on the GitHub repository.

## License

This project is open source and available for modification and redistribution.
