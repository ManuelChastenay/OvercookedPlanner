package org.example.constraints;

import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.example.utils.Pathfinding;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.*;

import java.util.Objects;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints
                penalizeUnfinishedTaskRequirements(constraintFactory),

                //Manu: Vous pouvez commenter cette contrainte si vous ne travaillez pas sur le multijouer
                //Adam, tu peux partir de ça pour les poids plus tard quand tu vas être rendu là (Sauvegarde des paths dans les characters)
                //Avoir un eventlistener qui gère les valeurs de startTime serait mieux qu'avoir le solveur qui s'en occuppe.
                penalizeDependenciesOrderTaskDependency(constraintFactory),

                //cantHoldMoreThanOneItem(constraintFactory),

                //Soft constraints
                penalizeChracterDoingNothing(constraintFactory),
                minimizeDistanceFromTaskToNext(constraintFactory),

        };
    }

    //TODO Le multijoueur va necessiter l'ordonnacement (Task::startTime)
    private Constraint penalizeUnfinishedTaskRequirements(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                //Vérifier si le bris de symétrie est plus efficace sous forme de joiner ou de filter
                .join(Task.class,
                        Joiners.lessThan(Task::getId))
                .filter(
                        (t1, t2) -> (t2.getDependencies().contains(t1) && !t2.getPreviousTasks().contains(t1)) ||
                                (t1.getDependencies().contains(t2) && !t1.getPreviousTasks().contains(t2))
                )
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        (t1, t2) -> 1L).asConstraint("Task Dependencies Requires");
        //TODO Meilleure gestion des poids
        //(t1, t2) -> (long) t1.getUnfinishedDependencies().size()).asConstraint("Task Dependencies Requires");
    }

    //TODO: Accélérer en joinant seulement les tâches à leurs dépendences
    private Constraint penalizeDependenciesOrderTaskDependency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(Task::getCharacter))
                .filter((t1, t2) -> t1.getPreviousTasks().contains(t2) && t1.getStartTime() <= t2.getStartTime())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        //Manu: Je sais pas pourquoi, mais un gros poids accélère vraiment la convergence ici
                        (t1, t2) -> 100L).asConstraint("Dependencies Have Lower Task Order");
    }

    //TODO: Minimiser le temps total (Non fonctionnel)
    private Constraint minimizeTotalAmountOfTask(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Character.class)
                //.forEach(CharacterSchedule.class)
                //.filter(s -> s.getTaskAmount() == 0)
                .penalize(HardSoftScore.ONE_SOFT/*, (schedule1, schedule2) -> Math.abs(schedule1.getTaskAmount() - schedule2.getTaskAmount())*/).asConstraint("Minimize Step amount");
    }

    private Constraint penalizeChracterDoingNothing(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Character.class)
                .filter(character -> character.getNextElement() == null)
                .penalize(HardSoftLongScore.ONE_SOFT).asConstraint("All characters must work");
    }

    private Constraint minimizeDistanceFromTaskToNext(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class, Joiners.equal(Task::getPreviousTaskId, Task::getId))
                .penalizeLong(
                        HardSoftLongScore.ONE_SOFT, // Le poids de la pénalité.
                        (currentTask, previousTask) -> Pathfinding.calculateDistance(currentTask,previousTask ) // La fonction de pénalité.
                ).asConstraint("motion penalty");
    }
}
