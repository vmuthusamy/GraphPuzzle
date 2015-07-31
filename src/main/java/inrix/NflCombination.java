package inrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Defined are the following American football scoring rules:
//
//   Safety                 : 2 points
//   Field Goal             : 3 points
//   Touchdown              : 6 points
//
//   After a touchdown, the scoring team attempts an Extra Point or a Two-Point Conversion
//     Extra Point          : 1 point
//     Two-Point Conversion : 2 points
//
// Write a program where
// Given a command-line input in the form A-B where A is the total score for Team A and B is the total score for Team B
// Return the possible combination of scores for each team
//
// For example, given 7-3, the possible combinations are:
//   Team A:
//     1 Touchdown, 1 Extra-Point
//     1 Field Goal, 2 Safety
//   Team B:
//     1 Field Goal
public class NflCombination
{

    private static Map<Integer, String> scoreExplanation = new HashMap<Integer, String>();
    private static final List<Integer> possibleScores = new ArrayList<Integer>();

    /**
     * The API provides the possible scoring plays for the scorecard provided. The scorecard should
     * be of the form <code>7-3,3-15</code>
     *
     * @param score
     *         the input score of two teams delimited by '-'
     *
     * @return
     */
    public static String generatePossibleScoringCombinationsForEachTeam (final String score)
    {
        preload();
        assert score != null : "score:null";

        final String[] parts = score.split("-");

        assert parts.length == 2 : "there should be 2 scores";

        final int teamAScore = Integer.parseInt(parts[0]);
        final int teamBScore = Integer.parseInt(parts[1]);

        final Set<List<Integer>> teamACombinations = findCombinations(teamAScore, possibleScores,
                possibleScores.size());
        final Set<List<Integer>> teamBCombinations = findCombinations(teamBScore, possibleScores,
                possibleScores.size());

        return combineCombinations(teamACombinations, teamBCombinations);
    }

    public static Set<List<Integer>> findCombinations (int matchScore, List<Integer> possibleScores,
            int numberOfScores)
    {
        Set<List<Integer>> results = new HashSet<List<Integer>>();

        for (Integer score : possibleScores)
        {
            List<Integer> result = new ArrayList<Integer>();
            result.add(score);
            int currentAmount = matchScore - score;
            if (currentAmount >= 0)
            {
                if (currentAmount == 0)
                {
                    results.add(result);
                } else
                {
                    if ((numberOfScores - 1) > 0)
                    {
                        Set<List<Integer>> recurseResults = findCombinations(currentAmount,
                                possibleScores, (numberOfScores - 1));
                        for (List<Integer> recurseResult : recurseResults)
                        {
                            recurseResult.add(score);
                            Collections.sort(recurseResult);
                        }
                        results.addAll(recurseResults);
                    }
                }
            }
        }

        return results;
    }

    private static String combineCombinations (final Set<List<Integer>> teamACombinations,
            final Set<List<Integer>> teamBCombinations)
    {

        final StringBuilder outputBuilder = new StringBuilder();

        final Set<Map<Integer, Integer>> teamAFrequencies = generateScoreFrequencies(
                teamACombinations);
        final Set<Map<Integer, Integer>> teamBFrequencies = generateScoreFrequencies(
                teamBCombinations);

        outputBuilder.append("Team A:");
        outputBuilder.append("\n");

        for (Map<Integer, Integer> map : teamAFrequencies)
        {
            int count = 0;
            for (Map.Entry<Integer, Integer> entry : map.entrySet())
            {
                count++;
                if (entry.getValue() > 0)
                {
                    outputBuilder.append(entry.getValue());
                    outputBuilder.append(" ");
                    outputBuilder.append(scoreExplanation.get(entry.getKey()));
                    if (count < map.size())
                    {
                        outputBuilder.append(",");
                    }
                }
            }
            outputBuilder.append("\n");
        }

        outputBuilder.append("Team B:");
        outputBuilder.append("\n");

        for (Map<Integer, Integer> map : teamBFrequencies)
        {
            int count = 0;
            for (Map.Entry<Integer, Integer> entry : map.entrySet())
            {
                count++;
                if (entry.getValue() > 0)
                {
                    outputBuilder.append(entry.getValue());
                    outputBuilder.append(" ");
                    outputBuilder.append(scoreExplanation.get(entry.getKey()));
                    if (count < map.size())
                    {
                        outputBuilder.append(",");
                    }
                }
            }
            outputBuilder.append("\n");
        }

        return outputBuilder.toString();
    }

    private static Set<Map<Integer, Integer>> generateScoreFrequencies (
            final Set<List<Integer>> teamCombinations)
    {

        final Set<Map<Integer, Integer>> collectionOfScoreFrequencies = new HashSet<Map<Integer, Integer>>();

        for (List<Integer> list : teamCombinations)
        {
            collectionOfScoreFrequencies.add(generateScoreFrequenciesForSingleList(list));
        }

        return collectionOfScoreFrequencies;
    }

    private static Map<Integer, Integer> generateScoreFrequenciesForSingleList (
            final List<Integer> oneScoringCombination)
    {
        final Map<Integer, Integer> scoreFrequencies = new HashMap<Integer, Integer>();

        for (final Integer key : oneScoringCombination)
        {
            if (!scoreFrequencies.containsKey(key))
            {
                scoreFrequencies.put(key, 1);
            } else
            {
                int value = scoreFrequencies.get(key);
                scoreFrequencies.put(key, ++value);
            }
        }

        return scoreFrequencies;
    }

    private static void preload ()
    {
        possibleScores.add(2);
        possibleScores.add(3);
        possibleScores.add(6);
        possibleScores.add(7);
        possibleScores.add(8);
        scoreExplanation.put(2, "Safety");
        scoreExplanation.put(3, "Field Goal");
        scoreExplanation.put(6, "Touchdown");
        scoreExplanation.put(7, "Touchdown and Extra Point Conversion");
        scoreExplanation.put(8, "Touchdown and Two Point Conversion");

    }
}
