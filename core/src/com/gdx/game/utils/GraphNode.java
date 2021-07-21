package com.gdx.game.utils;

import java.util.*;
/**
 * Node object used in Graph
 */
public class GraphNode {

    public int[] pos;

    public List<GraphNode> shortestPath = new LinkedList<>();

    public Integer distance = Integer.MAX_VALUE;

    Map<GraphNode, Integer> adjacentNodes = new HashMap<>();

    public void addAdjacent(GraphNode destination, int distance) {
        adjacentNodes.put(destination, distance);
    }
    public boolean isAdjacent(GraphNode n){
        if(this == n){
            return false;
        }
        else if(
            n.pos[0]==pos[0]-1 && n.pos[1]==pos[1] ||
            n.pos[0]==pos[0] && n.pos[1]==pos[1]-1 ||
            n.pos[0]==pos[0] && n.pos[1]==pos[1]+1 ||
            n.pos[0]==pos[0]+1 && n.pos[1]==pos[1]
        ){
            return true;
        }
        return false;
    }

    public GraphNode(int[] pos) {
        this.pos = pos;
    }
}