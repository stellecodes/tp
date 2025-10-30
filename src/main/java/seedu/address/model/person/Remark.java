package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents remarks associated with a Person in the address book.
 * Guarantees: mutable; is valid as declared in {@link #isValidRemark(String)}
 */
public class Remark {

    public static final String MESSAGE_CONSTRAINTS =
            """
            Remark is limited to 200 characters.
            It should not contain special characters '/' or '\\n'.""";

    /*
     * The remark cannot contain special character '/' or newline.
     */
    public static final String VALIDATION_REGEX = "[^\\n/]{0,200}";

    public final String remarks;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        if (remark == null) {
            this.remarks = "";
        } else {
            checkArgument(isValidRemark(remark), MESSAGE_CONSTRAINTS);
            this.remarks = remark;
        }
    }

    /**
     * Returns true if a given string is a valid remark.
     */
    public static boolean isValidRemark(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return remarks;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof Remark)) {
            return false;
        }
        Remark otherRemark = (Remark) other;
        return remarks.equals(otherRemark.remarks);
    }

    @Override
    public int hashCode() {
        return remarks.hashCode();
    }
}
