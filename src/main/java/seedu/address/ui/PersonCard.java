package seedu.address.ui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.Logic;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;
    private final Logic logic;

    @FXML
    private HBox cardPane;
    @FXML
    private VBox roleContainer;
    @FXML
    private Label role;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label linkedContacts;
    @FXML
    private Label remark;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, Logic logic) {
        super(FXML);
        this.person = person;
        this.logic = logic;

        if (person instanceof Student) {
            role.setText("Student");
        } else if (person instanceof Parent) {
            role.setText("Parent");
        } else {
            role.setText("Person");
        }

        // Apply role-specific modifier to the role container so the chip takes the right color
        if (roleContainer != null) {
            roleContainer.getStyleClass().removeAll("student-role", "parent-role", "person-role");
            if (person instanceof Student) {
                roleContainer.getStyleClass().add("student-role");
            } else if (person instanceof Parent) {
                roleContainer.getStyleClass().add("parent-role");
            } else {
                roleContainer.getStyleClass().add("person-role");
            }
            roleContainer.setVisible(true);
            roleContainer.setManaged(true);
        }
        role.setVisible(true);
        role.setManaged(true);

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        // Set remark if present
        if (person.getRemark() == null || person.getRemark().remarks.isEmpty()) {
            remark.setVisible(false);
            remark.setManaged(false);
        } else {
            remark.setText("Remark: " + person.getRemark().remarks);
        }

        // show linked contacts if any exist
        String linkedText = getLinkedText(person);
        if (linkedText.isEmpty()) {
            linkedContacts.setVisible(false);
            linkedContacts.setManaged(false); // removes it from layout spacing
        } else {
            if (person instanceof Parent) {
                linkedContacts.setText("Child(ren): " + linkedText);
            } else if (person instanceof Student) {
                linkedContacts.setText("Parent(s): " + linkedText);
            }
        }

        // show tags if person is a student
        if (person instanceof Student) {
            Student student = ((Student) person);
            student.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        } else {
            tags.getChildren().clear();
            tags.setManaged(false);
            tags.setVisible(false);
        }

        // Apply background color based on type
        cardPane.getStyleClass().removeAll("student-card", "parent-card");
        if (person instanceof Student) {
            cardPane.getStyleClass().add("student-card");
        } else if (person instanceof Parent) {
            cardPane.getStyleClass().add("parent-card");
        }
    }

    /**
     * Returns a comma-separated list of linked contact names, or an empty string if none.
     */
    private String getLinkedText(Person person) {
        if (logic == null) {
            return "";
        }

        var linkedList = logic.getLinkedPersons(person);
        if (linkedList.isEmpty()) {
            return "";
        }

        String joined = linkedList.stream()
                .map(p -> p.getName().fullName)
                .limit(3)
                .collect(Collectors.joining(", "));
        return linkedList.size() > 3 ? joined + "…" : joined;
    }
}
