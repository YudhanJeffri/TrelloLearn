package com.yudhanproject.trelloclone.models

import android.os.Parcel
import android.os.Parcelable

data class UserBirth (
        val day: String = "",
        val month: String = "",
        val year: String = "",
        val harapan: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(day)
        parcel.writeString(month)
        parcel.writeString(year)
        parcel.writeString(harapan)
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserBirth> {
        override fun createFromParcel(parcel: Parcel): UserBirth {
            return UserBirth(parcel)
        }

        override fun newArray(size: Int): Array<UserBirth?> {
            return arrayOfNulls(size)
        }
    }
}
