package com.chesia.bangkitcapstoneproject.Networking

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GetTrashResponse(
//	val data: Datas,
	val data: List<TrashReportsItem>,
	val success: Boolean
)

data class CreatedAt(
	val nanoseconds: Int,
	val seconds: Int
)

@Parcelize
data class TrashListItem(
//	val createdAt: CreatedAt,
	val quantity: Int,
	val photo: String,
	val category: String,
	val title: String
) : Parcelable

//data class Datas(
//	val trashReports: List<TrashReportsItem>
//)

data class UpdatedAt(
	val nanoseconds: Int,
	val seconds: Int
)

data class TrashReportsItem(
	val createdAt: CreatedAt,
	val collectionPoint: String,
	val trashList: ArrayList<TrashListItem>,
	val description: String,
	val id: String,
	val title: String,
	val user: String,
	val point: Int,
	val updatedAt: UpdatedAt,
	val status: String
)

