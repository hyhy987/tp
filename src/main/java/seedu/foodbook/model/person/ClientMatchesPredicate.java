package seedu.foodbook.model.person;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.foodbook.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name}, {@code Phone}, or {@code Email} matches the search criteria.
 * At least one of name, phone, or email search query must be present.
 */
public class ClientMatchesPredicate implements Predicate<Person> {
    private final Optional<String> nameQuery;
    private final Optional<String> phoneQuery;
    private final Optional<String> emailQuery;

    /**
     * Creates a ClientMatchesPredicate with the given search queries.
     * At least one query must be present (non-empty Optional).
     *
     * @param nameQuery Optional name search query (case-insensitive substring match)
     * @param phoneQuery Optional phone search query (substring match)
     * @param emailQuery Optional email search query (case-insensitive substring match)
     */
    public ClientMatchesPredicate(Optional<String> nameQuery, Optional<String> phoneQuery,
                                  Optional<String> emailQuery) {
        this.nameQuery = nameQuery;
        this.phoneQuery = phoneQuery;
        this.emailQuery = emailQuery;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesName = nameQuery.map(query ->
                person.getName().fullName.toLowerCase().contains(query.toLowerCase())
        ).orElse(true);

        boolean matchesPhone = phoneQuery.map(query ->
                person.getPhone().value.contains(query)
        ).orElse(true);

        boolean matchesEmail = emailQuery.map(query ->
                person.getEmail().value.toLowerCase().contains(query.toLowerCase())
        ).orElse(true);

        // Return true if ALL provided criteria match (AND logic)
        return matchesName && matchesPhone && matchesEmail;
    }

    /**
     * Returns true if at least one search criterion was provided.
     */
    public boolean hasSearchCriteria() {
        return nameQuery.isPresent() || phoneQuery.isPresent() || emailQuery.isPresent();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClientMatchesPredicate)) {
            return false;
        }

        ClientMatchesPredicate otherPredicate = (ClientMatchesPredicate) other;
        return nameQuery.equals(otherPredicate.nameQuery)
                && phoneQuery.equals(otherPredicate.phoneQuery)
                && emailQuery.equals(otherPredicate.emailQuery);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameQuery", nameQuery.orElse(""))
                .add("phoneQuery", phoneQuery.orElse(""))
                .add("emailQuery", emailQuery.orElse(""))
                .toString();
    }
}
