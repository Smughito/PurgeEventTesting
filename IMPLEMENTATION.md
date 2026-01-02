# PurgeEvent Implementation Details

## Requirements Completed

### ✓ Core Phase System
- **Removed Cooldown Phase**: No longer exists in the system
- **Default Phase**: Starts as INACTIVE
- **Phase Progression**: INACTIVE → PHASE_1 → PHASE_2 → INACTIVE
- **Manual Start**: `/purge start` begins with Phase 1
- **Auto-Phase Disabled by Default**: Must be manually enabled with `/purge autophase on`

### ✓ Player Management
- **Auto-Join**: All non-OP players automatically join the event when it starts or when they connect during an active event
- **OP Exclusion**: Players with OP status are excluded from the event
- **Spectator Mode**: Players become spectators upon death in all phases except INACTIVE
- **Win Condition**: Last player alive wins the event

### ✓ Scoreboard Features
- **Kills Display**: Shows individual player's kill count
- **Alive Count**: Shows total number of players still alive
- **Phase Display**: Shows current phase
- **Real-time Updates**: Scoreboard updates after each kill/death

### ✓ Death System
- **Custom Death Messages**: Fully customizable in config.yml
- **Player Kill Messages**: 7 different messages with {victim} and {killer} placeholders
- **Natural Death Messages**: 5 different messages for non-PvP deaths
- **Random Selection**: Messages are randomly chosen for variety
- **Color Code Support**: Use & for color codes in config

### ✓ Phase-Specific Features
#### Phase 1
- **Conditional PvP**: PvP disabled by default
- **WorldGuard Integration**: PvP enabled only in configured regions
- **Region List**: Configurable in `config.yml` under `phases.phase_1.pvp-regions`
- **Warning Message**: Players get notified when they try to PvP outside allowed regions

#### Phase 2
- **Full PvP**: PvP enabled everywhere
- **Survival Mode**: All players remain in survival mode
- **No Restrictions**: No region-based limitations

### ✓ Phase Announcements
- **Title Display**: Large title shown to all players when phase changes
- **Subtitle**: Customizable subtitle for each phase
- **Chat Message**: Broadcast message in chat
- **Sound Effect**: Ender dragon growl plays for dramatic effect
- **Fully Customizable**: All messages configurable in config.yml

### ✓ End Game Statistics
- **Winner Announcement**: Displays the last player alive
- **Top Killers**: Shows top 3 players by kill count
- **Formatted Display**: Clean, boxed format in chat
- **Player Name Resolution**: Handles offline players gracefully

### ✓ Commands System
- `/purge start` - Starts the event (Phase 1 begins immediately)
- `/purge stop` - Ends the event and returns to INACTIVE
- `/purge phase <1|2>` - Manually change phases during the event
- `/purge autophase [on|off]` - Toggle or view auto-phase status
- `/purge reload` - Reload configuration without restart
- **Tab Completion**: Full tab completion support
- **Permission System**: All commands require `purge.admin` (default: OP)

## Technical Implementation

### Class Structure

#### Core Plugin
- `PurgeEvent.java` - Main plugin class, initialization, and manager coordination

#### Managers
- `PhaseManager.java` - Handles phase transitions, timers, and win conditions
- `PlayerDataManager.java` - Tracks kills, alive status, and player statistics
- `ScoreboardManager.java` - Creates and updates player scoreboards

#### Listeners
- `PlayerDeathListener.java` - Handles death events, spectator mode, and kill tracking
- `PlayerJoinListener.java` - Auto-joins players to the event
- `PlayerQuitListener.java` - Updates alive count when players disconnect
- `PvpListener.java` - Controls PvP based on phase and WorldGuard regions

#### Utilities
- `DeathMessageUtil.java` - Handles custom death message selection and formatting

#### Enums
- `GamePhase.java` - Defines the three game phases (INACTIVE, PHASE_1, PHASE_2)

#### Commands
- `PurgeCommands.java` - Command executor and tab completer

### Configuration Structure

The plugin uses a YAML configuration file with the following sections:

1. **Phases Configuration**
   - Duration settings (for auto-phase)
   - PvP region lists for Phase 1

2. **Messages**
   - Phase start announcements
   - Phase subtitles
   - PvP disabled warning

3. **Death Messages**
   - Player kill messages (with placeholders)
   - Natural death messages

### Key Features

#### Data Safety
- All player data is properly cleaned up when the event ends
- Player data is reset at the start of each new event
- Graceful handling of player disconnects

#### Performance
- Efficient scoreboard updates (only when needed)
- Minimal overhead during normal gameplay
- Proper cleanup of scheduled tasks

#### WorldGuard Integration
- Safe fallback if WorldGuard is not available
- Supports multiple PvP regions
- Efficient region checking

## Building the Plugin

### Requirements
- Java 17+
- Gradle 7.0+
- Paper API 1.19.4
- WorldGuard 7.0.9

### Build Command
```bash
./gradlew clean build
```

The compiled JAR will be in `build/libs/PurgeEvent-1.0.0.jar`

## Testing Checklist

- [ ] Plugin loads without errors
- [ ] `/purge start` begins Phase 1
- [ ] Non-OP players auto-join the event
- [ ] OP players are excluded from the event
- [ ] Scoreboard shows kills and alive count
- [ ] Death messages display correctly
- [ ] Players become spectators on death
- [ ] PvP is blocked in Phase 1 outside regions
- [ ] PvP works in configured regions in Phase 1
- [ ] Phase 2 allows PvP everywhere
- [ ] Last player alive triggers win condition
- [ ] Final statistics display correctly
- [ ] `/purge stop` ends the event properly
- [ ] `/purge phase` changes phases manually
- [ ] `/purge autophase` toggles correctly
- [ ] Phase announcements show with title and sound
- [ ] Config reload works without restart

## Configuration Examples

### Adding PvP Regions
```yaml
phases:
  phase_1:
    pvp-regions:
      - "arena"
      - "colosseum"
      - "battleground"
```

### Customizing Death Messages
```yaml
death-messages:
  player-kill:
    - "&c{victim} &7was destroyed by &c{killer}!"
    - "&4{killer} &7eliminated &4{victim}!"
```

### Changing Phase Duration
```yaml
phases:
  phase_1:
    duration: 600  # 10 minutes in seconds
  phase_2:
    duration: 300  # 5 minutes in seconds
```

## Known Limitations

1. Requires WorldGuard for Phase 1 PvP region control
2. Requires Paper API (Spigot may have limited functionality)
3. Java 17 minimum requirement
4. Players who join mid-event are automatically added (design choice)

## Future Enhancement Ideas

- Database integration for persistent statistics
- Leaderboard system
- Multiple arenas support
- Configurable rewards
- Team-based mode
- More phase types
- GUI configuration editor
