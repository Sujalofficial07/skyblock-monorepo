package net.skyblock.core.events;

import lombok.Getter;
import lombok.Setter;
import net.skyblock.core.data.PlayerProfile;
import net.skyblock.core.stats.StatType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Custom damage event using Hypixel SkyBlock's exact damage formula:
 *
 * Damage = floor((WeaponDamage + 5) × (1 + Strength/100) × (1 + CritDamage/100)
 *               × (1 + AdditiveMult/100))
 *
 * This fires instead of Bukkit's EntityDamageEvent.
 */
@Getter
public class CustomDamageEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player attacker;
    private final Entity victim;
    private final PlayerProfile attackerProfile;

    // Raw components (mutable pre-calculation)
    @Setter private int weaponBaseDamage;
    @Setter private double strength;
    @Setter private double critDamage;
    @Setter private boolean isCrit;
    @Setter private double additiveMult = 0.0; // from enchants, abilities, etc.

    // Final calculated damage
    @Setter private double finalDamage;

    @Setter private boolean cancelled;

    // Damage type
    private final DamageType damageType;

    public CustomDamageEvent(Player attacker, Entity victim,
                             PlayerProfile attackerProfile,
                             int weaponBaseDamage,
                             DamageType damageType) {
        this.attacker = attacker;
        this.victim = victim;
        this.attackerProfile = attackerProfile;
        this.weaponBaseDamage = weaponBaseDamage;
        this.damageType = damageType;

        // Pull stats from profile
        this.strength = attackerProfile.getStat(StatType.STRENGTH);
        this.critDamage = attackerProfile.getStat(StatType.CRIT_DAMAGE);

        // Determine crit
        double critChance = attackerProfile.getStat(StatType.CRIT_CHANCE);
        this.isCrit = Math.random() * 100 < critChance;

        // Calculate initial damage
        recalculate();
    }

    /** Re-run Hypixel's exact formula. Call after modifying any component. */
    public void recalculate() {
        double base = weaponBaseDamage + 5;
        double strengthMult = 1.0 + strength / 100.0;
        double critMult = isCrit ? (1.0 + critDamage / 100.0) : 1.0;
        double addMult = 1.0 + additiveMult / 100.0;
        this.finalDamage = Math.floor(base * strengthMult * critMult * addMult);
    }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }

    public enum DamageType {
        MELEE, RANGED, ABILITY, TRUE, POISON, FALL, VOID
    }
}
