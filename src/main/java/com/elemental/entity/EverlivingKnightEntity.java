package com.elemental.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;

public class EverlivingKnightEntity extends HostileEntity implements GeoEntity {
    // ==================== CONSTANTS ====================
    public static final int MAX_HEALTH = 800;
    public static final float BASE_ATTACK_DAMAGE = 14.0f;
    public static final int BASE_ARMOR = 16;
    public static final float BASE_ARMOR_TOUGHNESS = 6.0f;
    public static final double BASE_KNOCKBACK_RESISTANCE = 0.9;
    public static final double BASE_MOVEMENT_SPEED = 0.25;

    public static final float REGEN_BASE_RATE = 0.02f; // 2% per second
    public static final int MAX_CORE_INTEGRITY = 100;
    public static final int CORE_DAMAGE_PER_THORN_CAGE = 20;

    // Regeneration states
    public enum RegenerationState {
        NORMAL(1.0f),
        THORN_CAGE(0.4f),
        FROSTBURST(0.0f),
        CORE_BROKEN(0.0f);

        public final float multiplier;
        RegenerationState(float multiplier) { this.multiplier = multiplier; }
    }

    // Boss states for AI priority
    public enum SolvaneState {
        DEATH,
        PHASE_TRANSITION,
        CORE_EXPOSED,
        STAGGERED,
        FROST_FROZEN,
        THORN_CAGED,
        ATTACK,
        CHASE,
        PATROL
    }

    // Phases
    public enum Phase {
        PHASE_1(1.0f, 0.7f),
        PHASE_2(0.7f, 0.35f),
        PHASE_3(0.35f, 0.0f);

        public final float healthStart;
        public final float healthEnd;
        Phase(float start, float end) {
            this.healthStart = start;
            this.healthEnd = end;
        }
    }

    // Combat ability constants
    private static final int SLASH_WINDUP = 12;
    private static final int SLASH_ACTIVE = 3;
    private static final int SLASH_RECOVERY = 20;
    private static final int SLASH_COOLDOWN = 30;
    private static final float SLASH_DAMAGE = 16.0f;
    private static final float SLASH_RANGE = 4.0f;

    private static final int SHIELD_BASH_COOLDOWN = 80;
    private static final int SHIELD_BASH_STUN = 10;
    private static final float SHIELD_BASH_DAMAGE = 10.0f;

    private static final int CHARGE_WARNING = 18;
    private static final int CHARGE_COOLDOWN = 120;
    private static final int CHARGE_RECOVERY = 30;
    private static final float CHARGE_DAMAGE = 20.0f;
    private static final float CHARGE_DISTANCE = 9.0f;

    private static final int CORE_PULSE_WARNING = 20;
    private static final int CORE_PULSE_COOLDOWN = 100;
    private static final float CORE_PULSE_DAMAGE = 10.0f;
    private static final float CORE_PULSE_RADIUS = 6.0f;

    // Frostburst
    private static final int FROSTBURST_DURATION = 300;
    private static final int FROSTBURST_RADIUS = 5;

    // Thorn Cage
    private static final int THORN_CAGE_ROOT_DURATION = 100;

    // Arena boundary
    private static final int ARENA_RADIUS = 14; // 29x29 arena = radius 14 from center
    private BlockPos arenaCenter;

    // Enrage timer (Phase 3)
    private static final int ENRAGE_TIMER = 600; // 30 seconds at 20 tps

    // ==================== DATA TRACKERS ====================
    private static final TrackedData<Integer> DATA_PHASE = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DATA_CORE_INTEGRITY = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> DATA_CORE_BROKEN = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> DATA_REGEN_FROZEN_TICKS = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DATA_REGEN_MULTIPLIER = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> DATA_REGEN_STATE = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> DATA_STAGGERED = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> DATA_ENRAGE_TIMER = DataTracker.registerData(EverlivingKnightEntity.class, TrackedDataHandlerRegistry.INTEGER);

    // ==================== INSTANCE FIELDS ====================
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ServerBossBar bossBar;

    // Ability timers
    private int slashCooldown = 0;
    private int shieldBashCooldown = 0;
    private int chargeCooldown = 0;
    private int corePulseCooldown = 0;
    private int attackWindup = 0;
    private int attackActive = 0;
    private int attackRecovery = 0;
    private int currentAttackType = 0; // 0=none, 1=slash, 2=shield bash, 3=charge, 4=core pulse
    private int chargeWarningTicks = 0;
    private Vec3d chargeTargetPos;
    private int corePulseWarningTicks = 0;

    // Phase tracking
    private Phase currentPhase = Phase.PHASE_1;
    private boolean phaseTransitionTriggered = false;

    // Regeneration
    private RegenerationState currentRegenState = RegenerationState.NORMAL;
    private int frostFrozenTicks = 0;
    private int thornCagedTicks = 0;

    // Healing flames (Phase 2)
    private int healingFlame1EntityId = -1;
    private int healingFlame2EntityId = -1;
    private int healingFlameSpawnCooldown = 0;

    // Damage prevention
    private long lastDamageTick = -1;
    private UUID lastDamageSourceUUID;

    // Animation triggers (for client sync)
    private String pendingAnimation = null;

    public EverlivingKnightEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5000;
        this.bossBar = new ServerBossBar(Text.literal("Sir Solvane, the Everliving Knight"), BossBar.Color.RED, BossBar.Style.PROGRESS);
        this.setStepHeight(1.0f);
    }

    // ==================== INITIALIZATION ====================
    public static DefaultAttributeContainer.Builder createKnightAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, MAX_HEALTH)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, BASE_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_ARMOR, BASE_ARMOR)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, BASE_ARMOR_TOUGHNESS)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, BASE_KNOCKBACK_RESISTANCE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, BASE_MOVEMENT_SPEED)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.0);
    }

    @Override
    protected void initGoals() {
        // Priority order matches SolvaneState priority
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new KnightMeleeAttackGoal());
        this.goalSelector.add(3, new KnightChargeGoal());
        this.goalSelector.add(4, new KnightShieldBashGoal());
        this.goalSelector.add(5, new KnightCorePulseGoal());
        this.goalSelector.add(6, new WanderNearTargetGoal(this, 1.0, 32.0f));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.8, 1));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 24.0f));
        this.goalSelector.add(9, new LookAroundGoal(this));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new RevengeGoal(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_PHASE, Phase.PHASE_1.ordinal());
        this.dataTracker.startTracking(DATA_CORE_INTEGRITY, MAX_CORE_INTEGRITY);
        this.dataTracker.startTracking(DATA_CORE_BROKEN, false);
        this.dataTracker.startTracking(DATA_REGEN_FROZEN_TICKS, 0);
        this.dataTracker.startTracking(DATA_REGEN_MULTIPLIER, 1.0f);
        this.dataTracker.startTracking(DATA_REGEN_STATE, RegenerationState.NORMAL.ordinal());
        this.dataTracker.startTracking(DATA_STAGGERED, false);
        this.dataTracker.startTracking(DATA_ENRAGE_TIMER, 0);
    }

    // ==================== REGENERATION SYSTEM ====================
    private void updateRegeneration() {
        if (this.getWorld().isClient()) return;

        // Handle frost frozen ticks
        if (frostFrozenTicks > 0) {
            frostFrozenTicks--;
            this.dataTracker.set(DATA_REGEN_FROZEN_TICKS, frostFrozenTicks);
            if (frostFrozenTicks == 0 && !isCoreBroken()) {
                // Frost ended, revert to appropriate state
                if (thornCagedTicks > 0) {
                    setRegenerationState(RegenerationState.THORN_CAGE);
                } else {
                    setRegenerationState(RegenerationState.NORMAL);
                }
            }
        }

        // Handle thorn caged ticks
        if (thornCagedTicks > 0) {
            thornCagedTicks--;
            if (thornCagedTicks == 0 && frostFrozenTicks == 0 && !isCoreBroken()) {
                setRegenerationState(RegenerationState.NORMAL);
            }
        }

        // Apply regeneration if not frozen and core not broken
        if (frostFrozenTicks == 0 && !isCoreBroken() && this.getHealth() < this.getMaxHealth()) {
            float maxHealth = this.getMaxHealth();
            float healAmount = REGEN_BASE_RATE * maxHealth * getRegenerationMultiplier() / 20.0f;
            this.heal(healAmount);
        }

        // Update boss bar with regen status
        updateBossBar();
    }

    private void setRegenerationState(RegenerationState state) {
        this.currentRegenState = state;
        this.dataTracker.set(DATA_REGEN_STATE, state.ordinal());
        this.dataTracker.set(DATA_REGEN_MULTIPLIER, state.multiplier);
    }

    public float getRegenerationMultiplier() {
        return this.dataTracker.get(DATA_REGEN_MULTIPLIER);
    }

    public void applyFrostburst() {
        if (this.getWorld().isClient()) return;
        frostFrozenTicks = Math.max(frostFrozenTicks, FROSTBURST_DURATION);
        this.dataTracker.set(DATA_REGEN_FROZEN_TICKS, frostFrozenTicks);
        setRegenerationState(RegenerationState.FROSTBURST);
        triggerAnimation("frostburst_reaction");
        syncBossState();
    }

    public void applyThornCage() {
        if (this.getWorld().isClient()) return;

        // Reduce core integrity
        int currentIntegrity = this.dataTracker.get(DATA_CORE_INTEGRITY);
        int newIntegrity = Math.max(0, currentIntegrity - CORE_DAMAGE_PER_THORN_CAGE);
        this.dataTracker.set(DATA_CORE_INTEGRITY, newIntegrity);

        // Apply regeneration slow
        thornCagedTicks = THORN_CAGE_ROOT_DURATION;
        setRegenerationState(RegenerationState.THORN_CAGE);
        this.dataTracker.set(DATA_STAGGERED, true);

        triggerAnimation("thorn_cage_reaction");
        syncBossState();

        // Check if core breaks
        if (newIntegrity <= 0 && !isCoreBroken()) {
            breakCore();
        }
    }

    public void applySolarArcDamage(PlayerEntity player) {
        if (this.getWorld().isClient()) return;
        if (isCoreBroken()) return; // Solar Arc does nothing after core break (or can still damage)

        float damage = MAX_HEALTH * 0.05f; // 5% of max health = 40 HP
        this.damage(this.getDamageSources().magic(), damage);
        triggerAnimation("solar_arc_reaction");
        syncBossState();
    }

    private void breakCore() {
        this.dataTracker.set(DATA_CORE_BROKEN, true);
        this.dataTracker.set(DATA_REGEN_FROZEN_TICKS, 0);
        this.dataTracker.set(DATA_REGEN_MULTIPLIER, 0.0f);
        this.dataTracker.set(DATA_REGEN_STATE, RegenerationState.CORE_BROKEN.ordinal());
        this.dataTracker.set(DATA_STAGGERED, true);
        this.currentRegenState = RegenerationState.CORE_BROKEN;
        frostFrozenTicks = 0;
        thornCagedTicks = 0;

        triggerAnimation("core_exposed");
        triggerAnimation("stagger");
        syncBossState();

        // Remove healing flames if they exist
        removeHealingFlames();
    }

    public boolean isCoreBroken() {
        return this.dataTracker.get(DATA_CORE_BROKEN);
    }

    public int getCoreIntegrity() {
        return this.dataTracker.get(DATA_CORE_INTEGRITY);
    }

    // ==================== PHASE SYSTEM ====================
    private void updatePhase() {
        if (this.getWorld().isClient()) return;

        float healthRatio = this.getHealth() / this.getMaxHealth();
        Phase newPhase = currentPhase;

        if (healthRatio <= Phase.PHASE_3.healthEnd) {
            newPhase = Phase.PHASE_3;
        } else if (healthRatio <= Phase.PHASE_2.healthEnd) {
            newPhase = Phase.PHASE_2;
        } else {
            newPhase = Phase.PHASE_1;
        }

        if (newPhase != currentPhase && !phaseTransitionTriggered) {
            currentPhase = newPhase;
            phaseTransitionTriggered = true;
            this.dataTracker.set(DATA_PHASE, currentPhase.ordinal());
            onPhaseTransition(currentPhase);
        } else if (newPhase == currentPhase) {
            phaseTransitionTriggered = false;
        }
    }

    private void onPhaseTransition(Phase phase) {
        switch (phase) {
            case PHASE_2:
                triggerAnimation("phase_two");
                // Spawn initial healing flames
                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    spawnHealingFlames(serverWorld);
                }
                break;
            case PHASE_3:
                triggerAnimation("phase_three");
                this.dataTracker.set(DATA_ENRAGE_TIMER, ENRAGE_TIMER);
                break;
        }
        syncBossState();
    }

    // ==================== HEALING FLAMES (PHASE 2) ====================
    private void spawnHealingFlames(ServerWorld world) {
        if (currentPhase != Phase.PHASE_2) return;

        Vec3d pos = this.getPos();
        for (int i = 0; i < 2; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2;
            double distance = 4.0 + this.random.nextDouble() * 3.0;
            double x = pos.x + Math.cos(angle) * distance;
            double z = pos.z + Math.sin(angle) * distance;
            BlockPos spawnPos = new BlockPos((int)x, (int)pos.y, (int)z);

            // Create a healing flame entity (using a simple marker entity or existing entity type)
            // For simplicity, we'll use a custom approach with particles and tracking
            // In a full implementation, this would be a separate entity class
        }
        healingFlameSpawnCooldown = 200; // 10 seconds
    }

    private void updateHealingFlames() {
        if (healingFlameSpawnCooldown > 0) {
            healingFlameSpawnCooldown--;
        }

        // Healing flames restore 1% boss health per second each (max 2)
        // Simplified: just heal the boss directly when flames are "active"
        if (currentPhase == Phase.PHASE_2 && !isCoreBroken()) {
            float healPerSecond = this.getMaxHealth() * 0.01f * 2; // 2 flames * 1%
            float healPerTick = healPerSecond / 20.0f;
            this.heal(healPerTick);
        }

        // Remove flames when core breaks or phase 3
        if (currentPhase == Phase.PHASE_3 || isCoreBroken()) {
            removeHealingFlames();
        }
    }

    private void removeHealingFlames() {
        healingFlame1EntityId = -1;
        healingFlame2EntityId = -1;
    }

    // ==================== COMBAT ABILITIES ====================
    private void updateAbilityCooldowns() {
        if (slashCooldown > 0) slashCooldown--;
        if (shieldBashCooldown > 0) shieldBashCooldown--;
        if (chargeCooldown > 0) chargeCooldown--;
        if (corePulseCooldown > 0) corePulseCooldown--;
        if (attackWindup > 0) attackWindup--;
        if (attackActive > 0) attackActive--;
        if (attackRecovery > 0) attackRecovery--;
        if (chargeWarningTicks > 0) chargeWarningTicks--;
        if (corePulseWarningTicks > 0) corePulseWarningTicks--;
    }

    private boolean canAttack() {
        return attackWindup == 0 && attackActive == 0 && attackRecovery == 0;
    }

    private void startSlash() {
        if (!canAttack() || slashCooldown > 0) return;
        currentAttackType = 1;
        attackWindup = SLASH_WINDUP;
        slashCooldown = SLASH_COOLDOWN;
        triggerAnimation("slash_windup");
    }

    private void executeSlash() {
        if (attackActive > 0) return;
        attackActive = SLASH_ACTIVE;
        attackRecovery = SLASH_RECOVERY;
        triggerAnimation("slash");

        // Deal damage to entities in range
        if (this.getWorld() instanceof ServerWorld world) {
            Box area = new Box(this.getPos().subtract(SLASH_RANGE, 2, SLASH_RANGE), this.getPos().add(SLASH_RANGE, 2, SLASH_RANGE));
            List<LivingEntity> targets = world.getNonSpectatingEntities(LivingEntity.class, area);
            for (LivingEntity target : targets) {
                if (target != this && this.squaredDistanceTo(target) <= SLASH_RANGE * SLASH_RANGE) {
                    target.damage(this.getDamageSources().mobAttack(this), SLASH_DAMAGE);
                }
            }
        }
    }

    private void startShieldBash() {
        if (!canAttack() || shieldBashCooldown > 0) return;
        currentAttackType = 2;
        attackWindup = 10; // windup for shield bash
        shieldBashCooldown = SHIELD_BASH_COOLDOWN;
        triggerAnimation("shield_bash_windup");
    }

    private void executeShieldBash() {
        if (attackActive > 0) return;
        attackActive = 5;
        attackRecovery = 15;
        triggerAnimation("shield_bash");

        if (this.getWorld() instanceof ServerWorld world) {
            Vec3d lookDir = this.getRotationVec(1.0f).normalize();
            Box cone = new Box(this.getPos().subtract(2, 2, 2), this.getPos().add(2, 2, 2)).stretch(lookDir.multiply(4));
            List<LivingEntity> targets = world.getNonSpectatingEntities(LivingEntity.class, cone);
            for (LivingEntity target : targets) {
                if (target != this) {
                    Vec3d toTarget = target.getPos().subtract(this.getPos()).normalize();
                    double dot = lookDir.dotProduct(toTarget);
                    if (dot > 0.7) { // ~45 degree cone
                        target.damage(this.getDamageSources().mobAttack(this), SHIELD_BASH_DAMAGE);
                        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, SHIELD_BASH_STUN, 9, false, false, true)); // Effective stun
                        target.addVelocity(lookDir.x * 2, 0.3, lookDir.z * 2);
                    }
                }
            }
        }
    }

    private void startCharge(LivingEntity target) {
        if (!canAttack() || chargeCooldown > 0 || target == null) return;
        currentAttackType = 3;
        chargeWarningTicks = CHARGE_WARNING;
        chargeCooldown = CHARGE_COOLDOWN;
        chargeTargetPos = target.getPos();
        triggerAnimation("charge_windup");
    }

    private void executeCharge() {
        if (chargeWarningTicks > 0) return;
        if (chargeTargetPos == null) return;

        Vec3d dir = chargeTargetPos.subtract(this.getPos()).normalize();
        double distance = Math.min(CHARGE_DISTANCE, this.getPos().distanceTo(chargeTargetPos));
        Vec3d targetPos = this.getPos().add(dir.multiply(distance));

        // Check for obstacles (simplified)
        this.setVelocity(dir.multiply(2.5));
        this.velocityModified = true;

        triggerAnimation("charge");

        // Damage entities along path
        if (this.getWorld() instanceof ServerWorld world) {
            Box pathBox = new Box(this.getPos(), targetPos).expand(1.5);
            List<LivingEntity> targets = world.getNonSpectatingEntities(LivingEntity.class, pathBox);
            for (LivingEntity entity : targets) {
                if (entity != this) {
                    entity.damage(this.getDamageSources().mobAttack(this), CHARGE_DAMAGE);
                    entity.addVelocity(dir.x * 1.5, 0.5, dir.z * 1.5);
                }
            }
        }

        attackRecovery = CHARGE_RECOVERY;
        chargeTargetPos = null;
    }

    private void startCorePulse() {
        if (!canAttack() || corePulseCooldown > 0) return;
        currentAttackType = 4;
        corePulseWarningTicks = CORE_PULSE_WARNING;
        corePulseCooldown = CORE_PULSE_COOLDOWN;
        triggerAnimation("core_pulse_windup");
    }

    private void executeCorePulse() {
        if (corePulseWarningTicks > 0) return;

        triggerAnimation("core_pulse");

        if (this.getWorld() instanceof ServerWorld world) {
            Box area = new Box(this.getPos().subtract(CORE_PULSE_RADIUS, CORE_PULSE_RADIUS, CORE_PULSE_RADIUS),
                    this.getPos().add(CORE_PULSE_RADIUS, CORE_PULSE_RADIUS, CORE_PULSE_RADIUS));
            List<LivingEntity> targets = world.getNonSpectatingEntities(LivingEntity.class, area);
            for (LivingEntity target : targets) {
                if (target != this) {
                    target.damage(this.getDamageSources().magic(), CORE_PULSE_DAMAGE);
                    Vec3d knockback = target.getPos().subtract(this.getPos()).normalize().multiply(1.5).add(0, 0.5, 0);
                    target.addVelocity(knockback.x, knockback.y, knockback.z);
                }
            }

            // Particles
            world.spawnParticles(ParticleTypes.FLASH, this.getX(), this.getY() + 1, this.getZ(), 1, 0, 0, 0, 0);
            world.spawnParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1, this.getZ(), 30, CORE_PULSE_RADIUS * 0.5, 1, CORE_PULSE_RADIUS * 0.5, 0.2);
            world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0f, 0.8f);
        }

        attackRecovery = 20;
    }

    // ==================== AI GOALS ====================
    private class KnightMeleeAttackGoal extends Goal {
        @Override
        public boolean canStart() {
            LivingEntity target = EverlivingKnightEntity.this.getTarget();
            if (target == null || !target.isAlive()) return false;
            if (!canAttack()) return false;
            if (EverlivingKnightEntity.this.isCoreBroken() && EverlivingKnightEntity.this.dataTracker.get(DATA_STAGGERED)) return false;

            // Prefer slash when in range
            double distSq = EverlivingKnightEntity.this.squaredDistanceTo(target);
            return distSq <= SLASH_RANGE * SLASH_RANGE * 1.5 && slashCooldown == 0;
        }

        @Override
        public void start() {
            startSlash();
        }

        @Override
        public void tick() {
            if (attackWindup > 0) return;
            if (attackActive > 0) {
                executeSlash();
            }
        }

        @Override
        public boolean shouldContinue() {
            return attackWindup > 0 || attackActive > 0 || attackRecovery > 0;
        }
    }

    private class KnightChargeGoal extends Goal {
        @Override
        public boolean canStart() {
            LivingEntity target = EverlivingKnightEntity.this.getTarget();
            if (target == null || !target.isAlive()) return false;
            if (!canAttack()) return false;
            if (chargeCooldown > 0) return false;

            double distSq = EverlivingKnightEntity.this.squaredDistanceTo(target);
            return distSq > SLASH_RANGE * SLASH_RANGE * 2 && distSq < CHARGE_DISTANCE * CHARGE_DISTANCE * 2;
        }

        @Override
        public void start() {
            startCharge(EverlivingKnightEntity.this.getTarget());
        }

        @Override
        public void tick() {
            if (chargeWarningTicks > 0) return;
            if (attackRecovery > 0) return;
            executeCharge();
        }

        @Override
        public boolean shouldContinue() {
            return chargeWarningTicks > 0 || attackRecovery > 0;
        }
    }

    private class KnightShieldBashGoal extends Goal {
        @Override
        public boolean canStart() {
            LivingEntity target = EverlivingKnightEntity.this.getTarget();
            if (target == null || !target.isAlive()) return false;
            if (!canAttack()) return false;
            if (shieldBashCooldown > 0) return false;

            double distSq = EverlivingKnightEntity.this.squaredDistanceTo(target);
            return distSq <= 9.0 && EverlivingKnightEntity.this.random.nextFloat() < 0.3;
        }

        @Override
        public void start() {
            startShieldBash();
        }

        @Override
        public void tick() {
            if (attackWindup > 0) return;
            if (attackActive > 0) {
                executeShieldBash();
            }
        }

        @Override
        public boolean shouldContinue() {
            return attackWindup > 0 || attackActive > 0 || attackRecovery > 0;
        }
    }

    private class KnightCorePulseGoal extends Goal {
        @Override
        public boolean canStart() {
            LivingEntity target = EverlivingKnightEntity.this.getTarget();
            if (target == null || !target.isAlive()) return false;
            if (!canAttack()) return false;
            if (corePulseCooldown > 0) return false;

            // More frequent in later phases
            float chance = currentPhase == Phase.PHASE_3 ? 0.15f : (currentPhase == Phase.PHASE_2 ? 0.08f : 0.05f);
            return EverlivingKnightEntity.this.random.nextFloat() < chance;
        }

        @Override
        public void start() {
            startCorePulse();
        }

        @Override
        public void tick() {
            if (corePulseWarningTicks > 0) return;
            if (attackRecovery > 0) return;
            executeCorePulse();
        }

        @Override
        public boolean shouldContinue() {
            return corePulseWarningTicks > 0 || attackRecovery > 0;
        }
    }

    // ==================== ARENA BOUNDARY ====================
    public void setArenaCenter(BlockPos center) {
        this.arenaCenter = center;
    }

    private void enforceArenaBoundary() {
        if (arenaCenter == null) return;
        if (this.getWorld().isClient()) return;

        double dist = this.getPos().distanceTo(Vec3d.ofCenter(arenaCenter));
        if (dist > ARENA_RADIUS) {
            // Gently push back toward center
            Vec3d toCenter = Vec3d.ofCenter(arenaCenter).subtract(this.getPos()).normalize();
            this.setVelocity(this.getVelocity().add(toCenter.multiply(0.1)));
            this.velocityModified = true;
        }
    }

    // ==================== ENRAGE TIMER ====================
    private void updateEnrageTimer() {
        if (currentPhase == Phase.PHASE_3 && !isCoreBroken()) {
            int timer = this.dataTracker.get(DATA_ENRAGE_TIMER);
            if (timer > 0) {
                this.dataTracker.set(DATA_ENRAGE_TIMER, timer - 1);
                if (timer == 1) {
                    triggerAnimation("enrage");
                    // Enrage effects: increase damage, speed, etc.
                    this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(BASE_ATTACK_DAMAGE * 1.5);
                    this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(BASE_MOVEMENT_SPEED * 1.3);
                }
            }
        }
    }

    // ==================== DAMAGE HANDLING ====================
    @Override
    public boolean damage(DamageSource source, float amount) {
        // Prevent duplicate damage in same tick
        long worldTime = this.getWorld().getTime();
        UUID sourceId = source.getAttacker() != null ? source.getAttacker().getUuid() : UUID.randomUUID();
        if (worldTime == lastDamageTick && sourceId.equals(lastDamageSourceUUID)) {
            return false;
        }
        lastDamageTick = worldTime;
        lastDamageSourceUUID = sourceId;

        // Before core break: cannot die
        if (!isCoreBroken()) {
            float newHealth = this.getHealth() - amount;
            if (newHealth <= 1.0f) {
                amount = this.getHealth() - 1.0f;
                if (amount <= 0) return false;
            }
        }

        boolean result = super.damage(source, amount);
        if (result && this.getWorld() instanceof ServerWorld) {
            syncBossState();
        }
        return result;
    }

    @Override
    public void onDeath(DamageSource source) {
        // Only allow death after core is broken
        if (!isCoreBroken()) {
            this.setHealth(1.0f);
            return;
        }

        // Clean up boss bar
        removeBossBar();

        super.onDeath(source);
    }

    // ==================== BOSS BAR ====================
    private void updateBossBar() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        String regenStatus;
        if (isCoreBroken()) {
            regenStatus = "Core Broken - Regeneration: Disabled";
        } else if (frostFrozenTicks > 0) {
            regenStatus = "Regeneration: Frozen (" + (frostFrozenTicks / 20) + "s)";
        } else if (thornCagedTicks > 0) {
            regenStatus = "Regeneration: Slowed (40%)";
        } else {
            regenStatus = "Regeneration: Active";
        }

        this.bossBar.setName(Text.literal("Sir Solvane, the Everliving Knight | " + regenStatus + " | Core: " + getCoreIntegrity() + "/100"));
    }

    private void addBossBarForPlayer(ServerPlayerEntity player) {
        this.bossBar.addPlayer(player);
    }

    private void removeBossBar() {
        this.bossBar.clearPlayers();
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        addBossBarForPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    // ==================== TICK ====================
    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient()) {
            updateRegeneration();
            updatePhase();
            updateAbilityCooldowns();
            updateHealingFlames();
            updateEnrageTimer();
            enforceArenaBoundary();

            // Handle attack execution based on current attack type
            if (attackWindup > 0) {
                // Windup phase - could add warning particles here
            } else if (attackActive > 0) {
                // Active attack window handled by specific goals
            }
        }

        // Handle pending animation triggers
        if (pendingAnimation != null && this.getWorld() instanceof ServerWorld) {
            // Animation trigger would be synced via packet to clients
            pendingAnimation = null;
        }
    }

    // ==================== ANIMATION HOOKS ====================
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.everliving_knight.idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.everliving_knight.walk");
    private static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("animation.everliving_knight.run");
    private static final RawAnimation SLASH_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.slash");
    private static final RawAnimation COMBO_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.combo");
    private static final RawAnimation SHIELD_BASH_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.shield_bash");
    private static final RawAnimation CHARGE_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.charge");
    private static final RawAnimation SOLAR_ARC_REACTION = RawAnimation.begin().thenPlay("animation.everliving_knight.solar_arc_reaction");
    private static final RawAnimation FROSTBURST_REACTION = RawAnimation.begin().thenPlay("animation.everliving_knight.frostburst_reaction");
    private static final RawAnimation THORN_CAGE_REACTION = RawAnimation.begin().thenPlay("animation.everliving_knight.thorn_cage_reaction");
    private static final RawAnimation CORE_PULSE_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.core_pulse");
    private static final RawAnimation CORE_EXPOSED_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.core_exposed");
    private static final RawAnimation STAGGER_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.stagger");
    private static final RawAnimation PHASE_TWO_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.phase_two");
    private static final RawAnimation PHASE_THREE_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.phase_three");
    private static final RawAnimation ENRAGE_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.enrage");
    private static final RawAnimation DEATH_ANIM = RawAnimation.begin().thenPlay("animation.everliving_knight.death");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, state -> {
            if (state.isMoving()) {
                return state.setAndContinue(this.isSprinting() ? RUN_ANIM : WALK_ANIM);
            }
            return state.setAndContinue(IDLE_ANIM);
        })
        .triggerableAnim("slash", SLASH_ANIM)
        .triggerableAnim("slash_windup", RawAnimation.begin().thenPlay("animation.everliving_knight.slash_windup"))
        .triggerableAnim("combo", COMBO_ANIM)
        .triggerableAnim("shield_bash", SHIELD_BASH_ANIM)
        .triggerableAnim("shield_bash_windup", RawAnimation.begin().thenPlay("animation.everliving_knight.shield_bash_windup"))
        .triggerableAnim("charge", CHARGE_ANIM)
        .triggerableAnim("charge_windup", RawAnimation.begin().thenPlay("animation.everliving_knight.charge_windup"))
        .triggerableAnim("solar_arc_reaction", SOLAR_ARC_REACTION)
        .triggerableAnim("frostburst_reaction", FROSTBURST_REACTION)
        .triggerableAnim("thorn_cage_reaction", THORN_CAGE_REACTION)
        .triggerableAnim("core_pulse", CORE_PULSE_ANIM)
        .triggerableAnim("core_pulse_windup", RawAnimation.begin().thenPlay("animation.everliving_knight.core_pulse_windup"))
        .triggerableAnim("core_exposed", CORE_EXPOSED_ANIM)
        .triggerableAnim("stagger", STAGGER_ANIM)
        .triggerableAnim("phase_two", PHASE_TWO_ANIM)
        .triggerableAnim("phase_three", PHASE_THREE_ANIM)
        .triggerableAnim("enrage", ENRAGE_ANIM)
        .triggerableAnim("death", DEATH_ANIM)
        .triggerableAnim("idle", IDLE_ANIM));
    }

    public void triggerAnimation(String animName) {
        this.pendingAnimation = animName;
        // In a full implementation, this would send a packet to clients to trigger the animation
        // For now, we track it for the animation controller
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // ==================== NETWORKING / SYNC ====================
    private void syncBossState() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) return;

        // Send state update to tracking players
        // This would use a custom packet in a full implementation
        // For now, data tracker handles most sync automatically
    }

    // ==================== NBT ====================
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Phase", currentPhase.ordinal());
        nbt.putInt("CoreIntegrity", getCoreIntegrity());
        nbt.putBoolean("CoreBroken", isCoreBroken());
        nbt.putInt("RegenFrozenTicks", frostFrozenTicks);
        nbt.putFloat("RegenMultiplier", getRegenerationMultiplier());
        nbt.putInt("RegenState", currentRegenState.ordinal());
        nbt.putInt("ThornCagedTicks", thornCagedTicks);
        nbt.putInt("SlashCooldown", slashCooldown);
        nbt.putInt("ShieldBashCooldown", shieldBashCooldown);
        nbt.putInt("ChargeCooldown", chargeCooldown);
        nbt.putInt("CorePulseCooldown", corePulseCooldown);
        nbt.putInt("EnrageTimer", this.dataTracker.get(DATA_ENRAGE_TIMER));
        if (arenaCenter != null) {
            nbt.putLong("ArenaCenter", arenaCenter.asLong());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Phase")) {
            currentPhase = Phase.values()[MathHelper.clamp(nbt.getInt("Phase"), 0, Phase.values().length - 1)];
            this.dataTracker.set(DATA_PHASE, currentPhase.ordinal());
        }
        if (nbt.contains("CoreIntegrity")) {
            this.dataTracker.set(DATA_CORE_INTEGRITY, nbt.getInt("CoreIntegrity"));
        }
        if (nbt.contains("CoreBroken")) {
            boolean broken = nbt.getBoolean("CoreBroken");
            this.dataTracker.set(DATA_CORE_BROKEN, broken);
        }
        if (nbt.contains("RegenFrozenTicks")) {
            frostFrozenTicks = nbt.getInt("RegenFrozenTicks");
            this.dataTracker.set(DATA_REGEN_FROZEN_TICKS, frostFrozenTicks);
        }
        if (nbt.contains("RegenMultiplier")) {
            this.dataTracker.set(DATA_REGEN_MULTIPLIER, nbt.getFloat("RegenMultiplier"));
        }
        if (nbt.contains("RegenState")) {
            int state = nbt.getInt("RegenState");
            currentRegenState = RegenerationState.values()[MathHelper.clamp(state, 0, RegenerationState.values().length - 1)];
            this.dataTracker.set(DATA_REGEN_STATE, state);
        }
        if (nbt.contains("ThornCagedTicks")) {
            thornCagedTicks = nbt.getInt("ThornCagedTicks");
        }
        if (nbt.contains("SlashCooldown")) slashCooldown = nbt.getInt("SlashCooldown");
        if (nbt.contains("ShieldBashCooldown")) shieldBashCooldown = nbt.getInt("ShieldBashCooldown");
        if (nbt.contains("ChargeCooldown")) chargeCooldown = nbt.getInt("ChargeCooldown");
        if (nbt.contains("CorePulseCooldown")) corePulseCooldown = nbt.getInt("CorePulseCooldown");
        if (nbt.contains("EnrageTimer")) this.dataTracker.set(DATA_ENRAGE_TIMER, nbt.getInt("EnrageTimer"));
        if (nbt.contains("ArenaCenter")) {
            arenaCenter = BlockPos.fromLong(nbt.getLong("ArenaCenter"));
        }
    }

    // ==================== ENTITY SPAWNING / PACKETS ====================
    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (data.equals(DATA_PHASE) || data.equals(DATA_CORE_INTEGRITY) || data.equals(DATA_CORE_BROKEN) ||
                data.equals(DATA_REGEN_STATE) || data.equals(DATA_REGEN_FROZEN_TICKS) || data.equals(DATA_STAGGERED)) {
            // State changed, could send custom packet for immediate client update
            updateBossBar();
        }
    }
}