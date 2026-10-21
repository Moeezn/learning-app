package com.example.data

import com.example.model.SqlQueryResult
import com.example.model.SqlTable
import java.util.Locale

class SqlEngine(initialTables: List<SqlTable>) {
    private val tables = mutableMapOf<String, SqlTable>()

    init {
        initialTables.forEach { table ->
            tables[table.tableName.lowercase(Locale.ROOT)] = SqlTable(
                tableName = table.tableName,
                columns = table.columns.toList(),
                rows = table.rows.map { it.toMutableMap() }.toMutableList()
            )
        }
    }

    fun getTables(): List<SqlTable> = tables.values.toList()

    fun executeQuery(rawSql: String): SqlQueryResult {
        val trimmed = rawSql.trim().trimEnd(';')
        if (trimmed.isBlank()) {
            return SqlQueryResult(success = false, message = "Empty query. Enter a valid SQL statement.")
        }

        val upper = trimmed.uppercase(Locale.ROOT)

        return try {
            when {
                upper == "SHOW TABLES" -> {
                    SqlQueryResult(
                        success = true,
                        message = "Found ${tables.size} tables.",
                        columns = listOf("Tables_in_devforge"),
                        rows = tables.keys.map { mapOf("Tables_in_devforge" to it) }
                    )
                }
                upper.startsWith("DESCRIBE ") || upper.startsWith("DESC ") -> {
                    val tableName = trimmed.split(Regex("\\s+"))[1].lowercase(Locale.ROOT)
                    val table = tables[tableName] ?: return SqlQueryResult(success = false, message = "Table '$tableName' does not exist.")
                    SqlQueryResult(
                        success = true,
                        message = "Schema for table $tableName",
                        columns = listOf("Field", "Type", "Null", "Key"),
                        rows = table.columns.map {
                            mapOf(
                                "Field" to it,
                                "Type" to if (it == "id" || it.endsWith("_id") || it == "xp" || it == "score") "INT" else "VARCHAR(255)",
                                "Null" to "NO",
                                "Key" to if (it == "id") "PRI" else ""
                            )
                        }
                    )
                }
                upper.startsWith("SELECT") -> handleSelect(trimmed)
                upper.startsWith("INSERT INTO") -> handleInsert(trimmed)
                upper.startsWith("UPDATE") -> handleUpdate(trimmed)
                upper.startsWith("DELETE FROM") -> handleDelete(trimmed)
                else -> SqlQueryResult(
                    success = false,
                    message = "Syntax error or unsupported statement. DevForge MySQL engine supports SELECT, INSERT, UPDATE, DELETE, SHOW TABLES, and DESCRIBE."
                )
            }
        } catch (e: Exception) {
            SqlQueryResult(success = false, message = "SQL Error: ${e.message ?: "Failed to parse query"}")
        }
    }

    private fun handleSelect(sql: String): SqlQueryResult {
        // e.g., SELECT * FROM users WHERE xp > 1000 ORDER BY xp DESC
        val fromIndex = sql.indexOf("FROM ", ignoreCase = true)
        if (fromIndex == -1) {
            return SqlQueryResult(success = false, message = "Missing FROM clause in SELECT statement.")
        }

        val selectPart = sql.substring(0, fromIndex).trim().removePrefix("SELECT").removePrefix("select").trim()
        val rest = sql.substring(fromIndex + 5).trim()

        val tokens = rest.split(Regex("\\s+"))
        val primaryTableName = tokens[0].lowercase(Locale.ROOT)
        val table = tables[primaryTableName] ?: return SqlQueryResult(success = false, message = "Table '$primaryTableName' does not exist.")

        var currentRows: List<Map<String, String>> = table.rows.map { it.toMap() }
        var activeColumns: List<String> = table.columns.toList()

        // Check for JOIN
        val joinRegex = Regex("(?i)INNER\\s+JOIN\\s+(\\w+)\\s+ON\\s+(\\w+)\\.(\\w+)\\s*=\\s*(\\w+)\\.(\\w+)")
        val joinMatch = joinRegex.find(rest)
        if (joinMatch != null) {
            val joinedTableName = joinMatch.groupValues[1].lowercase(Locale.ROOT)
            val joinedTable = tables[joinedTableName]
            if (joinedTable != null) {
                val t1 = joinMatch.groupValues[2].lowercase(Locale.ROOT)
                val c1 = joinMatch.groupValues[3].lowercase(Locale.ROOT)
                val t2 = joinMatch.groupValues[4].lowercase(Locale.ROOT)
                val c2 = joinMatch.groupValues[5].lowercase(Locale.ROOT)

                val combined = mutableListOf<Map<String, String>>()
                for (r1 in currentRows) {
                    for (r2 in joinedTable.rows) {
                        val val1 = if (t1 == primaryTableName) r1[c1] else r2[c1]
                        val val2 = if (t2 == joinedTableName) r2[c2] else r1[c2]
                        if (val1 != null && val1 == val2) {
                            val merged = mutableMapOf<String, String>()
                            r1.forEach { (k, v) -> merged["$primaryTableName.$k"] = v; merged[k] = v }
                            r2.forEach { (k, v) -> merged["$joinedTableName.$k"] = v; if (!merged.containsKey(k)) merged[k] = v }
                            combined.add(merged)
                        }
                    }
                }
                currentRows = combined
                activeColumns = (table.columns.map { "$primaryTableName.$it" } + joinedTable.columns.map { "$joinedTableName.$it" })
            }
        }

        // Check for WHERE clause
        val whereRegex = Regex("(?i)WHERE\\s+(.*?)(?:\\s+ORDER\\s+BY|\\s+LIMIT|$)")
        val whereMatch = whereRegex.find(rest)
        if (whereMatch != null) {
            val condition = whereMatch.groupValues[1].trim()
            currentRows = filterRows(currentRows, condition)
        }

        // Check for ORDER BY clause
        val orderRegex = Regex("(?i)ORDER\\s+BY\\s+(\\w+)(?:\\s+(ASC|DESC))?")
        val orderMatch = orderRegex.find(rest)
        if (orderMatch != null) {
            val orderCol = orderMatch.groupValues[1]
            val isDesc = orderMatch.groupValues[2].equals("DESC", ignoreCase = true)
            currentRows = currentRows.sortedWith { a, b ->
                val v1 = a[orderCol] ?: a.entries.find { it.key.endsWith(".$orderCol") }?.value ?: ""
                val v2 = b[orderCol] ?: b.entries.find { it.key.endsWith(".$orderCol") }?.value ?: ""
                val num1 = v1.toDoubleOrNull()
                val num2 = v2.toDoubleOrNull()
                val cmp = if (num1 != null && num2 != null) num1.compareTo(num2) else v1.compareTo(v2)
                if (isDesc) -cmp else cmp
            }
        }

        // Project columns
        val finalColumns = if (selectPart == "*") {
            activeColumns
        } else {
            selectPart.split(",").map { it.trim() }
        }

        return SqlQueryResult(
            success = true,
            message = "${currentRows.size} row(s) in set",
            columns = finalColumns,
            rows = currentRows.map { row ->
                finalColumns.associateWith { col ->
                    row[col] ?: row.entries.find { it.key.endsWith(".$col") }?.value ?: "NULL"
                }
            }
        )
    }

    private fun filterRows(rows: List<Map<String, String>>, condition: String): List<Map<String, String>> {
        val ops = listOf(">=", "<=", "!=", "=", ">", "<", "LIKE", "like")
        for (op in ops) {
            val idx = condition.indexOf(op, ignoreCase = true)
            if (idx != -1) {
                val col = condition.substring(0, idx).trim()
                val rawVal = condition.substring(idx + op.length).trim().trim('\'', '"')
                return rows.filter { row ->
                    val cell = row[col] ?: row.entries.find { it.key.endsWith(".$col") }?.value ?: return@filter false
                    val cellNum = cell.toDoubleOrNull()
                    val valNum = rawVal.toDoubleOrNull()

                    when (op.uppercase(Locale.ROOT)) {
                        "=" -> cell.equals(rawVal, ignoreCase = true)
                        "!=" -> !cell.equals(rawVal, ignoreCase = true)
                        ">" -> if (cellNum != null && valNum != null) cellNum > valNum else cell > rawVal
                        "<" -> if (cellNum != null && valNum != null) cellNum < valNum else cell < rawVal
                        ">=" -> if (cellNum != null && valNum != null) cellNum >= valNum else cell >= rawVal
                        "<=" -> if (cellNum != null && valNum != null) cellNum <= valNum else cell <= rawVal
                        "LIKE" -> cell.contains(rawVal.replace("%", ""), ignoreCase = true)
                        else -> true
                    }
                }
            }
        }
        return rows
    }

    private fun handleInsert(sql: String): SqlQueryResult {
        // INSERT INTO table (col1, col2) VALUES (val1, val2)
        val regex = Regex("(?i)INSERT\\s+INTO\\s+(\\w+)\\s*\\((.*?)\\)\\s*VALUES\\s*\\((.*?)\\)")
        val match = regex.find(sql) ?: return SqlQueryResult(success = false, message = "Malformed INSERT query.")
        val tableName = match.groupValues[1].lowercase(Locale.ROOT)
        val table = tables[tableName] ?: return SqlQueryResult(success = false, message = "Table '$tableName' does not exist.")

        val cols = match.groupValues[2].split(",").map { it.trim() }
        val vals = match.groupValues[3].split(",").map { it.trim().trim('\'', '"') }

        if (cols.size != vals.size) {
            return SqlQueryResult(success = false, message = "Column count (${cols.size}) does not match value count (${vals.size}).")
        }

        val newRow = mutableMapOf<String, String>()
        cols.forEachIndexed { i, c -> newRow[c] = vals[i] }
        table.rows.add(newRow)

        return SqlQueryResult(success = true, message = "Query OK, 1 row affected (INSERT).", rowsAffected = 1)
    }

    private fun handleUpdate(sql: String): SqlQueryResult {
        // UPDATE table SET col = val WHERE ...
        val regex = Regex("(?i)UPDATE\\s+(\\w+)\\s+SET\\s+(.*?)(?:\\s+WHERE\\s+(.*?))?$")
        val match = regex.find(sql) ?: return SqlQueryResult(success = false, message = "Malformed UPDATE query.")
        val tableName = match.groupValues[1].lowercase(Locale.ROOT)
        val table = tables[tableName] ?: return SqlQueryResult(success = false, message = "Table '$tableName' does not exist.")

        val setClause = match.groupValues[2].trim()
        val whereClause = match.groupValues[3].trim()

        val setParts = setClause.split("=").map { it.trim() }
        if (setParts.size != 2) {
            return SqlQueryResult(success = false, message = "Malformed SET clause.")
        }
        val setCol = setParts[0]
        val setVal = setParts[1].trim('\'', '"')

        var count = 0
        table.rows.forEach { row ->
            val matchRow = if (whereClause.isBlank()) true else filterRows(listOf(row), whereClause).isNotEmpty()
            if (matchRow) {
                row[setCol] = setVal
                count++
            }
        }

        return SqlQueryResult(success = true, message = "Query OK, $count row(s) affected (UPDATE).", rowsAffected = count)
    }

    private fun handleDelete(sql: String): SqlQueryResult {
        // DELETE FROM table WHERE ...
        val regex = Regex("(?i)DELETE\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(.*?))?$")
        val match = regex.find(sql) ?: return SqlQueryResult(success = false, message = "Malformed DELETE query.")
        val tableName = match.groupValues[1].lowercase(Locale.ROOT)
        val table = tables[tableName] ?: return SqlQueryResult(success = false, message = "Table '$tableName' does not exist.")
        val whereClause = match.groupValues[2].trim()

        val initialSize = table.rows.size
        if (whereClause.isBlank()) {
            table.rows.clear()
        } else {
            table.rows.removeAll { row -> filterRows(listOf(row), whereClause).isNotEmpty() }
        }
        val affected = initialSize - table.rows.size

        return SqlQueryResult(success = true, message = "Query OK, $affected row(s) affected (DELETE).", rowsAffected = affected)
    }
}
