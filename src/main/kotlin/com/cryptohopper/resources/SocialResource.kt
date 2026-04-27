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
    public suspend fun getProfile(alias: String): JsonElement? =
        transport.request("GET", "/social/getprofile", query = mapOf("alias" to alias))
    public suspend fun editProfile(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/editprofile", body = input)
    public suspend fun checkAlias(alias: String): JsonElement? =
        transport.request("GET", "/social/checkalias", query = mapOf("alias" to alias))
    public suspend fun getFeed(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/social/getfeed", query = extra)
    public suspend fun getTrends(): JsonElement? = transport.request("GET", "/social/gettrends")
    public suspend fun whoToFollow(): JsonElement? = transport.request("GET", "/social/whotofollow")
    public suspend fun search(query: String): JsonElement? =
        transport.request("GET", "/social/search", query = mapOf("query" to query))
    public suspend fun getNotifications(): JsonElement? = transport.request("GET", "/social/getnotifications")
    public suspend fun getConversationList(): JsonElement? = transport.request("GET", "/social/getconversationlist")
    public suspend fun getConversation(conversationId: Any): JsonElement? = transport.request(
        "GET", "/social/loadconversation",
        query = mapOf("conversation_id" to conversationId.toString()),
    )
    public suspend fun sendMessage(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/sendmessage", body = input)
    public suspend fun deleteMessage(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/deletemessage", body = input)
    public suspend fun createPost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/post", body = input)
    public suspend fun getPost(postId: Any): JsonElement? =
        transport.request("GET", "/social/getpost", query = mapOf("post_id" to postId.toString()))
    public suspend fun deletePost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/deletepost", body = input)
    public suspend fun pinPost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/pinpost", body = input)
    public suspend fun getComment(commentId: Any): JsonElement? =
        transport.request("GET", "/social/getcomment", query = mapOf("comment_id" to commentId.toString()))
    public suspend fun getComments(postId: Any): JsonElement? = transport.request(
        "GET", "/social/getcomments",
        query = mapOf("post_id" to postId.toString()),
    )
    public suspend fun deleteComment(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/deletecomment", body = input)
    public suspend fun getMedia(mediaId: Any): JsonElement? = transport.request(
        "GET", "/social/getmedia",
        query = mapOf("media_id" to mediaId.toString()),
    )
    public suspend fun follow(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/follow", body = input)
    public suspend fun getFollowers(aliasOrId: Any): JsonElement? = transport.request(
        "GET", "/social/followers",
        query = mapOf("alias" to aliasOrId.toString()),
    )
    public suspend fun getFollowing(aliasOrId: Any): JsonElement? = transport.request(
        "GET", "/social/following",
        query = mapOf("alias" to aliasOrId.toString()),
    )
    public suspend fun getFollowingProfiles(aliasOrId: Any): JsonElement? = transport.request(
        "GET", "/social/followingprofiles",
        query = mapOf("alias" to aliasOrId.toString()),
    )
    public suspend fun like(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/like", body = input)
    public suspend fun repost(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/repost", body = input)
    public suspend fun blockUser(input: JsonObject): JsonElement? =
        transport.request("POST", "/social/blockuser", body = input)
}
