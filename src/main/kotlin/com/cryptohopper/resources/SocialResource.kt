package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * `client.social` — community / social-trading features: profiles, posts,
 * follows, messages, notifications.
 */
public class SocialResource internal constructor(
    private val transport: Transport,
) {
    /** Fetch a public profile by alias. Requires `read`. */
    public suspend fun getProfile(alias: String): JsonElement? =
        transport.request("GET", "/social/getprofile", query = mapOf("alias" to alias))

    /** Update the authenticated user's own profile. Requires `user`. */
    public suspend fun editProfile(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/editprofile", body = input)

    /** Check whether an alias (display handle) is available. */
    public suspend fun checkAlias(alias: String): JsonElement? =
        transport.request("GET", "/social/checkalias", query = mapOf("alias" to alias))

    /** The authenticated user's personalised feed. Requires `read`. */
    public suspend fun getFeed(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/social/getfeed", query = extra)

    /** Trending topics. Requires `read`. */
    public suspend fun getTrends(): JsonElement? = transport.request("GET", "/social/gettrends")

    /** Suggested profiles to follow. Requires `read`. */
    public suspend fun whoToFollow(): JsonElement? = transport.request("GET", "/social/whotofollow")

    /** Search for posts / users. Requires `read`. */
    public suspend fun search(query: String): JsonElement? =
        transport.request("GET", "/social/search", query = mapOf("query" to query))

    /** Notifications for the authenticated user. Requires `notifications`. */
    public suspend fun getNotifications(): JsonElement? = transport.request("GET", "/social/getnotifications")

    /** List the user's DM conversations. Requires `read`. */
    public suspend fun getConversationList(): JsonElement? = transport.request("GET", "/social/getconversationlist")

    /** Load messages for a single conversation. Requires `read`. */
    public suspend fun getConversation(conversationId: Any): JsonElement? = transport.request(
        "GET", "/social/loadconversation",
        query = mapOf("conversation_id" to conversationId.toString()),
    )

    /** Send a DM. Requires `user`. */
    public suspend fun sendMessage(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/sendmessage", body = input)

    /** Delete a DM. Requires `user`. */
    public suspend fun deleteMessage(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/deletemessage", body = input)

    /** Create a new post. Requires `user`. */
    public suspend fun createPost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/post", body = input)

    /** Fetch a single post. Requires `read`. */
    public suspend fun getPost(postId: Any): JsonElement? =
        transport.request("GET", "/social/getpost", query = mapOf("post_id" to postId.toString()))

    /** Delete a post. Requires `user`. */
    public suspend fun deletePost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/deletepost", body = input)

    /** Pin/unpin a post on the user's profile. Requires `user`. */
    public suspend fun pinPost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/pinpost", body = input)

    /** Fetch a single comment. Requires `read`. */
    public suspend fun getComment(commentId: Any): JsonElement? =
        transport.request("GET", "/social/getcomment", query = mapOf("comment_id" to commentId.toString()))

    /** List comments on a post. Requires `read`. */
    public suspend fun getComments(postId: Any): JsonElement? = transport.request(
        "GET", "/social/getcomments",
        query = mapOf("post_id" to postId.toString()),
    )

    /** Delete a comment. Requires `user`. */
    public suspend fun deleteComment(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/deletecomment", body = input)

    /** Fetch a media attachment by id. Requires `read`. */
    public suspend fun getMedia(mediaId: Any): JsonElement? = transport.request(
        "GET", "/social/getmedia",
        query = mapOf("media_id" to mediaId.toString()),
    )

    /** Follow or unfollow an alias. Requires `user`. */
    public suspend fun follow(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/follow", body = input)

    /** List followers of a profile. Requires `read`. */
    public suspend fun getFollowers(aliasOrId: Any): JsonElement? = transport.request(
        "GET", "/social/followers",
        query = mapOf("alias" to aliasOrId.toString()),
    )

    /** Check whether the authenticated user follows the given profile. Requires `read`. */
    public suspend fun getFollowing(aliasOrId: Any): JsonElement? = transport.request(
        "GET", "/social/following",
        query = mapOf("alias" to aliasOrId.toString()),
    )

    /** List profiles the given user follows. Requires `read`. */
    public suspend fun getFollowingProfiles(aliasOrId: Any): JsonElement? = transport.request(
        "GET", "/social/followingprofiles",
        query = mapOf("alias" to aliasOrId.toString()),
    )

    /** Like/unlike a post. Requires `user`. */
    public suspend fun like(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/like", body = input)

    /** Repost a post. Requires `user`. */
    public suspend fun repost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/repost", body = input)

    /** Block a user. Requires `user`. */
    public suspend fun blockUser(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/blockuser", body = input)
}
