
package edu.upf.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Optional;

/**
 * The SimplifiedTweet class is a POJO (Plain Old Java Object) that contains the
 * essential
 * information of a tweet that is used in the language filtering process.
 * It includes:
 * - tweetId: the id of the tweet ('id')
 * - text: the content of the tweet ('text')
 * - userId: the user id ('user->id')
 * - userName: the user name ('user'->'name')
 * - language: the language of a tweet ('lang')
 * - timestampMs: seconduserIds from epoch ('timestamp_ms')
 */
public class SimplifiedTweet {

  // The JsonParser is used to parse JSON strings into JsonObjects
  private static Gson parser = new Gson();

  private final long tweetId; // the id of the tweet ('id')
  private final String text; // the content of the tweet ('text')
  private final long userId; // the user id ('user->id')
  private final String userName; // the user name ('user'->'name')
  private final String language; // the language of a tweet ('lang')
  private final long timestampMs; // seconduserIds from epoch ('timestamp_ms')

  /**
   * Constructor that creates a SimplifiedTweet object
   * 
   * @param tweetId:     the id of the tweet
   * @param text:        the content of the tweet
   * @param userId:      the user id
   * @param userName:    the user name
   * @param language:    the language of the tweet
   * @param timestampMs: the timestamp in milliseconds from epoch
   */
  public SimplifiedTweet(long tweetId, String text, long userId, String userName,
      String language, long timestampMs) {
    this.tweetId = tweetId;
    this.text = text;
    this.userId = userId;
    this.userName = userName;
    this.language = language;
    this.timestampMs = timestampMs;
  }

  /**
   * Returns a {@link SimplifiedTweet} from a JSON String.
   * If parsing fails, for any reason, return an {@link Optional#empty()}
   *
   * @param jsonStr: a JSON string representing a tweet
   * @return an {@link Optional} of a {@link SimplifiedTweet}
   */
  public static Optional<SimplifiedTweet> fromJson(String jsonStr) {
    try {
      // Parse the JSON string into a JsonObject
      JsonObject json = parser.fromJson(jsonStr, JsonObject.class);
      long tweetId = json.get("id").getAsLong();
      String text = json.get("text").getAsString();
      JsonObject user = json.get("user").getAsJsonObject();
      long userId = user.get("id").getAsLong();
      String userName = user.get("name").getAsString();
      String language = json.get("lang").getAsString();
      long timestampMs = json.get("timestamp_ms").getAsLong();

      // Create a SimplifiedTweet object and return it
      return Optional.of(new SimplifiedTweet(tweetId, text, userId, userName, language, timestampMs));
    } catch (Exception e) {

      // If parsing fails, return an empty Optional
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