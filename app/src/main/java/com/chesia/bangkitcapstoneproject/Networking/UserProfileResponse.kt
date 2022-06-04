package com.chesia.bangkitcapstoneproject.Networking

data class UserProfileResponse(
	val data: Data,
	val success: Boolean
)

data class Meta(
	val phone: String
)

data class User(
	val password: String,
	val meta: Meta,
	val id: String,
	val fullname: String,
	val email: String,
	val status: String
)

data class Data(
	val user: User
)


