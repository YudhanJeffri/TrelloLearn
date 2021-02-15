package com.yudhanproject.trelloclone.models.user

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val birth: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val image: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest){
        writeString(id)
        writeString(name)
        writeString(email)
        writeString(birth)
        writeString(image)
        writeString(day)
        writeString(month)
        writeString(year)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
