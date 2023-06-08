/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.core;

import me.kyren223.rrutils.config.RRUtilsConfig;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RRUtils implements ModInitializer {
    public static final String MOD_ID = "rrutils";
    public static final Logger LOGGER = LoggerFactory.getLogger("rrutils");

    public static final RRUtilsConfig CONFIG = RRUtilsConfig.createAndLoad();

    @Override
    public void onInitialize() {

    }
}