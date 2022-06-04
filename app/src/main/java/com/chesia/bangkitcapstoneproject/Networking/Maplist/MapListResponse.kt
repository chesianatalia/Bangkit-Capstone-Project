package com.chesia.bangkitcapstoneproject.Networking.Maplist

data class MapListResponse(
	val data: Data? = null,
	val success: Boolean? = null
)

data class Data(
	val collectionPoints: List<CollectionPointsItem?>? = null
)

data class CollectionPointsItem(
	val createdAt: CreatedAt? = null,
	val latitude: Double? = null,
	val description: String? = null,
	val id: String? = null,
	val title: String? = null,
	val longitude: Double? = null,
	val status: String? = null,
	val updatedAt: UpdatedAt? = null
)

data class UpdatedAt(
	val nanoseconds: Int? = null,
	val seconds: Int? = null
)

data class CreatedAt(
	val nanoseconds: Int? = null,
	val seconds: Int? = null
)

