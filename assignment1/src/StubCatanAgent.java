import java.util.List;
import java.util.Map;

/**
 * Stub implementation of CatanAgent
 */
public class StubCatanAgent implements CatanAgent {

    private int playerId;

    @Override
    public void init(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public Move chooseInitialSettlement(GameState state) {
        return new Move("INITIAL_SETTLEMENT");
    }

    @Override
    public Move chooseInitialRoad(GameState state) {
        return new Move("INITIAL_ROAD");
    }

    @Override
    public Move chooseMove(GameState state) {
        return new Move("END_TURN");
    }

    @Override
    public Map<ResourceType, Integer> chooseDiscard(GameState state, int discardCount) {
        return Map.of(ResourceType.SHEEP, discardCount);
    }

    @Override
    public ResourceType chooseResource(GameState state) {
        return ResourceType.WOOL;
    }

    @Override
    public int chooseRobberTarget(GameState state, List<Integer> possibleTargets) {
        return possibleTargets.isEmpty() ? -1 : possibleTargets.get(0);
    }

    @Override
    public DevelopmentCard chooseDevelopmentCard(GameState state) {
        return DevelopmentCard.KNIGHT;
    }
}
