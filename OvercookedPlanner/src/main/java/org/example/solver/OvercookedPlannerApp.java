package org.example.solver;

import org.example.constraints.RecipeConstraintProvider;
import org.example.domain.*;
import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.example.domain.actions.TaskOrCharacter;
import org.example.domain.grid.Grid;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OvercookedPlannerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(OvercookedPlannerApp.class);

    public static void plan(Grid grid) {
        SolverFactory<KitchenSchedule> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(KitchenSchedule.class)
                .withEntityClasses(Task.class, TaskOrCharacter.class)
                .withConstraintProviderClass(RecipeConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig().withBestScoreLimit("0hard/0soft").withSecondsSpentLimit(60L))
        );

        // Load the problem
        // TODO: Ajouter une classe comme Menu pour regrouper un collection de recettes.
        KitchenSchedule problem = generateDemoData();

        // Solve the problem
        Solver<KitchenSchedule> solver = solverFactory.buildSolver();
        KitchenSchedule solution = solver.solve(problem);

        // Visualize the solution
        printPlan(solution);
    }

    public static KitchenSchedule generateDemoData() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character("0"));
        characters.add(new Character("1"));

        List<String> recipesToFetch = new ArrayList<>();
        recipesToFetch.add(RecipeRepository.ONION_SOUP_RECIPE);

        RecipeRepository repository = new RecipeRepository();
        List<Recipe> recipes = repository.getRecipes(recipesToFetch);
        List<Task> tasks = new ArrayList<>();
        recipes.forEach(recipe -> tasks.addAll(recipe.getTasks()));
        tasks.add(new Task("dumb task 1", false, false));
        //tasks.add(new Task("dumb task 2", null, false, false));
        //tasks.add(new Task("dumb task 3", null, false, false));
        //tasks.add(new Task("dumb task 4", null, false, false));
        //tasks.add(new Task("dumb task 5", null, false, false));
        //tasks.add(new Task("dumb task 6", null, false, false));
        //tasks.add(new Task("dumb task 7", null, false, false));
        //tasks.add(new Task("dumb task 8", null, false, false));
        //tasks.add(new Task("dumb task 9", null, false, false));

        // TODO: Retourner la liste complète une fois la classe Menu implémentée
        return new KitchenSchedule(characters, tasks);
    }

    private static void printPlan(KitchenSchedule solution) {
        for (Character character : solution.getCharacterList()) {
            // recipe.getTaskAssignments().sort(Comparator.comparing(ta -> ta.getTask().getFinishedOrder()));
            LOGGER.info("");
            LOGGER.info(("Character " + character.getId()));
            Task task = character.getNextElement();
            while(task != null) {
                LOGGER.info(task.getName() + (task.isHandEmpty() ? "" : " ✋"));
                LOGGER.info("Task order: " + taskAssignment.getTask().getFinishedOrder());
                task = task.getNextElement();
            }
        }
    }
}
