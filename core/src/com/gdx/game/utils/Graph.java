package com.gdx.game.utils;

import java.util.*;
/**
 * Graph representation of the paths
 */
public class Graph {
    private static Set<GraphNode> nodes = new HashSet<>();
    /**
     * Adds a new node to the Graph
     * @param newNode: the node to be inserted
     */
    public static void addNode(GraphNode newNode) {
        nodes.add(newNode);
    }
    /**
     * Dijkstra's Algorithm from given root
     * @param root: the root
     */
    public static void calculateShortestPathsFromRoot(GraphNode root) {
        root.distance = 0;

        Set<GraphNode> settledNodes = new HashSet<>();
        Set<GraphNode> unsettledNodes = new HashSet<>();

        unsettledNodes.add(root);

        while (unsettledNodes.size() != 0) {
            GraphNode currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< GraphNode, Integer> adjacencyPair: currentNode.adjacentNodes.entrySet()) {
                GraphNode adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
    }
    /**
     * Helper method for the main function
     * @param unsettledNodes
     * @return the Node with the lowest distance
     */
    private static GraphNode getLowestDistanceNode(Set <GraphNode> unsettledNodes) {
        GraphNode lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (GraphNode node: unsettledNodes) {
            int nodeDistance = node.distance;
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }
    /**
     * Helper method for the main function
     * @param node: node that we calculate for
     * @param cost: cost of the edge
     * @param root: "ground" node for starting to add costs
     */
    private static void calculateMinimumDistance(GraphNode node, int cost, GraphNode root) {
        int rootDistance = root.distance;
        if (rootDistance + cost < node.distance) {
            node.distance = rootDistance + cost;
            LinkedList<GraphNode> shortestPath = new LinkedList<>(root.shortestPath);
            shortestPath.add(root);
            node.shortestPath=shortestPath;
        }
    }
    /**
     * Gets Node by Position
     * @param pos: the given position
     * @return the respective node
     */
    public static GraphNode getNodeByPos(int[] pos){
        GraphNode output = null;
        for(GraphNode n : nodes){
            if(n.pos[0]==pos[0] && n.pos[1]==pos[1]) output=n;
        }
        return output;
    }
    /**
     * Gets all graph nodes
     */
    public static Set<GraphNode> getNodes(){
        return nodes;
    }

    /**
     * Resets graph nodes
     */
    public static void resetNodes(){
        nodes = new HashSet<>();
    }
}
