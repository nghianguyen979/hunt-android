package com.ctech.eaty.network

import com.ctech.eaty.entity.AccessToken
import com.ctech.eaty.request.*
import com.ctech.eaty.response.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*


interface ProductHuntApi {
    @POST("/v1/oauth/token")
    fun getAccessToken(@Body auth: OAuthClientRequest): Observable<AccessToken>

    @PUT("/v1/sessions")
    fun getAccessToken(@Body auth: LoginRequest): Observable<LoginResponse>

    @GET("/v1/posts")
    fun getPosts(@Query("day") date: String): Single<ProductResponse>

    @GET("/v1/posts/{id}")
    fun getProductDetail(@Path("id") id: Int): Single<ProductDetailResponse>

    @GET("/v1/posts/all")
    fun getProductsByTopic(@Query("search[topic]") id: Int, @Query("per_page") limit: Int, @Query("page") page: Int): Single<ResponseBody>

    @GET("/v1/posts/{id}/comments")
    fun getComments(@Path("id") id: Int, @Query("per_page") limit: Int, @Query("page") page: Int): Single<ResponseBody>

    @GET("/v1/posts/{id}/votes")
    fun getVotes(@Path("id") id: Int, @Query("per_page") limit: Int, @Query("page") page: Int): Single<ResponseBody>

    @GET("/v1/collections?search[featured]=true")
    fun getCollections(@Query("per_page") limit: Int, @Query("page") page: Int): Single<ResponseBody>

    @GET("/v1/collections/{id}")
    fun getCollectionDetail(@Path("id") id: Int): Single<ResponseBody>

    @GET("/v1/topics?search[trending]=true")
    fun getTopics(@Query("per_page") limit: Int, @Query("page") page: Int): Single<ResponseBody>

    @GET("/v1/me?exclude[]=relationships")
    fun getMe(): Observable<UserResponse>

    @GET("/v1/me/interactions?include[]=following_user_ids")
    fun getUserFollowingRL(): Observable<RelationshipResponse>

    @GET("/v1/users/{id}?exclude[]=relationships")
    fun getUser(@Path("id") id: Int): Single<ResponseBody>

    @POST("/v1/users/{id}/follow")
    fun followUser(@Path("id") id: Int): Observable<Any>

    @DELETE("/v1/users/{id}/follow")
    fun unfollowUser(@Path("id") id: Int): Observable<Any>

    @GET("/v1/users/{id}/posts")
    fun getProductsByUser(@Path("id") id: Int, @Query("per_page") limit: Int, @Query("page") page: Int): Single<ResponseBody>

    @GET("/v1/users/{id}/followers")
    fun getFollowersByUser(@Path("id") id: Int, @Query("per_page") limit: Int, @Query("page") page: Int): Observable<FollowerResponse>

    @GET("/v1/users/{id}/following")
    fun getFollowingByUser(@Path("id") id: Int, @Query("per_page") limit: Int, @Query("page") page: Int): Observable<FollowingResponse>

    @GET("/v1/notifications")
    fun getNotifications(): Single<ResponseBody>

    @PUT("/v1/settings")
    fun updateUser(@Body request: UpdateUserRequest): Observable<UpdateUserResponse>

    @POST("/v1/posts/{id}/vote")
    fun likeProduct(@Path("id") id: Int): Observable<LikeResponse>

    @DELETE("/v1/posts/{id}/vote")
    fun unlikeProduct(@Path("id") id: Int): Observable<UnlikeResponse>
}