# PurgeEvent Plugin - Implementation Summary

## Features Implemented

### 1. Two-Phase Purge System
- **Phase 1 (WARNING/Countdown)**: PvP is OFF everywhere except in designated PvP zones
  - Players can only fight in zones specified in `pvp-zones` config
  - Editable countdown duration in config.yml
  - Visual and audio warnings at configurable intervals

- **Phase 2 (ACTIVE)**: PvP is ON everywhere except safe zones
  - All areas become PvP-enabled
  - Safe zones (spawn, safezone, etc.) are protected
  - Battle royale mode activated

### 2. Player Tracking System
- Automatically counts all non-OP players when `/purge start` is initiated
- Tracks player statistics: Kills, Deaths, Assists (KDA)
- Monitors alive vs eliminated players

### 3. Death & Respawn System
- **Immediate Respawn**: Players respawn instantly after death
- **Spectator Mode**: Eliminated players automatically enter spectator mode
- **Custom Messages**:
  - Death announcements
  - Kill messages
  - Spectator notification

### 4. Win Condition
- Event automatically ends when only one player remains alive
- Winner announcement broadcast to all players
- Event status returns to INACTIVE
- All spectators returned to survival mode

### 5. Live Scoreboard
Displays in real-time:
- **KDA Stats**: Player's Kills/Deaths/Assists
- **Time Remaining**: Formatted countdown timer
- **Current Phase**: Phase 1, Phase 2, Cooldown, or Inactive
- **Alive Players**: Shows alive/total participants
- **Server IP**: Configurable server address
- **Server Logo**: Customizable branding

### 6. Configuration
All features are configurable in `config.yml`:
- Phase 1 countdown duration
- Phase 2 duration
- PvP zones for Phase 1
- Safe zones for Phase 2
- Custom messages for all events
- Scoreboard customization
- Sound effects

## How to Build

### Requirements
- Java 21 or higher
- Gradle 8.1+

### Build Commands

```bash
# Build the plugin
./gradlew build

# Output JAR location
build/libs/PurgeEvent-1.0.0.jar
```

## Installation

1. Build the plugin using the command above
2. Copy `PurgeEvent-1.0.0.jar` to your server's `plugins/` folder
3. Install WorldGuard 7.0.9+ (required dependency)
4. Restart your server
5. Configure `plugins/PurgeEvent/config.yml` to your preferences

## Usage

### Commands
- `/purge start` - Start the Purge Event (Phase 1 begins)
- `/purge stop` - Manually stop the event
- `/purge skip` - Skip the current phase
- `/purge status` - View current status
- `/purge reload` - Reload configuration
- `/purge automation <enable|disable>` - Toggle automatic cycles

### Configuration Setup

Edit `config.yml` and set:
1. **pvp-zones**: Regions where PvP is allowed during Phase 1
   ```yaml
   pvp-zones:
     - arena
     - pvpzone
     - battleground
   ```

2. **safe-regions**: Regions protected during Phase 2
   ```yaml
   safe-regions:
     - spawn
     - safezone
   ```

3. **Phase Durations**: Adjust countdown and battle durations
   ```yaml
   phases:
     warning:
       duration: 300  # Phase 1 countdown (seconds)
     active:
       duration: 1800  # Phase 2 battle (seconds)
   ```

4. **Scoreboard**: Customize your server branding
   ```yaml
   scoreboard:
     title: "&c&lPURGE EVENT"
     logo: "&6&lYour Server"
     server-ip: "play.yourserver.com"
   ```

## Event Flow

1. Admin runs `/purge start`
2. All non-OP players are registered for the event
3. **Phase 1 begins**: Countdown with PvP only in designated zones
4. Warnings displayed at configured intervals
5. **Phase 2 begins**: Full PvP enabled, safe zones disabled
6. Players fight until one remains
7. Winner announced, event ends automatically
8. All spectators returned to survival mode

## Permissions

- `purgeevent.admin` - Access to all commands (default: OP)
- `purgeevent.bypass` - Bypass purge restrictions (default: false)

## Files Added/Modified

### New Files
- `managers/PlayerTracker.java` - Player tracking and statistics
- `managers/ScoreboardManager.java` - Real-time scoreboard display
- `listeners/DeathHandler.java` - Death, respawn, and win condition logic

### Modified Files
- `PurgeEventPlugin.java` - Integrated new managers and listeners
- `managers/PhaseManager.java` - Two-phase system implementation
- `managers/WorldGuardManager.java` - Phase-specific flag management
- `listeners/PvPListener.java` - Phase-aware PvP handling
- `config/ConfigManager.java` - New configuration options
- `resources/config.yml` - Extended configuration

## Technical Details

### Phase 1 (WARNING)
- WorldGuard flags set to DENY PvP for all regions except pvp-zones
- pvp-zones have PvP flag set to ALLOW
- Players can practice/warm up in designated areas

### Phase 2 (ACTIVE)
- WorldGuard flags set to ALLOW PvP for all regions except safe-regions
- safe-regions have PvP flag set to DENY
- Full battle royale mode

### Player Management
- Only non-OP players are tracked
- Stats persist throughout the event
- Spectators cannot interfere with gameplay
- Automatic cleanup on event end

## Notes

- Java 21 runtime is required for this plugin
- WorldGuard must be installed and configured
- All original flags are restored after the event
- Compatible with Paper 1.21.1+
