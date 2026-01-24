package com.sepulchre;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SepulchrePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SepulchrePlugin.class);
		RuneLite.main(args);
	}
}
