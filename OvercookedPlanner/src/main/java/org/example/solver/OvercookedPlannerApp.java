package org.example.solver;

import org.example.constraints.RecipeConstraintProvider;
import org.example.domain.*;
import org.example.domain.Character;
import org.example.domain.actions.Task;
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
        SolverFactory<Recipe> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(Recipe.class)
                .withEntityClasses(Task.class, TaskAssignment.class/*, CharacterOrTaskAssignment.class*/)
                .withConstraintProviderClass(RecipeConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig().withBestScoreLimit("0hard/0soft").withSecondsSpentLimit(60L))
        );

        // Load the problem
        // TODO: Ajouter une classe comme Menu pour regrouper un collection de recettes.
        Recipe problem = generateDemoData();

        // Solve the problem
        Solver<Recipe> solver = solverFactory.buildSolver();
        Recipe solution = solver.solve(problem);

        // Visualize the solution
        printPlan(solution);
    }

    public static Recipe generateDemoData() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character("0"));
        //characters.add(new Character("1"));

        List<String> recipesToFetch = new ArrayList<>();
        recipesToFetch.add(RecipeRepository.ONION_SOUP_RECIPE);

        RecipeRepository repository = new RecipeRepository();
        List<Recipe> recipes = repository.getRecipes(recipesToFetch);
        recipes.forEach(recipe -> recipe.setCharactersAndTaskAssignments(characters));

        // TODO: Retourner la liste complète une fois la classe Menu implémentée
        return recipes.get(0);
    }

    private static void printPlan(Recipe recipe) {
        LOGGER.info("");
        recipe.getTaskAssignments().sort(Comparator.comparing(ta -> ta.getTask().getFinishedOrder()));
        for (TaskAssignment taskAssignment: recipe.getTaskAssignments())
        {
            LOGGER.info("Task: " + taskAssignment.getTask().getTaskName());
            //Implémenter avec les poids des tâches
            //LOGGER.info("Task finished: " + taskAssignment.getTask().isFinished());
            LOGGER.info("Task order: " + taskAssignment.getTask().getFinishedOrder());
            //LOGGER.info("Executed by character: " + taskAssignment.getCharacter().getId());
            LOGGER.info("");
        }
    }
}
