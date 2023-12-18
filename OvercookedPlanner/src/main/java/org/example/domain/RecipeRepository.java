package org.example.domain;

import org.example.domain.actions.Item;
import org.example.domain.actions.Task;
import org.example.utils.Pathfinding;

import java.util.*;

public class RecipeRepository {
    public static final String ONION_SOUP_RECIPE = "ONION_SOUP_RECIPE";
    public static final String BROCOLI_SOUP_RECIPE = "BROCOLI_SOUP_RECIPE";

    Map<String, Recipe> repository = new HashMap<>();

    public RecipeRepository(){
        this.createRepository();
    }

    private void createRepository(){
        this.addOnionSoup(0);
        this.addBrocoliSoup(0);
    }

    public List<Recipe> getRecipes(List<String> recipesToFetch){
        List<Recipe> recipes = new ArrayList<>();
        for (String toFetch : recipesToFetch) {
            recipes.add(repository.get(toFetch));
        }
        return recipes;
    }

    private void addOnionSoup(Integer startTime){
        List<Task> taskList = new ArrayList<>();
        Recipe onionSoup = new Recipe(ONION_SOUP_RECIPE, startTime);

        Item onion = new Item("\uD83E\uDDC5");
        Task takeOnionTask1 = new Task("Take onion", null, onion, 1, Pathfinding.getRelatedPositionsOf("🧅"));
        Task takeOnionTask2 = new Task("Take onion", null, onion, 1, Pathfinding.getRelatedPositionsOf("🧅"));
        taskList.add(takeOnionTask1);
        taskList.add(takeOnionTask2);

        Item onionCut = new Item("\uD83E\uDDC5\uD83D\uDD2A");
        Task cutOnionTask1 = new Task("Cut onion", takeOnionTask1, onion, onionCut, 10, Pathfinding.getRelatedPositionsOf("🔪"));
        Task cutOnionTask2 = new Task("Cut onion", takeOnionTask2, onion, onionCut, 10, Pathfinding.getRelatedPositionsOf("🔪"));
        taskList.add(cutOnionTask1);
        taskList.add(cutOnionTask2);

        Task placeOnionInPotTask1 = new Task("Place onion in pot", cutOnionTask1, onionCut, null, 1, Pathfinding.getRelatedPositionsOf("🍲"));
        Task placeOnionInPotTask2 = new Task("Place onion in pot", cutOnionTask2, onionCut, null, 1, Pathfinding.getRelatedPositionsOf("🍲"));
        taskList.add(placeOnionInPotTask1);
        taskList.add(placeOnionInPotTask2);

        Item bowl = new Item("\uD83E\uDD63");
        List<Task> takeBowlDependencies = new ArrayList<>();
        takeBowlDependencies.add(placeOnionInPotTask1);
        takeBowlDependencies.add(placeOnionInPotTask2);

        Task takeBowlTask = new Task("Take bowl", takeBowlDependencies, null, bowl, 1, Pathfinding.getRelatedPositionsOf("🆕"));
        taskList.add(takeBowlTask);

        Item onionSoupBowl = new Item("\uD83E\uDD63\uD83E\uDDC5");
        Task putSoupTask = new Task("Put onion soup in bowl", takeBowlTask, bowl, onionSoupBowl, 5, Pathfinding.getRelatedPositionsOf("🍲"));
        taskList.add(putSoupTask);

        Task serveSoupTask = new Task("Serve onion soup", putSoupTask, onionSoupBowl, null, 1, Pathfinding.getRelatedPositionsOf("🤲"));
        taskList.add(serveSoupTask);

        onionSoup.setTasks(taskList);
        repository.put(ONION_SOUP_RECIPE, onionSoup);
    }

    private void addBrocoliSoup(Integer startTime){
        List<Task> taskList = new ArrayList<>();
        Recipe brocoliSoup = new Recipe(BROCOLI_SOUP_RECIPE, startTime);

        Item brocoli = new Item("\uD83E\uDD66");
        Task takeBrocoliTask1 = new Task("Take brocoli", null, brocoli, 1);
        Task takeBrocoliTask2 = new Task("Take brocoli", null, brocoli, 1);
        taskList.add(takeBrocoliTask1);
        taskList.add(takeBrocoliTask2);

        Item brocoliCut = new Item("\uD83E\uDD66\uD83D\uDD2A");
        Task cutBrocoliTask1 = new Task("Cut brocoli", takeBrocoliTask1, brocoli, brocoliCut, 15);
        Task cutBrocoliTask2 = new Task("Cut brocoli", takeBrocoliTask2, brocoli, brocoliCut, 15);
        taskList.add(cutBrocoliTask1);
        taskList.add(cutBrocoliTask2);

        Task placeBrocoliInPotTask1 = new Task("Place brocoli in pot", cutBrocoliTask1, brocoliCut, null, 1);
        Task placeBrocoliInPotTask2 = new Task("Place brocoli in pot", cutBrocoliTask2, brocoliCut, null, 1);
        taskList.add(placeBrocoliInPotTask1);
        taskList.add(placeBrocoliInPotTask2);

        Item bowl = new Item("\uD83E\uDD63");
        List<Task> takeBowlDependencies = new ArrayList<>();
        takeBowlDependencies.add(placeBrocoliInPotTask1);
        takeBowlDependencies.add(placeBrocoliInPotTask2);
        Task takeBowlTask = new Task("Take bowl", takeBowlDependencies, null, bowl, 1);
        taskList.add(takeBowlTask);

        Item brocoliSoupBowl = new Item("\uD83E\uDD63\uD83E\uDD66");
        Task putSoupTask = new Task("Put brocoli soup in bowl", takeBowlTask, bowl, brocoliSoupBowl, 3, Pathfinding.getRelatedPositionsOf("🍲"));
        taskList.add(putSoupTask);

        Task serveSoupTask = new Task("Serve brocoli soup", putSoupTask, brocoliSoupBowl, null, 1, Pathfinding.getRelatedPositionsOf("🤲"));
        taskList.add(serveSoupTask);

        brocoliSoup.setTasks(taskList);
        repository.put(BROCOLI_SOUP_RECIPE, brocoliSoup);
    }
}
