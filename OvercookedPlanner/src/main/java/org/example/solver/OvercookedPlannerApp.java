package org.example.solver;

import org.example.constraints.OvercookedConstraintProvider;
import org.example.domain.Character;
import org.example.domain.CharacterOrCharacterStep;
import org.example.domain.CharacterStep;
import org.example.domain.Grid;
import org.example.domain.OvercookedPlanner;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OvercookedPlannerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(OvercookedPlannerApp.class);

    public static void plan(Grid grid) {
        SolverFactory<OvercookedPlanner> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(OvercookedPlanner.class)
                .withEntityClasses(CharacterStep.class)
                //.withEntityClassList(List.of(CharacterStep.class, CharacterOrCharacterStep.class))
                .withConstraintProviderClass(OvercookedConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(5)));

        // Load the problem
        OvercookedPlanner problem = generateDemoData();

        // Solve the problem
        Solver<OvercookedPlanner> solver = solverFactory.buildSolver();
        OvercookedPlanner solution = solver.solve(problem);

        // Visualize the solution
        printPlan(solution);
    }

    public static OvercookedPlanner generateDemoData() {
        /*
            List<Timeslot> timeslotList = new ArrayList<>(10);
            timeslotList.add(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
            timeslotList.add(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));

            List<Room> roomList = new ArrayList<>(3);
            roomList.add(new Room("Room A"));
            roomList.add(new Room("Room B"));

            List<Lesson> lessonList = new ArrayList<>();
            long id = 0;

            lessonList.add(new Lesson(id++, "Math", "A. Turing", "9th grade"));
            lessonList.add(new Lesson(id++, "Math", "A. Turing", "10th grade"));
            ...

            return new OvercookedPlanner(...);
        */

        List<Character> characterList = new ArrayList<>();
        characterList.add(new Character("1"));
        characterList.add(new Character("2"));

        List<CharacterStep> stepList = new ArrayList<>();
        long id = 0;
        stepList.add(new CharacterStep(id++));
        stepList.add(new CharacterStep(id));

        return new OvercookedPlanner(characterList, stepList);
    }

    private static void printPlan(OvercookedPlanner plan) {
        LOGGER.info("");
        for (CharacterStep step:
             plan.getCharacterStepList()) {
            LOGGER.info("Step " + step.getId().toString());
            LOGGER.info("Executed by character " + step.getCharacter().getId());
            LOGGER.info("Next step : " + step.getNextStep());
            LOGGER.info("");
        }
    }
}
