package reviews;

import exceptions.DuplicateReviewInContestException;
import exceptions.InvalidRatingException;
import users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In-memory manager for reviews and weekly contests.
 */
public class ReviewManagerClass implements ReviewManager {

    private final Map<String, List<Review>> reviewsByBathroom;
    private final Set<String> userBathroomContestSubmissions;
    private int currentContestWeek;
    private int sequence;

    public ReviewManagerClass() {
        this.reviewsByBathroom = new LinkedHashMap<>();
        this.userBathroomContestSubmissions = new HashSet<>();
        this.currentContestWeek = 1;
        this.sequence = 0;
    }

    @Override
    public Review createReview(String bathroomId, User author, int rating, String comment)
            throws InvalidRatingException, DuplicateReviewInContestException {
        if (author == null) {
            throw new IllegalArgumentException();
        }
        if (bathroomId == null || bathroomId.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (rating < 0 || rating > 5) {
            throw new InvalidRatingException();
        }

        String cleanBathroomId = bathroomId.trim().toUpperCase();
        String submissionKey = author.getId().toLowerCase() + ":" + cleanBathroomId + ":" + currentContestWeek;

        // Only 1 review per user per bathroom each week
        if (userBathroomContestSubmissions.contains(submissionKey)) {
            throw new DuplicateReviewInContestException();
        }

        String cleanComment = "";
        if (comment != null) {
            cleanComment = comment.trim();
        }

        String revId = "REV-" + (++sequence);
        Review review = new ReviewClass(
                revId,
                cleanBathroomId,
                author.getId(),
                author.getName(),
                rating,
                cleanComment,
                LocalDateTime.now(),
                currentContestWeek
        );

        userBathroomContestSubmissions.add(submissionKey);

        List<Review> list = reviewsByBathroom.get(cleanBathroomId);
        if (list == null) {
            list = new ArrayList<>();
            reviewsByBathroom.put(cleanBathroomId, list);
        }
        list.add(review);

        author.incrementReviewCount();
        return review;
    }

    @Override
    public Iterator<Review> getReviewsForBathroom(String bathroomId) {
        if (bathroomId == null) {
            return Collections.emptyIterator();
        }
        List<Review> list = reviewsByBathroom.get(bathroomId.trim().toUpperCase());
        if (list == null) {
            return Collections.emptyIterator();
        }
        return list.iterator();
    }

    @Override
    public int getTotalReviewCount() {
        return sequence;
    }

    @Override
    public int getCurrentContestWeek() {
        return currentContestWeek;
    }

    @Override
    public void advanceContestWeek() {
        this.currentContestWeek++;
    }
}
