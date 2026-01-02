import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;

/**
 * Controller for the Electorial College application
 * <p>
 *     A controller that interacts with a FXML file to control certain areas
 *     and how they interact with one another.  Contains several like updating
 *     fields and selection buttons/bars to simulate an electorial college vote.
 * </p>
 *
 * @author augward
 */
public class ElectoralCollegeController {
    // Center Buttons that show live percents, records, and selections
    @FXML
    private TextArea pollsField;

    @FXML
    private ListView<String> stateListView;

    @FXML
    private TextArea recentResultsField;

    // The three buttons to choose from for the vote
    @FXML
    private RadioButton radioDem;

    @FXML
    private RadioButton radioUnd;

    @FXML
    private RadioButton radioRep;

    @FXML
    private ToggleGroup Choice;

    // Winner representation fields, to track progress
    @FXML
    private TextField demVotesField;

    @FXML
    private TextField winnerField;

    @FXML
    private TextField repVotesField;

    // Maps and Lists to hold values of States and choices
    private final List<String> statesList = new ArrayList<>();
    private final Map<String, String> choiceMap = new HashMap<>();
    private final Map<String, Integer> voteWeightMap = new LinkedHashMap<>();
    private final List<String> choiceRecord = new ArrayList<>();

    private int democraticVotes = 0;
    private int republicanVotes = 0;

    /**
     * Initializer of the controller class.  Sets up all the states and their weights
     * Then breaks them apart and builds lists, records, and choices maps.
     */
    @FXML
    public void initialize() {
        voteWeightMap.put("Alabama", 9);
        voteWeightMap.put("Alaska", 3);
        voteWeightMap.put("Arizona", 11);
        voteWeightMap.put("Arkansas", 6);
        voteWeightMap.put("California", 54);
        voteWeightMap.put("Colorado", 10);
        voteWeightMap.put("Connecticut", 7);
        voteWeightMap.put("Delaware", 3);
        voteWeightMap.put("District of Columbia", 3);
        voteWeightMap.put("Florida", 30);
        voteWeightMap.put("Georgia", 16);
        voteWeightMap.put("Hawaii", 4);
        voteWeightMap.put("Idaho", 4);
        voteWeightMap.put("Illinois", 19);
        voteWeightMap.put("Indiana", 11);
        voteWeightMap.put("Iowa", 6);
        voteWeightMap.put("Kansas", 6);

        voteWeightMap.put("Kentucky", 8);
        voteWeightMap.put("Louisiana", 8);

        voteWeightMap.put("Maine (State Winner)", 2);
        voteWeightMap.put("Maine 1", 1);
        voteWeightMap.put("Maine 2", 1);

        voteWeightMap.put("Maryland", 10);
        voteWeightMap.put("Massachusetts", 11);
        voteWeightMap.put("Michigan", 15);
        voteWeightMap.put("Minnesota", 10);
        voteWeightMap.put("Mississippi", 6);
        voteWeightMap.put("Missouri", 10);
        voteWeightMap.put("Montana", 4);

        voteWeightMap.put("Nebraska (State Winner)", 2);
        voteWeightMap.put("Nebraska 1", 1);
        voteWeightMap.put("Nebraska 2", 1);
        voteWeightMap.put("Nebraska 3", 1);

        voteWeightMap.put("Nevada", 6);
        voteWeightMap.put("New Hampshire", 4);
        voteWeightMap.put("New Jersey", 14);
        voteWeightMap.put("New Mexico", 5);
        voteWeightMap.put("New York", 28);
        voteWeightMap.put("North Carolina", 16);

        voteWeightMap.put("North Dakota", 3);
        voteWeightMap.put("Ohio", 17);
        voteWeightMap.put("Oklahoma", 7);
        voteWeightMap.put("Oregon", 8);
        voteWeightMap.put("Pennsylvania", 19);
        voteWeightMap.put("Rhode Island", 4);
        voteWeightMap.put("South Carolina", 9);
        voteWeightMap.put("South Dakota", 3);
        voteWeightMap.put("Tennessee", 11);
        voteWeightMap.put("Texas", 40);
        voteWeightMap.put("Utah", 6);
        voteWeightMap.put("Vermont", 3);
        voteWeightMap.put("Virginia", 13);
        voteWeightMap.put("Washington", 12);
        voteWeightMap.put("West Virginia", 4);
        voteWeightMap.put("Wisconsin", 10);
        voteWeightMap.put("Wyoming", 3);


        statesList.addAll(voteWeightMap.keySet());

        for (String state : statesList) {
            choiceMap.put(state, "Undecided");
            stateListView.getItems().add(state);
        }
    }


    /**
     * Toggles the visible radio button based off the selected state.
     * Keeps the memory of the choice visible instead of just cataloged.
     */
    @FXML
    private void updateListChoice() {
        String stateChoice = choiceMap.get(stateListView.getSelectionModel().getSelectedItem());

        switch (stateChoice) {
            case "Democrat" -> Choice.selectToggle(radioDem);
            case "Republican" -> Choice.selectToggle(radioRep);
            default -> Choice.selectToggle(radioUnd);
        }
    }

    /**
     * Happens when a button is selected for the state's choice.
     * Inserts it into the map and runs checks to update fields like winner
     */
    @FXML
    private void updateRadio() {
        String selected = stateListView.getSelectionModel().getSelectedItem();

        String newValue = (String) Choice.getSelectedToggle().getUserData();

        choiceMap.put(selected, newValue);

        updateRecord(selected, newValue);

        updateVotes();
        checkWinner();
    }

    /**
     * Updates the votes and their following fields.  Keeping a live tracker
     * and provide useful metrics for users.
     */
    private void updateVotes() {
        democraticVotes = 0;
        republicanVotes = 0;

        // Finds the current decided states and pulls their weights
        for (String state : statesList) {
            int weight = voteWeightMap.get(state);
            String choice = choiceMap.get(state);

            switch (choice) {
                case "Democrat" -> democraticVotes += weight;
                case "Republican" -> republicanVotes += weight;
            }
        }

        // Prints off stats to 3 live fields, voter numbers and percents
        demVotesField.setText("Democrats: " + democraticVotes + " votes");
        repVotesField.setText("Republicans: " + republicanVotes + " votes");
        int totalWeight = democraticVotes + republicanVotes;
        if (totalWeight != 0) {
            pollsField.setText("Current Polls:\n" + "D " + (democraticVotes * 100) / totalWeight +
                    "% / R " + (republicanVotes * 100) / totalWeight + "%");
        }
    }

    /**
     * Updates the record keeper in the right by appending the most recent
     * choice to the top of the list, shows progress
     *
     * @param state the state that got changed
     * @param choice the updated choice of the state
     */
    private void updateRecord(String state,  String choice) {
        choiceRecord.add(0, state + " : " + choice + " → " + voteWeightMap.get(state));
        recentResultsField.setText(String.join("\n", choiceRecord));
    }

    /**
     * Checks if either group has reach 270 votes.
     * Should only be one winner, but logic protects in case not
     */
    private void checkWinner() {
        if (democraticVotes >= 270 && republicanVotes >= 270) {
            winnerField.setText("Winner: Both?");
        } else if (democraticVotes >= 270) {
            winnerField.setText("Winner: Democrat");
        } else if (republicanVotes >= 270) {
            winnerField.setText("Winner: Republicans");
        } else {
            winnerField.clear();
        }
    }
}
