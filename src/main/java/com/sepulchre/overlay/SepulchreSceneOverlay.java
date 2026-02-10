package com.sepulchre.overlay;

import com.sepulchre.SepulchrePlugin;
import com.sepulchre.config.HighlightStyle;
import com.sepulchre.config.PortalDisplayMode;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import com.sepulchre.model.CrossbowStatue;
import com.sepulchre.model.LightningStrike;
import com.sepulchre.model.SwordStatue;
import com.sepulchre.model.WizardStatue;
import com.sepulchre.util.SepulchreConstants;
import com.sepulchre.util.SkillObstacleRequirements;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Set;

public class SepulchreSceneOverlay extends Overlay
{
	private static final Stroke DEFAULT_STROKE = new BasicStroke(1);

	private final Client client;
	private final SepulchrePlugin plugin;
	private final SepulchreConfig config;
	private final ObstacleHandler obstacleHandler;
	private final ModelOutlineRenderer modelOutlineRenderer;
	private final SkillObstacleRequirements requirements;

	private Stroke cachedFireStroke;
	private int cachedFireStrokeWidth = -1;

	private Stroke cachedProjectileStroke;
	private int cachedProjectileStrokeWidth = -1;

	private Stroke cachedCrossbowStroke;
	private int cachedCrossbowStrokeWidth = -1;

	private Color cachedFireBorderColor;
	private int cachedFireBorderOpacity = -1;
	private Color cachedFireBaseColor;

	private final BorderColorCache obeliskBorderCache = new BorderColorCache();
	private final BorderColorCache coffinBorderCache = new BorderColorCache();
	private final BorderColorCache skillObstacleBorderCache = new BorderColorCache();
	private final BorderColorCache skillObstacleMissingBorderCache = new BorderColorCache();
	private final BorderColorCache yellowPortalBorderCache = new BorderColorCache();
	private final BorderColorCache bluePortalBorderCache = new BorderColorCache();
	private final BorderColorCache navigationBorderCache = new BorderColorCache();

	@Inject
	public SepulchreSceneOverlay(Client client, SepulchrePlugin plugin, SepulchreConfig config,
		ObstacleHandler obstacleHandler, ModelOutlineRenderer modelOutlineRenderer,
		SkillObstacleRequirements requirements)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.obstacleHandler = obstacleHandler;
		this.modelOutlineRenderer = modelOutlineRenderer;
		this.requirements = requirements;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isInSepulchre())
		{
			return null;
		}

		int playerPlane = client.getPlane();

		renderLightning(graphics, playerPlane);
		renderCrossbowStatues(graphics, playerPlane);
		renderWizardStatues(graphics, playerPlane);
		renderSwordStatues(graphics, playerPlane);
		renderBoltNpcs(graphics, playerPlane);
		renderSwordNpcs(graphics, playerPlane);
		renderPortals(graphics, playerPlane);
		renderMagicalObelisks(graphics, playerPlane);
		renderCoffins(graphics, playerPlane);
		renderSkillObstacles(graphics, playerPlane);
		renderStairs(graphics, playerPlane);
		renderPlatforms(graphics, playerPlane);
		renderGates(graphics, playerPlane);
		renderPlayerImmunity(graphics);

		return null;
	}

	private Stroke getDangerBorderStroke()
	{
		int width = Math.max(1, Math.min(5, config.dangerBorderWidth()));
		if (width != cachedFireStrokeWidth)
		{
			cachedFireStrokeWidth = width;
			cachedFireStroke = new BasicStroke(width);
		}
		return cachedFireStroke;
	}

	private Stroke getProjectileBorderStroke()
	{
		int width = Math.max(1, Math.min(5, config.projectileBorderWidth()));
		if (width != cachedProjectileStrokeWidth)
		{
			cachedProjectileStrokeWidth = width;
			cachedProjectileStroke = new BasicStroke(width);
		}
		return cachedProjectileStroke;
	}

	private Stroke getCrossbowBorderStroke()
	{
		int width = Math.max(1, Math.min(5, config.crossbowBorderWidth()));
		if (width != cachedCrossbowStrokeWidth)
		{
			cachedCrossbowStrokeWidth = width;
			cachedCrossbowStroke = new BasicStroke(width);
		}
		return cachedCrossbowStroke;
	}

	private Color getDangerBorderColor(Color baseColor)
	{
		int borderOpacity = Math.max(0, Math.min(255, config.dangerBorderOpacity()));
		if (borderOpacity != cachedFireBorderOpacity || !baseColor.equals(cachedFireBaseColor))
		{
			cachedFireBorderOpacity = borderOpacity;
			cachedFireBaseColor = baseColor;
			cachedFireBorderColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), borderOpacity);
		}
		return cachedFireBorderColor;
	}

	private static Color toOpaque(Color color)
	{
		return color.getAlpha() == 255 ? color : new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
	}

	private Color getCrossbowBorderColor()
	{
		return config.crossbowBorderColor();
	}

	private Color getObeliskBorderColor()
	{
		return obeliskBorderCache.get(config.obeliskColor());
	}

	private Color getCoffinBorderColor()
	{
		return coffinBorderCache.get(config.coffinColor());
	}

	private Color getSkillObstacleBorderColor()
	{
		return skillObstacleBorderCache.get(config.skillObstacleColor());
	}

	private Color getSkillObstacleMissingBorderColor()
	{
		return skillObstacleMissingBorderCache.get(config.skillObstacleMissingReqColor());
	}

	private Color getYellowPortalBorderColor()
	{
		return yellowPortalBorderCache.get(config.portalYellowColor());
	}

	private Color getBluePortalBorderColor()
	{
		return bluePortalBorderCache.get(config.portalBlueColor());
	}

	private Color getNavigationBorderColor()
	{
		return navigationBorderCache.get(config.navigationColor());
	}

	private void renderTilePolygon(Graphics2D graphics, Polygon poly, Color fillColor, Color borderColor, Stroke stroke)
	{
		renderShape(graphics, poly, fillColor, borderColor, stroke);
	}

	private Polygon getTilePolygon(WorldPoint worldPoint, int playerPlane)
	{
		if (worldPoint.getPlane() != playerPlane)
		{
			return null;
		}

		LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
		if (localPoint == null)
		{
			return null;
		}

		return Perspective.getCanvasTilePoly(client, localPoint);
	}

	private WorldPoint getCanonicalFromInstance(WorldPoint instancePoint)
	{
		if (instancePoint == null)
		{
			return null;
		}
		LocalPoint localPoint = LocalPoint.fromWorld(client, instancePoint);
		if (localPoint == null)
		{
			return null;
		}
		return WorldPoint.fromLocalInstance(client, localPoint);
	}

	private void renderLightning(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightLightning())
		{
			return;
		}

		Color fillColor = config.lightningFillColor();
		Color borderColor = config.lightningBorderColor();

		for (LightningStrike lightning : obstacleHandler.getActiveLightning())
		{
			WorldPoint lightningLocation = lightning.getLocation();
			if (!obstacleHandler.shouldShowForCurrentRoute(lightningLocation))
			{
				continue;
			}

			Polygon poly = getTilePolygon(lightningLocation, playerPlane);
			if (poly != null)
			{
				renderTilePolygon(graphics, poly, fillColor, borderColor, DEFAULT_STROKE);
			}
		}
	}

	private void renderCrossbowStatues(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightCrossbows())
		{
			return;
		}

		Color dangerColor = config.crossbowColor();
		Color dangerBorderColor = getCrossbowBorderColor();
		HighlightStyle style = config.crossbowHighlightStyle();
		Stroke crossbowStroke = getCrossbowBorderStroke();

		for (CrossbowStatue statue : obstacleHandler.getCrossbowStatues())
		{
			GameObject statueObj = statue.getGameObject();
			if (statueObj.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(statueObj.getLocalLocation()))
			{
				continue;
			}

			if (statue.isDangerous())
			{
				renderGameObject(graphics, statue.getGameObject(), dangerColor, dangerBorderColor, style, crossbowStroke);
			}
		}
	}

	private void renderGameObject(Graphics2D graphics, net.runelite.api.GameObject gameObject,
		Color fillColor, Color borderColor, HighlightStyle style, Stroke stroke)
	{
		Shape shape = null;

		switch (style)
		{
			case CLICKBOX:
				shape = gameObject.getClickbox();
				break;
			case HULL:
				shape = gameObject.getConvexHull();
				break;
			case TILE:
				LocalPoint localPoint = gameObject.getLocalLocation();
				if (localPoint != null)
				{
					shape = Perspective.getCanvasTilePoly(client, localPoint);
				}
				break;
			default:
				break;
		}

		renderShape(graphics, shape, fillColor, borderColor, stroke);
	}

	private void renderWizardStatues(Graphics2D graphics, int playerPlane)
	{
		boolean showTickCounter = config.wizardTickCounter();
		boolean showDanger = config.showDanger();
		boolean showWarning = config.showWarning();

		if (!showTickCounter && !showDanger && !showWarning)
		{
			return;
		}

		Stroke dangerStroke = getDangerBorderStroke();

		for (WizardStatue statue : obstacleHandler.getWizardStatues())
		{
			GameObject statueObj = statue.getGameObject();
			if (statueObj.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(statueObj.getLocalLocation()))
			{
				continue;
			}

			if (!statue.isConfirmedActiveOrUnknown())
			{
				continue;
			}

			if (statue.isSafe())
			{
				continue;
			}

			boolean isFiring = statue.isFiring();
			Color color = isFiring ? config.dangerColor() : config.warningColor();

			if (showTickCounter)
			{
				String tickDisplay = statue.getDisplayTicks();
				LocalPoint statueLocal = statue.getGameObject().getLocalLocation();
				if (statueLocal != null)
				{
					Point textLocation = Perspective.getCanvasTextLocation(client, graphics, statueLocal, tickDisplay, 0);
					if (textLocation != null)
					{
						OverlayUtil.renderTextLocation(graphics, textLocation, tickDisplay, color);
					}
				}
			}

			boolean shouldShowTiles = (isFiring && showDanger) || (!isFiring && showWarning);
			if (!shouldShowTiles)
			{
				continue;
			}

			Color borderColor = getDangerBorderColor(color);

			for (WorldPoint fireTile : statue.getFireTiles())
			{
				Polygon poly = getTilePolygon(fireTile, playerPlane);
				if (poly != null)
				{
					renderTilePolygon(graphics, poly, color, borderColor, dangerStroke);
				}
			}
		}
	}

	private void renderSwordStatues(Graphics2D graphics, int playerPlane)
	{
		boolean showTickCounter = config.knightTickCounter();
		boolean showDanger = config.showDanger();
		boolean showWarning = config.showWarning();

		if (!showTickCounter && !showDanger && !showWarning)
		{
			return;
		}

		Stroke dangerStroke = getDangerBorderStroke();

		for (SwordStatue statue : obstacleHandler.getSwordStatues())
		{
			GameObject statueObj = statue.getGameObject();
			if (statueObj.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(statueObj.getLocalLocation()))
			{
				continue;
			}

			if (!statue.isInDangerousState())
			{
				continue;
			}

			boolean isImminent = statue.isInImminentDangerState();
			Color fillColor = isImminent ? config.dangerColor() : config.warningColor();
			Color borderColor = getDangerBorderColor(fillColor);

			WorldPoint dangerCenter = statue.getDangerZoneCenter();
			if (dangerCenter != null)
			{
				LocalPoint dangerLocal = LocalPoint.fromWorld(client, dangerCenter);
				if (dangerLocal != null)
				{
					boolean shouldShowTiles = (isImminent && showDanger) || (!isImminent && showWarning);
					if (shouldShowTiles)
					{
						Polygon poly = Perspective.getCanvasTileAreaPoly(client, dangerLocal, 3);
						if (poly != null)
						{
							renderTilePolygon(graphics, poly, fillColor, borderColor, dangerStroke);
						}
					}

					if (showTickCounter)
					{
						String tickDisplay = statue.getDisplayTicks();
						if (tickDisplay != null)
						{
							Point textLocation = Perspective.getCanvasTextLocation(client, graphics, dangerLocal, tickDisplay, 0);
							if (textLocation != null)
							{
								OverlayUtil.renderTextLocation(graphics, textLocation, tickDisplay, toOpaque(fillColor));
							}
						}
					}
				}
			}
		}
	}

	private void renderBoltNpcs(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightProjectiles())
		{
			return;
		}

		Color fillColor = config.boltFillColor();
		Color borderColor = config.boltBorderColor();
		Stroke stroke = getProjectileBorderStroke();

		boolean routeFilterEnabled = config.filterByRoute();

		for (NPC npc : obstacleHandler.getBoltNpcs())
		{
			if (npc.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			LocalPoint lp = npc.getLocalLocation();
			if (lp == null)
			{
				continue;
			}

			CrossbowStatue sourceStatue = findFiringCrossbowStatue(npc.getWorldLocation(), routeFilterEnabled);
			if (sourceStatue == null && routeFilterEnabled)
			{
				continue;
			}

			Polygon poly = Perspective.getCanvasTilePoly(client, lp);
			if (poly != null)
			{
				renderTilePolygon(graphics, poly, fillColor, borderColor, stroke);
			}
		}
	}

	private void renderSwordNpcs(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightProjectiles())
		{
			return;
		}

		Color fillColor = config.swordFillColor();
		Color borderColor = config.swordBorderColor();
		Stroke stroke = getProjectileBorderStroke();

		boolean routeFilterEnabled = config.filterByRoute();

		for (NPC npc : obstacleHandler.getSwordNpcs())
		{
			if (npc.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			LocalPoint lp = npc.getLocalLocation();
			if (lp == null)
			{
				continue;
			}

			NPCComposition composition = npc.getTransformedComposition();
			int size = (composition != null) ? composition.getSize() : 1;
			WorldPoint npcSW = npc.getWorldLocation();
			int centerOffset = size / 2;
			WorldPoint npcCenter = new WorldPoint(
				npcSW.getX() + centerOffset,
				npcSW.getY() + centerOffset,
				npcSW.getPlane()
			);
			SwordStatue sourceStatue = findFiringSwordStatue(npcCenter, routeFilterEnabled);
			if (sourceStatue == null && routeFilterEnabled)
			{
				continue;
			}

			Polygon poly = Perspective.getCanvasTileAreaPoly(client, lp, size);
			if (poly != null)
			{
				renderTilePolygon(graphics, poly, fillColor, borderColor, stroke);
			}
		}
	}

	private void renderPortals(Graphics2D graphics, int playerPlane)
	{
		renderPortalSet(graphics, playerPlane, obstacleHandler.getActiveYellowPortals(),
			config.portalYellowColor(), getYellowPortalBorderColor(),
			config.yellowPortalDisplay(), true);

		renderPortalSet(graphics, playerPlane, obstacleHandler.getActiveBluePortals(),
			config.portalBlueColor(), getBluePortalBorderColor(),
			config.bluePortalDisplay(), false);
	}

	private void renderPortalSet(Graphics2D graphics, int playerPlane, Set<WorldPoint> portals,
		Color fillColor, Color borderColor, PortalDisplayMode displayMode, boolean isYellow)
	{
		if (displayMode == PortalDisplayMode.NONE)
		{
			return;
		}

		boolean showTile = displayMode.showTile();
		boolean showCountdown = displayMode.showCountdown();

		for (WorldPoint portalLocation : portals)
		{
			if (!obstacleHandler.shouldShowForCurrentRoute(portalLocation))
			{
				continue;
			}

			Polygon poly = getTilePolygon(portalLocation, playerPlane);
			if (poly == null)
			{
				continue;
			}

			if (showTile)
			{
				renderTilePolygon(graphics, poly, fillColor, borderColor, DEFAULT_STROKE);
			}

			if (showCountdown)
			{
				int remainingTicks = isYellow
					? obstacleHandler.getYellowPortalRemainingTicks(portalLocation)
					: obstacleHandler.getBluePortalRemainingTicks(portalLocation);
				if (remainingTicks > 0)
				{
					LocalPoint localPoint = LocalPoint.fromWorld(client, portalLocation);
					if (localPoint != null)
					{
						String countdownText = String.valueOf(remainingTicks - 1);
						Point textLocation = Perspective.getCanvasTextLocation(
							client, graphics, localPoint, countdownText, 0);
						if (textLocation != null)
						{
							OverlayUtil.renderTextLocation(graphics, textLocation, countdownText, borderColor);
						}
					}
				}
			}
		}
	}

	private void renderMagicalObelisks(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightObelisk())
		{
			return;
		}

		int playerMaxFloor = obstacleHandler.getPlayerMaxFloor();
		boolean doorClosed = obstacleHandler.isDoorToNextFloorClosed();
		int currentFloor = obstacleHandler.getCurrentFloor();

		boolean shouldHighlight = currentFloor == playerMaxFloor || doorClosed;
		if (!shouldHighlight)
		{
			return;
		}

		Color fillColor = config.obeliskColor();
		Color borderColor = getObeliskBorderColor();

		for (GameObject obelisk : obstacleHandler.getMagicalObelisks())
		{
			if (obelisk.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(obelisk.getLocalLocation()))
			{
				continue;
			}

			renderShape(graphics, obelisk.getConvexHull(), fillColor, borderColor, DEFAULT_STROKE);
		}
	}

	private void renderPlayerImmunity(Graphics2D graphics)
	{
		if (!config.playerImmunityOutline() && !config.playerImmunityFill())
		{
			return;
		}

		if (!obstacleHandler.isPlayerImmune())
		{
			return;
		}

		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return;
		}

		Color color = config.playerImmunityColor();

		if (config.playerImmunityFill())
		{
			Shape hull = player.getConvexHull();
			if (hull != null)
			{
				Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), config.playerImmunityFillOpacity());
				graphics.setColor(fillColor);
				graphics.fill(hull);
			}
		}

		if (config.playerImmunityOutline())
		{
			modelOutlineRenderer.drawOutline(player, 4, color, 4);
		}
	}

	private void renderCoffins(Graphics2D graphics, int playerPlane)
	{
		Color fillColor = config.coffinColor();
		Color borderColor = getCoffinBorderColor();

		if (obstacleHandler.isCoffinLootingEnabledForCurrentFloor())
		{
			for (GameObject coffin : obstacleHandler.getCoffins())
			{
				if (coffin.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldShowForCurrentRoute(coffin.getLocalLocation()))
				{
					continue;
				}

				if (isObjectInAnyState(coffin, SepulchreConstants.COFFIN_MORPH_OPEN_IDS))
				{
					continue;
				}

				renderTileObject(graphics, coffin, fillColor, borderColor);
			}
		}

		if (obstacleHandler.isGrandCoffinLootingEnabled())
		{
			for (GameObject grandCoffin : obstacleHandler.getGrandCoffins())
			{
				if (grandCoffin.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldShowForCurrentRoute(grandCoffin.getLocalLocation()))
				{
					continue;
				}

				if (isObjectInState(grandCoffin, SepulchreConstants.GRAND_COFFIN_MORPH_OPEN))
				{
					continue;
				}

				renderTileObject(graphics, grandCoffin, fillColor, borderColor);
			}
		}

		boolean doorClosed = obstacleHandler.isDoorToNextFloorClosed();
		Color barrierFillColor = doorClosed ? config.skillObstacleMissingReqColor() : config.skillObstacleColor();
		Color barrierBorderColor = doorClosed ? getSkillObstacleMissingBorderColor() : getSkillObstacleBorderColor();

		for (WallObject barrier : obstacleHandler.getFloor5Barriers())
		{
			if (barrier.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(barrier.getLocalLocation()))
			{
				continue;
			}

			renderTileObject(graphics, barrier, barrierFillColor, barrierBorderColor);
		}
	}

	private void renderSkillObstacles(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightSkillObstacles())
		{
			return;
		}

		if (!obstacleHandler.isCoffinLootingEnabledForCurrentFloor())
		{
			return;
		}

		Color normalFillColor = config.skillObstacleColor();
		Color normalBorderColor = getSkillObstacleBorderColor();
		Color missingFillColor = config.skillObstacleMissingReqColor();
		Color missingBorderColor = getSkillObstacleMissingBorderColor();

		boolean doorClosed = obstacleHandler.isDoorToNextFloorClosed();

		if (config.highlightBridges())
		{
			boolean canBuild = !doorClosed && requirements.canBuildBridge();

			for (GroundObject bridge : obstacleHandler.getBridges())
			{
				if (bridge.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldShowForCurrentRoute(bridge.getLocalLocation()))
				{
					continue;
				}

				if (!obstacleHandler.shouldHighlightObstacle(bridge))
				{
					continue;
				}

				boolean useNormalColor = canBuild || obstacleHandler.hasObstacleBeenUsed(bridge);
				Color fillColor = useNormalColor ? normalFillColor : missingFillColor;
				Color borderColor = useNormalColor ? normalBorderColor : missingBorderColor;

				renderTileObject(graphics, bridge, fillColor, borderColor);
			}
		}

		if (config.highlightGrapples())
		{
			boolean canGrapple = !doorClosed && requirements.canUseGrapple();
			Color fillColor = canGrapple ? normalFillColor : missingFillColor;
			Color borderColor = canGrapple ? normalBorderColor : missingBorderColor;

			for (GameObject grapple : obstacleHandler.getGrapples())
			{
				if (grapple.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldShowForCurrentRoute(grapple.getLocalLocation()))
				{
					continue;
				}

				if (!obstacleHandler.shouldHighlightObstacle(grapple))
				{
					continue;
				}

				renderTileObject(graphics, grapple, fillColor, borderColor);
			}
		}

		if (config.highlightPortalFrames())
		{
			boolean canPortal = !doorClosed && requirements.canConjurePortal();
			Color fillColor = canPortal ? normalFillColor : missingFillColor;
			Color borderColor = canPortal ? normalBorderColor : missingBorderColor;

			for (GameObject portalFrame : obstacleHandler.getPortalFrames())
			{
				if (portalFrame.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldShowForCurrentRoute(portalFrame.getLocalLocation()))
				{
					continue;
				}

				if (!obstacleHandler.shouldHighlightObstacle(portalFrame))
				{
					continue;
				}

				renderTileObject(graphics, portalFrame, fillColor, borderColor);
			}
		}

		if (config.highlightBraziers())
		{
			boolean canBrazier = !doorClosed && requirements.canLightBrazier();
			Color fillColor = canBrazier ? normalFillColor : missingFillColor;
			Color borderColor = canBrazier ? normalBorderColor : missingBorderColor;

			for (GameObject brazier : obstacleHandler.getBraziers())
			{
				if (brazier.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldShowForCurrentRoute(brazier.getLocalLocation()))
				{
					continue;
				}

				if (!isObjectInAnyState(brazier, SepulchreConstants.BRAZIER_UNSACRIFICED_MORPHS))
				{
					continue;
				}

				renderTileObject(graphics, brazier, fillColor, borderColor);
			}

			for (GameObject barrier : obstacleHandler.getHolyBarriers())
			{
				if (barrier.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldShowForCurrentRoute(barrier.getLocalLocation()))
				{
					continue;
				}

				if (!obstacleHandler.shouldHighlightBarrier(barrier))
				{
					continue;
				}

				renderTileObject(graphics, barrier, normalFillColor, normalBorderColor);
			}
		}
	}

	private void renderTileObject(Graphics2D graphics, TileObject tileObject, Color fillColor, Color borderColor)
	{
		renderShape(graphics, tileObject.getClickbox(), fillColor, borderColor, DEFAULT_STROKE);
	}

	private void renderShape(Graphics2D graphics, Shape shape, Color fillColor, Color borderColor, Stroke stroke)
	{
		if (shape == null)
		{
			return;
		}
		graphics.setStroke(stroke);
		graphics.setColor(borderColor);
		graphics.draw(shape);
		graphics.setColor(fillColor);
		graphics.fill(shape);
	}

	private void renderStairs(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightNavigation())
		{
			return;
		}

		Color fillColor = config.navigationColor();
		Color borderColor = getNavigationBorderColor();

		boolean hideEndStairs = obstacleHandler.getCurrentFloor() == obstacleHandler.getPlayerMaxFloor()
			|| obstacleHandler.isDoorToNextFloorClosed();

		for (GameObject stair : obstacleHandler.getStairs())
		{
			if (stair.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(stair.getLocalLocation()))
			{
				continue;
			}

			if (hideEndStairs && SepulchreConstants.END_FLOOR_STAIRS_IDS.contains(stair.getId()))
			{
				continue;
			}

			renderTileObject(graphics, stair, fillColor, borderColor);
		}
	}

	private void renderPlatforms(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightNavigation())
		{
			return;
		}

		Color fillColor = config.navigationColor();
		Color borderColor = getNavigationBorderColor();

		for (GroundObject platform : obstacleHandler.getPlatforms())
		{
			if (platform.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(platform.getLocalLocation()))
			{
				continue;
			}

			renderTileObject(graphics, platform, fillColor, borderColor);
		}
	}

	private void renderGates(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightNavigation())
		{
			return;
		}

		Color fillColor = config.navigationColor();
		Color borderColor = getNavigationBorderColor();

		for (WallObject gate : obstacleHandler.getGates())
		{
			if (gate.getWorldLocation().getPlane() != playerPlane)
			{
				continue;
			}

			if (!obstacleHandler.shouldShowForCurrentRoute(gate.getLocalLocation()))
			{
				continue;
			}

			renderTileObject(graphics, gate, fillColor, borderColor);
		}
	}

	private ObjectComposition getImpostor(GameObject gameObject)
	{
		ObjectComposition composition = client.getObjectDefinition(gameObject.getId());
		if (composition == null || composition.getImpostorIds() == null)
		{
			return null;
		}
		return composition.getImpostor();
	}

	private boolean isObjectInState(GameObject gameObject, int morphId)
	{
		ObjectComposition impostor = getImpostor(gameObject);
		return impostor != null && impostor.getId() == morphId;
	}

	private boolean isObjectInAnyState(GameObject gameObject, java.util.Set<Integer> morphIds)
	{
		ObjectComposition impostor = getImpostor(gameObject);
		return impostor != null && morphIds.contains(impostor.getId());
	}

	private CrossbowStatue findFiringCrossbowStatue(WorldPoint projectileLocation, boolean onlyCurrentRoute)
	{
		return findFiringStatue(obstacleHandler.getCrossbowStatues(), CrossbowStatue::getGameObject,
			projectileLocation, onlyCurrentRoute);
	}

	private SwordStatue findFiringSwordStatue(WorldPoint projectileLocation, boolean onlyCurrentRoute)
	{
		return findFiringStatue(obstacleHandler.getSwordStatues(), SwordStatue::getGameObject,
			projectileLocation, onlyCurrentRoute);
	}

	private <T> T findFiringStatue(Iterable<T> statues, java.util.function.Function<T, GameObject> getObj,
		WorldPoint projectileLocation, boolean onlyCurrentRoute)
	{
		T best = null;
		int bestDistance = Integer.MAX_VALUE;

		for (T statue : statues)
		{
			GameObject obj = getObj.apply(statue);
			WorldPoint statueLoc = obj.getWorldLocation();
			if (statueLoc.getPlane() != projectileLocation.getPlane())
			{
				continue;
			}

			if (onlyCurrentRoute && !obstacleHandler.shouldShowForCurrentRoute(obj.getLocalLocation()))
			{
				continue;
			}

			if (!isOnFiringLine(projectileLocation, statueLoc, obj.getOrientation(), 1))
			{
				continue;
			}

			int distance = statueLoc.distanceTo(projectileLocation);
			if (distance < bestDistance)
			{
				bestDistance = distance;
				best = statue;
			}
		}

		return best;
	}

	private boolean isOnFiringLine(WorldPoint projectile, WorldPoint statueSW, int orientation, int statueWidth)
	{
		int px = projectile.getX();
		int py = projectile.getY();
		int sx = statueSW.getX();
		int sy = statueSW.getY();

		if (orientation >= 1280 && orientation < 1792)
		{
			return py >= sy && py < sy + statueWidth && px > sx;
		}
		else if (orientation >= 256 && orientation < 768)
		{
			return py >= sy && py < sy + statueWidth && px < sx;
		}
		else if (orientation >= 768 && orientation < 1280)
		{
			return px >= sx && px < sx + statueWidth && py > sy;
		}
		else
		{
			return px >= sx && px < sx + statueWidth && py < sy;
		}
	}

	private static class BorderColorCache
	{
		private Color lastInput;
		private Color cachedBorder;

		Color get(Color color)
		{
			if (!color.equals(lastInput))
			{
				lastInput = color;
				cachedBorder = toOpaque(color);
			}
			return cachedBorder;
		}
	}
}
