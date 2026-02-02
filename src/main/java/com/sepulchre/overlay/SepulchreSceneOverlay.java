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

	private Color cachedFireBorderColor;
	private int cachedFireBorderOpacity = -1;
	private Color cachedFireBaseColor;

	private Color cachedCrossbowColor;
	private Color cachedCrossbowBorder;
	private Color cachedObeliskColor;
	private Color cachedObeliskBorder;
	private Color cachedCoffinColor;
	private Color cachedCoffinBorder;
	private Color cachedSkillObstacleColor;
	private Color cachedSkillObstacleBorder;
	private Color cachedSkillObstacleMissingColor;
	private Color cachedSkillObstacleMissingBorder;
	private Color cachedYellowPortalColor;
	private Color cachedYellowPortalBorder;
	private Color cachedBluePortalColor;
	private Color cachedBluePortalBorder;

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
		Color color = config.crossbowColor();
		if (!color.equals(cachedCrossbowColor))
		{
			cachedCrossbowColor = color;
			cachedCrossbowBorder = toOpaque(color);
		}
		return cachedCrossbowBorder;
	}

	private Color getObeliskBorderColor()
	{
		Color color = config.obeliskColor();
		if (!color.equals(cachedObeliskColor))
		{
			cachedObeliskColor = color;
			cachedObeliskBorder = toOpaque(color);
		}
		return cachedObeliskBorder;
	}

	private Color getCoffinBorderColor()
	{
		Color color = config.coffinColor();
		if (!color.equals(cachedCoffinColor))
		{
			cachedCoffinColor = color;
			cachedCoffinBorder = toOpaque(color);
		}
		return cachedCoffinBorder;
	}

	private Color getSkillObstacleBorderColor()
	{
		Color color = config.skillObstacleColor();
		if (!color.equals(cachedSkillObstacleColor))
		{
			cachedSkillObstacleColor = color;
			cachedSkillObstacleBorder = toOpaque(color);
		}
		return cachedSkillObstacleBorder;
	}

	private Color getSkillObstacleMissingBorderColor()
	{
		Color color = config.skillObstacleMissingReqColor();
		if (!color.equals(cachedSkillObstacleMissingColor))
		{
			cachedSkillObstacleMissingColor = color;
			cachedSkillObstacleMissingBorder = toOpaque(color);
		}
		return cachedSkillObstacleMissingBorder;
	}

	private Color getYellowPortalBorderColor()
	{
		Color color = config.portalYellowColor();
		if (!color.equals(cachedYellowPortalColor))
		{
			cachedYellowPortalColor = color;
			cachedYellowPortalBorder = toOpaque(color);
		}
		return cachedYellowPortalBorder;
	}

	private Color getBluePortalBorderColor()
	{
		Color color = config.portalBlueColor();
		if (!color.equals(cachedBluePortalColor))
		{
			cachedBluePortalColor = color;
			cachedBluePortalBorder = toOpaque(color);
		}
		return cachedBluePortalBorder;
	}

	private void renderTilePolygon(Graphics2D graphics, Polygon poly, Color fillColor, Color borderColor, Stroke stroke)
	{
		graphics.setStroke(stroke);
		graphics.setColor(borderColor);
		graphics.draw(poly);
		graphics.setColor(fillColor);
		graphics.fill(poly);
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
			Polygon poly = getTilePolygon(lightning.getLocation(), playerPlane);
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

		Color color = config.crossbowColor();
		Color borderColor = getCrossbowBorderColor();
		HighlightStyle style = config.crossbowHighlightStyle();

		for (CrossbowStatue statue : obstacleHandler.getCrossbowStatues())
		{
			WorldPoint statueLocation = statue.getGameObject().getWorldLocation();
			if (statueLocation.getPlane() != playerPlane)
			{
				continue;
			}

			if (!statue.isDangerous())
			{
				continue;
			}

			renderGameObject(graphics, statue.getGameObject(), color, borderColor, style);
		}
	}

	private void renderGameObject(Graphics2D graphics, net.runelite.api.GameObject gameObject,
		Color fillColor, Color borderColor, HighlightStyle style)
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

		if (shape == null)
		{
			return;
		}

		graphics.setStroke(DEFAULT_STROKE);
		graphics.setColor(borderColor);
		graphics.draw(shape);
		graphics.setColor(fillColor);
		graphics.fill(shape);
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
			WorldPoint statueLocation = statue.getLocation();
			if (statueLocation.getPlane() != playerPlane)
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
			WorldPoint statueLocation = statue.getLocation();
			if (statueLocation.getPlane() != playerPlane)
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

		for (NPC npc : obstacleHandler.getBoltNpcs())
		{
			WorldPoint npcLocation = npc.getWorldLocation();
			if (npcLocation.getPlane() != playerPlane)
			{
				continue;
			}

			LocalPoint lp = npc.getLocalLocation();
			if (lp == null)
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

		for (NPC npc : obstacleHandler.getSwordNpcs())
		{
			WorldPoint npcLocation = npc.getWorldLocation();
			if (npcLocation.getPlane() != playerPlane)
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
			WorldPoint obeliskLocation = obelisk.getWorldLocation();
			if (obeliskLocation.getPlane() != playerPlane)
			{
				continue;
			}

			Shape hull = obelisk.getConvexHull();
			if (hull != null)
			{
				graphics.setStroke(DEFAULT_STROKE);
				graphics.setColor(borderColor);
				graphics.draw(hull);
				graphics.setColor(fillColor);
				graphics.fill(hull);
			}
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
				WorldPoint coffinLocation = coffin.getWorldLocation();
				if (coffinLocation.getPlane() != playerPlane)
				{
					continue;
				}

				if (isObjectInAnyState(coffin, SepulchreConstants.COFFIN_MORPH_OPEN_IDS))
				{
					continue;
				}

				Shape clickbox = coffin.getClickbox();
				if (clickbox != null)
				{
					graphics.setStroke(DEFAULT_STROKE);
					graphics.setColor(borderColor);
					graphics.draw(clickbox);
					graphics.setColor(fillColor);
					graphics.fill(clickbox);
				}
			}
		}

		if (obstacleHandler.isGrandCoffinLootingEnabled())
		{
			for (GameObject grandCoffin : obstacleHandler.getGrandCoffins())
			{
				WorldPoint coffinLocation = grandCoffin.getWorldLocation();
				if (coffinLocation.getPlane() != playerPlane)
				{
					continue;
				}

				if (isObjectInState(grandCoffin, SepulchreConstants.GRAND_COFFIN_MORPH_OPEN))
				{
					continue;
				}

				Shape clickbox = grandCoffin.getClickbox();
				if (clickbox != null)
				{
					graphics.setStroke(DEFAULT_STROKE);
					graphics.setColor(borderColor);
					graphics.draw(clickbox);
					graphics.setColor(fillColor);
					graphics.fill(clickbox);
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

				renderTileObject(graphics, barrier, barrierFillColor, barrierBorderColor);
			}
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
			Color fillColor = canBuild ? normalFillColor : missingFillColor;
			Color borderColor = canBuild ? normalBorderColor : missingBorderColor;

			for (GroundObject bridge : obstacleHandler.getBridges())
			{
				if (bridge.getWorldLocation().getPlane() != playerPlane)
				{
					continue;
				}

				if (!obstacleHandler.shouldHighlightObstacle(bridge))
				{
					continue;
				}

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
		Shape clickbox = tileObject.getClickbox();
		if (clickbox != null)
		{
			graphics.setStroke(DEFAULT_STROKE);
			graphics.setColor(borderColor);
			graphics.draw(clickbox);
			graphics.setColor(fillColor);
			graphics.fill(clickbox);
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
}
