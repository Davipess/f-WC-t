package reviews;

import exceptions.DuplicateReviewInContestException;
import exceptions.InvalidRatingException;
import users.User;

import java.util.Iterator;

/**
 * Manager interface responsible for creating and retrieving reviews across the campus.
 * Prevents review-bombing by enforcing a single review per user per bathroom in each contest period.
 */
public interface ReviewManager {

    /**
     * Creates and registers a new review.
     *
     * @param bathroomId the ID of the bathroom being reviewed
     * @param author     the user submitting the review
     * @param rating     star score from 0 to 5
     * @param comment    text commentary
     * @return the created Review instance
     * @throws InvalidRatingException           if score is out of bounds [0..5]
     * @throws DuplicateReviewInContestException if user already reviewed this bathroom in current contest
     */
    Review createReview(String bathroomId, User author, int rating, String comment)
            throws InvalidRatingException, DuplicateReviewInContestException;

    /**
     * Retrieves an iterator over all reviews for a specific bathroom.
     *
     * @param bathroomId the ID of the bathroom
     * @return iterator of Review instances
     */
    Iterator<Review> getReviewsForBathroom(String bathroomId);

    /**
     * Retrieves the total count of reviews recorded in the system.
     *
     * @return total review count
     */
    int getTotalReviewCount();

    /**
     * Retrieves the active contest cycle / week identifier.
     *
     * @return active contest week integer
     */
    int getCurrentContestWeek();

    /**
     * Advances the contest cycle to the next week.
     */
    void advanceContestWeek();
}
