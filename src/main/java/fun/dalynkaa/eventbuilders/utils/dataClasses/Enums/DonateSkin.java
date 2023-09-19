package fun.dalynkaa.eventbuilders.utils.dataClasses.Enums;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DonateSkin {
    DEFAULT("Обычка",1, Arrays.asList(Material.GRASS_BLOCK, Material.GREEN_CONCRETE_POWDER, Material.GREEN_CONCRETE, Material.MOSS_BLOCK),Material.SMOOTH_RED_SANDSTONE_SLAB, Material.SMOOTH_SANDSTONE_SLAB),
    GOLD("Золотой" ,2, Arrays.asList(Material.YELLOW_CONCRETE_POWDER, Material.YELLOW_CONCRETE, Material.RAW_GOLD_BLOCK, Material.YELLOW_WOOL),Material.CUT_RED_SANDSTONE_SLAB, Material.JUNGLE_SLAB),
    NETHER("Незер" ,3, Arrays.asList(Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM, Material.NETHERRACK, Material.NETHER_WART_BLOCK),Material.CRIMSON_SLAB, Material.WARPED_SLAB),
    DONATE("ДОНАТ" ,4, Arrays.asList(Material.REDSTONE_BLOCK, Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.NETHERITE_BLOCK),Material.MUD_BRICK_SLAB, Material.SMOOTH_SANDSTONE_SLAB),
    END("Эндер мир" ,5, Arrays.asList(Material.END_STONE, Material.END_STONE_BRICKS, Material.SMOOTH_SANDSTONE, Material.BIRCH_PLANKS),Material.PURPUR_SLAB, Material.END_STONE_BRICK_SLAB);
    private String translate;
    private Integer id;
    private List<Material> materials;
    private Material cornerDefault;
    private Material getCornerVoted;

    DonateSkin(String translate, Integer id, List<Material> materials, Material cornerDefault, Material getCornerVoted) {
        this.translate = translate;
        this.id = id;
        this.materials = materials;
        this.cornerDefault = cornerDefault;
        this.getCornerVoted = getCornerVoted;
    }

    public Integer getId() {
        return id;
    }

    public DonateSkin setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTranslate() {
        return translate;
    }

    public DonateSkin setTranslate(String translate) {
        this.translate = translate;
        return this;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public DonateSkin setMaterials(List<Material> materials) {
        this.materials = materials;
        return this;
    }

    public Material getCornerDefault() {
        return cornerDefault;
    }

    public DonateSkin setCornerDefault(Material cornerDefault) {
        this.cornerDefault = cornerDefault;
        return this;
    }

    public Material getCornerVoted() {
        return getCornerVoted;
    }

    public DonateSkin setGetCornerVoted(Material getCornerVoted) {
        this.getCornerVoted = getCornerVoted;
        return this;
    }
}
