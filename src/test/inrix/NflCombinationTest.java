package inrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Tests around {@link inrix.NflCombination}
 */
public class NflCombinationTest
{
    @Test
    public void testGenerate ()
    {

        List<Integer> possibleScores = new ArrayList<Integer>();
        possibleScores.add(2);
        possibleScores.add(3);
        possibleScores.add(6);
        possibleScores.add(7);
        possibleScores.add(8);
        Set<List<Integer>> output = NflCombination.findCombinations(7, possibleScores,
                possibleScores.size());

        System.out.println(output);

    }

    @Test
    public void testOneScore ()
    {
        System.out.println(NflCombination.generatePossibleScoringCombinationsForEachTeam("7-3"));
        System.out.println(NflCombination.generatePossibleScoringCombinationsForEachTeam("21-3"));

    }
}
