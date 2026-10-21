package com.example

import com.example.data.DevForgeRepository
import com.example.data.SqlEngine
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SqlEngineTest {

    private lateinit var sqlEngine: SqlEngine

    @Before
    fun setUp() {
        sqlEngine = SqlEngine(DevForgeRepository.createInitialDatabase())
    }

    @Test
    fun testSelectAllUsers() {
        val result = sqlEngine.executeQuery("SELECT * FROM users;")
        assertTrue(result.success)
        assertEquals(4, result.rows.size)
        assertTrue(result.columns.contains("username"))
    }

    @Test
    fun testSelectWithWhereClause() {
        val result = sqlEngine.executeQuery("SELECT * FROM users WHERE xp > 2000;")
        assertTrue(result.success)
        assertEquals(3, result.rows.size)
    }

    @Test
    fun testInsertAndQuery() {
        val insertResult = sqlEngine.executeQuery("INSERT INTO users (id, username, email, xp, level, role) VALUES ('99', 'tester', 't@test.com', '1200', '3', 'student');")
        assertTrue(insertResult.success)

        val queryResult = sqlEngine.executeQuery("SELECT * FROM users WHERE username = 'tester';")
        assertTrue(queryResult.success)
        assertEquals(1, queryResult.rows.size)
        assertEquals("tester", queryResult.rows[0]["username"])
    }

    @Test
    fun testInnerJoin() {
        val result = sqlEngine.executeQuery("SELECT u.username, s.score, c.title FROM users u INNER JOIN submissions s ON u.id = s.user_id INNER JOIN courses c ON s.course_id = c.id;")
        assertTrue(result.success)
        assertTrue(result.rows.isNotEmpty())
    }
}
