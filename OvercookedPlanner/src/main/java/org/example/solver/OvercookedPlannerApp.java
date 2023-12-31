package org.example.solver;

import org.example.constraints.RecipeConstraintProvider;
import org.example.domain.*;
import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.example.domain.actions.TaskOrCharacter;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OvercookedPlannerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(OvercookedPlannerApp.class);

    public static void plan() {
        SolverFactory<KitchenSchedule> solverFactory = SolverFactory.create(new SolverConfig()
                .withEnvironmentMode(EnvironmentMode.FULL_ASSERT)
                .withSolutionClass(KitchenSchedule.class)
                .withEntityClasses(Task.class, TaskOrCharacter.class)
                .withConstraintProviderClass(RecipeConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig().withSecondsSpentLimit(5L))
        );

        // Load the problem
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
        //recipesToFetch.add(RecipeRepository.BROCCOLI_SOUP_RECIPE);

        RecipeRepository repository = new RecipeRepository();
        List<Recipe> recipes = repository.getRecipes(recipesToFetch);
        List<Task> tasks = new ArrayList<>();
        recipes.forEach(recipe -> tasks.addAll(recipe.getTasks()));

        //Collections.shuffle(tasks);

        return new KitchenSchedule(characters, tasks);
    }

    private static void printPlan(KitchenSchedule solution) {
        for (Character character : solution.getCharacterList()) {
            LOGGER.info("");
            LOGGER.info(("Character " + character.getId()));
            Task task = character.getNextElement();
            while(task != null) {
                LOGGER.info("  StartTime: " + task.getStartTime());
                LOGGER.info("  " + task.getName() + (task.getOutputItem() != null ? " ✋(" + task.getOutputItem().getName() + ")" : "") + ", Duration: " + task.getDuration());
                LOGGER.info("  Position: { x = " + task.getPosition().x + ", y = " + task.getPosition().y + " }");
                LOGGER.info(" ");
                task = task.getNextElement();
            }
        }
    }
}
