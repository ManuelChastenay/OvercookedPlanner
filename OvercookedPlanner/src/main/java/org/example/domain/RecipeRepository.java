package org.example.domain;

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

        Task takeOnionTask1 = new Task("Take onion1", false, true);
        Task takeOnionTask2 = new Task("Take onion2", false, true);
        taskList.add(takeOnionTask1);
        taskList.add(takeOnionTask2);

        //Pour l'instant, on coupe l'objet dans les mains et le le reprends, on ne s'en discossie pas vraiment
        Task cutOnionTask1 = new Task("Cut onion1", takeOnionTask1, true, true);
        Task cutOnionTask2 = new Task("Cut onion2", takeOnionTask2, true, true);
        taskList.add(cutOnionTask1);
        taskList.add(cutOnionTask2);

        Task placeOnionInPotTask1 = new Task("Place onion1 in pot", cutOnionTask1, true, false);
        Task placeOnionInPotTask2 = new Task("Place onion2 in pot", cutOnionTask2, true, false);
        taskList.add(placeOnionInPotTask1);
        taskList.add(placeOnionInPotTask2);

        List<Task> takeBowlDependencies = new ArrayList<>();
        takeBowlDependencies.add(placeOnionInPotTask1);
        takeBowlDependencies.add(placeOnionInPotTask2);
        Task takeBowlTask = new Task("Take bowl", takeBowlDependencies, false, true);
        taskList.add(takeBowlTask);

        Task putSoupTask = new Task("Put soup in bowl", takeBowlTask, true, true);
        taskList.add(putSoupTask);

        Task serveSoupTask = new Task("Serve soup", putSoupTask, true, false);
        taskList.add(serveSoupTask);

        //TODO Tester en décommentant cette ligne, il va manquer une contrainte pour réorganiser les TaskAssignment, mais on doit leur donner un timestamp de début/fin
        //Cette inversion a un impact majeur dans le temps de calcul, pour l'instant, avec 2 contraintes, on passe de 0.5sec à ~30 sec.
        //taskList = taskList.reversed();
        Collections.shuffle(taskList);

        onionSoup.setTasks(taskList);
        repository.put(ONION_SOUP_RECIPE, onionSoup);
    }
}
