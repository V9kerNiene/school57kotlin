package ru.tbank.education.school.homework

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Интерфейс для подсчёта строк и слов в файле.
 */
interface FileAnalyzer {

    /**
     * Считает количество строк и слов в указанном входном файле и записывает результат в выходной файл.
     *
     * Словом считается последовательность символов, разделённая пробелами,
     * табуляциями или знаками перевода строки. Пустые части после разделения не считаются словами.
     *
     * @param inputFilePath путь к входному текстовому файлу
     * @param outputFilePath путь к выходному файлу, в который будет записан результат
     * @return true если операция успешна, иначе false
     */
    fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean
}

class IOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        val reader = File(inputFilePath).bufferedReader()
        val res = File(outputFilePath)
        var countLines = 0
        var countWords = 0
        try {
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                countLines++
                var i = 0
                while (i < line!!.length) {
                    while (line[i] != ' ')  {
                        i++
                    }
                    countWords++
                    while (line[i] == ' ') {
                        i++
                    }
                }
            }
            res.writeText("Количество строк: $countLines\nКоличество слов: $countWords")
            return true
        }
        catch (e: Exception) {
            println("Ошибка при чтении файла: ${e.message}")
            return false
        }
        finally {
            reader.close()
        }
    }
}

class NIOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        var countLines = 0
        var countWords = 0
        try {
            val jj = Files.readString(Paths.get(inputFilePath))
            for (line in jj.split("\n")) {
                countLines++
                var i = 0
                while (i < line.length) {
                    while (line[i] != ' ')  {
                        i++
                    }
                    countWords++
                    while (line[i] == ' ') {
                        i++
                    }
                }
            }
            val res = "Количество строк: $countLines\nКоличество слов: $countWords"
            Files.write(Paths.get(outputFilePath),res.toByteArray(StandardCharsets.UTF_8))
            return true
        }
        catch (e: Exception) {
            println("Ошибка при чтении файла: ${e.message}")
            return false
        }
    }
}