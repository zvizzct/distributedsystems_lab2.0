package edu.upf.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.Optional;

public class ExtendedSimplifiedTweet implements Serializable {
    private final long tweetId; // the id of the tweet (’id’)
    private final String text; // the content of the tweet (’text’)
    private final long userId; // the user id (’user->id’)
    private final String userName; // the user name (’user’->’name’)
    private final long followersCount; // the number of followers (’user’->’followers_count’)
    private final String language; // the language of a tweet (’lang’)
    private final boolean isRetweeted; // is it a retweet? (the object ’retweeted_status’ exists?)
    private final Long retweetedUserId; // [if retweeted] (’retweeted_status’->’user’->’id’)
    private final Long retweetedTweetId; // [if retweeted] (’retweeted_status’->’id’)
    private final long timestampMs; // seconds from epoch (’timestamp_ms’)

    // The JsonParser is used to parse JSON strings into JsonObjects
    private static Gson parser = new Gson();

    public ExtendedSimplifiedTweet(long tweetId, String text, long userId, String userName,
            long followersCount, String language, boolean isRetweeted,
            Long retweetedUserId, Long retweetedTweetId, long timestampMs) {
        this.tweetId = tweetId;
        this.text = text;
        this.userId = userId;
        this.userName = userName;
        this.followersCount = followersCount;
        this.language = language;
        this.isRetweeted = isRetweeted;
        this.retweetedUserId = retweetedUserId;
        this.retweetedTweetId = retweetedTweetId;
        this.timestampMs = timestampMs;
    }

    /*
     * Returns a {@link ExtendedSimplifiedTweet} from a JSON String.
     * If parsing fails, for any reason, return an {@link Optional#empty()}
     *
     * @param jsonStr
     * 
     * @return an {@link Optional} of a {@link ExtendedSimplifiedTweet}
     */

    public static Optional<ExtendedSimplifiedTweet> fromJson(String jsonStr) {
        try {
            JsonObject json = parser.fromJson(jsonStr, JsonObject.class);
            long tweetId = json.get("id").getAsLong();
            String text = json.get("text").getAsString();
            JsonObject user = json.getAsJsonObject("user");
            long userId = user.get("id").getAsLong();
            String userName = user.get("name").getAsString();
            long followersCount = user.get("followers_count").getAsLong();
            String language = json.get("lang").getAsString();
            boolean isRetweeted = json.has("retweeted_status");
            Long retweetedUserId = null;
            Long retweetedTweetId = null;
            if (isRetweeted) {
                JsonObject retweetedStatus = json.getAsJsonObject("retweeted_status");
                JsonObject retweetedUser = retweetedStatus.getAsJsonObject("user");
                retweetedUserId = retweetedUser.get("id").getAsLong();
                retweetedTweetId = retweetedStatus.get("id").getAsLong();
            }
            long timestampMs = json.get("timestamp_ms").getAsLong();
            return Optional.of(new ExtendedSimplifiedTweet(tweetId, text, userId, userName,
                    followersCount, language, isRetweeted, retweetedUserId, retweetedTweetId, timestampMs));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Returns a JSON representation of the SimplifiedTweet object
     * 
     * @return a JSON representation of the SimplifiedTweet object
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    // Getters
    public long getTweetId() {
        return tweetId;
    }

    public String getText() {
        return text;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getLanguage() {
        return language;
    }

    public long getTimestampMs() {
        return timestampMs;
    }
}
