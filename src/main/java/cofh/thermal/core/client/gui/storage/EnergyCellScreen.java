package cofh.thermal.core.client.gui.storage;

import cofh.core.client.gui.element.ElementBase;
import cofh.core.client.gui.element.ElementButton;
import cofh.core.client.gui.element.ElementTexture;
import cofh.core.network.packet.server.TileConfigPacket;
import cofh.thermal.core.inventory.container.storage.EnergyCellContainer;
import cofh.thermal.lib.client.gui.CellScreenReconfigurable;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;

import static cofh.core.util.helpers.GuiHelper.*;
import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;
import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static cofh.lib.util.helpers.SoundHelper.playClickSound;
import static cofh.lib.util.helpers.StringHelper.format;
import static cofh.lib.util.helpers.StringHelper.localize;

public class EnergyCellScreen extends CellScreenReconfigurable<EnergyCellContainer> {

    public static final String TEX_PATH = ID_THERMAL + ":textures/gui/container/energy_cell.png";
    public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

    public static final String TEX_INCREMENT = ID_COFH_CORE + ":textures/gui/elements/button_increment.png";
    public static final String TEX_DECREMENT = ID_COFH_CORE + ":textures/gui/elements/button_decrement.png";

    public EnergyCellScreen(EnergyCellContainer container, Inventory inv, Component titleIn) {

        super(container, inv, container.tile, titleIn);
        texture = TEXTURE;
        info = generatePanelInfo("info.thermal.energy_cell");
        name = "energy_cell";
    }

    @Override
    public void init() {

        super.init();

        addElement(new ElementTexture(this, 24, 16)
                .setSize(20, 20)
                .setTexture(INFO_INPUT, 20, 20));
        addElement(new ElementTexture(this, 132, 16)
                .setSize(20, 20)
                .setTexture(INFO_OUTPUT, 20, 20));

        addElement(setClearable(createDefaultEnergyStorage(this, 80, 22, tile.getEnergyStorage()), tile, 0));

        addButtons();
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {

        String input = format(tile.amountInput);
        String output = format(tile.amountOutput);

        fontRenderer().draw(matrixStack, input, getCenteredOffset(input, 34), 42, 0x404040);
        fontRenderer().draw(matrixStack, output, getCenteredOffset(output, 142), 42, 0x404040);

        super.renderLabels(matrixStack, mouseX, mouseY);
    }

    // region ELEMENTS
    protected void addButtons() {

        ElementBase decInput = new ElementButton(this, 19, 56) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

                int change = getChangeAmount(mouseButton);
                float pitch = getPitch(mouseButton);
                pitch -= 0.1F;
                playClickSound(pitch);

                int curInput = tile.amountInput;
                tile.amountInput -= change;
                TileConfigPacket.sendToServer(tile);
                tile.amountInput = curInput;
                return true;
            }
        }
                .setTooltipFactory((element, mouseX, mouseY) -> {

                    if (element.enabled()) {
                        int change = 1000;

                        if (hasShiftDown()) {
                            change *= 10;
                        }
                        if (hasControlDown()) {
                            change /= 100;
                        }
                        return Collections.singletonList(Component.literal(
                                localize("info.cofh.decrease_by")
                                        + " " + format(change)
                                        + "/" + format(change / 10)));
                    }
                    return Collections.emptyList();
                })
                .setName("DecInput")
                .setSize(14, 14)
                .setTexture(TEX_DECREMENT, 42, 14)
                .setEnabled(() -> tile.amountInput > 0);

        ElementBase incInput = new ElementButton(this, 35, 56) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

                int change = getChangeAmount(mouseButton);
                float pitch = getPitch(mouseButton);
                pitch += 0.1F;
                playClickSound(pitch);

                int curInput = tile.amountInput;
                tile.amountInput += change;
                TileConfigPacket.sendToServer(tile);
                tile.amountInput = curInput;
                return true;
            }
        }
                .setTooltipFactory((element, mouseX, mouseY) -> {

                    if (element.enabled()) {
                        int change = 1000;

                        if (hasShiftDown()) {
                            change *= 10;
                        }
                        if (hasControlDown()) {
                            change /= 100;
                        }
                        return Collections.singletonList(Component.literal(
                                localize("info.cofh.increase_by")
                                        + " " + format(change)
                                        + "/" + format(change / 10)));
                    }
                    return Collections.emptyList();
                })
                .setName("IncInput")
                .setSize(14, 14)
                .setTexture(TEX_INCREMENT, 42, 14)
                .setEnabled(() -> tile.amountInput < tile.getMaxInput());

        ElementBase decOutput = new ElementButton(this, 127, 56) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

                int change = getChangeAmount(mouseButton);
                float pitch = getPitch(mouseButton);
                pitch -= 0.1F;
                playClickSound(pitch);

                int curInput = tile.amountOutput;
                tile.amountOutput -= change;
                TileConfigPacket.sendToServer(tile);
                tile.amountOutput = curInput;
                return true;
            }
        }
                .setTooltipFactory((element, mouseX, mouseY) -> {

                    if (element.enabled()) {
                        int change = 1000;

                        if (hasShiftDown()) {
                            change *= 10;
                        }
                        if (hasControlDown()) {
                            change /= 100;
                        }
                        return Collections.singletonList(Component.literal(
                                localize("info.cofh.decrease_by")
                                        + " " + format(change)
                                        + "/" + format(change / 10)));
                    }
                    return Collections.emptyList();
                })
                .setName("DecOutput")
                .setSize(14, 14)
                .setTexture(TEX_DECREMENT, 42, 14)
                .setEnabled(() -> tile.amountOutput > 0);

        ElementBase incOutput = new ElementButton(this, 143, 56) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

                int change = getChangeAmount(mouseButton);
                float pitch = getPitch(mouseButton);
                pitch += 0.1F;
                playClickSound(pitch);

                int curInput = tile.amountOutput;
                tile.amountOutput += change;
                TileConfigPacket.sendToServer(tile);
                tile.amountOutput = curInput;
                return true;
            }
        }
                .setTooltipFactory((element, mouseX, mouseY) -> {

                    if (element.enabled()) {
                        int change = 1000;

                        if (hasShiftDown()) {
                            change *= 10;
                        }
                        if (hasControlDown()) {
                            change /= 100;
                        }
                        return Collections.singletonList(Component.literal(
                                localize("info.cofh.increase_by")
                                        + " " + format(change)
                                        + "/" + format(change / 10)));
                    }
                    return Collections.emptyList();
                })
                .setName("IncOutput")
                .setSize(14, 14)
                .setTexture(TEX_INCREMENT, 42, 14)
                .setEnabled(() -> tile.amountOutput < tile.getMaxOutput());

        addElement(decInput);
        addElement(incInput);
        addElement(decOutput);
        addElement(incOutput);
    }

    protected int getChangeAmount(int mouseButton) {

        int change = 1000;

        if (hasShiftDown()) {
            change *= 10;
        }
        if (hasControlDown()) {
            change /= 100;
        }
        if (mouseButton == 1) {
            change /= 10;
        }
        return change;
    }

    protected float getPitch(int mouseButton) {

        float pitch = 0.7F;

        if (hasShiftDown()) {
            pitch += 0.1F;
        }
        if (hasControlDown()) {
            pitch -= 0.2F;
        }
        if (mouseButton == 1) {
            pitch -= 0.1F;
        }
        return pitch;
    }
    // endregion
}
