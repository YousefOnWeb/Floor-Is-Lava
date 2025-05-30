package com.bluenova.floorislava.util;

import com.bluenova.floorislava.FloorIsLava;
import com.bluenova.floorislava.game.object.GamePlot;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.block.BlockReplace;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ElevateLava extends Workload {

    GamePlot gp;
    int y;

    public ElevateLava(GamePlot gp, int y) {
        this.gp = gp;
        this.y = y;
    }

    @Override
    public void compute() {
        Bukkit.getScheduler().runTaskAsynchronously(FloorIsLava.getInstance(), () -> {
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(FloorIsLava.getVoidWorld()))) {
                Region region = new CuboidRegion(BlockVector3.at(gp.plotStart.getX(), y, gp.plotStart.getZ()),
                        BlockVector3.at(gp.plotEnd.getX() - 1, y, gp.plotEnd.getZ() - 1));
                Pattern lavaPattern = BukkitAdapter.adapt(Material.LAVA.createBlockData());
                RegionFunction lavaFunction = new BlockReplace(editSession, lavaPattern);
                RegionVisitor lavaVisitor = new RegionVisitor(region, lavaFunction);
                Operations.complete(lavaVisitor);
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

