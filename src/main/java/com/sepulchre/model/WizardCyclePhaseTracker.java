package com.sepulchre.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class WizardCyclePhaseTracker
{
	private int currentCycle = 0;
	private boolean isActive = false;

	private List<WizardStatue> southRow = new ArrayList<>();
	private List<WizardStatue> northRow = new ArrayList<>();

	private WizardStatue southSkip = null;
	private WizardStatue northSkip = null;

	private int minX = 0;
	private int maxX = 0;
	private int minY = 0;
	private int maxY = 0;

	private boolean detected = false;

	private boolean prevSouthFiring = false;
	private boolean prevNorthFiring = false;

	public void onGameTick(List<WizardStatue> allWizards)
	{
		if (allWizards.isEmpty())
		{
			return;
		}

		if (!detected)
		{
			detect(allWizards);
			if (!detected)
			{
				return;
			}
		}

		trackCycle();
	}

	private void detect(List<WizardStatue> allWizards)
	{
		Map<Integer, List<WizardStatue>> byY = new HashMap<>();
		for (WizardStatue w : allWizards)
		{
			int y = w.getLocation().getY();
			byY.computeIfAbsent(y, k -> new ArrayList<>()).add(w);
		}

		List<Map.Entry<Integer, List<WizardStatue>>> sorted = new ArrayList<>(byY.entrySet());
		sorted.sort((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()));

		if (sorted.size() < 2)
		{
			return;
		}

		List<WizardStatue> group1 = sorted.get(0).getValue();
		List<WizardStatue> group2 = sorted.get(1).getValue();
		int y1 = sorted.get(0).getKey();
		int y2 = sorted.get(1).getKey();

		if (group1.size() != 10 || group2.size() != 10)
		{
			return;
		}

		if (y1 > y2)
		{
			northRow = new ArrayList<>(group1);
			southRow = new ArrayList<>(group2);
		}
		else
		{
			northRow = new ArrayList<>(group2);
			southRow = new ArrayList<>(group1);
		}

		southSkip = findEasternmost(southRow);
		northSkip = findEasternmost(northRow);

		if (southSkip == null || northSkip == null)
		{
			return;
		}

		minX = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		minY = Integer.MAX_VALUE;
		maxY = Integer.MIN_VALUE;

		for (WizardStatue w : southRow)
		{
			int x = w.getLocation().getX();
			int y = w.getLocation().getY();
			minX = Math.min(minX, x);
			maxX = Math.max(maxX, x);
			minY = Math.min(minY, y);
		}
		for (WizardStatue w : northRow)
		{
			int y = w.getLocation().getY();
			maxY = Math.max(maxY, y);
		}

		detected = true;
		isActive = true;
	}

	private WizardStatue findEasternmost(List<WizardStatue> row)
	{
		WizardStatue easternmost = null;
		int maxX = Integer.MIN_VALUE;

		for (WizardStatue w : row)
		{
			int x = w.getLocation().getX();
			if (x > maxX)
			{
				maxX = x;
				easternmost = w;
			}
		}

		return easternmost;
	}

	private void trackCycle()
	{
		int southFiringCount = 0;
		int northFiringCount = 0;

		for (WizardStatue w : southRow)
		{
			if (w.isFiring())
			{
				southFiringCount++;
			}
		}

		for (WizardStatue w : northRow)
		{
			if (w.isFiring())
			{
				northFiringCount++;
			}
		}

		boolean skipFiring = (southSkip != null && southSkip.isFiring())
		                  || (northSkip != null && northSkip.isFiring());
		boolean southRowFiring = southFiringCount > 0;
		boolean northRowFiring = northFiringCount > 0;

		boolean southJustStopped = prevSouthFiring && !southRowFiring;
		boolean northJustStopped = prevNorthFiring && !northRowFiring;

		if (southJustStopped || northJustStopped)
		{
			int endedCycle = determineCycleFromPrevState();
			if (endedCycle > 0)
			{
				currentCycle = endedCycle == 5 ? 1 : endedCycle + 1;
			}
		}
		else if (currentCycle == 0 && (southRowFiring || northRowFiring))
		{
			int newCycle = determineCycle(skipFiring, southRowFiring, northRowFiring);
			if (newCycle > 0)
			{
				currentCycle = newCycle;
			}
		}

		prevSouthFiring = southRowFiring;
		prevNorthFiring = northRowFiring;
	}

	private int determineCycleFromPrevState()
	{
		if (prevSouthFiring && prevNorthFiring)
		{
			return 5;
		}
		if (prevSouthFiring && !prevNorthFiring)
		{
			return (currentCycle == 0 || currentCycle == 5 || currentCycle == 1) ? 1 : 3;
		}
		if (!prevSouthFiring && prevNorthFiring)
		{
			return (currentCycle == 1 || currentCycle == 2) ? 2 : 4;
		}
		return 0;
	}

	private int determineCycle(boolean skip, boolean south, boolean north)
	{
		if (!skip && south && !north)
		{
			return 1;
		}
		if (!skip && !south && north)
		{
			return 2;
		}
		if (skip && south && !north)
		{
			return 3;
		}
		if (skip && !south && north)
		{
			return 4;
		}
		if (south && north)
		{
			return 5;
		}
		return 0;
	}

	public boolean isPlayerInRange(int playerX, int playerY)
	{
		if (!detected)
		{
			return false;
		}
		boolean inXRange = playerX >= minX - 9 && playerX <= maxX + 9;
		boolean inYRange = playerY >= minY - 4 && playerY <= maxY + 4;
		return inXRange && inYRange;
	}

	public String getDisplayText()
	{
		if (!isActive)
		{
			return null;
		}
		if (currentCycle == 0)
		{
			return "?/5";
		}
		return currentCycle + "/5";
	}

	public void reset()
	{
		currentCycle = 0;
		isActive = false;
		southRow.clear();
		northRow.clear();
		southSkip = null;
		northSkip = null;
		minX = 0;
		maxX = 0;
		minY = 0;
		maxY = 0;
		detected = false;
		prevSouthFiring = false;
		prevNorthFiring = false;
	}
}
