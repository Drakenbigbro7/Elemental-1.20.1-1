package com.elemental.client.screen;

import com.elemental.Elemental;
import com.elemental.screen.WeaponerScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Client-side GUI for the Weaponer crafting station.
 * Features custom elemental forge chamber styling, active forging arrow overlay,
 * and informative hover tooltips for empty input slots.
 */
public class WeaponerScreen extends HandledScreen<WeaponerScreenHandler> {
    public static final Identifier TEXTURE = new Identifier(Elemental.MOD_ID, "textures/gui/weaponer.png");

    public WeaponerScreen(WeaponerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        // Dynamically center the title on the top banner
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.titleY = 5;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = 73;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int startX = (this.width - this.backgroundWidth) / 2;
        int startY = (this.height - this.backgroundHeight) / 2;

        // Draw main container texture
        context.drawTexture(TEXTURE, startX, startY, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // When a weapon is forged in slot 2, overlay the radiant active forging arrow
        if (this.handler.getSlot(WeaponerScreenHandler.RESULT_SLOT).hasStack()) {
            context.drawTexture(TEXTURE, startX + 98, startY + 48, 176, 0, 28, 17);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        // Guide tooltips when hovering over empty input slots with no item held
        if (this.focusedSlot == null && this.handler.getCursorStack().isEmpty()) {
            int relX = mouseX - this.x;
            int relY = mouseY - this.y;

            if (relX >= 27 && relX <= 43 && relY >= 47 && relY <= 63) {
                if (!this.handler.getSlot(WeaponerScreenHandler.STENCIL_SLOT).hasStack()) {
                    context.drawTooltip(this.textRenderer, Text.translatable("tooltip.elemental.weaponer.stencil_slot"), mouseX, mouseY);
                }
            } else if (relX >= 76 && relX <= 92 && relY >= 47 && relY <= 63) {
                if (!this.handler.getSlot(WeaponerScreenHandler.ELEMENT_SLOT).hasStack()) {
                    context.drawTooltip(this.textRenderer, Text.translatable("tooltip.elemental.weaponer.element_slot"), mouseX, mouseY);
                }
            }
        }
    }
}
