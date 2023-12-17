package org.example.domain;

import org.example.domain.actions.Item;
import org.example.domain.actions.Task;

import java.util.*;

public class RecipeRepository {
    public static final String ONION_SOUP_RECIPE = "ONION_SOUP_RECIPE";

    Map<String, Recipe> repository = new HashMap<>();

    public RecipeRepository(){
        this.createRepository();
    }

    private void createRepository(){
        this.addOnionSoup();
    }

    public List<Recipe> getRecipes(List<String> recipesToFetch){
        List<Recipe> recipes = new ArrayList<>();
        for (String toFetch : recipesToFetch) {
            recipes.add(repository.get(toFetch));
        }
        return recipes;
    }

    private void addOnionSoup(){
        List<Task> taskList = new ArrayList<>();

        Recipe onionSoup = new Recipe(ONION_SOUP_RECIPE);

        Item onion = new Item("\uD83E\uDDC5");
        Task takeOnionTask1 = new Task("Take onion", null, onion);
        Task takeOnionTask2 = new Task("Take onion", null, onion);
        taskList.add(takeOnionTask1);
        taskList.add(takeOnionTask2);

        //Pour l'instant, on coupe l'objet dans les mains et le le reprends, on ne s'en discossie pas vraiment
        Item onionCut = new Item("\uD83E\uDDC5\uD83D\uDD2A");
        Task cutOnionTask1 = new Task("Cut onion", onion, onionCut);
        Task cutOnionTask2 = new Task("Cut onion", onion, onionCut);
        taskList.add(cutOnionTask1);
        taskList.add(cutOnionTask2);

        Task placeOnionInPotTask1 = new Task("Place onion in pot", onionCut, null);
        Task placeOnionInPotTask2 = new Task("Place onion in pot", onionCut, null);
        taskList.add(placeOnionInPotTask1);
        taskList.add(placeOnionInPotTask2);

        Item bowl = new Item("\uD83E\uDD63");
        Task takeBowlTask = new Task("Take bowl", null, bowl);
        taskList.add(takeBowlTask);

        Item onionSoupBowl = new Item("\uD83E\uDD63\uD83E\uDDC5");
        List<Task> takeSoupDependencies = new ArrayList<>();
        takeSoupDependencies.add(placeOnionInPotTask1);
        takeSoupDependencies.add(placeOnionInPotTask2);
        Task putSoupTask = new Task("Put onion soup in bowl", takeSoupDependencies, bowl, onionSoupBowl);
        taskList.add(putSoupTask);

        Task serveSoupTask = new Task("Serve onion soup", onionSoupBowl, null);
        taskList.add(serveSoupTask);

        // Random order to full test the solution
        Collections.shuffle(taskList);

        onionSoup.setTasks(taskList);
        repository.put(ONION_SOUP_RECIPE, onionSoup);
    }
}
