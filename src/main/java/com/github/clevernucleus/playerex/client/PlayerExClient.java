package com.github.clevernucleus.playerex.client;

import org.lwjgl.glfw.GLFW;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageRegistry;
import com.github.clevernucleus.playerex.client.gui.AttributesPageLayer;
import com.github.clevernucleus.playerex.client.gui.CombatPageLayer;
import com.github.clevernucleus.playerex.client.gui.ExScreen;
import com.github.clevernucleus.playerex.client.gui.Page;
import com.github.clevernucleus.playerex.handler.NetworkHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class PlayerExClient implements ClientModInitializer {
	public static final Identifier GUI = new Identifier(ExAPI.MODID, "textures/gui/gui.png");
	public static final Identifier GUI_DARK = new Identifier(ExAPI.MODID, "textures/gui/gui_dark.png");
	public static final Identifier ATTRIBUTES_PAGE = new Identifier(ExAPI.MODID, "attributes");
	public static final Identifier COMBAT_PAGE = new Identifier(ExAPI.MODID, "combat");
	public static final Page INVENTORY = new Page(new Identifier(ExAPI.MODID, "inventory"), new Identifier(ExAPI.MODID, "textures/gui/inventory.png"), new TranslatableText("playerex.gui.page.inventory.title"));
	public static KeyBinding keyBinding;
	
	@Override
	public void onInitializeClient() {
		ClientLoginNetworking.registerGlobalReceiver(PlayerEx.HANDSHAKE, NetworkHandlerClient::loginQueryReceived);
		ClientPlayNetworking.registerGlobalReceiver(NetworkHandler.NOTIFY, NetworkHandlerClient::notifiedLevelUp);
		
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("playerex.key.screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.inventory"));
		
		ScreenRegistry.register(PlayerEx.EX_SCREEN, ExScreen::new);
		PageRegistry.registerPage(ATTRIBUTES_PAGE, new Identifier(ExAPI.MODID, "textures/gui/attributes.png"), new TranslatableText("playerex.gui.page.attributes.title"));
		PageRegistry.registerPage(COMBAT_PAGE, new Identifier(ExAPI.MODID, "textures/gui/combat.png"), new TranslatableText("playerex.gui.page.combat.title"));
		PageRegistry.registerLayer(ATTRIBUTES_PAGE, AttributesPageLayer::new);
		PageRegistry.registerLayer(COMBAT_PAGE, CombatPageLayer::new);
		
		ScreenEvents.AFTER_INIT.register(EventHandlerClient::onScreenInit);
		ClientTickEvents.END_CLIENT_TICK.register(EventHandlerClient::onKeyPressed);
	}
}
