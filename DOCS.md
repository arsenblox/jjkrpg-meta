# JJKRPG / JJKPort Feature Documentation

This document explains the current JJKPort plugin features and the related JJKRPG Denizen usage patterns.

Primary repositories:

- Plugin repo: `arsenblox/jjkport-Atox`
- Denizen script repo: `arsenblox/JJKRPG-ATOX-Denizen`
- Meta/docs repo: `arsenblox/jjkrpg-meta`

> This file is human-readable documentation. Denizen/SharpDenizenTools meta comments live in `src/main/java/me/arsenplugin/jjkrpg/meta/JJKPortMeta.java`.

---

## 1. Plugin Overview

Plugin name: `jjkport`

Main class:

```text
me.arsenplugin.jjkport.Jjkport
```

Package root:

```text
me.arsenplugin.jjkport
```

Target server:

```text
Paper 1.21.11
```

Important dependencies:

```text
Denizen 1.3.1-b7255-DEV
ModelEngine R4.1.0-14 on server
ModelEngine R4.0.9 as current compile dependency
PacketEvents / packetevents-spigot 2.11.1
MythicMobsPremium 5.12.0-SNAPSHOT on server
```

Maven notes:

- Denizen is installed locally into Maven.
- PacketEvents is installed locally into Maven as:

```xml
<groupId>com.github.retrooper</groupId>
<artifactId>packetevents-spigot</artifactId>
<version>2.11.1</version>
```

---

## 2. Important Project Rule

When a new large feature or behavior is described, ask questions and give suggestions first.
Do not immediately patch code unless the instruction clearly says to continue.

When a screenshot of IntelliJ or build output is sent, treat it as the current rebuild error/output, not as a request to blindly rerun the previous patcher.

Do not update `NOTE.txt` in `jjkrpg-meta` unless explicitly requested. Prefer updating:

```text
src/main/java/me/arsenplugin/jjkrpg/meta/JJKPortMeta.java
```

or this file:

```text
DOCS.md
```

When the user says `update`, update both `DOCS.md` and `JJKPortMeta.java` from the latest `arsenblox/jjkport-Atox` source.

---

## 3. Admin Command and Settings

Bukkit command:

```text
/jjkport <debug|reload_after_load|status> [true/false]
```

Permission:

```text
jjkport.admin
```

Default permission is op.

Subcommands:

```text
/jjkport status
/jjkport debug
/jjkport debug true
/jjkport debug false
/jjkport reload_after_load
/jjkport reload_after_load true
/jjkport reload_after_load false
```

Settings written to config:

```yaml
debug: false
reload_after_load: false
```

Behavior:

- `debug` enables extra jjkport debug logging.
- `reload_after_load` schedules Denizen `ex reload` 20 ticks after jjkport loads.
- Running `/jjkport debug` with no boolean toggles the current value.
- Running `/jjkport reload_after_load` with no boolean toggles the current value.

---

## 4. Denizen Visual Body Rule

In `JJKRPG-ATOX-Denizen`, real players are hidden for gameplay.

A player's visual/game body is stored in:

```denizen
<player.flag[game.PlayerEntity]>
```

The visual entity also has Mythic variable:

```text
ownername=<player.name>
```

Domain and cinematic logic should treat the visual entity as the player's real gameplay body when available.

That means:

- teleport the visual entity, not only the real player
- containment checks should use the visual entity location
- combat/domain membership should consider the visual body
- `<PlayerTag.inside_domain>` should check the visual entity location when possible
- `arsen_cinematic hide_player_entity:true` hides the visual body with MythicSkill `hide_player` and restores it with `show_player`

The plugin resolver also checks lowercase fallback:

```text
game.playerentity
```

---

## 5. Domain System

The main command is:

```denizen
domaineffect
```

A domain is represented by:

```denizen
domain@<id>
```

Example:

```denizen
- domaineffect <player.location> owner:<player> duration:20s domain_opening:3s radius:15 material:obsidian floor_material:barrier save:my_domain
- narrate "Domain: <entry[my_domain].result>"
```

---

## 6. Domain Modes

There are two domain modes:

```text
BARRIER
OPEN
```

### BARRIER Mode

Barrier domains are closed domains with terrain, containment, and combat isolation.

Behavior:

- creates shell/border during opening
- final arena is built after opening
- creates a floor at `domainY - 1`
- snapshots affected players/entities
- teleports affected entities to domain Y only if they are below domain Y
- keeps same X/Z/yaw/pitch for Y-only opening teleport
- prevents affected entities from escaping
- prevents outside entities from entering
- isolates combat between inside and outside members
- restores the world when canceled or ended

Example:

```denizen
- domaineffect <player.location> owner:<player> duration:20s domain_opening:3s radius:20 radiusY:20 shape:sphere material:deepslate_tiles floor_material:barrier save:barrier_domain
```

### OPEN Mode

Open domains are radius-only domains.

Behavior:

- no shell
- no floor
- no forced teleport
- no containment
- no combat isolation
- affected players/entities update dynamically as they enter/leave radius
- owner can leave radius without destroying the domain
- domain still cancels if owner dies, leaves server, or no longer exists

Example from Malevolent Shrine:

```denizen
- domaineffect <player.location> owner:<player> duration:1290t radius:30 radiusY:30 open_domain domain_tick_task:Malevolent_Shrine_DomainTick domain_cancel_task:Malevolent_Shrine_DomainCancel save:malevolent_shrine_domain
```

Aliases currently accepted for open mode:

```text
open_domain
opendomain
non_barrier
nonbarrier
no_barrier
nobarrier
open
barrier:false
```

Note: `open` is accepted by the current parser, but `open_domain` is still safer and clearer because `open` can be confused with `domain_opening` wording.

---

## 7. Domain Lifecycle

A domain has these useful states:

```text
OPENING
ACTIVE
```

Tags:

```denizen
<[domaintag].state>
<[domaintag].mode>
```

Example:

```denizen
- announce "domain=<[domaintag]> id=<[domaintag].id> mode=<[domaintag].mode> state=<[domaintag].state>"
```

Expected output style:

```text
domain=domain@1 id=1 mode=OPEN state=ACTIVE
```

Canceled domains are normally removed instead of staying as a `CANCELED` state.

Exception: a canceled domain may stay registered temporarily when it has domain assets and a cancel task, so the cancel task can still access `<[domaintag].assets>`.

---

## 8. Barrier Domain Terrain Behavior

### Opening Phase

During opening:

- no floor is created yet
- shell/border animates during opening
- blocks inside become the configured `material`
- air is ignored
- non-solid blocks inside are removed/turned to air
- solid blocks fully surrounded by solid blocks on all 6 sides may be skipped for performance
- shell creates the blockade/no-escape effect

### After Opening Completes

After opening:

- all blocks inside the domain are cleared
- floor is instantly created at `domainY - 1`
- affected entities are moved up to domain Y only if below domain Y
- X/Z/yaw/pitch are preserved when only Y changes

### Ending / Cancel

When the domain ends or is canceled:

1. restore the world exactly as it was
2. teleport affected entities to safe ground inside the domain radius

Ground search rule:

1. same X/Z, below entity Y, inside radius
2. same X/Z, above entity Y, inside radius
3. nearest valid ground inside domain radius
4. fallback to same X/Z at domain center Y

Ground block means:

```text
a solid block with 2 air blocks above
```

Avoid center-block snap/jitter unless no better safe location exists.

---

## 9. Domain Command Syntax

Current intended syntax:

```denizen
domaineffect <location|domain|entity> \
    (cancel) \
    (enter/leave) \
    (owner:<player/entity>) \
    (for:<player>|...) \
    (duration:<duration>) \
    (domain_opening:<duration>) \
    (opening_animation:<down_to_up/back_to_front>) \
    (radius:<#>) \
    (radius2:<#>) \
    (radiusY:<#>) \
    (shape:sphere/cube) \
    (material:<material>) \
    (floor_material:<material>) \
    (below_floor_air_depth:<#>) \
    (open_domain/non_barrier/barrier:false) \
    (domain_start_task:<task>) \
    (domain_after_opening_task:<task>) \
    (domain_tick_task:<task>) \
    (domain_cancel_task:<task>) \
    (domain_assets:<entity>|...) \
    (add_domain_assets:<entity>|...) \
    (remove_domain_assets:<entity>|...) \
    (clear_domain_assets) \
    (hide_inside) \
    (save:<name>)
```

Parser aliases:

```text
cancel / stop / remove
leave / exit
owner / caster
for / players / viewer / viewers
domain_opening / opening / domain_open / opening_duration
opening_animation / opening_direction / opening_style / animation
below_floor_air_depth / belowfloorairdepth / floor_air_depth / under_floor_air / underfloorair
radius2 / inner_radius / inner
radiusY / radiusy / radius_y / ry
radius / r
floor_material / floormaterial / floor
material / mat
domain_start_task / start_task
domain_after_opening_task / after_opening_task / domain_after_open_task / after_open_task
domain_tick_task / tick_task
domain_cancel_task / cancel_task
domain_assets / assets
add_domain_assets / add_assets
remove_domain_assets / remove_assets
clear_domain_assets / clear_assets
```

---

## 10. Domain Callbacks

Callbacks allow Denizen scripts to run during domain lifecycle events.

Available callback arguments:

```text
domain_start_task:<task>
domain_after_opening_task:<task>
domain_tick_task:<task>
domain_cancel_task:<task>
```

Callback definitions:

```text
domain_start_task: domaintag
domain_after_opening_task: domaintag
domain_tick_task: domaintag
domain_cancel_task: domaintag|reason
```

Example tick task:

```denizen
Malevolent_Shrine_DomainTick:
    type: task
    definitions: domaintag
    script:
    - announce "domain=<[domaintag]> id=<[domaintag].id> mode=<[domaintag].mode> state=<[domaintag].state>"
```

Example cancel task:

```denizen
Malevolent_Shrine_DomainCancel:
    type: task
    definitions: domaintag|reason
    script:
    - announce "Canceled <[domaintag]> because <[reason]>"
```

Known cancel reasons:

```text
duration
command
owners_invalid
plugin_shutdown
```

The Java task runner uses queue IDs like:

```text
domain_<domainId>_<age>
```

and passes the domain tag like:

```text
def:<domain[<domainId>]>
```

---

## 11. DomainTag Tags

Base tag:

```denizen
<domain[1]>
```

Important tags:

```denizen
<[domaintag].id>
<[domaintag].exists>
<[domaintag].state>
<[domaintag].mode>
<[domaintag].location>
<[domaintag].owner>
<[domaintag].owners>
<[domaintag].radius>
<[domaintag].radiusY>
<[domaintag].radius2>
<[domaintag].assets>
<[domaintag].affected_players>
<[domaintag].affected_players_opening>
<[domaintag].affected_mythicmobs>
<[domaintag].affected_mythicmobs_opening>
<[domaintag].random_location_inside_domain_radius[<radius>]>
```

Player tag:

```denizen
<player.inside_domain>
```

`<player.inside_domain>` checks the player's domain/control location, using the visual body when available.

---

## 12. Domain Assets

Domain assets are manually managed entities associated with a domain.

They are useful for things like:

- Malevolent Shrine model/entity
- dismantle visual entities
- large dismantle visual entities
- any VFX entity that should be cleaned by the cancel task

Assets are not automatically removed by the plugin.
Scripts are responsible for animating/removing them.

Intended command behavior:

```denizen
- domaineffect <[domaintag]> domain_assets:<list[<[entity1]>|<[entity2]>]>        # replace
- domaineffect <[domaintag]> add_domain_assets:<list[<[entity]>]>                # add
- domaineffect <[domaintag]> remove_domain_assets:<list[<[entity]>]>             # remove
- domaineffect <[domaintag]> clear_domain_assets                                 # clear
```

Read assets:

```denizen
- foreach <[domaintag].assets> as:asset:
    - narrate "Asset: <[asset]>"
```

Cancel cleanup example:

```denizen
Malevolent_Shrine_DomainCancel:
    type: task
    definitions: domaintag|reason
    script:
    - foreach <[domaintag].assets> as:asset:
        - if <[asset].is_truthy||false>:
            - remove <[asset]>
```

Important behavior:

- assets remain available during `domain_cancel_task`
- canceled domains can stay registered while valid assets still exist
- once assets are removed or invalid, the canceled domain can be removed from the manager
- invalid/removed assets are filtered out of `<[domaintag].assets>`

---

## 13. Malevolent Shrine Current Integration

Current script:

```text
chars/Shrine.dsc
```

Current domain creation pattern:

```denizen
- domaineffect <[domain_location]> owner:<[player]> duration:1290t radius:30 radiusY:30 open_domain domain_tick_task:Malevolent_Shrine_DomainTick domain_cancel_task:Malevolent_Shrine_DomainCancel save:malevolent_shrine_domain
```

Suggested asset migration shape:

```denizen
- mythicspawn MalevolentShrine <[player_entity].location> save:malevolent_shrine
- define shrine_mob <entry[malevolent_shrine].spawned_mythicmob>
- define shrine_entity <[shrine_mob].entity>

- domaineffect <[domain_location]> owner:<[player]> duration:1290t radius:30 radiusY:30 open_domain domain_tick_task:Malevolent_Shrine_DomainTick domain_cancel_task:Malevolent_Shrine_DomainCancel save:malevolent_shrine_domain
- define domaintag <entry[malevolent_shrine_domain].result>

- domaineffect <[domaintag]> add_domain_assets:<list[<[shrine_entity]>]>
```

When dismantle assets are spawned later:

```denizen
- mythicspawn DEDismantle <[domain_location].with_yaw[<util.random.decimal[0].to[360]>]> save:malevolent_shrine_dismantle
- mythicspawn DEDismantleBig <[domain_location].with_yaw[<util.random.decimal[0].to[360]>]> save:malevolent_shrine_dismantle_big
- define new_dismantle <entry[malevolent_shrine_dismantle].spawned_mythicmob.entity>
- define new_dismantle_big <entry[malevolent_shrine_dismantle_big].spawned_mythicmob.entity>
- domaineffect <[domaintag]> add_domain_assets:<list[<[new_dismantle]>|<[new_dismantle_big]>]>
```

Cancel cleanup target:

```denizen
Malevolent_Shrine_DomainCancel:
    type: task
    definitions: domaintag|reason
    script:
    - foreach <[domaintag].assets> as:asset:
        - if <[asset].is_truthy||false>:
            - remove <[asset]>
```

Possible future improvement:

```denizen
<[domaintag].owner_player>
<[domaintag].owner_players>
```

These would let Shrine tick/cancel tasks find the owner without server owner-name flags.

---

## 14. Combat Isolation

Barrier-only combat isolation means:

- affected members inside the same barrier domain can fight each other
- outside entities should not damage inside members
- inside members should not damage outside entities
- open domains do not apply combat isolation

Visual-player bodies should be treated as player combat bodies when resolving membership.

---

## 15. Owner Handling

Domains support multiple owners.

Behavior history:

- opening a barrier domain inside an opening barrier domain merges the owner into that opening domain
- opening a barrier domain inside/near an active finished barrier domain is canceled
- domains are destroyed when all owners are invalid
- open domain owner can leave the radius, but the domain still cancels if the owner dies/leaves/no longer exists

Tags:

```denizen
<[domaintag].owner>   # first owner UUID compatibility
<[domaintag].owners>  # all owner UUIDs
```

---

## 16. ModelEngine Bridge

The plugin adds tags for ModelEngine active models and bones.

Entity tag:

```denizen
<entity.active_models>
```

This returns a map of active models attached to the entity.

Access by model ID:

```denizen
<player.target.active_models.get[naoya]>
```

Access by index:

```denizen
<player.target.active_models.get[1]>
```

Model tags:

```denizen
<meg_model.id>
<meg_model.bones>
<meg_model.bone[head]>
<meg_model.has_bone[head]>
<meg_model.bone_count>
```

Bone tags:

```denizen
<meg_bone.id>
<meg_bone.model_id>
<meg_bone.location>
<meg_bone.position>
<meg_bone.scale>
<meg_bone.translation>
<meg_bone.debug_pose>
```

Example:

```denizen
- define model <player.flag[game.PlayerEntity].entity.active_models.get[naoya]>
- define head_bone <[model].bone[head]>
- narrate "Head bone location: <[head_bone].location>"
```

---

## 17. MEGAttach Command

Command:

```denizen
megattach
```

Purpose:

- attach real Bukkit entities to ModelEngine bones/entities
- attach packet particles to ModelEngine bones/entities
- update attachments every tick
- support socket-like bone offsets with `to_owner`

General syntax:

```denizen
megattach [<entity>|...]|[<meg_particle>|...] \
    (to:<meg_bone>/<entity>) \
    (to_owner:<entity>) \
    (cancel) \
    (pivot) \
    (relative) \
    (eye_location/head) \
    (scale) \
    (rotation) \
    (bone_rotation) \
    (smooth:<#>) \
    (position_smooth:<#>) \
    (rotation_smooth:<#>) \
    (scale_smooth:<#>) \
    (offset:<x,y,z>|<x,y,z,yaw,pitch>) \
    (offset_yaw:<#>) \
    (offset_pitch:<#>) \
    (yaw_offset:<#>) \
    (pitch_offset:<#>)
```

Example:

```denizen
- define bone <player.target.active_models.get[naoya].bone[head]>
- megattach <server.flag[eye_display]> to:<[bone]>
```

Packet particle attach example:

```denizen
- megattach <entry[vfx].result> to:<[bone]> to_owner:<player> eye_location relative scale rotation smooth:1 position_smooth:1 rotation_smooth:1 scale_smooth:0
```

---

## 18. ParticleEmit Command

Command:

```denizen
particleemit
```

Purpose:

Creates a packet-only fake `item_display` particle for selected viewers.
It animates the `item_model` component over frames.

This does not spawn a real Bukkit entity.

General syntax:

```denizen
particleemit (<meg_particle>) \
    (entity:<meg_particle>) \
    (at:<location>) \
    (for:<player>|...) \
    (billboard:<billboard>) \
    (duration:<duration>) \
    (save:<name>) \
    (frame:<frame>|...) \
    (frame_start:<#>) \
    (frame_end:<#>) \
    (frame_time:<duration>) \
    (scale:<#>) \
    (scale_bone:<meg_bone>) \
    (smooth_scale_bone:<#>) \
    (item:<material>) \
    (cancel)
```

Important behavior:

- fullbright by default
- packet-only fake display entity
- can use numbered frames from `frame_start` to `frame_end`
- can use explicit frame list
- can follow ModelEngine bone scale with `scale_bone`
- can be canceled with `cancel`, `stop`, or `remove`

Example:

```denizen
- particleemit at:<player.eye_location.forward[0.2]> for:<server.online_players> frame:jjk:red_startup_vfx/redstartup frame_start:1 frame_end:10 frame_time:1t duration:10t billboard:CENTER scale:6 save:red_vfx
```

Cancel example:

```denizen
- particleemit entity:<entry[red_vfx].result> cancel
```

Particle tags:

```denizen
<meg_particle.id>
<meg_particle.exists>
<meg_particle.frame>
<meg_particle.location>
```

---

## 19. BlockWave Command

Command:

```denizen
blockwave
```

Purpose:

Creates a packet-only fake falling-block wave visual.
It does not spawn real falling blocks and does not place blocks when they land.

General syntax:

```denizen
blockwave <location> \
    (for:<player>|...) \
    (duration:<duration>) \
    (radius:<#>) \
    (radiusY:<#>) \
    (radius2:<#>) \
    (velocity:<#>) \
    (velocityX:<list[#|#]>|<#>) \
    (velocityY:<list[#|#]>|<#>) \
    (velocityZ:<list[#|#]>|<#>) \
    (noise:<#>) \
    (shape:sphere/cube) \
    (material:<material>/none) \
    (offset:<x,y,z>) \
    (ground_block) \
    (include_non_solid) \
    (exclude_liquid) \
    (hide_source_block) \
    (gravity:true/false) \
    (limit:<#>)
```

Example:

```denizen
- blockwave <player.location> duration:1s radius:3 radiusY:1 velocity:0.2 shape:sphere ground_block limit:40
```

Useful behavior:

- `radius` controls X/Z radius
- `radiusY` controls vertical radius
- `radius2` creates an empty inner radius
- `velocity` sets random default velocity
- per-axis velocity options override individual axes
- `noise` controls chance to include collected blocks
- `material:none` or omitted uses original targeted block material
- `ground_block` only targets blocks without a solid block above
- `hide_source_block` sends client-side fake air and restores after duration

---

## 20. Arsen Cinematic Command

Command:

```denizen
arsen_cinematic
```

Purpose:

Plays or cancels a JJKPort cinematic camera sequence for selected viewers.
The current implementation is mainly a bone-follow camera using `bone:<meg_bone>` and `look_bone:<meg_bone>`.

General syntax:

```denizen
arsen_cinematic (play/cancel) \
    (sequence:<list>) \
    (location:<location>) \
    (owner:<player>) \
    (for:<player>|...) \
    (bone:<meg_bone>) \
    (look_bone:<meg_bone>) \
    (hide_player_entity:<true/false>) \
    (camera_mode:per_player/shared) \
    (position_smooth:<#>) \
    (rotation_smooth:<#>) \
    (smoothing_mode:interpolation/lerp) \
    (update_tick:<#>) \
    (save:<name>)
```

Required:

- `for:<player>|...` is required for both play and cancel.
- `play` is the default if action is omitted.
- Playing requires `sequence:<list>`, `location:<location>`, and `owner:<player>`.
- For current bone camera usage, provide both `bone:<meg_bone>` and `look_bone:<meg_bone>`.

Parser aliases:

```text
play / start / run
cancel / stop / end
sequence / seq
location / loc / at
owner / caster
for / players / viewer / viewers
bone / camera_bone
look_bone / lookbone / camera_look_bone / camlook
hide_player_entity / hide_visual / hide_player_visual
camera_mode / mode
position_smooth / pos_smooth
rotation_smooth / rot_smooth
smoothing_mode / smooth_mode
update_tick / update_ticks / tick_update / camera_update_tick
```

Defaults:

```text
camera_mode: per_player
position_smooth: 3
rotation_smooth: 3
smoothing_mode: interpolation
update_tick: 1
hide_player_entity: false unless the flag is provided without a value
```

Save behavior:

```denizen
<entry[result]>              # cinematic UUID
<entry[my_save].result>      # if save:my_save is used
```

Cancel example:

```denizen
- arsen_cinematic cancel for:<player>
```

### Cinematic runtime behavior

When cinematic starts, each viewer:

- leaves vehicle
- becomes invulnerable
- gets flight enabled and starts flying
- gets invisibility
- has inventory hidden by fake inventory helper
- is hidden from other players, and other players are hidden from them
- has flags set:

```denizen
<player.flag[cinematic.active]>
<player.flag[cinematic.id]>
<player.flag[cinematic.owner]>
<player.flag[cinematic.role]>
<player.flag[cinematic.camera_mode]>
<player.flag[cinematic.last_location]>
<player.flag[disable_skills]>
```

If the viewer has a visual body in `game.PlayerEntity` or `game.playerentity`, the visual entity is locked in place and gets flags:

```denizen
<[visual].flag[cinematic.active]>
<[visual].flag[cinematic.id]>
<[visual].flag[cinematic.lock_location]>
<[visual].flag[disable_skills]>
```

When cinematic stops, the camera returns to the player, hidden players are shown again, original gamemode/flight/invulnerability/invisibility are restored, inventory is restored, the player teleports back to original location, and the cinematic flags are cleared.

If `hide_player_entity:true` was used and the visual body exists, the plugin casts MythicSkill `hide_player` on start and `show_player` on stop.

---

## 21. Arsen Cinematic Sequence Format

The sequence is a Denizen list. It can be pipe-based lines or a flat list. Pipe-based lines are recommended.

Supported pipe-based sequence lines:

```text
camera_cframe|<relative_location>|<duration>|<easing_style>|<easing_function>|<start_tick>
camera_shake|<pos/rot/posrot>|<strength>|<frequency>|<duration>|<fade_in>|<fade_out>|<start_tick>
mythicskill|<skill_name>|<tick>
denizen_task|<task_name or task.path>|<tick>
potion_effect|<effect_type>|<amplifier>|<duration>|<tick>
bone_smoothing|<position_smooth>|<rotation_smooth>|<interpolation/lerp>|<tick>
exit_cinematic|<tick>
```

### `camera_cframe`

Format:

```text
camera_cframe|<relative_location>|<duration>|<easing_style>|<easing_function>|<start_tick>
```

This is parsed and still supported by the timeline parser, but current camera pose resolution uses bone-follow mode when `bone` and `look_bone` are provided.

Relative location can be a Denizen location-like input, including simple coordinates or `location[x,y,z,yaw,pitch]`.

Supported easing styles:

```text
linear
instant
quad
back
smooth
sine
```

Supported easing functions:

```text
in
out
inout
in_out
in-out
```

### `camera_shake`

Format:

```text
camera_shake|<pos/rot/posrot>|<strength>|<frequency>|<duration>|<fade_in>|<fade_out>|<start_tick>
```

Modes:

```text
pos
rot
posrot
```

### `mythicskill`

Format:

```text
mythicskill|<skill_name>|<tick>
```

Casts the MythicSkill on the owner visual body if found, otherwise on the owner player.

### `denizen_task`

Format:

```text
denizen_task|<task_name or task.path>|<tick>
```

Runs a Denizen task directly through the plugin bridge instead of dispatching an `ex` command.

Definitions passed:

```text
owner|cinematic_id
```

Example task:

```denizen
My_Cinematic_Task:
    type: task
    definitions: owner|cinematic_id
    script:
    - narrate "cinematic=<[cinematic_id]> owner=<[owner]>"
```

A dotted name like `My_Task.some_path` runs the `some_path` section from `My_Task`.

Queue ID style:

```text
FORCE:arsen_cinematic_<cinematic_id>_<tick>
```

### `potion_effect`

Format:

```text
potion_effect|<effect_type>|<amplifier>|<duration>|<tick>
```

Applies a Bukkit potion effect to each viewer.
Duration can be a Denizen duration like `20t`, `1s`, etc.

Example:

```text
potion_effect|blindness|0|40t|10
```

### `bone_smoothing`

Format:

```text
bone_smoothing|<position_smooth>|<rotation_smooth>|<interpolation/lerp>|<tick>
```

Changes the active cinematic camera smoothing values at the given tick.

### `exit_cinematic`

Format:

```text
exit_cinematic|<tick>
```

Sets an explicit cinematic stop tick.
The implementation stores duration as `exit_tick - 2`, so `exit_cinematic|125` is intended to stop before processing tick 125.

### Basic cinematic example

```denizen
- define model <player.flag[game.PlayerEntity].entity.active_models.get[naoya]>
- define camera_bone <[model].bone[camera]>
- define look_bone <[model].bone[look]>
- define sequence <list[exit_cinematic|125]>
- arsen_cinematic play sequence:<[sequence]> location:<player.location> owner:<player> for:<player> bone:<[camera_bone]> look_bone:<[look_bone]> camera_mode:per_player hide_player_entity:true save:cutscene
```

Example with actions:

```denizen
- define sequence <list[
    exit_cinematic|125|
    mythicskill|some_camera_flash|10|
    denizen_task|My_Cinematic_Task|20|
    potion_effect|blindness|0|20t|30|
    bone_smoothing|1|1|lerp|40
]>
- arsen_cinematic sequence:<[sequence]> location:<player.location> owner:<player> for:<player> bone:<[camera_bone]> look_bone:<[look_bone]> save:cutscene
```

---

## 22. Arsen Bone Debug Command

Command:

```denizen
arsen_bone_debug
```

Purpose:

Broadcasts debug information for a `MEGBoneTag`.
Useful when checking ModelEngine bone position, scale, translation, reflected model bone data, and rendered bone data.

Syntax:

```denizen
arsen_bone_debug <meg_bone> (deep)
```

Behavior:

- parses `meg_bone@<entity_uuid>|<model_id>|<bone_id>`
- accepts Denizen-tag-ish wrapped input like `<[bone]>`
- broadcasts output to online players
- splits long chat lines into safe chunks
- `deep` includes deeper reflected fields and methods

Example:

```denizen
- define bone <player.flag[game.PlayerEntity].entity.active_models.get[naoya].bone[head]>
- arsen_bone_debug <[bone]>
```

Deep example:

```denizen
- arsen_bone_debug <[bone]> deep
```

---

## 23. Current Recommended Domain Asset Test

After plugin rebuild and reload, test asset commands like this:

```denizen
- announce <domain[1].assets>
- domaineffect <domain[1]> add_domain_assets:<list[<[some_entity]>]>
- announce <domain[1].assets>
```

If that works, then test cancel-time access:

```denizen
Test_Domain_Cancel:
    type: task
    definitions: domaintag|reason
    script:
    - announce "Cancel <[domaintag]> reason=<[reason]> assets=<[domaintag].assets>"
    - foreach <[domaintag].assets> as:asset:
        - remove <[asset]>
```

---

## 24. Suggested Next Implementation Steps

1. Test `arsen_cinematic` with real `bone` and `look_bone` from the visual player's ModelEngine model.
2. Test `denizen_task` timeline actions and confirm definitions are `owner|cinematic_id`.
3. Test `potion_effect` actions on viewers.
4. Test `arsen_bone_debug <[bone]> deep` only when you need extra ModelEngine reflection info because it can spam chat.
5. Convert `chars/Shrine.dsc` from server-flag entity storage to domain assets.
6. Consider adding owner helper tags:

```denizen
<[domaintag].owner_player>
<[domaintag].owner_players>
```

7. Avoid large refactors until domain assets and cinematic flow compile and work in-game.

---

## 25. Quick Reference

Reload/debug settings:

```text
/jjkport status
/jjkport debug true
/jjkport reload_after_load true
```

Create open domain:

```denizen
- domaineffect <player.location> owner:<player> duration:1290t radius:30 radiusY:30 open_domain domain_tick_task:Malevolent_Shrine_DomainTick domain_cancel_task:Malevolent_Shrine_DomainCancel save:active_domain
```

Get saved domain:

```denizen
- define domaintag <entry[active_domain].result>
```

Get domain ID:

```denizen
<[domaintag].id>
```

Add assets:

```denizen
- domaineffect <[domaintag]> add_domain_assets:<list[<[entity]>]>
```

Read assets:

```denizen
<[domaintag].assets>
```

Cancel domain:

```denizen
- domaineffect <[domaintag]> cancel
```

Force entity/player into domain:

```denizen
- domaineffect <[domaintag]> enter for:<player>
```

Force entity/player out of domain:

```denizen
- domaineffect <[domaintag]> leave for:<player>
```

Check player domain:

```denizen
<player.inside_domain>
```

Start cinematic:

```denizen
- define model <player.flag[game.PlayerEntity].entity.active_models.get[naoya]>
- define camera_bone <[model].bone[camera]>
- define look_bone <[model].bone[look]>
- define sequence <list[exit_cinematic|125]>
- arsen_cinematic sequence:<[sequence]> location:<player.location> owner:<player> for:<player> bone:<[camera_bone]> look_bone:<[look_bone]> hide_player_entity:true save:cutscene
```

Cancel cinematic:

```denizen
- arsen_cinematic cancel for:<player>
```

Debug bone:

```denizen
- arsen_bone_debug <[camera_bone]>
```
