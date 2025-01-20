package com.synthestra.xeno_artifacts.item.component;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.synthestra.xeno_artifacts.registry.ModDataComponents;
import com.synthestra.xeno_artifacts.registry.ModItems;
import com.synthestra.xeno_artifacts.xeno_artifact.XenoArtifactNode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public record XenoArtifactScrapbookData(List<XenoArtifactScrapbookPage> pages) implements TooltipComponent {
    public static final XenoArtifactScrapbookData EMPTY = new XenoArtifactScrapbookData(List.of());

    public static final Codec<XenoArtifactScrapbookData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            XenoArtifactScrapbookPage.CODEC.listOf().fieldOf("pages").forGetter(e -> e.pages)
    ).apply(instance, XenoArtifactScrapbookData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, XenoArtifactScrapbookData> STREAM_CODEC = StreamCodec.composite(
            XenoArtifactScrapbookPage.STREAM_CODEC.apply(ByteBufCodecs.list()), XenoArtifactScrapbookData::pages,
            XenoArtifactScrapbookData::new
    );


//
//    public Iterable<ItemStack> itemsCopy() {
//        return Lists.transform(this.items, ItemStack::copy);
//    }

    public int size() {
        return this.pages.size();
    }

    public boolean isEmpty() {
        return this.pages.isEmpty();
    }

    public String toString() {
        return "XenoArtifactScrapbookData" + this.pages;
    }

    public static class Mutable {
        private final List<XenoArtifactScrapbookPage> pages;

        public Mutable(XenoArtifactScrapbookData contents) {
            this.pages = new ArrayList<>(contents.pages);
        }

        public XenoArtifactScrapbookData.Mutable clearPages() {
            this.pages.clear();
            return this;
        }

        public ItemStack createNote(XenoArtifactScrapbookPage page) {
            ItemStack stack = new ItemStack(Items.STONE);
            UUID uuid = page.uuid();
            XenoArtifactNode node = page.notes().removeFirst();
            stack.set(ModDataComponents.NODE_SCANNER_DATA.get(), new NodeScannerPrintOutData(uuid, node));
            return stack;
        }

        public boolean tryInsert(ItemStack stack) {
            NodeScannerPrintOutData data = stack.get(ModDataComponents.NODE_SCANNER_DATA.get());
            if (data == null) return false;
            Optional<XenoArtifactScrapbookPage> foundPage = getPage(data.uuid());
            if (foundPage.isPresent()) System.out.println("page already exists, append");
            XenoArtifactScrapbookPage page = foundPage.orElseGet(() -> new XenoArtifactScrapbookPage(data.uuid(), new ArrayList<>()));
            page.notes().add(data.node());
            int index = this.pages.indexOf(page);
            if (index == -1) {
                System.out.println("new page");
                this.pages.add(page);
            } else {
                this.pages.set(index, page);
            }
            //XenoArtifactScrapbookPage page = this.pages.removeFirst();
            stack.shrink(1);
            //this.pages.addFirst(itemStack2);

            return true;

        }

        public Optional<XenoArtifactScrapbookPage> getPage(UUID uuid) {
            return this.pages.stream().filter(e -> e.uuid().equals(uuid)).findFirst();
        }

//        public int tryTransfer(Slot slot, Player player) {
//            ItemStack itemStack = slot.getItem();
//            return this.tryInsert(slot.safeTake(itemStack.getCount(), -1, player));
//        }

        @Nullable
        public ItemStack removeOne() {
            if (this.pages.isEmpty()) return null;
            return createNote(this.pages.getFirst().copy());

        }

        public XenoArtifactScrapbookData toImmutable() {
            return new XenoArtifactScrapbookData(List.copyOf(this.pages));
        }

    }
}