/*
 * MegaMek - Copyright (C) 2000-2011 Ben Mazur (bmazur@sev.org)
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
package megamek.client.bot.princess;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import megamek.client.bot.BotClient;
import megamek.client.bot.ChatProcessor;
import megamek.client.bot.PhysicalOption;
import megamek.common.AmmoType;
import megamek.common.BattleArmor;
import megamek.common.Building;
import megamek.common.BuildingTarget;
import megamek.common.Coords;
import megamek.common.Compute;
import megamek.common.Entity;
import megamek.common.GunEmplacement;
import megamek.common.IAero;
import megamek.common.IGame;
import megamek.common.IHex;
import megamek.common.Infantry;
import megamek.common.Mech;
import megamek.common.MechWarrior;
import megamek.common.Minefield;
import megamek.common.Mounted;
import megamek.common.MovePath;
import megamek.common.MovePath.MoveStepType;
import megamek.common.MoveStep;
import megamek.common.PilotingRollData;
import megamek.common.Tank;
import megamek.common.Targetable;
import megamek.common.Terrains;
import megamek.common.WeaponType;
<<<<<<< HEAD
import megamek.common.actions.EntityAction;
=======
import megamek.common.annotations.Nullable;
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
import megamek.common.containers.PlayerIDandList;
import megamek.common.event.GamePlayerChatEvent;
import megamek.common.logging.LogLevel;
import megamek.common.logging.Logger;
import megamek.common.net.Packet;
import megamek.common.options.OptionsConstants;
import megamek.common.pathfinder.AeroGroundPathFinder;
import megamek.common.util.StringUtil;
import megamek.common.weapons.AmmoWeapon;

public class Princess extends BotClient {

    private static final Logger logger = new Logger();

    private static final char PLUS = '+';
    private static final char MINUS = '-';

    private final IHonorUtil honorUtil = new HonorUtil();

    private boolean initialized = false;

    //private PathSearcher pathSearcher;
    private BasicPathRanker pathRanker;
    private FireControl fireControl;
    private BehaviorSettings behaviorSettings;
    private double moveEvaluationTimeEstimate = 0;
    private final Precognition precognition;
    private final Thread precogThread;
    /**
     * Mapping to hold the damage allocated to each targetable, stored by ID.
     * Used to allocate damage more intelligently and avoid overkill.
     */
<<<<<<< HEAD
    private ConcurrentHashMap<Integer, Double> damageMap = new ConcurrentHashMap<>(); 
=======
    private final ConcurrentHashMap<Integer, Double> damageMap =
            new ConcurrentHashMap<>(); 
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    private final Set<Coords> strategicBuildingTargets = new HashSet<>();
    private boolean fallBack = false;
<<<<<<< HEAD
    protected ChatProcessor chatProcessor = new ChatProcessor();
=======
    private final ChatProcessor chatProcessor = new ChatProcessor();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    private boolean fleeBoard = false;
<<<<<<< HEAD
    private IMoralUtil moralUtil = new MoralUtil(logger);
=======
    private final IMoralUtil moralUtil = new MoralUtil(getLogger());
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    private final Set<Integer> attackedWhileFleeing =
            Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
    private final Set<Integer> myFleeingEntities = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());


    public Princess(final String name,
                    final String host,
                    final int port,
                    final LogLevel verbosity) {
        super(name, host, port);
        logger.setVerbosity(verbosity);
        setBehaviorSettings(BehaviorSettingsFactory.getInstance(logger).DEFAULT_BEHAVIOR);
        
        // Start-up precog now, so that it can instantiate its game instance,
        // and it will stay up-to date.
        precognition = new Precognition(this);
        precogThread = new Thread(precognition, "Princess-precognition ("
                + getName() + ")");
        precogThread.start();
    }

<<<<<<< HEAD
    public void setVerbosity(LogLevel level) {
        logger.setVerbosity(level);
=======
    MMLogger getLogger() {
        if (null == logger) {
            logger = DefaultMmLogger.getInstance();
        }
        return logger;
    }

    /**
     * Use to set a fake logger during unit testing.
     *
     * @param logger The logger to be used.
     */
    void setLogger(final MMLogger logger) {
        if (null == logger) {
            return;
        }
        this.logger = logger;
    }

    public void setVerbosity(final LogLevel level) {
        getBehaviorSettings().setVerbosity(level);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    }

    public LogLevel getVerbosity() {
<<<<<<< HEAD
        return logger.getVerbosity();
=======
        return getBehaviorSettings().getVerbosity();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    }

    public BasicPathRanker getPathRanker() {
        return pathRanker;
    }

    public boolean getFallBack() {
        return fallBack;
    }

    public boolean getFleeBoard() {
        return fleeBoard;
    }

    public boolean getForcedWithdrawal() {
        return getBehaviorSettings().isForcedWithdrawal();
    }

<<<<<<< HEAD
    public void setFleeBoard(boolean fleeBoard, String reason) {
        log(getClass(), "setFleeBoard(boolean, String)", LogLevel.DEBUG, "Setting Flee Board " + fleeBoard +
                                                                         " because: " + reason);
=======
    private void setFleeBoard(final boolean fleeBoard,
                              @SuppressWarnings("SameParameterValue") final String reason) {
        log(getClass(), "setFleeBoard(boolean, String)",
            LogLevel.DEBUG,
            "Setting Flee Board " + fleeBoard + " because: " + reason);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek

        this.fleeBoard = fleeBoard;
    }

    protected Precognition getPrecognition() {
        return precognition;
    }

<<<<<<< HEAD
    public void setFallBack(boolean fallBack, String reason) {
        log(getClass(), "setFallBack(boolean, String)", LogLevel.DEBUG, "Setting Fall Back " + fallBack +
                                                                        " because: " + reason);
=======
    public void setFallBack(final boolean fallBack,
                            final String reason) {
        log(getClass(), "setFallBack(boolean, String)",
            LogLevel.DEBUG,
            "Setting Fall Back " + fallBack + " because: " + reason);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        this.fallBack = fallBack;
    }

<<<<<<< HEAD
    public void setBehaviorSettings(BehaviorSettings behaviorSettings) {
        log(getClass(), "setBehaviorSettings(BehaviorSettings)", LogLevel.INFO, "New behavior settings for " +
                                                                                getName() + "\n" +
                                                                                behaviorSettings.toLog());
=======
    public void setBehaviorSettings(final BehaviorSettings behaviorSettings) {
        log(getClass(),
            "setBehaviorSettings(BehaviorSettings)",
            LogLevel.INFO,
            "New behavior settings for " + getName() +
            "\n" + behaviorSettings.toLog());
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        try {
            this.behaviorSettings = behaviorSettings.getCopy();
<<<<<<< HEAD
        } catch (PrincessException e) {
            log(getClass(), "setBehaviorSettings(BehaviorSettings)", e);
=======
        } catch (final PrincessException e) {
            log(getClass(),
                "setBehaviorSettings(BehaviorSettings)",
                e);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            return;
        }
        getStrategicBuildingTargets().clear();
        setFallBack(behaviorSettings.shouldGoHome(), "Fall Back Configuration.");
        setFleeBoard(behaviorSettings.shouldAutoFlee(), "Flee Board Configuration.");
        if (getFallBack()) {
            return;
        }

<<<<<<< HEAD
        for (String targetCoords : behaviorSettings.getStrategicBuildingTargets()) {
            if (!StringUtil.isPositiveInteger(targetCoords) || (targetCoords.length() != 4)) {
=======
        for (final String targetCoords : behaviorSettings.getStrategicBuildingTargets()) {
            if (!StringUtil.isPositiveInteger(targetCoords) ||
                (4 != targetCoords.length())) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                continue;
            }
<<<<<<< HEAD
            String x = targetCoords.substring(0, 2);
            String y = targetCoords.replaceFirst(x, "");
            // Need to subtract 1, since we are given a Hex number string, which is Coords X+1Y+1 
            Coords coords = new Coords(Integer.parseInt(x) - 1, Integer.parseInt(y) - 1);
=======
            final String x = targetCoords.substring(0, 2);
            final String y = targetCoords.replaceFirst(x, "");
            // Need to subtract 1, since we are given a Hex number string, 
            // which is Coords X+1Y+1 
            final Coords coords = new Coords(Integer.parseInt(x) - 1,
                                       Integer.parseInt(y) - 1);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            getStrategicBuildingTargets().add(coords);
        }
    }

    public FireControl getFireControl() {
        return fireControl;
    }

<<<<<<< HEAD
    public ConcurrentHashMap<Integer,Double> getDamageMap() {
        return damageMap;
    }

    public double getDamageAlreadyAssigned(Targetable target) {
        Integer targetId = new Integer(target.getTargetId());
=======
    double getDamageAlreadyAssigned(final Targetable target) {
        final Integer targetId = target.getTargetId();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        if(damageMap.containsKey(targetId)) {
            return damageMap.get(targetId);
        }
        return 0.0; // If we have no entry, return zero
    }

    public BehaviorSettings getBehaviorSettings() {
        return behaviorSettings;
    }

    public Set<Coords> getStrategicBuildingTargets() {
        return strategicBuildingTargets;
    }

    public void addStrategicBuildingTarget(final Coords coords) {
        if (null == coords) {
            throw new NullPointerException("Coords is null.");
        }
        if (!getGame().getBoard().contains(coords)) {
            log(getClass(), "addStrategicBuildingTarget(Coords)", LogLevel.WARNING,
                "Board does not contain " + coords.toFriendlyString());
            return;
        }
        getStrategicBuildingTargets().add(coords);
    }

    public Set<Integer> getPriorityUnitTargets() {
        return getBehaviorSettings().getPriorityUnitTargets();
    }

    @Override
    protected Vector<Coords> calculateArtyAutoHitHexes() {
        final String METHOD_NAME = "calculateArtyAutoHitHexes()";
        methodBegin(getClass(), METHOD_NAME);

        try {
            // currently returns no artillery hit spots
            // make an empty list
            final PlayerIDandList<Coords> artyAutoHitHexes = new PlayerIDandList<>();
            // attach my player id to it
            artyAutoHitHexes.setPlayerID(getLocalPlayer().getId());
            return artyAutoHitHexes;
        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    @Override
    protected void calculateDeployment() {
        final String METHOD_NAME = "calculateDeployment()";
        methodBegin(getClass(), METHOD_NAME);

        try {

            // get the first unit
<<<<<<< HEAD
            int entityNum = game.getFirstDeployableEntityNum();
            if (logger.getVerbosity().getLevel() > LogLevel.WARNING.getLevel()) {
                sendChat("deploying unit " + getEntity(entityNum).getChassis());
=======
            final int entityNum = game.getFirstDeployableEntityNum(game.getTurnForPlayer(localPlayerNumber));
            if (getLogger().getLogLevel(LOGGING_CATEGORY).toInt() > LogLevel.WARNING.toInt()) {
                sendChat("deploying unit " + getEntity(entityNum).getChassis(), LogLevel.INFO);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            }

            // on the list to be deployed get a set of all the
            final List<Coords> startingCoords = getStartingCoordsArray(game.getEntity(entityNum));
            if (0 == startingCoords.size()) {
                log(getClass(), METHOD_NAME, LogLevel.ERROR,
                    "No valid locations to deploy " + getEntity(entityNum).getDisplayName());
            }

            // get the coordinates I can deploy on
<<<<<<< HEAD
            Coords deployCoords = getFirstValidCoords(getEntity(entityNum), startingCoords);
            if (deployCoords == null) {
=======
            final Coords deployCoords = getFirstValidCoords(getEntity(entityNum), startingCoords);
            if (null == deployCoords) {
                // if I cannot deploy anywhere, then I get rid of the entity instead so that we may go about our business                
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                log(getClass(),
                        METHOD_NAME,
                        LogLevel.ERROR,
                        "getCoordsAround gave no location for "
                        + getEntity(entityNum).getChassis() + ". Removing unit.");
                
                sendDeleteEntity(entityNum);
                return;
            }

<<<<<<< HEAD
            // first coordinate that it is legal to put this unit on now find some sort of reasonable facing. If there
            // are deployed enemies, face them
=======
            // first coordinate that it is legal to put this unit on now find 
            // some sort of reasonable facing. If there are deployed enemies, 
            // face them
            
            // specifically, face the last deployed enemy.
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            int decentFacing = -1;
            for (final Entity e : getEnemyEntities()) {
                if (e.isDeployed() && (!e.isOffBoard())) {
                    decentFacing = deployCoords.direction(e.getPosition());
                    break;
                }
            }

<<<<<<< HEAD
            // if I haven't found a decent facing, then at least face towards the center of the board
            if (decentFacing == -1) {
                Coords center = new Coords(game.getBoard().getWidth() / 2, game
                                                                                   .getBoard().getHeight() / 2);
=======
            // if I haven't found a decent facing, then at least face towards 
            // the center of the board
            if (-1 == decentFacing) {
                final Coords center = new Coords(game.getBoard().getWidth() / 2,
                                           game.getBoard().getHeight() / 2);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                decentFacing = deployCoords.direction(center);
            }

            final Entity deployEntity = game.getEntity(entityNum);
            final IHex deployHex = game.getBoard().getHex(deployCoords);

            // Entity.elevatoinOccupied performs a null check on IHex
            int deployElevation = deployEntity.elevationOccupied(deployHex);

            // Compensate for hex elevation where != 0...
            deployElevation -= deployHex.getLevel();
            deploy(entityNum, deployCoords, decentFacing, deployElevation);
        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    /**
     * Returns the first set of valid deployment coordinates for the given unit out of the given deployment coordinates.
     * It's possible to return null, which indicates that there are no valid hexes for the given unit to deploy into.
     */
    @Nullable
    @Override
    protected Coords getFirstValidCoords(final Entity deployedUnit,
                                         final List<Coords> possibleDeployCoords) {
        if (Entity.ETYPE_GUN_EMPLACEMENT == (deployedUnit.getEntityType() & Entity.ETYPE_GUN_EMPLACEMENT)) {
            final List<Coords> validCoords = calculateTurretDeploymentLocations((GunEmplacement) deployedUnit,
                                                                                possibleDeployCoords);
            if (0 < validCoords.size()) {            
                return validCoords.get(0);
            }
            
            return null;
        } else {
            return super.getFirstValidCoords(deployedUnit, possibleDeployCoords);
        }
    }
    
    /**
     * Helper function that calculates the possible locations where a given gun emplacement can be deployed
     * @param deployedUnit The unit to check
     * @param possibleDeployCoords The list of possible deployment coordinates
     */
    private List<Coords> calculateTurretDeploymentLocations(final GunEmplacement deployedUnit,
                                                            final List<Coords> possibleDeployCoords) {
        // algorithm:
        // get all hexes in deployment zone with buildings
        // for each building, if deploying on the roof does not cause a stacking violation, add it to the list
        // sort the list in decreasing order based on CF then height
        final List<Coords> turretDeploymentLocations = new Vector<>();

        for (final Coords coords : possibleDeployCoords) {
            final Building building = game.getBoard().getBuildingAt(coords);
            final IHex hex = game.getBoard().getHex(coords);

            if (null != building) {
                final int buildingHeight = hex.terrainLevel(Terrains.BLDG_ELEV);
                
                // check stacking violation at the roof level
                final Entity violation = Compute.stackingViolation(game,
                                                                   deployedUnit,
                                                                   coords,
                                                                   buildingHeight,
                                                                   coords,
                                                                   null);
                // Ignore coords that could cause a stacking violation
                if (null == violation) {
                    turretDeploymentLocations.add(coords);
                }
            }
        }
        
        turretDeploymentLocations.sort(new Comparator<Coords>() {
            @Override
            public int compare(final Coords arg0,
                               final Coords arg1) {
                return calculateTurretDeploymentValue(arg1) - calculateTurretDeploymentValue(arg0);
            }
        });        
        return turretDeploymentLocations;
    }
    
    /**
     * Helper function that calculates the "utility" of placing a turret at the given coords
     * @param coords The location of the building being considered.
     * @return An "arbitrary" utility number
     */
    private int calculateTurretDeploymentValue(final Coords coords) {
        // algorithm: a building is valued by the following formula:
        //      (CF + height * 2) / # turrets placed on the roof
        //      This way, we will generally favor unpopulated higher CF buildings, 
        //      but have some wiggle room in case of a really tall high CF building
        final Building building = game.getBoard().getBuildingAt(coords);
        final IHex hex = game.getBoard().getHex(coords);
        final int turretCount = 1 + game.getGunEmplacements(coords).size();

        return (building.getCurrentCF(coords) + hex.terrainLevel(Terrains.BLDG_ELEV) * 2) / turretCount;
    }
    
    @Override
    protected void calculateFiringTurn() {
        final String METHOD_NAME = "calculateFiringTurn()";
        methodBegin(getClass(), METHOD_NAME);

        try {
<<<<<<< HEAD
            // get the first entity that can act this turn make sure weapons are loaded
            Entity shooter = game.getFirstEntity(getMyTurn());
=======
            // get the first entity that can act this turn make sure weapons 
            // are loaded
            final Entity shooter = game.getFirstEntity(getMyTurn());
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek

            // If my unit is forced to withdraw, don't fire unless I've been fired on.
            if (getForcedWithdrawal() && shooter.isCrippled()) {
<<<<<<< HEAD
                StringBuilder msg = new StringBuilder(shooter.getDisplayName()).append(" is crippled and withdrawing.");
=======
                final StringBuilder msg = new StringBuilder(shooter.getDisplayName())
                        .append(" is crippled and withdrawing.");
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                try {
                    if (attackedWhileFleeing.contains(shooter.getId())) {
                        msg.append("\n\tBut I was fired on, so I will return fire.");
                    } else {
                        msg.append("\n\tI will not fire so long as I'm not fired on.");
                        sendAttackData(shooter.getId(), new Vector<EntityAction>(0));
                        return;
                    }
                } finally {
                    log(getClass(), METHOD_NAME, LogLevel.INFO, msg);
                }
            }

            // Set up ammo conservation.
            final Map<Mounted, Double> ammoConservation = calcAmmoConservation(shooter);

            // entity that can act this turn make sure weapons are loaded
<<<<<<< HEAD
            FiringPlan plan = fireControl.getBestFiringPlan(shooter, getHonorUtil(), game, ammoConservation);
            if (plan != null) {
=======
            final FiringPlan plan = fireControl.getBestFiringPlan(shooter,
                                                                  getHonorUtil(),
                                                                  game,
                                                                  ammoConservation);
            if (null != plan) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                fireControl.loadAmmo(shooter, plan);
                plan.sortPlan();

                log(getClass(), METHOD_NAME, LogLevel.INFO, shooter.getDisplayName() + " - Best Firing Plan: " +
                                                            plan.getDebugDescription(LogLevel.DEBUG == getVerbosity()));

<<<<<<< HEAD
                // Add expected damage from the chosen FiringPlan to the damageMap for the target enemy.
                Integer targetId = new Integer(plan.getTarget().getTargetId());
                Double newDamage = damageMap.get(targetId)+plan.getExpectedDamage();
=======
                // Add expected damage from the chosen FiringPlan to the 
                // damageMap for the target enemy.
                final Integer targetId = plan.getTarget().getTargetId();
                final Double newDamage = damageMap.get(targetId) + plan.getExpectedDamage();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                damageMap.replace(targetId,newDamage);

                // tell the game I want to fire
                sendAttackData(shooter.getId(), plan.getEntityActionVector());

            } else {
                log(getClass(), METHOD_NAME, LogLevel.INFO, "No best firing plan for " + shooter.getDisplayName());
                sendAttackData(shooter.getId(), new Vector<EntityAction>(0));
            }
        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

<<<<<<< HEAD
    Map<Mounted, Double> calcAmmoConservation(Entity shooter) {
=======
    private Map<Mounted, Double> calcAmmoConservation(final Entity shooter) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        final String METHOD_NAME = "calcAmmoConservation(Entity)";
<<<<<<< HEAD
        final double aggroFactor = (10 - getBehaviorSettings().getHyperAggressionIndex()) * 2;
        StringBuilder msg = new StringBuilder("\nCalculating ammo conservation for ").append(shooter.getDisplayName());
=======
        final double aggroFactor =
                (10 - getBehaviorSettings().getHyperAggressionIndex()) * 2;
        final StringBuilder msg =
                new StringBuilder("\nCalculating ammo conservation for ")
                        .append(shooter.getDisplayName());
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        msg.append("\nAggression Factor = ").append(aggroFactor);

        try {
            final Map<AmmoType, Integer> ammoCounts = new HashMap<>();
            msg.append("\nPooling Ammo:");
            for (final Mounted ammo : shooter.getAmmo()) {
                final AmmoType ammoType = (AmmoType) ammo.getType();
                msg.append("\n\t").append(ammoType);
                if (ammoCounts.containsKey(ammoType)) {
                    ammoCounts.put(ammoType, ammoCounts.get(ammoType) + ammo.getUsableShotsLeft());
                    msg.append(" + ").append(ammo.getUsableShotsLeft()).append(" = ").append(ammoCounts.get(ammoType));
                    continue;
                }
                ammoCounts.put(ammoType, ammo.getUsableShotsLeft());
                msg.append(" + ").append(ammo.getUsableShotsLeft()).append(" = ").append(ammoCounts.get(ammoType));
            }

            final Map<Mounted, Double> ammoConservation = new HashMap<>();
            msg.append("\nCalculating conservation for each weapon");
            for (final Mounted weapon : shooter.getWeaponList()) {
                final WeaponType weaponType = (WeaponType) weapon.getType();
                msg.append("\n\t").append(weaponType);
                if (!(weaponType instanceof AmmoWeapon)) {
                    ammoConservation.put(weapon, 0.0);
                    msg.append(" doesn't use ammo.");
                    continue;
                }

                int ammoCount = 0;
                for (final AmmoType ammoType : ammoCounts.keySet()) {
                    if (!AmmoType.isAmmoValid(ammoType, weaponType)) {
                        continue;
                    }
                    ammoCount += ammoCounts.get(ammoType);
                }
                msg.append(" has ").append(ammoCount).append(" shots left");
<<<<<<< HEAD
                double toHitThreshold = Math.max(0.0, 1 - (ammoCount / aggroFactor));
                msg.append("; To Hit Threshold = ").append(new DecimalFormat("0.000").format(toHitThreshold));
=======
                final double toHitThreshold =
                        Math.max(0.0, 1 - (ammoCount / aggroFactor));
                msg.append("; To Hit Threshold = ")
                   .append(new DecimalFormat("0.000")
                                   .format(toHitThreshold));
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                ammoConservation.put(weapon, toHitThreshold);
            }

            return ammoConservation;
        } finally {
            log(getClass(), METHOD_NAME, LogLevel.DEBUG, msg);
        }
    }

    @Override
    protected Vector<Minefield> calculateMinefieldDeployment() {
        final String METHOD_NAME = "calculateMinefieldDeployment()";
        methodBegin(getClass(), METHOD_NAME);

        try {
            // currently returns no minefields
            // make an empty vector
            return new Vector<>();
        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    /**
     * Calculates the move index for the given unit.
     * In general, faster units and units closer to the enemy should move before others.
     * Additional modifiers for being prone, stealthed, unit type and so on are also factored in.
     *
     * @param entity The unit to be indexed.
     * @return The movement index of this unit.  May be positive or negative.  Higher index values should move first.
     */
<<<<<<< HEAD
    protected double calculateMoveIndex(Entity entity, StringBuilder msg) {
        msg.append("\n\tCalculating move index for ").append(entity.getDisplayName());
        StringBuilder modifiers = new StringBuilder();
        NumberFormat numberFormat = DecimalFormat.getInstance();
=======
    double calculateMoveIndex(final Entity entity,
                              final StringBuilder msg) {

        final double PRIORITY_PRONE = 1.1;
        final double PRIORITY_TANK = 1.5;
        final double PRIORITY_BA = 2;
        final double PRIORITY_INF = 3;
        final double PRIORITY_FALLBACK = 2;
        final double PRIORITY_COMMANDER = 0.5;
        final double PRIORITY_CIVILIAN = 5;
        final double PRIORITY_STEALTH = 1.0 / 3;

        msg.append("\n\tCalculating move index for ")
           .append(entity.getDisplayName());
        final StringBuilder modifiers = new StringBuilder();
        final NumberFormat numberFormat = DecimalFormat.getInstance();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        double total = 0;
        try {
            // Find out how fast this unit can move.
            int fastestMove = entity.getRunMP(true, false, false);
            if (entity.getJumpMP(true) > fastestMove) {
                fastestMove = entity.getJumpMP(true);
            }
            msg.append("\n\t\tFastest Move = ").append(fastestMove);

            // Get the distance to the nearest enemy.
<<<<<<< HEAD
            double distance = getPathRanker().distanceToClosestEnemy(entity, entity.getPosition(), game);
            msg.append("\n\t\tDistance to Nearest Enemy: ").append(numberFormat.format(distance));
=======
            final double distance =
                    getPathRanker().distanceToClosestEnemy(entity,
                                                           entity.getPosition(),
                                                           game);
            msg.append("\n\t\tDistance to Nearest Enemy: ")
               .append(numberFormat.format(distance));
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek

            // Get the ratio of distance to speed.
            // Faster units that are closer to the enemy should move later.
<<<<<<< HEAD
            if (fastestMove == 0) {
                total = distance * 2; // This unit should have already moved due to the isImmobilized check.
=======
            if (0 == fastestMove) {
                // This unit should have already moved due to the isImmobilized 
                // check.
                total = distance * 2;
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            } else {
                total = distance / fastestMove;
            }
            msg.append("\n\t\tDistance to Move Ratio (dist / move): ").append(numberFormat.format(total));

            // Prone enemies move sooner.
            if (entity.isProne()) {
                total *= PRIORITY_PRONE;
                modifiers.append("\tx1.1 (Is Prone)");
            }

            // If all else is equal, Infantry before Battle Armor before Tanks before Mechs.
            if (entity instanceof BattleArmor) {
                total *= PRIORITY_BA;
                modifiers.append("\tx2.0 (is BA)");
            } else if (entity instanceof Infantry) {
                total *= PRIORITY_INF;
                modifiers.append("\tx3.0 (is Inf)");
            } else if (entity instanceof Tank) {
                total *= PRIORITY_TANK;
                modifiers.append("\tx1.5 (is Tank)");
            }

            // Fleeing entities should move before those not fleeing.
            if (isFallingBack(entity)) {
                total *= PRIORITY_FALLBACK;
                modifiers.append("\tx2.0 (is Fleeing)");
            }

            // Move commanders after other units.
            if (entity.isCommander()) {
                total *= PRIORITY_COMMANDER;
                modifiers.append("\tx0.5 (is Commander)");
            }

            // Move civilian units before military.
            if (!entity.isMilitary()) {
                total *= PRIORITY_CIVILIAN;
                modifiers.append("\tx5.0 (is Civilian)");
            }

            // Move stealthy units later.
<<<<<<< HEAD
            if (entity.isStealthActive() || entity.isStealthOn() || entity.isVoidSigActive() || entity.isVoidSigOn()) {
                total /= 3;
=======
            if (entity.isStealthActive() || entity.isStealthOn() ||
                entity.isVoidSigActive() || entity.isVoidSigOn()) {
                total *= PRIORITY_STEALTH;
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                modifiers.append("\tx1/3 (is Stealthed)");
            }

            return total;
        } finally {
            msg.append("\n\t\tModifiers:").append(modifiers);
            msg.append("\n\t\tTotal = ").append(numberFormat.format(total));
        }
    }

    /**
     * Loops through the list of entities controlled by this Princess instance and decides which should be moved first.
     * Immobile units and ejected mechwarriors/crews will be moved first.  After that, each unit is given an index
     * via the {@link #calculateMoveIndex(Entity, StringBuilder)} method.  The highest index value is moved first.
     *
     * @return The entity that should be moved next.
     */
    protected Entity getEntityToMove() {

        // first move useless units: immobile units, ejected mechwarrior, etc
        Entity movingEntity = null;
        final List<Entity> myEntities = getEntitiesOwned();
        double highestIndex = -Double.MAX_VALUE;
        final StringBuilder msg = new StringBuilder("Deciding who to move next.");
        for (final Entity entity : myEntities) {
            msg.append("\n\tUnit ").append(entity.getDisplayName());
            if (entity.isOffBoard() || (null == entity.getPosition())
                || !entity.isSelectableThisTurn()
                || !getGame().getTurn().isValidEntity(entity, getGame())) {
                msg.append("cannot be moved.");
                continue;
            }

            // Move immobile units & ejected mechwarriors immediately.
            if (isImmobilized(entity) && !(entity instanceof Infantry)) {
                msg.append("is immobile.");
                movingEntity = entity;
                break;
            }
            if (entity instanceof MechWarrior) {
                msg.append("is ejected crew.");
                movingEntity = entity;
                break;
            }

            // If I only have 1 unit, no need to calculate an index.
            if (1 == myEntities.size()) {
                msg.append("is my only unit.");
                movingEntity = entity;
                break;
            }

            // We will move the entity with the highest index.
<<<<<<< HEAD
            double moveIndex = calculateMoveIndex(entity, msg);
            msg.append("\n\thas index ").append(moveIndex).append(" vs ").append(highestIndex);
=======
            final double moveIndex = calculateMoveIndex(entity, msg);
            msg.append("\n\thas index ").append(moveIndex).append(" vs ")
               .append(highestIndex);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            if (moveIndex >= highestIndex) {
                highestIndex = moveIndex;
                movingEntity = entity;
            }
        }

<<<<<<< HEAD
        LogLevel level = (movingEntity == null ? LogLevel.WARNING : LogLevel.DEBUG);
=======
        final LogLevel level = (null == movingEntity ? LogLevel.WARNING :
                                LogLevel.DEBUG);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        log(getClass(), "getEntityToMove()", level, msg.toString());

        return movingEntity;
    }

    @Override
    protected MovePath calculateMoveTurn() {
        final String METHOD_NAME = "calculateMoveTurn()";
        methodBegin(getClass(), METHOD_NAME);

        try {
            return continueMovementFor(getEntityToMove());
        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    @Override
    protected PhysicalOption calculatePhysicalTurn() {
        final String METHOD_NAME = "calculatePhysicalTurn()";
        methodBegin(getClass(), METHOD_NAME);

        try {
            initialize();
            // get the first entity that can act this turn
            final Entity attacker = game.getFirstEntity(getMyTurn());

            // If my unit is forced to withdraw, don't attack unless I've been attacked.
            if (getForcedWithdrawal() && attacker.isCrippled()) {
<<<<<<< HEAD
                StringBuilder msg = new StringBuilder(attacker.getDisplayName()).append(" is crippled and withdrawing.");
=======
                final StringBuilder msg =
                        new StringBuilder(attacker.getDisplayName())
                                .append(" is crippled and withdrawing.");
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                if (attackedWhileFleeing.contains(attacker.getId())) {
                    msg.append("\n\tBut I was fired on, so I will hit back.");
                } else {
                    msg.append("\n\tI will not attack so long as I'm not fired on.");
                    return null;
                }
                log(getClass(), METHOD_NAME, LogLevel.INFO, msg);
            }

            PhysicalInfo best_attack = null;
            final int firstEntityId = attacker.getId();
            int nextEntityId = firstEntityId;

            // this is an array of all my enemies
            final List<Entity> enemies = getEnemyEntities();

            do {
                final Entity hitter = game.getEntity(nextEntityId);
                nextEntityId = game.getNextEntityNum(hitter.getId());

                if (null == hitter.getPosition()) {
                    continue;
                }

                log(getClass(), METHOD_NAME, LogLevel.DEBUG,
                    "Calculating physical attacks for " + hitter.getDisplayName());

                // cycle through potential enemies
                for (final Entity e : enemies) {
                    if (null == e.getPosition()) {
                        continue; // Skip enemies not on the board.
                    }
                    if (1 < hitter.getPosition().distance(e.getPosition())) {
                        continue;
                    }
                    if (getHonorUtil().isEnemyBroken(e.getTargetId(), e.getOwnerId(), getForcedWithdrawal())) {
                        continue;
                    }

<<<<<<< HEAD
                    PhysicalInfo right_punch = new PhysicalInfo(hitter, e, PhysicalAttackType.RIGHT_PUNCH, game, this,
                                                                false);
=======
                    final PhysicalInfo right_punch =
                            new PhysicalInfo(hitter,
                                             e,
                                             PhysicalAttackType.RIGHT_PUNCH,
                                             game,
                                             this,

                                             false);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                    fireControl.calculateUtility(right_punch);
<<<<<<< HEAD
                    if (right_punch.getUtility() > 0) {
                        if ((best_attack == null) || (right_punch.getUtility() > best_attack.getUtility())) {
=======
                    if (0 < right_punch.getUtility()) {
                        if ((null == best_attack) ||
                            (right_punch.getUtility() > best_attack.getUtility())) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                            best_attack = right_punch;
                        }
                    }
<<<<<<< HEAD
                    PhysicalInfo left_punch = new PhysicalInfo(
                            hitter, e, PhysicalAttackType.LEFT_PUNCH, game, this, false);
=======
                    final PhysicalInfo left_punch = new PhysicalInfo(
                            hitter,
                            e,
                            PhysicalAttackType.LEFT_PUNCH,
                            game,
                            this,
                            false);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                    fireControl.calculateUtility(left_punch);
<<<<<<< HEAD
                    if (left_punch.getUtility() > 0) {
                        if ((best_attack == null)
                            || (left_punch.getUtility() > best_attack.getUtility())) {
=======
                    if (0 < left_punch.getUtility()) {
                        if ((null == best_attack)
                            || (left_punch.getUtility() >
                                best_attack.getUtility())) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                            best_attack = left_punch;
                        }
                    }
<<<<<<< HEAD
                    PhysicalInfo right_kick = new PhysicalInfo(
                            hitter, e, PhysicalAttackType.RIGHT_KICK, game, this, false);
=======
                    final PhysicalInfo right_kick = new PhysicalInfo(
                            hitter,
                            e,
                            PhysicalAttackType.RIGHT_KICK,
                            game,
                            this,
                            false);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                    fireControl.calculateUtility(right_kick);
<<<<<<< HEAD
                    if (right_kick.getUtility() > 0) {
                        if ((best_attack == null)
                            || (right_kick.getUtility() > best_attack.getUtility())) {
=======
                    if (0 < right_kick.getUtility()) {
                        if ((null == best_attack)
                            || (right_kick.getUtility() >
                                best_attack.getUtility())) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                            best_attack = right_kick;
                        }
                    }
<<<<<<< HEAD
                    PhysicalInfo left_kick = new PhysicalInfo(
                            hitter, e, PhysicalAttackType.LEFT_KICK, game, this, false);
=======
                    final PhysicalInfo left_kick = new PhysicalInfo(
                            hitter,
                            e,
                            PhysicalAttackType.LEFT_KICK,
                            game,
                            this,
                            false);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                    fireControl.calculateUtility(left_kick);
<<<<<<< HEAD
                    if (left_kick.getUtility() > 0) {
                        if ((best_attack == null)
                            || (left_kick.getUtility() > best_attack.getUtility())) {
=======
                    if (0 < left_kick.getUtility()) {
                        if ((null == best_attack)
                            || (left_kick.getUtility() >
                                best_attack.getUtility())) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                            best_attack = left_kick;
                        }
                    }

                }
<<<<<<< HEAD
                if (best_attack != null) {
                    log(getClass(), METHOD_NAME, LogLevel.INFO, "Best Physical Attack is " +
                                                                best_attack.getDebugDescription());
=======
                if (null != best_attack) {
                    log(getClass(), METHOD_NAME, LogLevel.INFO,
                        "Best Physical Attack is " +
                        best_attack.getDebugDescription());
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                } else {
                    log(getClass(), METHOD_NAME, LogLevel.INFO, "No useful physical attack to be made");
                }
                if (null != best_attack) {
                    return best_attack.getAsPhysicalOption();
                }
            } while (nextEntityId != firstEntityId);

            // no one can hit anything anymore, so give up
            return null;
        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

<<<<<<< HEAD
    protected boolean wantsToFallBack(Entity entity) {
=======
    boolean wantsToFallBack(final Entity entity) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        return (entity.isCrippled() && getForcedWithdrawal()) || getFallBack();
    }

    protected IMoralUtil getMoralUtil() {
        return moralUtil;
    }

<<<<<<< HEAD
    protected boolean isFallingBack(Entity entity) {
=======
    boolean isFallingBack(final Entity entity) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        return getMyFleeingEntities().contains(entity.getId());
    }

<<<<<<< HEAD
    protected boolean mustFleeBoard(Entity entity) {
=======
    boolean mustFleeBoard(final Entity entity) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        if (!isFallingBack(entity)) {
            return false;
        }
        if (!entity.canFlee()) {
            return false;
        }
<<<<<<< HEAD
        if (getPathRanker().distanceToHomeEdge(entity.getPosition(), getHomeEdge(), getGame()) > 0) {
=======
        if (0 < getPathRanker().distanceToHomeEdge(entity.getPosition(),
                                                   getHomeEdge(), getGame())) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            return false;
        }
        //noinspection RedundantIfStatement
        if (!getFleeBoard() && !(entity.isCrippled() && getForcedWithdrawal())) {
            return false;
        }
        return true;
    }

<<<<<<< HEAD
    protected boolean isImmobilized(Entity mover) {
=======
    boolean isImmobilized(final Entity mover) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        final String METHOD_NAME = "isImmobilized(Entity, MovePath)";
        if (mover.isImmobile() && !mover.isShutDown()) {
            log(getClass(), METHOD_NAME, LogLevel.INFO, "Is truly immobile.");
            return true;
        }
        if (1 > mover.getRunMP()) {
            log(getClass(), METHOD_NAME, LogLevel.INFO, "Has 0 movement.");
            return true;
        }
        if (!(mover instanceof Mech)) {
            return false;
        }

        final Mech mech = (Mech) mover;
        if (!mech.isProne() && !mech.isStuck() && !mech.isStalled()) {
            return false;
        }

        final MovePath movePath = new MovePath(getGame(), mover);

<<<<<<< HEAD
        // For a normal fall-shame setting (index 5), our threshold should be a 10+ piloting roll.
        int threshold;
=======
        // For a normal fall-shame setting (index 5), our threshold should be 
        // a 10+ piloting roll.
        final int threshold;
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        switch (getBehaviorSettings().getFallShameIndex()) {
            case 10:
                threshold = 7;
                break;
            case 9:
                threshold = 8;
                break;
            case 8:
            case 7:
                threshold = 9;
                break;
            case 6:
            case 5:
                threshold = 10;
                break;
            case 4:
                threshold = 11;
                break;
            case 3:
                threshold = 12;
                break;
            default:
                threshold = 13; // Actually impossible.
        }

        // If we're prone, see if we have a chance of getting up.
        if (mech.isProne()) {
            if (mech.cannotStandUpFromHullDown()) {
                log(getClass(), METHOD_NAME, LogLevel.INFO, "Cannot stand up.");
                return true;
            }

            final MovePath.MoveStepType type = (getBooleanOption(OptionsConstants.ADVGRNDMOV_TACOPS_CAREFUL_STAND) ?
                                                MovePath.MoveStepType.CAREFUL_STAND :
                                                MovePath.MoveStepType.GET_UP);
            final MoveStep getUp = new MoveStep(movePath, type);

<<<<<<< HEAD
            // If our odds to get up are equal to or worse than the threshold, consider ourselves immobile.
            PilotingRollData target = mech.checkGetUp(getUp,
                    movePath.getLastStepMovementType());
=======
            // If our odds to get up are equal to or worse than the threshold, 
            // consider ourselves immobile.
            final PilotingRollData target = mech.checkGetUp(getUp,
                                                            movePath.getLastStepMovementType());
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            log(getClass(), METHOD_NAME, LogLevel.INFO,
                "Need to roll " + target.getValue() + " to stand and our tolerance is " + threshold);
            return (target.getValue() >= threshold);
        }

        // How likely are we to get unstuck.
        final MovePath.MoveStepType type = MovePath.MoveStepType.FORWARDS;
        final MoveStep walk = new MoveStep(movePath, type);
        final IHex hex = getHex(mech.getPosition());
        final PilotingRollData target = mech.checkBogDown(walk,
                                                          movePath.getLastStepMovementType(), hex,
                                                          mech.getPriorPosition(), mech.getPosition(), hex.getLevel(),
                                                          false);
        log(getClass(), METHOD_NAME, LogLevel.INFO,
            "Need to roll " + target.getValue() + " to get unstuck and our tolerance is " + threshold);
        return (target.getValue() >= threshold);
    }

<<<<<<< HEAD
    protected boolean getBooleanOption(String name) {
=======
    boolean getBooleanOption(final String name) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        return getGame().getOptions().booleanOption(name);
    }

    protected IHex getHex(final Coords coords) {
        return getBoard().getHex(coords);
    }

<<<<<<< HEAD
    protected ArrayList<RankedPath> rankPaths(List<MovePath> paths, int maxRange, double fallTollerance,
                                              int startingHomeDistance,
                                              List<Entity> enemies, List<Entity> friends) {
        return getPathRanker().rankPaths(paths, getGame(), maxRange, fallTollerance, startingHomeDistance,
                                         enemies, friends);
=======
    private ArrayList<RankedPath> rankPaths(final List<MovePath> paths,
                                            final int maxRange,
                                            final double fallTollerance,
                                            final int startingHomeDistance,
                                            final List<Entity> enemies,
                                            final List<Entity> friends) {
        return getPathRanker().rankPaths(paths,
                                         getGame(),
                                         maxRange,
                                         fallTollerance,
                                         startingHomeDistance,
                                         enemies,
                                         friends);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    }

    @Override
    protected MovePath continueMovementFor(final Entity entity) {
        final String METHOD_NAME = "continueMovementFor(Entity)";
        methodBegin(getClass(), METHOD_NAME);

        if (null == entity) {
            throw new NullPointerException("Entity is null.");
        }

        try {
            // figure out who moved last, and who's move lists need to be updated

            // moves this entity during movement phase
            log(getClass(), METHOD_NAME, "Moving " + entity.getDisplayName() + " (ID " + entity.getId() + ")");
            getPrecognition().ensureUpToDate();

            if (isFallingBack(entity)) {
                String msg = entity.getDisplayName();
                if (getFallBack()) {
                    msg += " is falling back.";
                } else if (entity.isCrippled()) {
                    msg += " is crippled and withdrawing.";
                }
                log(getClass(), METHOD_NAME, msg);
                sendChat(msg, LogLevel.WARNING);

                // If this entity is falling back, able to flee the board, on its home edge, and must flee, do so.
                if (mustFleeBoard(entity)) {
                    final MovePath mp = new MovePath(game, entity);
                    mp.addStep(MovePath.MoveStepType.FLEE);
                    return mp;
                }

                // If we want to flee, but cannot, eject the crew.
                if (isImmobilized(entity) && entity.isEjectionPossible()) {
                    msg = entity.getDisplayName() + " is immobile.  Abandoning unit.";
                    log(getClass(), METHOD_NAME, LogLevel.INFO, msg);
                    sendChat(msg, LogLevel.WARNING);
                    final MovePath mp = new MovePath(game, entity);
                    mp.addStep(MovePath.MoveStepType.EJECT);
                    return mp;
                }
            }

<<<<<<< HEAD
            List<MovePath> paths = getPrecognition().getPathEnumerator().getUnitPaths().get(entity.getId());
=======
            final List<MovePath> paths =
                    getPrecognition().getPathEnumerator()
                                     .getUnitPaths()
                                     .get(entity.getId());
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek

            if (null == paths) {
                log(getClass(), METHOD_NAME, LogLevel.WARNING,
                    "No valid paths found.");
                return new MovePath(game, entity);
            }

<<<<<<< HEAD
            double thisTimeEstimate = (paths.size() * moveEvaluationTimeEstimate) / 1e3;
            if (logger.getVerbosity().getLevel() > LogLevel.WARNING.getLevel()) {
=======
            final double thisTimeEstimate =
                    (paths.size() * moveEvaluationTimeEstimate) / 1e3;
            if (getLogger().getLogLevel(LOGGING_CATEGORY).toInt() >
                LogLevel.WARNING.toInt()) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                String timeestimate = "unknown.";
                if (0 != thisTimeEstimate) {
                    timeestimate = Integer.toString((int) thisTimeEstimate)
                                   + " seconds";
                }
                final String message = "Moving " + entity.getChassis() + ". "
                                       + Long.toString(paths.size())
                                       + " paths to consider.  Estimated time to completion: "
                                       + timeestimate;
                sendChat(message, LogLevel.INFO);
            }

            final long startTime = System.currentTimeMillis();
            getPathRanker().initUnitTurn(entity, getGame());
<<<<<<< HEAD
            double fallTolerance = getBehaviorSettings().getFallShameIndex() / 10d;
            int startingHomeDistance = getPathRanker().distanceToHomeEdge(entity.getPosition(),
                                                                          getBehaviorSettings().getHomeEdge(),
                                                                          getGame());
            List<RankedPath> rankedpaths = rankPaths(paths, entity.getMaxWeaponRange(), fallTolerance,
                                                     startingHomeDistance, getEnemyEntities(),
                                                     getFriendEntities());
            long stop_time = System.currentTimeMillis();
=======
            final double fallTolerance =
                    getBehaviorSettings().getFallShameIndex() / 10d;
            final int startingHomeDistance = getPathRanker().distanceToHomeEdge(
                    entity.getPosition(),
                    getBehaviorSettings().getHomeEdge(),
                    getGame());
            final List<RankedPath> rankedpaths = rankPaths(paths,
                                                           entity.getMaxWeaponRange(),
                                                           fallTolerance,
                                                           startingHomeDistance,
                                                           getEnemyEntities(),
                                                           getFriendEntities());
            final long stop_time = System.currentTimeMillis();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek

            // update path evaluation time estimate
<<<<<<< HEAD
            double updatedEstimate = ((double) (stop_time - startTime)) / ((double) paths.size());
            if (moveEvaluationTimeEstimate == 0) {
=======
            final double updatedEstimate =
                    ((double) (stop_time - startTime)) / ((double) paths.size());
            if (0 == moveEvaluationTimeEstimate) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                moveEvaluationTimeEstimate = updatedEstimate;
            }
<<<<<<< HEAD
            moveEvaluationTimeEstimate = 0.5 * (updatedEstimate + moveEvaluationTimeEstimate);
            if (rankedpaths.size() == 0) {
=======
            moveEvaluationTimeEstimate =
                    0.5 * (updatedEstimate + moveEvaluationTimeEstimate);
            if (0 == rankedpaths.size()) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                return new MovePath(game, entity);
            }
<<<<<<< HEAD
            log(getClass(), METHOD_NAME, "Path ranking took " + Long.toString(stop_time - startTime) + " millis");
            RankedPath bestpath = getPathRanker().getBestPath(rankedpaths);
            log(getClass(), METHOD_NAME, LogLevel.INFO, "Best Path: " + bestpath.path.toString() + "  Rank: "
                                                        + bestpath.rank);
            return bestpath.path;
=======
            log(getClass(), METHOD_NAME,
                "Path ranking took " +
                Long.toString(stop_time - startTime) + " millis");
            final RankedPath bestpath = getPathRanker().getBestPath(rankedpaths);
            log(getClass(), METHOD_NAME, LogLevel.INFO,
                "Best Path: " + bestpath.getPath() + "  Rank: "
                + bestpath.getRank());
            
            performPathPostProcessing(bestpath);
            
            return bestpath.getPath();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
        } finally {
            precognition.unPause();
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    @Override
    protected void initFiring() {
        final String METHOD_NAME = "initFiring()";
        methodBegin(getClass(), METHOD_NAME);

        try {
            initialize();

            // ----Debugging: print out any errors made in guessing to hit
            // values-----
<<<<<<< HEAD
            List<Entity> ents = game.getEntitiesVector();
            for (Entity ent : ents) {
                String errors = fireControl.checkAllGuesses(ent, game);
                if (errors != null) {
=======
            final List<Entity> ents = game.getEntitiesVector();
            for (final Entity ent : ents) {
                final String errors = fireControl.checkAllGuesses(ent, game);
                if (!StringUtil.isNullOrEmpty(errors)) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                    log(getClass(), METHOD_NAME, LogLevel.WARNING, errors);
                }
            }
            // -----------------------------------------------------------------------

            // Pick up on any infantry/BA in buildings post-movement and shoot
            // their buildings, similar to the turret check
            // pre-movement(infantry can move so we only set target buildings
            // after they do).
            final Enumeration<Building> buildings = game.getBoard().getBuildings();
            while (buildings.hasMoreElements()) {
                final Building bldg = buildings.nextElement();
                final Enumeration<Coords> bldgCoords = bldg.getCoords();
                while (bldgCoords.hasMoreElements()) {
                    final Coords coords = bldgCoords.nextElement();
                    for (final Entity entity : game.getEntitiesVector(coords)) {
                        final BuildingTarget bt = new BuildingTarget(coords,
                                                                     game.getBoard(), false);
                        // Want to target buildings with hostile infantry/BA
                        // inside them, since there's no other way to attack
                        // them.
                        if (isEnemyInfantry(entity, coords)
                                && Compute.isInBuilding(game, entity)
                                && !entity.isHidden()) {
                            fireControl.getAdditionalTargets().add(bt);
                            sendChat("Building in Hex "
                                     + coords.toFriendlyString()
                                     + " designated target due to"
                                     + " infantry inside building.", LogLevel.INFO);
                        }
                    }
                }
            }

            //Next, collect the ID's of each potential target and store them in the damageMap for allocating damage during firing.

            //Reset the map generated during the movement phase- The available targets may have changed during that time(ejections, enemies fleeing, etc).
            damageMap.clear();
            //Now add an ID for each possible target.
<<<<<<< HEAD
            List<Targetable> potentialTargets = fireControl.getAllTargetableEnemyEntities(getLocalPlayer(), getGame());
            for (Targetable target : potentialTargets) {
                damageMap.put(new Integer(target.getTargetId()), new Double(0));
=======
            final List<Targetable> potentialTargets =
                    fireControl.getAllTargetableEnemyEntities(getLocalPlayer(),
                                                              getGame());
            for (final Targetable target : potentialTargets) {
                damageMap.put(target.getTargetId(), 0d);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            }

        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    private void checkForDishonoredEnemies() {
        final String METHOD_NAME = "checkForDishonoredEnemies()";

        final StringBuilder msg = new StringBuilder("Checking for dishonored enemies.");

        try {
            // If the Forced Withdrawal rule is not turned on, then it's a fight to the death anyway.
            if (!getForcedWithdrawal()) {
                msg.append("\n\tForced withdrawal turned off.");
                return;
            }

            for (final Entity mine : getEntitiesOwned()) {

                // Who just attacked me?
                final Collection<Integer> attackedBy = mine.getAttackedByThisTurn();
                if (attackedBy.isEmpty()) {
                    continue;
                }

                // Is my unit trying to withdraw?
                final boolean fleeing = getMyFleeingEntities().contains(mine.getId());

                for (final int id : attackedBy) {
                    final Entity entity = getGame().getEntity(id);
                    if (null == entity) {
                        continue;
                    }

                    if (getHonorUtil().isEnemyBroken(entity.getTargetId(), entity.getOwnerId(),
                                                     getForcedWithdrawal()) || !entity.isMilitary()) {
                        // If he'd just continued running, I would have let him go, but the bastard shot at me!
                        msg.append("\n\t")
                           .append(entity.getDisplayName())
                           .append("dishonored himself by attacking me even though he is ");
                        if (!entity.isMilitary()) {
                            msg.append("a civilian.");
                        } else {
                            msg.append("fleeing.");
                        }
                        getHonorUtil().setEnemyDishonored(entity.getOwnerId());
                        continue;
                    }

                    // He shot me while I was running away!
                    if (fleeing) {
                        msg.append("\n\t")
                           .append(entity.getDisplayName())
                           .append("dishonored himself by attacking a fleeing unit (")
                           .append(mine.getDisplayName())
                           .append(").");
                        getHonorUtil().setEnemyDishonored(entity.getOwnerId());
                        attackedWhileFleeing.add(mine.getId());
                    }
                }
            }
        } finally {
            log(getClass(), METHOD_NAME, LogLevel.INFO, msg);
        }
    }

    private void checkForBrokenEnemies() {

        // If the Forced Withdrawal rule is not turned on, then it's a fight to the death anyway.
        if (!getForcedWithdrawal()) {
            return;
        }

        for (final Entity entity : getEnemyEntities()) {
            getHonorUtil().checkEnemyBroken(entity, getForcedWithdrawal());
        }
    }

    @Override
    protected void initMovement() {
        final String METHOD_NAME = "initMovement()";
        methodBegin(getClass(), METHOD_NAME);

        try {
            initialize();
            checkMoral();

            // reset strategic targets
<<<<<<< HEAD
            fireControl.setAdditionalTargets(new ArrayList<Targetable>());
            for (Coords strategicTarget : getStrategicBuildingTargets()) {
                if (game.getBoard().getBuildingAt(strategicTarget) == null) {
                    sendChat("No building to target in Hex " + strategicTarget.toFriendlyString() + ", ignoring.");
=======
            fireControl.setAdditionalTargets(new ArrayList<>());
            for (final Coords strategicTarget : getStrategicBuildingTargets()) {
                if (null == game.getBoard().getBuildingAt(strategicTarget)) {
                    sendChat("No building to target in Hex " +
                             strategicTarget.toFriendlyString() +
                             ", ignoring.", LogLevel.INFO);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                } else {
<<<<<<< HEAD
                    fireControl.getAdditionalTargets().add(new BuildingTarget(strategicTarget, game.getBoard(), false));
                    sendChat("Building in Hex " + strategicTarget.toFriendlyString() + " designated strategic target.");
=======
                    fireControl.getAdditionalTargets().add(
                            new BuildingTarget(strategicTarget,
                                               game.getBoard(),
                                               false));
                    sendChat("Building in Hex " +
                             strategicTarget.toFriendlyString() +
                             " designated strategic target.", LogLevel.INFO);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                }
            }

            // Pick up on any turrets and shoot their buildings as well.
            final Enumeration<Building> buildings = game.getBoard().getBuildings();
            while (buildings.hasMoreElements()) {
                final Building bldg = buildings.nextElement();
                final Enumeration<Coords> bldgCoords = bldg.getCoords();
                while (bldgCoords.hasMoreElements()) {
<<<<<<< HEAD
                    Coords coords = bldgCoords.nextElement();
                    for (Entity entity : game.getEntitiesVector(coords, true)) {
                        BuildingTarget bt = new BuildingTarget(coords, game.getBoard(), false);
=======
                    final Coords coords = bldgCoords.nextElement();
                    for (final Entity entity : game.getEntitiesVector(coords,
                                                                      true)) {
                        final BuildingTarget bt = new BuildingTarget(coords,
                                                                     game.getBoard(),
                                                                     false);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                        if (isEnemyGunEmplacement(entity, coords)) {
                            fireControl.getAdditionalTargets().add(bt);
<<<<<<< HEAD
                            sendChat("Building in Hex " + coords.toFriendlyString()
                                     + " designated target due to Gun Emplacement.");
=======
                            sendChat("Building in Hex " +
                                     coords.toFriendlyString()
                                     + " designated target due to Gun Emplacement.", LogLevel.INFO);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                        }
                    }
                }
            }

            //Next, collect the ID's of each potential target and store them in the damageMap for allocating damage during movement.
            //Right now, this doesn't get filled because I can't find where FiringPlans for potential move paths are calculated(pretty sure they are, though). This needs to be fixed at some point.

            //Reset last round's damageMap
            damageMap.clear();
            //Now add an ID for each possible target.
<<<<<<< HEAD
            List<Targetable> potentialTargets = fireControl.getAllTargetableEnemyEntities(getLocalPlayer(), getGame());
            for (Targetable target : potentialTargets) {
                damageMap.put(new Integer(target.getTargetId()), new Double(0));
=======
            final List<Targetable> potentialTargets =
                    fireControl.getAllTargetableEnemyEntities(getLocalPlayer(),
                                                              getGame());
            for (final Targetable target : potentialTargets) {
                damageMap.put(target.getTargetId(), 0d);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            }

        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    public IGame getGame() {
        return game;
    }

    @Override
    public void initialize() {
        final String METHOD_NAME = "initialize()";
        methodBegin(getClass(), METHOD_NAME);

        try {
            if (initialized) {
                return; // no need to initialize twice
            }
            final PathSearcher pathSearcher = new PathSearcher(this);
            pathRanker = new BasicPathRanker(this);
            pathSearcher.ranker = pathRanker;
            fireControl = new FireControl(this);
            pathRanker.setFireControl(fireControl);
            pathRanker.setPathEnumerator(precognition.getPathEnumerator());

<<<<<<< HEAD
            // Pick up any turrets and add their buildings to the strategic targets list.
            Enumeration<Building> buildings = getGame().getBoard().getBuildings();
=======
            // Pick up any turrets and add their buildings to the strategic 
            // targets list.
            final Enumeration<Building> buildings = getGame().getBoard()
                                                             .getBuildings();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            while (buildings.hasMoreElements()) {
                final Building bldg = buildings.nextElement();
                final Enumeration<Coords> bldgCoords = bldg.getCoords();
                while (bldgCoords.hasMoreElements()) {
<<<<<<< HEAD
                    Coords coords = bldgCoords.nextElement();
                    for (Entity entity : game.getEntitiesVector(coords, true)) {
=======
                    final Coords coords = bldgCoords.nextElement();
                    for (final Entity entity : game.getEntitiesVector(coords,
                                                                      true)) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                        if (isEnemyGunEmplacement(entity, coords)) {
                            getStrategicBuildingTargets().add(coords);
<<<<<<< HEAD
                            sendChat("Building in Hex " + coords.toFriendlyString() +
                                     " designated target due to Gun Emplacement.");
=======
                            sendChat("Building in Hex " +
                                     coords.toFriendlyString() +
                                     " designated target due to Gun Emplacement.", LogLevel.INFO);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
                        }
                    }
                }
            }

            initialized = true;
            BotGeometry.debugSelfTest(this);
        } finally {
            methodEnd(getClass(), METHOD_NAME);
        }
    }

    private boolean isEnemyGunEmplacement(final Entity entity,
                                          final Coords coords) {
        return entity instanceof GunEmplacement
               && entity.getOwner().isEnemyOf(getLocalPlayer())
               && !getStrategicBuildingTargets().contains(coords)
               && (null != entity.getCrew()) && !entity.getCrew().isDead();
    }

<<<<<<< HEAD
    private boolean isEnemyInfantry(Entity entity, Coords coords) {
        return (entity instanceof Infantry
                || entity instanceof BattleArmor)
                && entity.getOwner().isEnemyOf(getLocalPlayer())
                && !getStrategicBuildingTargets().contains(coords);
=======
    private boolean isEnemyInfantry(final Entity entity,
                                    final Coords coords) {
        return (entity instanceof Infantry)
               && entity.getOwner().isEnemyOf(getLocalPlayer())
               && !getStrategicBuildingTargets().contains(coords);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    }

    @Override
    public synchronized void die() {
        super.die();
        if (null != precognition) {
            precognition.signalDone();
            precogThread.interrupt();
        }
    }

    @Override
    protected void processChat(final GamePlayerChatEvent ge) {
        chatProcessor.processChat(ge, this);
    }

<<<<<<< HEAD
    public void log(Class<?> callingClass, String methodName, LogLevel level,
                    String msg) {
        logger.log(callingClass, methodName, level, msg);
=======
    public void log(final Class<?> callingClass,
                    final String methodName,
                    final LogLevel level,
                    final String msg) {
        getLogger().log(callingClass, methodName, level, msg);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    }

<<<<<<< HEAD
    public void log(Class<?> callingClass, String methodName, LogLevel level, StringBuilder msg) {
        if (msg == null) {
=======
    public void log(final Class<?> callingClass,
                    final String methodName,
                    final LogLevel level,
                    final StringBuilder msg) {
        if (null == msg) {
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            return;
        }
        log(callingClass, methodName, level, msg.toString());
    }

    public void log(final Class<?> callingClass,
                    final String methodName,
                    final String msg) {
        log(callingClass, methodName, LogLevel.DEBUG, msg);
    }

<<<<<<< HEAD
    public void log(Class<?> callingClass, String methodName, LogLevel level,
                    Throwable t) {
        logger.log(callingClass, methodName, level, t);
=======
    public void log(final Class<?> callingClass,
                    final String methodName,
                    final LogLevel level,
                    final Throwable t) {
        getLogger().log(callingClass, methodName, level, t);
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
    }

    public void log(final Class<?> callingClass,
                    final String methodName,
                    final Throwable t) {
        log(callingClass, methodName, LogLevel.ERROR, t);
    }

    public void methodBegin(final Class<?> callingClass,
                            final String methodName) {
        log(callingClass, methodName, LogLevel.DEBUG, "method begin");
    }

    public void methodEnd(final Class<?> callingClass,
                          final String methodName) {
        log(callingClass, methodName, LogLevel.DEBUG, "method end");
    }

    public HomeEdge getHomeEdge() {
        return getBehaviorSettings().getHomeEdge();
    }

    public int calculateAdjustment(final String ticks) {
        
        int adjustment = 0;
        if (StringUtil.isNullOrEmpty(ticks)) {
            return 0;
        }
        for (final char tick : ticks.toCharArray()) {
            if (PLUS == tick) {
                adjustment++;
            } else if (MINUS == tick) {
                adjustment--;
            } else {
                log(getClass(), "calculateAdjustment", LogLevel.WARNING, "Invalid tick: '" + tick + "'.");
            }
        }
        return adjustment;
    }

    @Override
    protected void checkMoral() {
        moralUtil.checkMoral(behaviorSettings.isForcedWithdrawal(), behaviorSettings.getBraveryIndex(),
                             behaviorSettings.getSelfPreservationIndex(), getLocalPlayer(), game);
    }

    public IHonorUtil getHonorUtil() {
        return honorUtil;
    }

    @Override
    public void endOfTurnProcessing() {
        logger.methodBegin(getClass(), "endOfTurnProcessing()");
        checkForDishonoredEnemies();
        updateMyFleeingEntities();
        checkForBrokenEnemies();
        logger.methodEnd(getClass(), "endOfTurnProcessing()");
    }

    Set<Integer> getMyFleeingEntities() {
        return myFleeingEntities;
    }

    private void updateMyFleeingEntities() {
        final String METHOD_NAME = "updateMyFleeingEntities()";

<<<<<<< HEAD
        StringBuilder msg = new StringBuilder("Updating my list of falling back units.");
=======
        final StringBuilder msg =
                new StringBuilder("Updating my list of falling back units.");
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek

        try {
            // If the Forced Withdrawal rule is not turned on, then it's a fight to the death anyway.
            if (!getForcedWithdrawal()) {
                msg.append("\n\tForced withdrawal turned off.");
                return;
            }

            for (final Entity mine : getEntitiesOwned()) {
                if (myFleeingEntities.contains(mine.getId())) {
                    continue;
                }
                if (wantsToFallBack(mine)) {
                    msg.append("\n\tAdding ").append(mine.getDisplayName());
                    myFleeingEntities.add(mine.getId());
                }
            }
        } finally {
            log(getClass(), METHOD_NAME, LogLevel.INFO, msg);
        }
    }

    protected void handlePacket(final Packet c) {
        final String METHOD_NAME = "handlePacket()";
        final StringBuilder msg = new StringBuilder("Received packet, cmd: "
                                                    + c.getCommand());
        try {
            super.handlePacket(c);
            getPrecognition().handlePacket(c);
        }
        finally {
            log(getClass(), METHOD_NAME, LogLevel.TRACE, msg);
        }
    }
    
    /**
     * sends a load game file to the server
     */
    public void sendLoadGame(final File f) {
        precognition.resetGame();
        super.sendLoadGame(f);
    }
    
    protected void disconnected() {
        if (null != precognition) {
            precognition.signalDone();
            precogThread.interrupt();
        }
        super.disconnected();
    }

    public int getHighestEnemyInitiativeId() {
        int highestEnemyInitiativeBonus = -1;
        int highestEnemyInitiativeId = -1;
<<<<<<< HEAD
        for (Entity entity : getEnemyEntities()) {
            int initBonus = entity.getHQIniBonus() + entity.getQuirkIniBonus(); //+ entity.getMDIniBonus() Removed in IO
=======
        for (final Entity entity : getEnemyEntities()) {
            final int initBonus = entity.getHQIniBonus() +
                                  entity.getQuirkIniBonus();
>>>>>>> branch 'master' of https://github.com/MegaMek/megamek
            if (initBonus > highestEnemyInitiativeBonus) {
                highestEnemyInitiativeBonus = initBonus;
                highestEnemyInitiativeId = entity.getId();
            }
        }
        return highestEnemyInitiativeId;
    }
    
    /**
     * Helper function to perform some modifications to a given path
     * Currently insinuates an "evasion" step for aircraft that will not be shooting.
     * @param path The path to process
     */
    private void performPathPostProcessing(final RankedPath path) {
        // if we're an airborne aircraft
        // and we're not going to do any damage anyway
        // and we can do so without causing a PSR
        // then evade
        if(path.getPath().getEntity().isAirborne() &&
           (0 == path.getExpectedDamage()) &&
           (path.getPath().getMpUsed() <= AeroGroundPathFinder.calculateMaxSafeThrust((IAero) path.getPath()
                                                                                                  .getEntity()) - 2)) {
            path.getPath().addStep(MoveStepType.EVADE);
        }
    }

    public void sendChat(final String message,
                         final LogLevel logLevel) {
        if (logLevel.getLevel().isGreaterOrEqual(getVerbosity().getLevel())) {
            return;
        }
        super.sendChat(message);
    }
}
