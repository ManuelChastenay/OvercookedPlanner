package org.example.domain;

import org.example.domain.actions.Task;
import org.example.utils.Pathfinding;

import java.awt.*;
import java.util.*;
import java.util.List;

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

        for (Point position : Pathfinding.getRelatedPositionsOf("🧅")){
            Task takeOnionTask1 = new Task("Take onion1", true, false, position);
            Task takeOnionTask2 = new Task("Take onion2", true, false, position);
            taskList.add(takeOnionTask1);
            taskList.add(takeOnionTask2);
            for (Point positionKnife : Pathfinding.getRelatedPositionsOf("🔪")) {
                //Pour l'instant, on coupe l'objet dans les mains et le le reprends, on ne s'en discossie pas vraiment
                Task cutOnionTask1 = new Task("Cut onion1", takeOnionTask1, false, false,positionKnife);
                Task cutOnionTask2 = new Task("Cut onion2", takeOnionTask2, false, false, positionKnife);
                taskList.add(cutOnionTask1);
                taskList.add(cutOnionTask2);

                for (Point positionPot : Pathfinding.getRelatedPositionsOf("🍲")){
                    Task placeOnionInPotTask1 = new Task("Place onion1 in pot", cutOnionTask1, false, true,positionPot);
                    Task placeOnionInPotTask2 = new Task("Place onion2 in pot", cutOnionTask2, false, true,positionPot);
                    taskList.add(placeOnionInPotTask1);
                    taskList.add(placeOnionInPotTask2);

                    for (Point positionBowl : Pathfinding.getRelatedPositionsOf("🆕")){
                        List<Task> takeBowlDependencies = new ArrayList<>();
                        takeBowlDependencies.add(placeOnionInPotTask1);
                        takeBowlDependencies.add(placeOnionInPotTask2);
                        Task takeBowlTask = new Task("Take bowl", takeBowlDependencies, true, false, positionBowl);
                        taskList.add(takeBowlTask);

                        for (Point positionPot2 : Pathfinding.getRelatedPositionsOf("🍲")){
                            Task putSoupTask = new Task("Put soup in bowl", takeBowlTask, false, false,positionPot2);
                            taskList.add(putSoupTask);
                            for (Point positionHand : Pathfinding.getRelatedPositionsOf("🤲")){
                                Task serveSoupTask = new Task("Serve soup", putSoupTask, false, true, positionHand);
                                taskList.add(serveSoupTask);
                            }
                        }
                    }
                }
            }
        }


        //TODO Tester en décommentant cette ligne, il va manquer une contrainte pour réorganiser les TaskAssignment, mais on doit leur donner un timestamp de début/fin
        //Cette inversion a un impact majeur dans le temps de calcul, pour l'instant, avec 2 contraintes, on passe de 0.5sec à ~30 sec.
        //taskList = taskList.reversed();
        Collections.shuffle(taskList);

        onionSoup.setTasks(taskList);
        repository.put(ONION_SOUP_RECIPE, onionSoup);
    }
}
