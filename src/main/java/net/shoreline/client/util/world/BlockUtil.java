package net.shoreline.client.util.world;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.shoreline.client.util.Globals;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class BlockUtil implements Globals
{
    /**
     *
     * @return
     */
    public static List<BlockEntity> blockEntities()
    {
        List<BlockEntity> list = new ArrayList<>();
        for (WorldChunk chunk : loadedChunks())
        {
            list.addAll(chunk.getBlockEntities().values());
        }
        return list;
    }

    /**
     * Credit https://github.com/BleachDev/BleachHack/blob/1.19.4/src/main/java/org/bleachhack/util/world/WorldUtils.java#L83
     *
     * @return
     */
    public static List<WorldChunk> loadedChunks()
    {
        List<WorldChunk> chunks = new ArrayList<>();
        int viewDist = mc.options.getViewDistance().getValue();
        for (int x = -viewDist; x <= viewDist; x++)
        {
            for (int z = -viewDist; z <= viewDist; z++)
            {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk(
                        (int) mc.player.getX() / 16 + x, (int) mc.player.getZ() / 16 + z);
                if (chunk != null)
                {
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }

    /**
     *
     * @param pos
     * @return
     */
    public static boolean isBlockAccessible(BlockPos pos)
    {
        return mc.world.isAir(pos) && !mc.world.isAir(pos.add(0, -1, 0))
                && mc.world.isAir(pos.add(0, 1, 0)) && mc.world.isAir(pos.add(0, 2, 0));
    }


    /**
     *
     * @param x
     * @param z
     * @return
     */
    public static boolean isBlockLoaded(double x, double z)
    {
        ChunkManager chunkManager = mc.world.getChunkManager();
        if (chunkManager != null)
        {
            return chunkManager.isChunkLoaded(ChunkSectionPos.getSectionCoord(x),
                    ChunkSectionPos.getSectionCoord(z));
        }
        return false;
    }
    public static List<BlockPos> getSphere(Vec3d origin, float range)
    {
        List<BlockPos> sphere = new ArrayList<>();
        double rad = Math.ceil(range);
        for (double x = -rad; x <= rad; ++x)
        {
            for (double y = -rad; y <= rad; ++y)
            {
                for (double z = -rad; z <= rad; ++z)
                {
                    Vec3i pos = new Vec3i((int) (origin.getX() + x),
                            (int) (origin.getY() + y), (int) (origin.getZ() + z));
                    final BlockPos p = new BlockPos(pos);
                    sphere.add(p);
                }
            }
        }
        return sphere;
    }
}
