package reviews;

import java.time.LocalDateTime;

/**
 * Interface representing a user review and rating for a campus bathroom.
 */
public interface Review {

    /**
     * Retrieves the unique identifier of the review.
     *
     * @return review ID
     */
    String getId();

    /**
     * Retrieves the ID of the bathroom evaluated in this review.
     *
     * @return bathroom ID
     */
    String getBathroomId();

    /**
     * Retrieves the ID of the user who authored the review.
     *
     * @return author ID
     */
    String getAuthorId();

    /**
     * Retrieves the name of the user who authored the review.
     *
     * @return author name
     */
    String getAuthorName();

    /**
     * Retrieves the general rating score (from 0 to 5).
     *
     * @return rating score
     */
    int getRating();

    /**
     * Retrieves the text comment submitted with the review.
     *
     * @return review comment
     */
    String getComment();

    /**
     * Retrieves the timestamp when this review was recorded.
     *
     * @return creation date and time
     */
    LocalDateTime getTimestamp();

    /**
     * Retrieves the contest cycle / week number when this review was posted.
     *
     * @return contest week integer
     */
    int getContestWeek();
}
