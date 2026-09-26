package reviews;

import java.time.LocalDateTime;

/**
 * Immutable review record storing author, bathroom ID, rating, and feedback comment.
 */
public record ReviewClass(
        String id,
        String bathroomId,
        String authorId,
        String authorName,
        int rating,
        String comment,
        LocalDateTime timestamp,
        int contestWeek
) implements Review {

    @Override
    public String getId() {
        return id();
    }

    @Override
    public String getBathroomId() {
        return bathroomId();
    }

    @Override
    public String getAuthorId() {
        return authorId();
    }

    @Override
    public String getAuthorName() {
        return authorName();
    }

    @Override
    public int getRating() {
        return rating();
    }

    @Override
    public String getComment() {
        return comment();
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp();
    }

    @Override
    public int getContestWeek() {
        return contestWeek();
    }
}
