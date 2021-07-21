package com.gdx.game.utils;

import com.gdx.game.entities.IPlaceable;

public class PositionUtils {
    /**
     * Check if 2 positions are the same
     */
    public static boolean samePos(int[] pos1, int[] pos2){
        return (pos1[0] == pos2[0] && pos1[1] == pos2[1]);
    }
    /**
     * Checks if this building is overlapping with another
     */
    public static boolean overlaps(IPlaceable a, IPlaceable b){
        // check pos1=pos2
        if(samePos(a.getPlacementPos(), b.getPlacementPos())) return true;
        // check overlapping1=pos2
        for(Distance d : a.getOverlappingTiles()){
            if(samePos(new int[]{a.getPlacementPos()[0]+d.dx, a.getPlacementPos()[1]+d.dy}, b.getPlacementPos())) return true;
        }
        // check pos1=overlapping2
        for(Distance d : b.getOverlappingTiles()){
            if(samePos(new int[]{b.getPlacementPos()[0]+d.dx, b.getPlacementPos()[1]+d.dy}, a.getPlacementPos())) return true;
        }
        // check overlapping1=overlapping2
        for(Distance d1 : a.getOverlappingTiles()){
            for(Distance d2 : b.getOverlappingTiles()){
                if(samePos(
                        new int[]{a.getPlacementPos()[0]+d1.dx, a.getPlacementPos()[1]+d1.dy},
                        new int[]{b.getPlacementPos()[0]+d2.dx, b.getPlacementPos()[1]+d2.dy}
                )) return true;
            }
        }
        return false;
    }
}
