package net.unladenswallow.minecraft.autofish.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.unladenswallow.minecraft.autofish.config.AutoFishModConfig;
import net.unladenswallow.minecraft.autofish.config.ConfigOption;

public class ConfigGui extends Screen {

    private static final int BUTTON_HEIGHT = 16;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_START_Y = 45;
    private static final int X_PADDING = 30;
    private int _longestConfigLabelWidth = 0;
    // I don't know what this magic number means, but I saw it in Forge code, so there you go.
    public static final int DRAW_STRING_MAGIC_NUMBER = 16777215;

    private List<Label> labels = new ArrayList<Label>();


    public ConfigGui() {
        // TODO Auto-generated constructor stub
        super(new StringTextComponent("Test String"));
    }

    @Override
    public void func_230430_a_(MatrixStack matfixStack, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(matfixStack);
        this.drawString(ForgeI18n.parseMessage("gui.autofish.config.title"), this.field_230708_k_ / 2, 10, DRAW_STRING_MAGIC_NUMBER);
        this.drawString(AutoFishModConfig.ConfigFilePath, this.field_230708_k_ / 2, 25, DRAW_STRING_MAGIC_NUMBER);
        int bottomLabelY = 0;
        for (Label label : labels) {
            label.func_230430_a_(matfixStack, mouseX, mouseY, 1f);
            bottomLabelY = Math.max(bottomLabelY, label.y);
        }
        super.func_230430_a_(matfixStack, mouseX, mouseY, partialTicks);
        /* for (Label label : labels) {
            if (label.func_230449_g_()) {
                label.renderToolTip(mouseX, mouseY);
            }
        } */
    }

    public void drawString(String text, int x, int y, int color) {
		try {
			FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
			//Reflect.on(fontRenderer).call("renderString", ChatColor.translateAlternateColorCodes(text), x, y, color,
			//		TransformationMatrix.identity().getMatrix(), true, true);
			//fontRenderer.getStringWidth(text);

			IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			fontRenderer.func_238411_a_(text, x, y, color, true,
					TransformationMatrix.identity().getMatrix(), buffer, false, 0, 15728880, false);
			buffer.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void closeGui() {
        this.func_231175_as__();
    }

    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        int maxAllowedLabelWidth = this.field_230708_k_ - BUTTON_WIDTH - (3 * X_PADDING);
        int labelWidth = Math.min(maxAllowedLabelWidth, getLongestConfigLabelWidth());
        int labelX = (this.field_230708_k_ - (labelWidth + X_PADDING + BUTTON_WIDTH)) / 2;
        int buttonX = this.field_230708_k_ - labelX - BUTTON_WIDTH;
        int buttonIndex = 1;
        this.labels = new ArrayList<Label>();
        for (ConfigOption option : AutoFishModConfig.getOrderedConfigValues()) {
            int rowY = BUTTON_START_Y + (int)((buttonIndex-1) * BUTTON_HEIGHT * 1.2);
            this.labels.add(new Label(
                    ForgeI18n.parseMessage(option.configLabelI18nPattern),
                    ForgeI18n.parseMessage(String.format("%s.description", option.configLabelI18nPattern)),
                    labelX,
                    rowY+2,
                    this.field_230708_k_,
                    this.field_230709_l_));
            func_230480_a_(new ConfigGuiButton(this.field_230712_o_, buttonX, rowY, BUTTON_WIDTH, BUTTON_HEIGHT, option));
            buttonIndex++;
        }
        func_230480_a_(new ExtendedButton((this.field_230708_k_ - BUTTON_WIDTH) / 2, this.field_230709_l_ - 20 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, new StringTextComponent(ForgeI18n.parseMessage("gui.autofish.config.done")),
                b -> {this.closeGui();}));
    }

    private int getLongestConfigLabelWidth() {
        if (_longestConfigLabelWidth > 0) {
            return _longestConfigLabelWidth;
        }
        _longestConfigLabelWidth = 1;
        for (ConfigOption option : AutoFishModConfig.getOrderedConfigValues()) {
            int labelWidth = this.field_230712_o_.getStringWidth(ForgeI18n.parseMessage(option.configLabelI18nPattern));
            if (labelWidth > _longestConfigLabelWidth) {
                _longestConfigLabelWidth = labelWidth;
            }
        }
        return _longestConfigLabelWidth;
    }

    class Label extends Widget {
        private int x;
        private int y;
        private String text;
        private String tooltip;
        private int screenWidth;
        private int screenHeight;

        public Label(String text, String tooltip, int x, int y, int screenWidth, int screenHeight) {
            super(x, y, 200, 20, new StringTextComponent(text));
            this.x = x;
            this.y = y;
            this.field_230691_m_ = field_230712_o_.FONT_HEIGHT + 2;
            this.text = text;
            this.tooltip = tooltip;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
        }


        @Override
        public void func_230430_a_(MatrixStack matfixStack, int mouseX, int mouseY, float p_render_3_) {
            super.func_230430_a_(matfixStack, mouseX, mouseY, p_render_3_);
            drawString(this.text, this.x, this.y, DRAW_STRING_MAGIC_NUMBER);
        }

        @Override
        public void func_230443_a_(MatrixStack matfixStack, int mouseX, int mouseY) {
            /* GuiUtils.drawHoveringText(
                    Arrays.asList(this.tooltip),
                    mouseX,
                    mouseY,
                    this.screenWidth,
                    this.screenHeight,
                    -1,
                    field_230712_o_); */

        }


    }

}
