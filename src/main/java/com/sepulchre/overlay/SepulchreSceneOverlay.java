package com.sepulchre.overlay;

import com.sepulchre.SepulchrePlugin;
import com.sepulchre.config.HighlightStyle;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import com.sepulchre.model.CrossbowStatue;
import com.sepulchre.model.LightningStrike;
import com.sepulchre.model.WizardStatue;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
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

public class SepulchreSceneOverlay extends Overlay
{
	private static final Stroke DEFAULT_STROKE = new BasicStroke(1);

	private final Client client;
	private final SepulchrePlugin plugin;
	private final SepulchreConfig config;
	private final ObstacleHandler obstacleHandler;

	private Stroke cachedFireStroke;
	private int cachedFireStrokeWidth = -1;

	private Stroke cachedProjectileStroke;
	private int cachedProjectileStrokeWidth = -1;

	private Color cachedFireBorderColor;
	private int cachedFireBorderOpacity = -1;
	private Color cachedFireBaseColor;

	@Inject
	public SepulchreSceneOverlay(Client client, SepulchrePlugin plugin, SepulchreConfig config, ObstacleHandler obstacleHandler)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.obstacleHandler = obstacleHandler;

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
		renderBoltNpcs(graphics, playerPlane);
		renderSwordNpcs(graphics, playerPlane);
		renderPortals(graphics, playerPlane);

		return null;
	}

	private Stroke getFireBorderStroke()
	{
		int width = Math.max(1, Math.min(5, config.fireBorderWidth()));
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

	private Color getFireBorderColor(Color baseColor)
	{
		int borderOpacity = Math.max(0, Math.min(255, config.fireBorderOpacity()));
		if (borderOpacity != cachedFireBorderOpacity || baseColor != cachedFireBaseColor)
		{
			cachedFireBorderOpacity = borderOpacity;
			cachedFireBaseColor = baseColor;
			cachedFireBorderColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), borderOpacity);
		}
		return cachedFireBorderColor;
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
		Color borderColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
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

	private void renderGameObject(Graphics2D graphics, net.runelite.api.GameObject gameObject, Color fillColor, Color borderColor, HighlightStyle style)
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
		if (!config.highlightWizards() && !config.wizardTickCounter())
		{
			return;
		}

		Stroke fireStroke = getFireBorderStroke();

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
			Color color = isFiring ? config.fireColor() : config.incomingColor();

			if (config.wizardTickCounter())
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

			if (!config.highlightWizards())
			{
				continue;
			}

			if (isFiring && !config.showFireTiles())
			{
				continue;
			}
			else if (!isFiring && !config.showFireIncoming())
			{
				continue;
			}

			Color borderColor = getFireBorderColor(color);

			for (WorldPoint fireTile : statue.getFireTiles())
			{
				Polygon poly = getTilePolygon(fireTile, playerPlane);
				if (poly != null)
				{
					renderTilePolygon(graphics, poly, color, borderColor, fireStroke);
				}
			}
		}
	}

	private void renderBoltNpcs(Graphics2D graphics, int playerPlane)
	{
		if (!config.highlightBolts())
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
		if (!config.highlightSwords())
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
		if (config.highlightYellowPortals())
		{
			Color yellowColor = config.portalYellowColor();
			Color yellowBorder = new Color(yellowColor.getRed(), yellowColor.getGreen(), yellowColor.getBlue(), 255);
			for (WorldPoint portalLocation : obstacleHandler.getActiveYellowPortals())
			{
				Polygon poly = getTilePolygon(portalLocation, playerPlane);
				if (poly != null)
				{
					renderTilePolygon(graphics, poly, yellowColor, yellowBorder, DEFAULT_STROKE);
				}
			}
		}

		if (config.highlightBluePortals())
		{
			Color blueColor = config.portalBlueColor();
			Color blueBorder = new Color(blueColor.getRed(), blueColor.getGreen(), blueColor.getBlue(), 255);
			for (WorldPoint portalLocation : obstacleHandler.getActiveBluePortals())
			{
				Polygon poly = getTilePolygon(portalLocation, playerPlane);
				if (poly != null)
				{
					renderTilePolygon(graphics, poly, blueColor, blueBorder, DEFAULT_STROKE);
				}
			}
		}
	}
}
