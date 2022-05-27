package model;

import java.util.List;

public record GameCondition(Condition condition, List<GameObject> gameObjects) {}
