package com.chesia.bangkitcapstoneproject.Networking

data class UserResponse(
	val data: Data,
	val success: Boolean
)

data class Data(
	val createdAt: CreatedAt,
	val password: String,
	val meta: Meta,
	val id: String,
	val fullname: String,
	val privilege: String,
	val email: String,
	val points: Int,
	val status: String,
	val updatedAt: UpdatedAt
)



data class Meta(
	val phone: String
)

