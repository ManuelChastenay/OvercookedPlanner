package org.example.solver;

import org.example.constraints.RecipeConstraintProvider;
import org.example.domain.*;
import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.example.domain.grid.Grid;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OvercookedPlannerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(OvercookedPlannerApp.class);

    public static void plan(Grid grid) {
        SolverFactory<KitchenSchedule> solverFactory = SolverFactory.create(new SolverConfig()
                //.withSolutionClass(Recipe.class)
                .withSolutionClass(KitchenSchedule.class)
                .withEntityClasses(CharacterSchedule.class)
                //.withEntityClasses(Task.class, TaskAssignment.class/*, CharacterOrTaskAssignment.class*/)
                .withConstraintProviderClass(RecipeConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(2))
                //.withTerminationConfig(new TerminationConfig().withBestScoreLimit("-5hard/0soft"))
        );

        // Load the problem
        // TODO: Ajouter une classe comme Menu pour regrouper un collection de recettes.
        //Recipe problem = generateDemoData();
        KitchenSchedule problem = generateDemoData();

        // Solve the problem
        //Solver<Recipe> solver = solverFactory.buildSolver();
        Solver<KitchenSchedule> solver = solverFactory.buildSolver();
        //Recipe solution = solver.solve(problem);
        KitchenSchedule solution = solver.solve(problem);

        // Visualize the solution
        //printPlan(solution);
        for (CharacterSchedule schedule : solution.getCharacterScheduleList()) {
            LOGGER.info(schedule.toString());
        }
    }

    public static KitchenSchedule/*Recipe*/ generateDemoData() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character("0"));
        characters.add(new Character("1"));

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("Prendre un onion"));
        steps.add(new Step("Couper un onion"));
        steps.add(new Step("Prendre une tomate"));
        //steps.add(new Step("Couper une tomate"));
        //steps.add(new Step("Prendre une assiette"));
        //steps.add(new Step("Mettre un onion coupé dans une assiette"));
        //steps.add(new Step("Mettre une tomate coupée dans une assiette"));
        //steps.add(new Step("Servir une assiette d'onion coupé"));
        //steps.add(new Step("Servir une assiete de tomate coupée"));

        //List<String> recipesToFetch = new ArrayList<>();
        //recipesToFetch.add(RecipeRepository.ONION_SOUP_RECIPE);

        //RecipeRepository repository = new RecipeRepository();
        //List<Recipe> recipes = repository.getRecipes(recipesToFetch);
        //recipes.forEach(recipe -> recipe.setCharactersAndTaskAssignments(characters));

        // TODO: Retourner la liste complète une fois la classe Menu implémentée
        return new KitchenSchedule(characters, steps);
    }

    private static void printPlan(Recipe recipe) {
        LOGGER.info("");
        for (TaskAssignment taskAssignment:
                recipe.getTaskAssignments()) {
            /*if(taskAssignment.getPreviousTask() instanceof TaskAssignment){
                LOGGER.info("Previous Task: " + ((TaskAssignment) taskAssignment.getPreviousTask()).getTask().getTaskName());
            }*/
            LOGGER.info("Task: " + taskAssignment.getTask().getTaskName());
            LOGGER.info("Task finished: " + taskAssignment.getTask().isFinished());
            LOGGER.info("Executed by character: " + taskAssignment.getCharacter().getId());
            LOGGER.info("");
        }
    }
}
