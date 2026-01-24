package com.sepulchre.overlay;

import com.sepulchre.SepulchrePlugin;
import com.sepulchre.config.HighlightStyle;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import com.sepulchre.model.CrossbowStatue;
import com.sepulchre.model.SepulchreObstacle;
import com.sepulchre.model.TrackedGraphicsObject;
import com.sepulchre.model.WizardStatue;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import java.awt.Shape;

import javax.inject.Inject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;

/**
 * Scene overlay for rendering in-game highlights (danger zones, portals, coffins, etc.)
 */
public class SepulchreSceneOverlay extends Overlay
{
	private final Client client;
	private final SepulchrePlugin plugin;
	private final SepulchreConfig config;
	private final ObstacleHandler obstacleHandler;

	// Stroke is now dynamically created based on config

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

		renderObstacles(graphics);
		renderCrossbowStatues(graphics);
		renderWizardStatues(graphics);
		renderArrowNpcs(graphics);
		renderSwordNpcs(graphics);
		renderCoffins(graphics);
		renderPortals(graphics);
		renderDebugGraphics(graphics);
		renderDebugGameObjects(graphics);

		return null;
	}

	private void renderObstacles(Graphics2D graphics)
	{
		int playerPlane = client.getPlane();

		for (SepulchreObstacle obstacle : obstacleHandler.getActiveObstacles())
		{
			// Skip obstacles on different planes
			WorldPoint obstacleLocation = obstacle.getLocation();
			if (obstacleLocation != null && obstacleLocation.getPlane() != playerPlane)
			{
				continue;
			}

			if (!shouldRenderObstacle(obstacle))
			{
				continue;
			}

			renderDangerZone(graphics, obstacle, playerPlane);

			if (shouldRenderTickCounter(obstacle))
			{
				renderTickCounter(graphics, obstacle);
			}
		}
	}

	private boolean shouldRenderObstacle(SepulchreObstacle obstacle)
	{
		switch (obstacle.getType())
		{
			case ARROW:
				return config.highlightArrows();
			case SWORD:
				return config.highlightSwords();
			case WIZARD:
				return config.highlightWizards();
			case LIGHTNING:
				return config.highlightLightning();
			case CROSSBOW:
				return config.highlightCrossbows();
			default:
				return false;
		}
	}

	private boolean shouldRenderTickCounter(SepulchreObstacle obstacle)
	{
		switch (obstacle.getType())
		{
			case WIZARD:
				return config.wizardTickCounter();
			default:
				return false;
		}
	}

	private Stroke getBorderStroke()
	{
		int width = Math.max(1, Math.min(5, config.tileBorderWidth()));
		return new BasicStroke(width);
	}

	private void renderDangerZone(Graphics2D graphics, SepulchreObstacle obstacle, int playerPlane)
	{
		Color color = getObstacleColor(obstacle);
		if (color == null)
		{
			return; // Zone type is disabled
		}

		for (WorldPoint dangerTile : obstacle.getDangerZone())
		{
			// Skip tiles on different planes
			if (dangerTile.getPlane() != playerPlane)
			{
				continue;
			}

			LocalPoint localPoint = LocalPoint.fromWorld(client, dangerTile);
			if (localPoint == null)
			{
				continue;
			}

			Polygon poly = Perspective.getCanvasTilePoly(client, localPoint);
			if (poly == null)
			{
				continue;
			}

			graphics.setStroke(getBorderStroke());
			int borderOpacity = Math.max(0, Math.min(255, config.tileBorderOpacity()));
			graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), borderOpacity));
			graphics.draw(poly);
			graphics.setColor(color);
			graphics.fill(poly);
		}
	}

	private Color getObstacleColor(SepulchreObstacle obstacle)
	{
		int ticksRemaining = obstacle.getTicksUntilActive();

		// For wizard flames: fire at 5,4,3,2 (danger), safe at 6,7,0,1
		if (ticksRemaining >= 2 && ticksRemaining <= 5)
		{
			return config.showDangerZones() ? config.dangerColor() : null;
		}
		else if (ticksRemaining == 6)
		{
			return config.showWarningZones() ? config.warningColor() : null;
		}
		else
		{
			return config.showSafeZones() ? config.safeColor() : null;
		}
	}

	private void renderTickCounter(Graphics2D graphics, SepulchreObstacle obstacle)
	{
		WorldPoint location = obstacle.getLocation();
		if (location == null)
		{
			return;
		}

		LocalPoint localPoint = LocalPoint.fromWorld(client, location);
		if (localPoint == null)
		{
			return;
		}

		int ticksRemaining = obstacle.getTicksUntilActive();
		String displayText = getDisplayTicks(ticksRemaining);

		Point textLocation = Perspective.getCanvasTextLocation(client, graphics, localPoint,
			displayText, 0);
		if (textLocation == null)
		{
			return;
		}

		Color textColor = getObstacleColor(obstacle);
		OverlayUtil.renderTextLocation(graphics, textLocation, displayText, textColor);
	}

	/**
	 * Convert internal tick counter to display value.
	 * Danger (flames): 5,4,3,2 -> displays 4,3,2,1
	 * Warning: 6 -> displays 0
	 * Safe: 7,0,1 -> displays 3,2,1
	 */
	private String getDisplayTicks(int ticksRemaining)
	{
		if (ticksRemaining >= 2 && ticksRemaining <= 5)
		{
			// Danger phase: 5->4, 4->3, 3->2, 2->1
			return String.valueOf(ticksRemaining - 1);
		}
		else if (ticksRemaining == 6)
		{
			// Warning phase
			return "0";
		}
		else
		{
			// Safe phase: 7->3, 0->2, 1->1
			if (ticksRemaining == 7)
			{
				return "3";
			}
			else if (ticksRemaining == 0)
			{
				return "2";
			}
			else // ticksRemaining == 1
			{
				return "1";
			}
		}
	}

	private void renderCrossbowStatues(Graphics2D graphics)
	{
		if (!config.highlightCrossbows() && !config.showAnimationIds())
		{
			return;
		}

		Color color = config.crossbowColor();
		HighlightStyle style = config.crossbowHighlightStyle();
		int playerPlane = client.getPlane();

		for (CrossbowStatue statue : obstacleHandler.getCrossbowStatues())
		{
			// Skip statues on different planes
			WorldPoint statueLocation = statue.getGameObject().getWorldLocation();
			if (statueLocation == null || statueLocation.getPlane() != playerPlane)
			{
				continue;
			}

			// Show debug animation ID if enabled
			if (config.showAnimationIds())
			{
				renderAnimationId(graphics, statue.getGameObject().getLocalLocation(), statue.getAnimationId());
			}

			if (!config.highlightCrossbows() || !statue.isDangerous())
			{
				continue;
			}

			renderGameObject(graphics, statue.getGameObject(), color, style);
		}
	}

	private void renderGameObject(Graphics2D graphics, net.runelite.api.GameObject gameObject, Color color, HighlightStyle style)
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

		graphics.setStroke(getBorderStroke());
		int borderOpacity = Math.max(0, Math.min(255, config.tileBorderOpacity()));
		graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), borderOpacity));
		graphics.draw(shape);
		graphics.setColor(color);
		graphics.fill(shape);
	}

	private void renderAnimationId(Graphics2D graphics, LocalPoint localPoint, int animationId)
	{
		if (localPoint == null)
		{
			return;
		}

		Point textLoc = Perspective.getCanvasTextLocation(client, graphics, localPoint, String.valueOf(animationId), 150);
		if (textLoc != null)
		{
			OverlayUtil.renderTextLocation(graphics, textLoc, "Anim: " + animationId, Color.YELLOW);
		}
	}

	private void renderWizardStatues(Graphics2D graphics)
	{
		if (!config.highlightWizards() && !config.showAnimationIds() && !config.wizardTickCounter())
		{
			return;
		}

		int playerPlane = client.getPlane();

		for (WizardStatue statue : obstacleHandler.getWizardStatues())
		{
			// Skip statues on different planes
			WorldPoint statueLocation = statue.getLocation();
			if (statueLocation == null || statueLocation.getPlane() != playerPlane)
			{
				continue;
			}

			// Show debug animation ID if enabled
			if (config.showAnimationIds())
			{
				renderAnimationId(graphics, statue.getGameObject().getLocalLocation(), statue.getAnimationId());
			}

			// Skip statues that have been confirmed as inactive (never fired after a full cycle)
			if (!statue.isConfirmedActiveOrUnknown())
			{
				continue;
			}

			// Determine color based on animation state
			Color color;
			if (statue.isFiring())
			{
				color = config.dangerColor();
			}
			else if (statue.isWarning())
			{
				color = config.warningColor();
			}
			else
			{
				color = config.safeColor();
			}

			// Render tick counter if enabled
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

			// Check if this zone type should be rendered
			if (statue.isFiring() && !config.showDangerZones())
			{
				continue;
			}
			else if (statue.isWarning() && !config.showWarningZones())
			{
				continue;
			}
			else if (statue.isSafe() && !config.showSafeZones())
			{
				continue;
			}

			// Render danger zone tiles
			for (WorldPoint dangerTile : statue.getDangerZone())
			{
				// Skip tiles on different planes
				if (dangerTile.getPlane() != playerPlane)
				{
					continue;
				}

				LocalPoint localPoint = LocalPoint.fromWorld(client, dangerTile);
				if (localPoint == null)
				{
					continue;
				}

				Polygon poly = Perspective.getCanvasTilePoly(client, localPoint);
				if (poly == null)
				{
					continue;
				}

				graphics.setStroke(getBorderStroke());
				int borderOpacity = Math.max(0, Math.min(255, config.tileBorderOpacity()));
				graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), borderOpacity));
				graphics.draw(poly);
				graphics.setColor(color);
				graphics.fill(poly);
			}
		}
	}

	private void renderArrowNpcs(Graphics2D graphics)
	{
		if (!config.highlightArrows())
		{
			return;
		}

		Color color = config.arrowColor();
		int playerPlane = client.getPlane();

		for (NPC npc : obstacleHandler.getArrowNpcs())
		{
			// Skip NPCs on different planes
			WorldPoint npcLocation = npc.getWorldLocation();
			if (npcLocation == null || npcLocation.getPlane() != playerPlane)
			{
				continue;
			}

			// Use normal tile polygon
			LocalPoint lp = npc.getLocalLocation();
			if (lp == null)
			{
				continue;
			}

			Polygon poly = Perspective.getCanvasTilePoly(client, lp);
			if (poly != null)
			{
				graphics.setStroke(getBorderStroke());
				int borderOpacity = Math.max(0, Math.min(255, config.tileBorderOpacity()));
				graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), borderOpacity));
				graphics.draw(poly);
				graphics.setColor(color);
				graphics.fill(poly);
			}
		}
	}

	private void renderSwordNpcs(Graphics2D graphics)
	{
		if (!config.highlightSwords())
		{
			return;
		}

		Color color = config.swordColor();
		int playerPlane = client.getPlane();

		for (NPC npc : obstacleHandler.getSwordNpcs())
		{
			// Skip NPCs on different planes
			WorldPoint npcLocation = npc.getWorldLocation();
			if (npcLocation == null || npcLocation.getPlane() != playerPlane)
			{
				continue;
			}

			// Use getCanvasTileAreaPoly with NPC composition size for proper 3x3 rendering
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
				graphics.setStroke(getBorderStroke());
				int borderOpacity = Math.max(0, Math.min(255, config.tileBorderOpacity()));
				graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), borderOpacity));
				graphics.draw(poly);
				graphics.setColor(color);
				graphics.fill(poly);
			}
		}
	}

	private void renderCoffins(Graphics2D graphics)
	{
		if (!config.highlightCoffins() && !config.highlightGrandCoffin())
		{
			return;
		}

		// Coffin rendering will be implemented when we track coffin objects
	}

	private void renderPortals(Graphics2D graphics)
	{
		if (!config.highlightPortals())
		{
			return;
		}

		Color color = config.portalColor();
		int playerPlane = client.getPlane();

		for (WorldPoint portalLocation : obstacleHandler.getActivePortalLocations())
		{
			// Skip portals on different planes
			if (portalLocation.getPlane() != playerPlane)
			{
				continue;
			}

			LocalPoint localPoint = LocalPoint.fromWorld(client, portalLocation);
			if (localPoint == null)
			{
				continue;
			}

			Polygon poly = Perspective.getCanvasTilePoly(client, localPoint);
			if (poly == null)
			{
				continue;
			}

			graphics.setStroke(getBorderStroke());
			int borderOpacity = Math.max(0, Math.min(255, config.tileBorderOpacity()));
			graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), borderOpacity));
			graphics.draw(poly);
			graphics.setColor(color);
			graphics.fill(poly);
		}
	}

	private void renderDebugGraphics(Graphics2D graphics)
	{
		if (!config.showGraphicsIds())
		{
			return;
		}

		int playerPlane = client.getPlane();

		for (TrackedGraphicsObject tracked : obstacleHandler.getTrackedGraphics())
		{
			// Skip graphics on different planes
			WorldPoint worldLocation = tracked.getWorldPoint();
			if (worldLocation != null && worldLocation.getPlane() != playerPlane)
			{
				continue;
			}

			LocalPoint localPoint = tracked.getLocalLocation();
			if (localPoint == null)
			{
				continue;
			}

			// Draw tile highlight
			Polygon poly = Perspective.getCanvasTilePoly(client, localPoint);
			if (poly != null)
			{
				graphics.setStroke(getBorderStroke());
				graphics.setColor(Color.MAGENTA);
				graphics.draw(poly);
			}

			// Draw graphics ID text
			Point textLoc = Perspective.getCanvasTextLocation(client, graphics, localPoint, String.valueOf(tracked.getId()), 100);
			if (textLoc != null)
			{
				OverlayUtil.renderTextLocation(graphics, textLoc, "GFX: " + tracked.getId(), Color.MAGENTA);
			}
		}
	}

	private void renderDebugGameObjects(Graphics2D graphics)
	{
		if (!config.showGameObjectIds())
		{
			return;
		}

		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		int plane = client.getPlane();

		// Only render objects near the player to avoid clutter
		WorldPoint playerLoc = client.getLocalPlayer().getWorldLocation();

		for (int x = 0; x < tiles[plane].length; x++)
		{
			for (int y = 0; y < tiles[plane][x].length; y++)
			{
				Tile tile = tiles[plane][x][y];
				if (tile == null)
				{
					continue;
				}

				for (GameObject gameObject : tile.getGameObjects())
				{
					if (gameObject == null)
					{
						continue;
					}

					WorldPoint objLoc = gameObject.getWorldLocation();
					if (objLoc.distanceTo(playerLoc) > 15)
					{
						continue;
					}

					LocalPoint localPoint = gameObject.getLocalLocation();
					if (localPoint == null)
					{
						continue;
					}

					int id = gameObject.getId();
					String name = "";
					if (client.getObjectDefinition(id) != null)
					{
						name = client.getObjectDefinition(id).getName();
					}

					// Draw object ID and name
					Point textLoc = Perspective.getCanvasTextLocation(client, graphics, localPoint, String.valueOf(id), 50);
					if (textLoc != null)
					{
						String label = "ID:" + id;
						if (name != null && !name.isEmpty() && !name.equals("null"))
						{
							label += " (" + name + ")";
						}
						OverlayUtil.renderTextLocation(graphics, textLoc, label, Color.WHITE);
					}
				}
			}
		}
	}
}
