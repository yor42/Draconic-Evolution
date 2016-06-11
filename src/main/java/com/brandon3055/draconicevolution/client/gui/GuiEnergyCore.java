package com.brandon3055.draconicevolution.client.gui;

import com.brandon3055.brandonscore.client.gui.GuiButtonAHeight;
import com.brandon3055.brandonscore.client.utills.GuiHelper;
import com.brandon3055.brandonscore.inventory.ContainerBCBase;
import com.brandon3055.brandonscore.network.PacketTileMessage;
import com.brandon3055.brandonscore.utils.InfoHelper;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandon3055 on 7/4/2016.
 */
public class GuiEnergyCore extends GuiContainer {

    public EntityPlayer player;
    public TileEnergyStorageCore tile;
    private GuiButton activate;
    private GuiButton tierUp;
    private GuiButton tierDown;
    private GuiButton toggleGuide;
    private GuiButton creativeBuild;

    public GuiEnergyCore(EntityPlayer player, TileEnergyStorageCore tile) {
        super(new ContainerBCBase<TileEnergyStorageCore>(player, tile).addPlayerSlots(10, 116));
        this.tile = tile;
        this.xSize = 180;
        this.ySize = 200;
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(activate = new GuiButtonAHeight(0, guiLeft + 9, guiTop + 99, 162, 12, "Activate-L"));
        buttonList.add(tierUp = new GuiButtonAHeight(1, guiLeft + 91, guiTop + 86, 80, 12, I18n.translateToLocal("button.de.tierUp.txt")));
        buttonList.add(tierDown = new GuiButtonAHeight(2, guiLeft + 9, guiTop + 86, 80, 12, I18n.translateToLocal("button.de.tierDown.txt")));
        buttonList.add(toggleGuide = new GuiButtonAHeight(3, guiLeft + 9, guiTop + 73, 162, 12, I18n.translateToLocal("button.de.buildGuide.txt")));
        buttonList.add(creativeBuild = new GuiButtonAHeight(4, guiLeft + 9, guiTop + ySize, 162, 12, "Creative Build"));
        updateButtonStates();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GuiHelper.drawGuiBaseBackground(this, guiLeft, guiTop, xSize, ySize);
        GuiHelper.drawPlayerSlots(this, guiLeft + (xSize / 2), guiTop + 115, true);
        drawCenteredString(fontRendererObj, String.format(I18n.translateToLocal("gui.de.energyStorageCore.name"), tile.tier), guiLeft + (xSize / 2), guiTop + 5, InfoHelper.GUI_TITLE);

        if (tile.active.value) {

            GuiHelper.drawCenteredString(fontRendererObj, I18n.translateToLocal("gui.de.capacity.txt"), guiLeft + xSize / 2, guiTop + 16, 0xFFAA00, true);
            String capText = tile.tier.value == 8 ? I18n.translateToLocal("gui.de.almostInfinite.txt") : Utils.formatNumber(tile.getExtendedCapacity());
            GuiHelper.drawCenteredString(fontRendererObj, capText, guiLeft + xSize / 2, guiTop + 27, 0x555555, false);

            GuiHelper.drawCenteredString(fontRendererObj, I18n.translateToLocal("info.de.charge.txt"), guiLeft + xSize / 2, guiTop + 38, 0xFFAA00, true);
            GuiHelper.drawCenteredString(fontRendererObj, Utils.formatNumber(tile.getExtendedStorage()) + "RF", guiLeft + xSize / 2, guiTop + 49, 0x555555, false);

            int coreColour = tile.transferRate.value > 0 ? 0x00FF00 : tile.transferRate.value < 0 ? 0xFF0000 : 0x222222;
            String transfer = (tile.transferRate.value > 0 ? "+" : "") + tile.transferRate.value + " RF/t";
            GuiHelper.drawCenteredString(fontRendererObj, I18n.translateToLocal("gui.de.transfer.txt"), guiLeft + xSize / 2, guiTop + 59, 0xFFAA00, true);
            GuiHelper.drawCenteredString(fontRendererObj, transfer, guiLeft + xSize / 2, guiTop + 70, coreColour, tile.transferRate.value > 0);

        } else {
            int stabColour = tile.stabilizersOK.value ? 0x00FF00 : 0xFF0000;
            String stabText = I18n.translateToLocal("gui.de.stabilizers.txt") + ": " + (tile.stabilizersOK.value ? I18n.translateToLocal("gui.de.valid.txt") : I18n.translateToLocal("gui.de.invalid.txt"));
            GuiHelper.drawCenteredString(fontRendererObj, stabText, guiLeft + xSize / 2, guiTop + 18, stabColour, tile.stabilizersOK.value);
            GuiHelper.drawCenteredString(fontRendererObj, I18n.translateToLocal("gui.de.advancedStabilizersRequired.txt"), guiLeft + xSize / 2, guiTop + 28, 0x777777, false);

            int coreColour = tile.coreValid.value ? 0x00FF00 : 0xFF0000;
            String coreText = I18n.translateToLocal("gui.de.core.txt") + ": " + (tile.coreValid.value ? I18n.translateToLocal("gui.de.valid.txt") : I18n.translateToLocal("gui.de.invalid.txt"));
            GuiHelper.drawCenteredString(fontRendererObj, coreText, guiLeft + xSize / 2, guiTop + 48, coreColour, tile.coreValid.value);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (tile.active.value) {
            GuiHelper.drawEnergyBar(this, guiLeft + 5, guiTop + 82, 170, true, tile.getExtendedStorage(), tile.getExtendedCapacity(), true, mouseX, mouseY);

            if (GuiHelper.isInRect(guiLeft + 40, guiTop + 27, xSize - 80, 8, mouseX, mouseY)) {
                List<String> list = new ArrayList<String>();
                list.add(TextFormatting.GRAY + "[" + Utils.addCommas(tile.getExtendedCapacity()) + " RF]");
                drawHoveringText(list, mouseX, mouseY);
            }

            if (GuiHelper.isInRect(guiLeft + 40, guiTop + 48, xSize - 80, 8, mouseX, mouseY)) {
                List<String> list = new ArrayList<String>();
                list.add(TextFormatting.GRAY + "[" + Utils.addCommas(tile.getExtendedStorage()) + " RF]");
                drawHoveringText(list, mouseX, mouseY);
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateButtonStates();
    }

    private void updateButtonStates() {
        if (tile.active.value) {
            activate.displayString = I18n.translateToLocal("button.de.deactivate.txt");
        } else {
            activate.displayString = I18n.translateToLocal("button.de.activate.txt");

            toggleGuide.displayString = I18n.translateToLocal("button.de.buildGuide.txt") + " " + (tile.buildGuide.value ? I18n.translateToLocal("gui.de.active.txt") : I18n.translateToLocal("gui.de.inactive.txt"));

            tierUp.enabled = tile.tier.value < 8;
            tierDown.enabled = tile.tier.value > 1;
        }


        tierUp.visible = tierDown.visible = toggleGuide.visible = !tile.active.value;
        creativeBuild.visible = player.capabilities.isCreativeMode && !tile.active.value;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        tile.sendPacketToServer(new PacketTileMessage(tile, (byte) button.id, true, false));
    }
}