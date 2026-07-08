public class RTPMenu {

    private final MoulcchttaaRTP plugin;

    public RTPMenu(MoulcchttaaRTP plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Chọn thế giới RTP");

        ItemStack filler = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, filler);
        }

        inv.setItem(10, createItem(Material.GRASS_BLOCK, "§aTài nguyên",
                "§7Nhấn để RTP tới world thường",
                "§7Đếm ngược: §e5 giây",
                "§7Di chuyển sẽ hủy"));

        inv.setItem(13, createItem(Material.NETHERRACK, "§cNether",
                "§7Nhấn để RTP tới Nether",
                "§7Đếm ngược: §e5 giây",
                "§7Di chuyển sẽ hủy"));

        inv.setItem(16, createItem(Material.END_STONE, "§dThe End",
                "§7Nhấn để RTP tới End",
                "§7Đếm ngược: §e5 giây",
                "§7Di chuyển sẽ hủy"));

        inv.setItem(11, createItem(Material.COMPASS, "§eThông tin RTP",
                "§7- Teleport ngẫu nhiên an toàn",
                "§7- Đếm ngược 5 giây",
                "§7- Di chuyển sẽ hủy"));

        inv.setItem(15, createItem(Material.BARRIER, "§cĐóng menu"));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material material, String name, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (loreLines != null && loreLines.length > 0) {
                meta.setLore(Arrays.asList(loreLines));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
