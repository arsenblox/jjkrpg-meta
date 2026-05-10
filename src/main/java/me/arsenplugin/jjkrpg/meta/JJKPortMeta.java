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
    // @ExampleForReturns
    // - narrate "Model ID: %VALUE%"
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
    // @ExampleForReturns
    // - narrate "Bone location: %VALUE%"
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
    // Use frame:<base> with frame_start:<#> and frame_end:<#> to generate numbered item_model frames, for example jjk:vfx/wind1 through jjk:vfx/wind10.
    // Use frame:<list[...]> or a pipe-separated list to specify custom frame names.
    // Use frame_time:<duration> to control frame duration. Minimum practical update is 1 tick.
    // Use duration:<duration> to control how long the particle lasts.
    // Use scale:<#> to set the base display scale.
    // Use scale_bone:<meg_bone> to multiply the base scale by a ModelEngine bone's scale every tick.
    // Use smooth_scale_bone:<#> to set display transform interpolation for scale_bone updates. This can only be used if scale_bone is defined.
    // Use billboard:CENTER or another Display billboard mode to control facing.
    // Use save:<name> to save the result as <entry[name].result> and <entry[name].particle>.
    // Use cancel, stop, or remove with a MEGParticleTag to destroy the packet particle.
    // Packet particles are fullbright by default.
    //
    // @Tags
    // <MEGParticleTag.id>
    // <MEGParticleTag.exists>
    // <MEGParticleTag.frame>
    // <MEGParticleTag.location>
    // <MEGBoneTag.scale>
    //
    // @Usage
    // Spawn an animated startup VFX and save it.
    // - particleemit at:<player.eye_location.forward[0.2]> for:<server.online_players> frame:jjk:red_startup_vfx/redstartup frame_start:1 frame_end:10 frame_time:1t duration:10t billboard:CENTER scale:6 save:red_vfx
    //
    // @Usage
    // Cancel a saved particle.
    // - particleemit entity:<entry[red_vfx].result> cancel
    //
    // @Usage
    // Spawn a particle whose scale follows a ModelEngine bone scale.
    // - define bone <player.target.active_models.get[naoya].bone[head]>
    // - particleemit at:<[bone].location> for:<server.online_players> frame:jjk:blue_startup_vfx/bluestartup frame_start:1 frame_end:10 frame_time:1t duration:5s billboard:CENTER scale:5 scale_bone:<[bone]> smooth_scale_bone:1 save:blue_vfx
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
    // The target can be a ModelEngine bone via to:<meg_bone> or a normal entity via to:<entity>.
    // Use cancel, stop, or detach to remove an existing attachment.
    //
    // Bone target options:
    // Use pivot to rotate the manual offset around the bone yaw.
    // Use scale to multiply a real Display entity's original scale by the bone scale.
    // Use rotation to copy the target yaw and pitch to the attached entity or packet particle.
    // Use bone_rotation to allow ModelEngine/model/bone rotation to affect the bone socket yaw/pitch behavior.
    //
    // to_owner socket mode:
    // Use to_owner:<entity> together with to:<meg_bone> to use the owner entity location as the origin while using the bone only as a socket offset reference.
    // If eye_location/head/eyes is specified, the owner eye/head location is used as the origin.
    // If relative is specified, the neutral bone offset is applied relative to the to_owner yaw/pitch, similar to <owner.eye_location.relative[x,y,z]>.
    // Without bone_rotation, the ModelEngine model entity rotation is ignored for the socket offset.
    // With bone_rotation, ModelEngine/model/bone rotation is included.
    //
    // Entity target options:
    // Use eye_location, eyelocation, head, or eyes to use the target LivingEntity eye location instead of base location.
    // Use relative to rotate the offset with the target entity's yaw and pitch.
    // Use yaw_offset:<#> and pitch_offset:<#> to offset the relative-position calculation.
    //
    // General options:
    // Use offset:<x,y,z> to add a position offset.
    // Use offset:<x,y,z,yaw,pitch> to include final yaw/pitch offsets inside the offset value.
    // Use offset_yaw:<#> and offset_pitch:<#> to add final rotation offsets. These imply rotation.
    //
    // Smoothing options:
    // smooth:<#> applies the same fallback smoothing value to position, rotation, and scale.
    // position_smooth:<#> controls Display teleport smoothing.
    // rotation_smooth:<#> is reserved for rotation smoothing behavior.
    // scale_smooth:<#> controls real Display scale smoothing when using bone scale.
    //
    // @Tags
    // <EntityTag.active_models>
    // <MEGModelTag.bone>
    // <MEGBoneTag.location>
    // <MEGBoneTag.scale>
    // <MEGParticleTag.location>
    //
    // @Usage
    // Attach one real item_display to a ModelEngine bone.
    // - define bone <player.target.active_models.get[naoya].bone[head]>
    // - megattach <server.flag[eye_display]> to:<[bone]>
    //
    // @Usage
    // Attach a packet particle to a ModelEngine bone.
    // - particleemit at:<player.location> for:<server.online_players> frame:jjk:red_startup_vfx/redstartup frame_start:1 frame_end:10 duration:5s save:vfx
    // - define bone <player.target.active_models.get[naoya].bone[head]>
    // - megattach <entry[vfx].result> to:<[bone]> pivot rotation offset:0,0.05,0.1
    //
    // @Usage
    // Attach a packet particle to a player's eye/head location.
    // - particleemit at:<player.eye_location> for:<server.online_players> frame:jjk:blue_startup_vfx/bluestartup frame_start:1 frame_end:10 duration:5s save:vfx
    // - megattach <entry[vfx].result> to:<player> eye_location relative offset:0,0,1
    //
    // @Usage
    // Use a ModelEngine bone as a VFX socket offset but move from the player eye location.
    // - define bone <[player_entity].active_models.get[1].bone[hollow_blue]>
    // - megattach <entry[vfx].result> to:<[bone]> to_owner:<player> eye_location relative scale rotation smooth:1 position_smooth:1 rotation_smooth:1 scale_smooth:0
    //
    // @Usage
    // Include ModelEngine model/bone rotation in to_owner socket mode.
    // - megattach <entry[vfx].result> to:<[bone]> to_owner:<player> eye_location relative bone_rotation
    //
    // @Usage
    // Cancel an attachment.
    // - megattach <entry[vfx].result> cancel
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
    //
    // The command first collects blocks within radius/radiusY, filters them, then sends fake falling block visuals at those block locations.
    // If for:<player>|... is omitted, all online players in the same world are viewers.
    //
    // radius:<#> controls X/Z radius. radiusY:<#> overrides vertical radius. If radiusY is omitted, it uses radius.
    // radius2:<#> creates an inner empty radius that is skipped; radius2 is also scaled vertically by radiusY.
    // shape:sphere uses an ellipsoid when radiusY differs from radius. shape:cube uses a box.
    //
    // velocity:<#> sets default random velocity: X/Z from -velocity to velocity, and Y from 0 to velocity.
    // velocityX, velocityY, and velocityZ override individual axes.
    // Axis velocity can be a fixed number or a list/range like <list[-4|4]>.
    //
    // noise:<#> controls chance to include collected blocks, where 1 is 100% and 0.9 skips about 10% randomly.
    // material:none or omitted uses the original targeted block material. material:<material> overrides the visual material.
    // offset:<x,y,z> offsets the fake falling block spawn position.
    // ground_block only targets blocks that do not have a solid block above them.
    // include_non_solid allows non-solid blocks to be used; by default non-solid blocks are skipped.
    // exclude_liquid skips water and lava.
    // hide_source_block sends client-side fake air for targeted source blocks and restores them after duration.
    // gravity:true/false controls fake falling block gravity metadata. Default is true.
    // limit:<#> caps the amount of fake falling blocks spawned. 0 means no cap.
    //
    // @Usage
    // Spawn a small upward block wave around the player.
    // - blockwave <player.location> duration:1s radius:3 radiusY:1 velocity:0.2 shape:sphere ground_block limit:40
    //
    // @Usage
    // Spawn blocks straight upward using axis velocity overrides.
    // - blockwave <player.location> duration:2s radius:2 velocity:0 velocityX:0 velocityY:5 velocityZ:0 ground_block
    //
    // @Usage
    // Spawn a hollow cube wave and hide source blocks client-side.
    // - blockwave <player.location> duration:20t radius:5 radiusY:2 radius2:2 shape:cube velocity:0.3 hide_source_block limit:80
    //
    // @Usage
    // Spawn fake falling blocks as a specific material.
    // - blockwave <player.location> duration:1s radius:4 material:stone velocityY:<list[0.2|0.8]> ground_block
    // -->
}
