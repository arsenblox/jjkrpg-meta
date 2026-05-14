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

---

## 3. Denizen Visual Body Rule

In `JJKRPG-ATOX-Denizen`, real players are hidden for gameplay.

A player's visual/game body is stored in:

```denizen
<player.flag[game.PlayerEntity]>
```

The visual entity also has Mythic variable:

```text
ownername=<player.name>
```

Domain logic should treat the visual entity as the player's real combat body when available.

That means:

- teleport the visual entity, not only the real player
- containment checks should use the visual entity location
- combat/domain membership should consider the visual body
- `<PlayerTag.inside_domain>` should check the visual entity location when possible

The plugin resolver also checks lowercase fallback:

```text
game.playerentity
```

---

## 4. Domain System

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

## 5. Domain Modes

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

Aliases currently intended for open mode:

```text
open_domain
opendomain
non_barrier
nonbarrier
no_barrier
nobarrier
barrier:false
```

Avoid relying on ambiguous `open` as an alias because it previously conflicted with `domain_opening` parsing.

---

## 6. Domain Lifecycle

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

## 7. Barrier Domain Terrain Behavior

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

## 8. Domain Command Syntax

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

---

## 9. Domain Callbacks

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

## 10. DomainTag Tags

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

## 11. Domain Assets

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

Known implementation note:

- `DomainInstance`, `DomainManager`, and `DomainTag` have domain asset support.
- If `domaineffect <[domaintag]> add_domain_assets:...` reports unhandled arguments, check `DomainEffectCommand.parseArgs()` because execute-side helpers exist but parsing may still need to be completed.

---

## 12. Malevolent Shrine Current Integration

Current script:

```text
chars/Shrine.dsc
```

Current domain creation pattern:

```denizen
- domaineffect <[domain_location]> owner:<[player]> duration:1290t radius:30 radiusY:30 open_domain domain_tick_task:Malevolent_Shrine_DomainTick domain_cancel_task:Malevolent_Shrine_DomainCancel save:malevolent_shrine_domain
```

Current script still uses server flags such as:

```denizen
server.flag[game.malevolent_shrine.<domain_id>.owner]
server.flag[game.malevolent_shrine.<domain_id>.center]
server.flag[game.malevolent_shrine.<domain_id>.radius]
server.flag[game.malevolent_shrine.<domain_id>.shrine]
server.flag[game.malevolent_shrine.<domain_id>.dismantle]
server.flag[game.malevolent_shrine.<domain_id>.dismantle_big]
server.flag[game.malevolent_shrine.<domain_id>.tick]
server.flag[game.malevolent_shrine.<domain_id>.setup]
```

Long-term target:

- keep player-specific gameplay flags only where useful
- replace shrine/dismantle asset storage with domain assets
- eventually avoid `game.malevolent_shrine.<domain_id>.*` server flags for spawned entities

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

## 13. Combat Isolation

Barrier-only combat isolation means:

- affected members inside the same barrier domain can fight each other
- outside entities should not damage inside members
- inside members should not damage outside entities
- open domains do not apply combat isolation

Visual-player bodies should be treated as player combat bodies when resolving membership.

---

## 14. Owner Handling

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

## 15. ModelEngine Bridge

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

## 16. MEGAttach Command

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

## 17. ParticleEmit Command

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

## 18. BlockWave Command

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

## 19. Current Recommended Domain Asset Test

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

## 20. Suggested Next Implementation Steps

1. Confirm `domaineffect` parses these arguments:

```text
domain_assets:
add_domain_assets:
remove_domain_assets:
clear_domain_assets
```

2. If not parsed, patch `DomainEffectCommand.parseArgs()`.

3. Rebuild plugin.

4. Test `<domain[1].assets>` in Denizen.

5. Convert `chars/Shrine.dsc` from server-flag entity storage to domain assets.

6. Consider adding owner helper tags:

```denizen
<[domaintag].owner_player>
<[domaintag].owner_players>
```

7. Avoid large refactors until domain assets compile and work in-game.

---

## 21. Quick Reference

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

