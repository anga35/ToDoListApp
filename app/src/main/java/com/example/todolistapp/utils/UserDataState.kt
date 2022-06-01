package com.example.todolistapp.utils

sealed class UserDataState<out R> {

    class UserData<out T>(val data:T):UserDataState<T>()
    class NewUser:UserDataState<Nothing>()
    class AnonymousUser:UserDataState<Nothing>()
    class Loading:UserDataState<Nothing>()
}