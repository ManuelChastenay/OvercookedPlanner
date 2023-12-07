package org.example.domain;

import org.example.domain.actions.Task;

import java.util.*;

public class RecipeRepository {
    public static final String ONION_SOUP_RECIPE = "ONION_SOUP_RECIPE";
    public static final String BROCOLI_SOUP_RECIPE = "BROCOLI_SOUP_RECIPE";

    Map<String, Recipe> repository = new HashMap<>();

    public RecipeRepository(){
        this.createRepository();
    }

    private void createRepository(){
        this.addOnionSoup(1);
        this.addBrocoliSoup(5);
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

        Task takeOnionTask1 = new Task("Take onion1", true, false);
        Task takeOnionTask2 = new Task("Take onion2", true, false);
        taskList.add(takeOnionTask1);
        taskList.add(takeOnionTask2);

        //Pour l'instant, on coupe l'objet dans les mains et le le reprends, on ne s'en discossie pas vraiment
        Task cutOnionTask1 = new Task("Cut onion1", takeOnionTask1, false, false);
        Task cutOnionTask2 = new Task("Cut onion2", takeOnionTask2, false, false);
        taskList.add(cutOnionTask1);
        taskList.add(cutOnionTask2);

        Task placeOnionInPotTask1 = new Task("Place onion1 in pot", cutOnionTask1, false, true);
        Task placeOnionInPotTask2 = new Task("Place onion2 in pot", cutOnionTask2, false, true);
        taskList.add(placeOnionInPotTask1);
        taskList.add(placeOnionInPotTask2);

        List<Task> takeBowlDependencies = new ArrayList<>();
        takeBowlDependencies.add(placeOnionInPotTask1);
        takeBowlDependencies.add(placeOnionInPotTask2);
        Task takeBowlTask = new Task("Take bowl", takeBowlDependencies, true, false);
        taskList.add(takeBowlTask);

        Task putSoupTask = new Task("Put soup in bowl", takeBowlTask, false, false);
        taskList.add(putSoupTask);

        Task serveSoupTask = new Task("Serve soup", putSoupTask, false, true);
        taskList.add(serveSoupTask);

        //TODO Tester en décommentant cette ligne, il va manquer une contrainte pour réorganiser les TaskAssignment, mais on doit leur donner un timestamp de début/fin
        //Cette inversion a un impact majeur dans le temps de calcul, pour l'instant, avec 2 contraintes, on passe de 0.5sec à ~30 sec.
        //taskList = taskList.reversed();
        Collections.shuffle(taskList);

        onionSoup.setTasks(taskList);
        repository.put(ONION_SOUP_RECIPE, onionSoup);
    }

    // Ajout d'une seconde recette pour l'ordonnancement
    private void addBrocoliSoup(Integer startTime){
        List<Task> taskList = new ArrayList<>();

        Recipe brocoliSoup = new Recipe(BROCOLI_SOUP_RECIPE, startTime);

        Task takeBrocoliTask1 = new Task("Take brocoli1", true, false);
        Task takeBrocoliTask2 = new Task("Take brocoli2", true, false);
        taskList.add(takeBrocoliTask1);
        taskList.add(takeBrocoliTask2);

        //Pour l'instant, on coupe l'objet dans les mains et le le reprends, on ne s'en discossie pas vraiment
        Task cutBrocoliTask1 = new Task("Cut brocoli1", takeBrocoliTask1, false, false);
        Task cutBrocoliTask2 = new Task("Cut brocoli2", takeBrocoliTask2, false, false);
        taskList.add(cutBrocoliTask1);
        taskList.add(cutBrocoliTask2);

        Task placeBrocoliInPotTask1 = new Task("Place brocoli1 in pot", cutBrocoliTask1, false, true);
        Task placeBrocoliInPotTask2 = new Task("Place brocoli2 in pot", cutBrocoliTask2, false, true);
        taskList.add(placeBrocoliInPotTask1);
        taskList.add(placeBrocoliInPotTask2);

        List<Task> takeBowlDependencies = new ArrayList<>();
        takeBowlDependencies.add(placeBrocoliInPotTask1);
        takeBowlDependencies.add(placeBrocoliInPotTask2);
        Task takeBowlTask = new Task("Take bowl", takeBowlDependencies, true, false);
        taskList.add(takeBowlTask);

        Task putSoupTask = new Task("Put soup in bowl", takeBowlTask, false, false);
        taskList.add(putSoupTask);

        Task serveSoupTask = new Task("Serve soup", putSoupTask, false, true);
        taskList.add(serveSoupTask);

        //TODO Tester en décommentant cette ligne, il va manquer une contrainte pour réorganiser les TaskAssignment, mais on doit leur donner un timestamp de début/fin
        //Cette inversion a un impact majeur dans le temps de calcul, pour l'instant, avec 2 contraintes, on passe de 0.5sec à ~30 sec.
        //taskList = taskList.reversed();
        Collections.shuffle(taskList);

        brocoliSoup.setTasks(taskList);
        repository.put(BROCOLI_SOUP_RECIPE, brocoliSoup);
    }
}
