/* MegaMek - Copyright (C) 2004,2005 Ben Mazur (bmazur@sev.org)
 * 
 *  This program is free software; you can redistribute it and/or modify it 
 *  under the terms of the GNU General Public License as published by the Free 
 *  Software Foundation; either version 2 of the License, or (at your option) 
 *  any later version.
 * 
 *  This program is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 *  for more details.
 */
/*
 * Created on Sep 25, 2004
 *
 */
package megamek.common.weapons.bayweapons;

import megamek.common.Entity;
import megamek.common.IGame;
import megamek.common.Mounted;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.common.weapons.AttackHandler;
import megamek.common.weapons.CapitalMissileBayHandler;
import megamek.common.weapons.CapitalMissileBearingsOnlyHandler;
import megamek.server.Server;

/**
 * @author Jay Lawson
 */
public class CapitalMissileBayWeapon extends AmmoBayWeapon {
    /**
     * 
     */
    private static final long serialVersionUID = 8756042527483383101L;

    /**
     * 
     */
    public CapitalMissileBayWeapon() {
        super();
        // tech levels are a little tricky
        this.name = "Capital Missile Bay";
        this.setInternalName(this.name);
        this.heat = 0;
        this.damage = DAMAGE_VARIABLE;
        this.shortRange = 12;
        this.mediumRange = 24;
        this.longRange = 40;
        this.extremeRange = 50;
        this.tonnage = 0.0f;
        this.bv = 0;
        this.cost = 0;
        this.flags = flags.or(F_MISSILE);
        this.atClass = CLASS_CAPITAL_MISSILE;
        this.capital = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * megamek.common.weapons.Weapon#getCorrectHandler(megamek.common.ToHitData,
     * megamek.common.actions.WeaponAttackAction, megamek.common.IGame)
     */
    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit,
            WeaponAttackAction waa, IGame game, Server server) {
        Mounted weapon = game.getEntity(waa.getEntityId())
                .getEquipment(waa.getWeaponId());
        Entity attacker = game.getEntity(waa.getEntityId());
        int rangeToTarget = attacker.getPosition().distance(
                game.getEntity(waa.getTargetId()).getPosition());
        if (((weapon.curMode().equals("Bearings-Only Extreme Detection Range"))
               || (weapon.curMode().equals("Bearings-Only Long Detection Range"))
               || (weapon.curMode().equals("Bearings-Only Medium Detection Range"))
               || (weapon.curMode().equals("Bearings-Only Short Detection Range")))
                && (rangeToTarget > 50)) {
            return new CapitalMissileBearingsOnlyHandler(toHit, waa, game, server);
        } else {    
            return new CapitalMissileBayHandler(toHit, waa, game, server);
        }
    }
    
    @Override
    public int getBattleForceClass() {
        return BFCLASS_CAPITAL_MISSILE;
    }
}
