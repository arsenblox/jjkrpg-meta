package me.arsenplugin.jjkrpg.meta;

/**
 * Denizen meta documentation for JJKRPG / JJKPort.
 * This class is only for Denizen VS Code / SharpDenizenTools meta scanning.
 */
public final class JJKPortMeta {

    private JJKPortMeta() {
    }

    // <--[ObjectType]
    // @name MEGModelTag
    // @prefix meg_model
    // @base ObjectTag
    // @ExampleTagBase player.target.active_models.get[naoya]
    // @ExampleValues <player.target.active_models.get[naoya]>
    // @format
    // meg_model@<base_entity_uuid>|<model_id>
    // @description
    // Represents one active ModelEngine model attached to a Bukkit entity.
    // Returned by <@link tag EntityTag.active_models>.
    // -->

    // <--[ObjectType]
    // @name MEGBoneTag
    // @prefix meg_bone
    // @base ObjectTag
    // @ExampleTagBase player.target.active_models.get[naoya].bone[head]
    // @ExampleValues <player.target.active_models.get[naoya].bone[head]>
    // @format
    // meg_bone@<base_entity_uuid>|<model_id>|<bone_id>
    // @description
    // Represents one ModelEngine bone from an active model.
    // Returned by <@link tag MEGModelTag.bones> and <@link tag MEGModelTag.bone>.
    // -->

    // <--[ObjectType]
    // @name MEGParticleTag
    // @prefix meg_particle
    // @base ObjectTag
    // @ExampleTagBase entry[vfx].result
    // @ExampleValues <entry[vfx].result>
    // @format
    // meg_particle@<id>
    // @description
    // Represents one packet-only fake item_display particle created by <@link command particleemit>.
    // -->

    // <--[ObjectType]
    // @name DomainTag
    // @prefix domain
    // @base ObjectTag
    // @ExampleTagBase domain[1]
    // @ExampleValues <domain[1]>
    // @format
    // domain@<id>
    // @description
    // Represents one active JJKPort domain instance created by <@link command domaineffect>.
    // Domains can be BARRIER or OPEN mode.
    // BARRIER domains create terrain, containment, combat isolation, and owner-validity destruction.
    // OPEN domains are radius-only domains with no shell, no floor, no forced teleport, and no containment.
    // -->

    // <--[tag]
    // @attribute <EntityTag.active_models>
    // @returns MapTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns a map of active ModelEngine models attached to this entity.
    // The map can be accessed by model ID, like <entity.active_models.get[naoya]>, or numeric index, like <entity.active_models.get[1]>.
    // Each map value is a MEGModelTag.
    // -->

    // <--[tag]
    // @attribute <MEGModelTag.id>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the ModelEngine model ID.
    // -->

    // <--[tag]
    // @attribute <MEGModelTag.bones>
    // @returns ListTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns a list of MEGBoneTag objects available on this active model.
    // -->

    // <--[tag]
    // @attribute <MEGModelTag.bone[<bone_id>]>
    // @returns MEGBoneTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns a single ModelEngine bone from this active model by bone ID.
    // -->

    // <--[tag]
    // @attribute <MEGModelTag.has_bone[<bone_id>]>
    // @returns ElementTag(Boolean)
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns whether this active model has the specified bone ID.
    // -->

    // <--[tag]
    // @attribute <MEGModelTag.bone_count>
    // @returns ElementTag(Number)
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the number of available bones on this active model.
    // -->

    // <--[tag]
    // @attribute <MEGBoneTag.id>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the bone ID.
    // -->

    // <--[tag]
    // @attribute <MEGBoneTag.model_id>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the ModelEngine model ID that owns this bone.
    // -->

    // <--[tag]
    // @attribute <MEGBoneTag.location>
    // @returns LocationTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the current world location of this ModelEngine bone.
    // -->

    // <--[tag]
    // @attribute <MEGBoneTag.position>
    // @returns ListTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the bone position vector as x, y, z.
    // In to_owner megattach usage, this vector is treated as a neutral local offset reference before being applied relative to the owner.
    // -->

    // <--[tag]
    // @attribute <MEGBoneTag.scale>
    // @returns ListTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the bone scale vector as x, y, z.
    // -->

    // <--[tag]
    // @attribute <MEGBoneTag.translation>
    // @returns ListTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns the bone translation vector as x, y, z.
    // -->

    // <--[tag]
    // @attribute <MEGBoneTag.debug_pose>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group modelengine
    // @description
    // Returns debug pose data for the bone, when available.
    // -->

    // <--[tag]
    // @attribute <MEGParticleTag.id>
    // @returns ElementTag(Number)
    // @Plugin JJKPort
    // @group particle
    // @description
    // Returns the packet particle ID.
    // -->

    // <--[tag]
    // @attribute <MEGParticleTag.exists>
    // @returns ElementTag(Boolean)
    // @Plugin JJKPort
    // @group particle
    // @description
    // Returns whether this packet particle still exists.
    // -->

    // <--[tag]
    // @attribute <MEGParticleTag.frame>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group particle
    // @description
    // Returns the current item_model frame ID for this packet particle.
    // -->

    // <--[tag]
    // @attribute <MEGParticleTag.location>
    // @returns LocationTag
    // @Plugin JJKPort
    // @group particle
    // @description
    // Returns the current location of this packet particle.
    // -->

    // <--[tag]
    // @attribute <DomainTag.id>
    // @returns ElementTag(Number)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns the domain ID.
    // -->

    // <--[tag]
    // @attribute <DomainTag.exists>
    // @returns ElementTag(Boolean)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns whether the domain still exists.
    // -->

    // <--[tag]
    // @attribute <DomainTag.state>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns OPENING while the domain opening phase is running, or ACTIVE after opening finishes.
    // Canceled domains are removed instead of returning CANCELED.
    // -->

    // <--[tag]
    // @attribute <DomainTag.mode>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns BARRIER or OPEN.
    // -->

    // <--[tag]
    // @attribute <DomainTag.location>
    // @returns LocationTag
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns the domain center location.
    // -->

    // <--[tag]
    // @attribute <DomainTag.owner>
    // @returns ElementTag
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns the first owner UUID for compatibility. For multi-owner domains, use <DomainTag.owners>.
    // -->

    // <--[tag]
    // @attribute <DomainTag.owners>
    // @returns ListTag
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns all owner UUIDs for the domain.
    // -->

    // <--[tag]
    // @attribute <DomainTag.radius>
    // @returns ElementTag(Decimal)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns the X/Z domain radius.
    // -->

    // <--[tag]
    // @attribute <DomainTag.radiusY>
    // @returns ElementTag(Decimal)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns the vertical domain radius.
    // -->

    // <--[tag]
    // @attribute <DomainTag.radius2>
    // @returns ElementTag(Decimal)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns the inner hollow radius.
    // -->

    // <--[tag]
    // @attribute <DomainTag.affected_players>
    // @returns ListTag(PlayerTag)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns players affected by the domain.
    // In OPEN mode, this updates dynamically as players enter and leave the radius.
    // -->

    // <--[tag]
    // @attribute <DomainTag.affected_players_opening>
    // @returns ListTag(PlayerTag)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns players captured during the opening snapshot.
    // -->

    // <--[tag]
    // @attribute <DomainTag.affected_mythicmobs>
    // @returns ListTag(EntityTag)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns non-player entities affected by the domain.
    // In OPEN mode, this updates dynamically as entities enter and leave the radius.
    // -->

    // <--[tag]
    // @attribute <DomainTag.affected_mythicmobs_opening>
    // @returns ListTag(EntityTag)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns non-player entities captured during the opening snapshot.
    // -->

    // <--[tag]
    // @attribute <DomainTag.assets>
    // @returns ListTag(EntityTag)
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns valid entities manually registered as assets of this domain.
    // Invalid or removed entities are filtered out by the plugin.
    // Assets remain available during domain_cancel_task so scripts can animate or remove them before the domain is discarded.
    // -->

    // <--[tag]
    // @attribute <DomainTag.random_location_inside_domain_radius[<radius>]>
    // @returns LocationTag
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns a random location inside the domain radius.
    // -->

    // <--[tag]
    // @attribute <PlayerTag.inside_domain>
    // @returns DomainTag
    // @Plugin JJKPort
    // @group domain
    // @description
    // Returns the active domain containing this player or their visual game.PlayerEntity body.
    // -->

    // <--[command]
    // @Name Particleemit
    // @Syntax particleemit (<meg_particle>) (entity:<meg_particle>) (at:<location>) (for:<player>|...) (billboard:<billboard>) (duration:<duration>) (save:<name>) (frame:<frame>|...) (frame_start:<#>) (frame_end:<#>) (frame_time:<duration>) (scale:<#>) (scale_bone:<meg_bone>) (smooth_scale_bone:<#>) (item:<material>) (cancel)
    // @Required 1
    // @Maximum 15
    // @Short Creates or cancels a packet-only item_display particle.
    // @Group particle
    // @Description
    // Creates a fake packet-only item_display entity for selected viewers and animates its item_model component.
    // This command does not spawn a real Bukkit entity.
    //
    // Use at:<location> to choose spawn location.
    // Use for:<player>|... to choose viewers.
    // Use frame:<base> with frame_start:<#> and frame_end:<#> to generate numbered item_model frames.
    // Use frame:<list[...]> or a pipe-separated list to specify custom frame names.
    // Use frame_time:<duration> to control frame duration. Minimum practical update is 1 tick.
    // Use duration:<duration> to control how long the particle lasts.
    // Use scale:<#> to set the base display scale.
    // Use scale_bone:<meg_bone> to multiply the base scale by a ModelEngine bone's scale every tick.
    // Use smooth_scale_bone:<#> to set display transform interpolation for scale_bone updates. This can only be used if scale_bone is defined.
    // Use save:<name> to save the result as <entry[name].result> and <entry[name].particle>.
    // Use cancel, stop, or remove with a MEGParticleTag to destroy the packet particle.
    // Packet particles are fullbright by default.
    //
    // @Usage
    // - particleemit at:<player.eye_location.forward[0.2]> for:<server.online_players> frame:jjk:red_startup_vfx/redstartup frame_start:1 frame_end:10 frame_time:1t duration:10t billboard:CENTER scale:6 save:red_vfx
    //
    // @Usage
    // - particleemit entity:<entry[red_vfx].result> cancel
    // -->

    // <--[command]
    // @Name Megattach
    // @Syntax megattach [<entity>|...]|[<meg_particle>|...] (to:<meg_bone>/<entity>) (to_owner:<entity>) (cancel) (pivot) (relative) (eye_location/head) (scale) (rotation) (bone_rotation) (smooth:<#>) (position_smooth:<#>) (rotation_smooth:<#>) (scale_smooth:<#>) (offset:<x,y,z>|<x,y,z,yaw,pitch>) (offset_yaw:<#>) (offset_pitch:<#>) (yaw_offset:<#>) (pitch_offset:<#>)
    // @Required 1
    // @Maximum 20
    // @Short Attaches real Bukkit entities or packet particles to a ModelEngine bone or entity target.
    // @Group entity
    // @Description
    // Attaches one or more real Bukkit entities or MEGParticleTag packet particles to a target by updating them every tick.
    // Use to:<meg_bone> for a bone target, to:<entity> for a normal entity target, or to_owner:<entity> with to:<meg_bone> for socket-offset behavior.
    // Use cancel, stop, or detach to remove an existing attachment.
    //
    // @Usage
    // - define bone <player.target.active_models.get[naoya].bone[head]>
    // - megattach <server.flag[eye_display]> to:<[bone]>
    //
    // @Usage
    // - megattach <entry[vfx].result> to:<[bone]> to_owner:<player> eye_location relative scale rotation smooth:1 position_smooth:1 rotation_smooth:1 scale_smooth:0
    // -->

    // <--[command]
    // @Name Blockwave
    // @Syntax blockwave <location> (for:<player>|...) (duration:<duration>) (radius:<#>) (radiusY:<#>) (radius2:<#>) (velocity:<#>) (velocityX:<list[#|#]>|<#>) (velocityY:<list[#|#]>|<#>) (velocityZ:<list[#|#]>|<#>) (noise:<#>) (shape:sphere/cube) (material:<material>/none) (offset:<x,y,z>) (ground_block) (include_non_solid) (exclude_liquid) (hide_source_block) (gravity:true/false) (limit:<#>)
    // @Required 1
    // @Maximum 21
    // @Short Creates a packet-only fake falling-block wave visual.
    // @Group particle
    // @Description
    // Spawns fake packet-only FallingBlock entities for selected viewers around a target location.
    // This command does not spawn real Bukkit falling blocks and does not place blocks when they land.
    // radius:<#> controls X/Z radius. radiusY:<#> overrides vertical radius.
    // radius2:<#> creates an inner empty radius that is skipped.
    // velocity:<#> sets default random velocity. Axis velocity options override individual axes.
    // noise:<#> controls chance to include collected blocks.
    // material:none or omitted uses the original targeted block material.
    // ground_block only targets blocks that do not have a solid block above them.
    // hide_source_block sends client-side fake air for targeted source blocks and restores them after duration.
    //
    // @Usage
    // - blockwave <player.location> duration:1s radius:3 radiusY:1 velocity:0.2 shape:sphere ground_block limit:40
    // -->

    // <--[command]
    // @Name Domaineffect
    // @Syntax domaineffect <location|domain|entity> (cancel) (enter/leave) (owner:<player/entity>) (for:<player>|...) (duration:<duration>) (domain_opening:<duration>) (opening_animation:<down_to_up/back_to_front>) (radius:<#>) (radius2:<#>) (radiusY:<#>) (shape:sphere/cube) (material:<material>) (floor_material:<material>) (below_floor_air_depth:<#>) (open_domain/non_barrier/barrier:false) (domain_start_task:<task>) (domain_after_opening_task:<task>) (domain_tick_task:<task>) (domain_cancel_task:<task>) (domain_assets:<entity>|...) (add_domain_assets:<entity>|...) (remove_domain_assets:<entity>|...) (clear_domain_assets) (hide_inside) (save:<name>)
    // @Required 1
    // @Maximum 26
    // @Short Creates, updates, or cancels a JJKPort domain effect.
    // @Group domain
    // @Description
    // Creates a domain around a location, entity, or player.
    // BARRIER domains create opening terrain, a final arena/floor, containment, combat isolation, owner validity checks, and restoration.
    // OPEN domains are radius-only domains with no shell, no floor, no forced teleport, no containment, and dynamic affected lists.
    // If a barrier domain is opened inside another barrier domain that is still OPENING, the owner is merged into that opening domain.
    // If a barrier domain is opened inside or near an ACTIVE barrier domain, creation is canceled.
    //
    // domain_start_task:<task> runs when created with definitions: domaintag.
    // domain_after_opening_task:<task> runs when opening completes with definitions: domaintag.
    // domain_tick_task:<task> runs every tick after opening with definitions: domaintag.
    // domain_cancel_task:<task> runs on duration, command cancel, owner invalid, or plugin shutdown with definitions: domaintag|reason.
    // Cancel reasons include duration, command, owners_invalid, and plugin_shutdown.
    //
    // domain_assets:<entity>|... or assets:<entity>|... replaces all assets.
    // add_domain_assets:<entity>|... or add_assets:<entity>|... adds assets.
    // remove_domain_assets:<entity>|... or remove_assets:<entity>|... removes assets.
    // clear_domain_assets or clear_assets clears all assets.
    // Assets are manually managed and are not automatically removed by the plugin on cancel.
    // Assets remain queryable during domain_cancel_task so scripts can animate or remove them before cleanup.
    //
    // @Usage
    // Create a barrier domain and save it.
    // - domaineffect <player.location> owner:<player> duration:20s domain_opening:3s radius:15 material:obsidian floor_material:barrier save:my_domain
    //
    // @Usage
    // Create an open / non-barrier domain for radius-based effects.
    // - domaineffect <player.location> owner:<player> duration:1290t radius:30 radiusY:30 open_domain domain_tick_task:Malevolent_Shrine_DomainTick domain_cancel_task:Malevolent_Shrine_DomainCancel save:active_domain
    //
    // @Usage
    // Add spawned entities as domain assets.
    // - domaineffect <[domaintag]> add_domain_assets:<list[<[shrine_entity]>|<[dismantle_entity]>]>
    //
    // @Usage
    // Remove all assets during cancel.
    // - foreach <[domaintag].assets> as:asset:
    //   - remove <[asset]>
    // -->
}
