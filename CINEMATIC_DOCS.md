# Arsen Cinematic Cutscene Docs

This documents the custom `arsen_cinematic` system in JJKPort.

## Command

```denizen
- arsen_cinematic play sequence:<sequence> location:<origin> owner:<player> for:<player>|... (bone:<meg_bone>) (hide_player_entity) (camera_mode:per_player/shared) (position_smooth:<#>) (rotation_smooth:<#>) (smoothing_mode:interpolation/lerp) (save:<name>)
```

Cancel targeted viewers only:

```denizen
- arsen_cinematic cancel for:<player>|...
```

If a targeted viewer is already in a cinematic, the old cinematic is canceled for that viewer and the new one starts.

## Viewer Camera Modes

### per_player

Default. Each viewer gets their own camera entity. This follows Typewriter-style behavior and is safest for per-viewer cancel and cleanup.

```denizen
camera_mode:per_player
```

### shared

All viewers spectate the same camera entity. This is lighter, useful when everyone should see exactly the same cutscene.

```denizen
camera_mode:shared
```

## Sequence Entries

Sequences are Denizen lists, usually stored in a data script.

### camera_CFrame

```text
camera_CFrame|location|duration|easing_style|easing_function|ticks
```

The location is relative to the command `location:` origin.

- relative X/Y/Z offset is rotated by origin yaw and pitch
- relative yaw/pitch are added to origin yaw/pitch
- if another `camera_CFrame` starts while one is active, it starts from the current camera transform

Supported easing styles:

```text
Instant
Linear
Sine
Quad
Back
Smooth
```

Supported easing functions:

```text
In
Out
InOut
In-Out
In_Out
```

Example:

```denizen
- camera_CFrame|<location[0,2,-6].with_yaw[0].with_pitch[10]>|40t|Sine|InOut|0
```

### camera_Shake

```text
camera_Shake|mode|strength|frequency|duration|fade_in|fade_out|ticks
```

Modes:

```text
Pos
Rot
PosRot
```

Shake is added on top of the current camera transform, so it works with normal CFrame mode and bone mode.

Example:

```denizen
- camera_Shake|PosRot|0.25|10|20t|3t|6t|10
```

### bone_smoothing

Only applies when command uses `bone:<meg_bone>`.

```text
bone_smoothing|position_smooth|rotation_smooth|smoothing_mode|ticks
```

Defaults:

```text
position_smooth = 3
rotation_smooth = 3
smoothing_mode = interpolation
```

Command-level defaults can be overridden:

```denizen
- arsen_cinematic play sequence:<...> location:<origin> owner:<player> for:<player> bone:<[bone]> position_smooth:2 rotation_smooth:2 smoothing_mode:lerp
```

Sequence override:

```denizen
- bone_smoothing|5|5|lerp|20
```

### mythicskill

Runs a MythicSkill at a tick. Target and caster are the owner visual entity when available, otherwise the owner player.

```text
mythicskill|skill_name|ticks
```

### denizen_task

Runs a Denizen task at a tick.

```text
denizen_task|task_name|ticks
```

The task receives:

```text
owner|cinematic_id
```

### exit_cinematic

Ends the cinematic at the given tick.

```text
exit_cinematic|ticks
```

Example:

```denizen
- exit_cinematic|125
```

## Bone Mode

When `bone:<meg_bone>` is specified:

- `camera_CFrame` entries are ignored
- camera follows the bone every tick
- `camera_Shake` still applies
- if the bone/base entity/model disappears, the cinematic cancels immediately
- command-level `position_smooth`, `rotation_smooth`, and `smoothing_mode` apply
- `bone_smoothing` entries can change smoothing during the cinematic

## Protection State

During cinematic, the plugin sets:

Real player flags:

```text
cinematic.active
cinematic.id
cinematic.owner
cinematic.role
cinematic.camera_mode
cinematic.last_location
disable_skills
```

Visual entity flags, if found:

```text
cinematic.active
cinematic.id
cinematic.lock_location
disable_skills
```

The protection listener cancels damage and targeting involving cinematic players or their visual entity.

## Malevolent Shrine Converted Cinematic Data

Stored in `JJKRPG-ATOX-Denizen/chars/Shrine_Cinematic_Data.dsc`.

Use:

```denizen
- arsen_cinematic play sequence:<script[Shrine_Cinematic_Data].data_key[Data.Shrine_Malevolent_Shrine]> location:<[player_entity].location.with_pitch[0]> owner:<[player]> for:<[player]> hide_player_entity
```

Converted Typewriter notes:

- `camera_cinematic_2` starts at tick `0`.
- `camera_cinematic_3` starts at tick `62`.
- The first `camera_cinematic_3` point was converted to `camera_CFrame` with `Instant` easing at tick `62`, then the next movement starts at tick `63`.
- `exit_cinematic|125` ends the cutscene.
