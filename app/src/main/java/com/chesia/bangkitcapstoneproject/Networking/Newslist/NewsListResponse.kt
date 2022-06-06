package com.chesia.bangkitcapstoneproject.Networking.Newslist

data class NewsListResponse(
	val data: Data? = null,
	val success: Boolean? = null
)

data class Data(
	val news: List<NewsItem?>? = null
)

data class CreatedAt(
	val nanoseconds: Int? = null,
	val seconds: Int? = null
)

data class UpdatedAt(
	val nanoseconds: Int? = null,
	val seconds: Int? = null
)

data class NewsItem(
	val createdAt: CreatedAt? = null,
	val image: String? = null,
	val createdBy: String? = null,
	val description: String? = null,
	val id: String? = null,
	val title: String? = null,
	val category: String? = null,
	val content: String? = null,
	val updatedAt: UpdatedAt? = null,
	val status: String? = null
)

