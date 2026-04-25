package com.example.aura_style.data.repository

import com.example.aura_style.data.local.dao.UserDao
import com.example.aura_style.data.local.entity.User

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
}
