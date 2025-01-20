package com.synthestra.xeno_artifacts.client.gui.screen;

import com.synthestra.xeno_artifacts.item.component.XenoArtifactScrapbookData;
import com.synthestra.xeno_artifacts.registry.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.List;

public class XenoArtifactNotebookScreen extends Screen {
    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
    public static final int PAGE_TEXT_X_OFFSET = 36;
    public static final int PAGE_TEXT_Y_OFFSET = 30;
    public static final XenoArtifactScrapbookData EMPTY = new XenoArtifactScrapbookData(List.of());
    public static final ResourceLocation BOOK_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/book.png");
    protected static final int TEXT_WIDTH = 114;
    protected static final int TEXT_HEIGHT = 128;
    protected static final int IMAGE_WIDTH = 192;
    protected static final int IMAGE_HEIGHT = 192;
    private int currentPage;
    private PageButton forwardButton;
    private PageButton backButton;
    public XenoArtifactScrapbookData data;

    public XenoArtifactNotebookScreen() {
        this(EMPTY);
    }

    public XenoArtifactNotebookScreen(XenoArtifactScrapbookData data) {
        super(Component.translatable("item.xeno_artifacts.xeno_artifact_scrapbook"));
        this.data = data;
    }

    protected void init() {
        this.createMenuControls();
        //this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onClose();
        }).bounds(this.width / 2 - 100, 196, 200, 20).build());
    }

//    protected void createPageControlButtons() {
//        int i = (this.width - 192) / 2;
//        this.forwardButton = this.addRenderableWidget(new PageButton(i + 116, 159, true, (button) -> {
//            this.pageForward();
//        }, this.playTurnSound));
//        this.backButton = this.addRenderableWidget(new PageButton(i + 43, 159, false, (button) -> {
//            this.pageBack();
//        }, this.playTurnSound));
//        this.updateButtonVisibility();
//    }
//
//    private int getNumPages() {
//        return this.bookAccess.getPageCount();
//    }
//
//    protected void pageBack() {
//        if (this.currentPage > 0) {
//            --this.currentPage;
//        }
//
//        this.updateButtonVisibility();
//    }
//
//    protected void pageForward() {
//        if (this.currentPage < this.getNumPages() - 1) {
//            ++this.currentPage;
//        }
//
//        this.updateButtonVisibility();
//    }
//
//    private void updateButtonVisibility() {
//        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
//        this.backButton.visible = this.currentPage > 0;
//    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return switch (keyCode) {
            case 266 -> {
                this.backButton.onPress();
                yield true;
            }
            case 267 -> {
                this.forwardButton.onPress();
                yield true;
            }
            default -> false;
        };
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int i = (this.width - 192) / 2;

        Component pageMsg = Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(0, 1));

        int k = this.font.width(pageMsg);
        guiGraphics.drawString(this.font, pageMsg, i - k + 192 - 44, 18, 0, false);

        this.renderPage(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void renderPage(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int page = 0;
        int center = (this.width -16) / 2;
        int dWidth = 0;
        int imgSize = 0;
        int padding = 3;
        int edgeBoundary = 80;

        List<Integer> a = new ArrayList<>();
        a.add(0);
        a.add(1);
        a.add(1);
        a.add(1);


//        for (int i = 0; i < a.size(); i++) {
//            if (a.get(i) == 0) continue;
//            int width = edgeBoundary / 2 + (16 + widthGapSize) * (i -1); // i = index of box on row
//
//            guiGraphics.renderFakeItem(ModItems.NODE_SCANNER_PRINT_OUT.get().getDefaultInstance(), width + (this.width / 2) - (192 / 2), 30 + (a.get(i) * 30));
//        }
        this.renderNoteContainer(guiGraphics, 0, 0, 0, 0);
    }

    public void renderNoteContainer(GuiGraphics guiGraphics, int x, int y, int xWidth, int yHeight) {
        int edgeBoundary = 80;

        List<Integer> a = new ArrayList<>();
        a.add(0);
        a.add(1);
        a.add(1);
        a.add(1);
        int widthGapSize = (((IMAGE_WIDTH - edgeBoundary - 48) / 2) - 1);


        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) == 0) continue;
            int width = edgeBoundary / 2 + (16 + widthGapSize) * (i -1); // i = index of box on row

            guiGraphics.renderFakeItem(Items.STONE.getDefaultInstance(), width + (this.width / 2) - (192 / 2), 30 + (a.get(i) * 30));
        }

    }

//    public Bounds2D getBounds() {
//
//
//    }

    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        //guiGraphics.blit(BOOK_LOCATION, (this.width - 192) / 2, 2, 0, 0, 192, 192);
    }

//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (button == 0) {
//            Style style = this.getClickedComponentStyleAt(mouseX, mouseY);
//            if (style != null && this.handleComponentClicked(style)) {
//                return true;
//            }
//        }
//
//        return super.mouseClicked(mouseX, mouseY, button);
//    }

//    public boolean handleComponentClicked(Style style) {
//        ClickEvent clickEvent = style.getClickEvent();
//        if (clickEvent == null) {
//            return false;
//        } else if (clickEvent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
//            String string = clickEvent.getValue();
//
//            try {
//                int i = Integer.parseInt(string) - 1;
//                return this.forcePage(i);
//            } catch (Exception var5) {
//                return false;
//            }
//        } else {
//            boolean bl = super.handleComponentClicked(style);
//            if (bl && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
//                this.closeScreen();
//            }
//
//            return bl;
//        }
//    }

    protected void closeScreen() {
        this.minecraft.setScreen(null);
    }

//    @Nullable
//    public Style getClickedComponentStyleAt(double mouseX, double mouseY) {
//        if (!this.cachedPageComponents.isEmpty()) {
//            int i = Mth.floor(mouseX - (double) ((this.width - 192) / 2) - 36.0);
//            int j = Mth.floor(mouseY - 2.0 - 30.0);
//            if (i >= 0 && j >= 0) {
//                Objects.requireNonNull(this.font);
//                int k = Math.min(128 / 9, this.cachedPageComponents.size());
//                if (i <= 114) {
//                    Objects.requireNonNull(this.minecraft.font);
//                    if (j < 9 * k + k) {
//                        Objects.requireNonNull(this.minecraft.font);
//                        int l = j / 9;
//                        if (l >= 0 && l < this.cachedPageComponents.size()) {
//                            FormattedCharSequence formattedCharSequence = this.cachedPageComponents.get(l);
//                            return this.minecraft.font.getSplitter().componentStyleAtWidth(formattedCharSequence, i);
//                        }
//
//                        return null;
//                    }
//                }
//
//            }
//        }
//        return null;
//    }
}
