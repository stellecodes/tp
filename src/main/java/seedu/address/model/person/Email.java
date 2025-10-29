package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    private static final String SPECIAL_CHARACTERS = "+_.-";
    public static final String MESSAGE_CONSTRAINTS =
            """
            Emails should be of the format "local-part@domain-part" where:
            1. The local-part:
                - Should only contain alphanumeric characters and special characters '+', '_', '.', '-'
                - Must not start or end with any special characters
                - Should not exceed 64 characters
            2. The domain-part:
                - Should only contain alphanumeric labels connected by hyphens '-', if any
                - Must end with a domain label that is at least 2 characters long
                - Should not exceed 255 characters""";

    // alphanumeric and special characters
    private static final String ALPHANUMERIC = "[^\\W]+"; // alphanumeric characters

    // Max 64 characters in local part
    private static final String LOCAL_PART_REGEX = "^(?=.{1,64}$)" + ALPHANUMERIC + "([" + SPECIAL_CHARACTERS + "]"
            + ALPHANUMERIC + ")*";

    // Max 255 characters in domain part
    private static final String DOMAIN_PART_REGEX = "(?=.{1,253}$)" + ALPHANUMERIC+ "(-" + ALPHANUMERIC + ")*";
    private static final String DOMAIN_LAST_PART_REGEX = "(" + DOMAIN_PART_REGEX + "){2,}$"; // At least two chars
    private static final String DOMAIN_REGEX = "(?=.{1,255}$)(" + DOMAIN_PART_REGEX + "\\.)*" + DOMAIN_LAST_PART_REGEX;

    // Max 320 characters in total
    public static final String VALIDATION_REGEX = "(?=.{1,320}$)" + LOCAL_PART_REGEX + "@" + DOMAIN_REGEX;

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        value = email;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equalsIgnoreCase(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
