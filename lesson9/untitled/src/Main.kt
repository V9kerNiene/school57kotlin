import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

private fun addFilesToZip(
    file: File,
    zipOutputStream: ZipOutputStream,
    entryNamePrefix: String,
    allowedExtensions: List<String>
) {
    val entries = file.listFiles()
    if (entries == null) {
        println("Не удалось получить список файлов в каталоге: ${file.path}")
        return
    }

    for (entry in entries) {
        val currentEntryName = if (entryNamePrefix.isEmpty()) entry.name else "$entryNamePrefix/${entry.name}"

        if (entry.isDirectory) {
            addFilesToZip(entry, zipOutputStream, currentEntryName, allowedExtensions)
        } else {
            val extension = entry.extension.lowercase()
            if (allowedExtensions.contains(extension)) {
                try {
                    val fis = FileInputStream(entry)
                    val zipEntry = ZipEntry(currentEntryName)
                    zipOutputStream.putNextEntry(zipEntry)

                    val buffer = ByteArray(1024)
                    var len: Int
                    fis.use {
                        while (fis.read(buffer).also { len = it } > 0) {
                            zipOutputStream.write(buffer, 0, len)
                        }
                    }
                    zipOutputStream.closeEntry()

                    println("Добавлен файл: ${entry.path} (${entry.length()} байт)")

                } catch (e: IOException) {
                    println("Ошибка при добавлении файла '${entry.path}' в архив: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }
}


fun zipIt(dirPath: String, archivePath: String, allowedExtensions: List<String>) {
    val sourceDirectory = File(dirPath)
    if (!sourceDirectory.isDirectory) {
        println("Ошибка: Исходный каталог '$dirPath' не существует или не является каталогом.")
        return
    }

    var zipOutputStream: ZipOutputStream? = null
    try {
        zipOutputStream = ZipOutputStream(FileOutputStream(archivePath))

        println("Создание архива: $archivePath")

        addFilesToZip(sourceDirectory, zipOutputStream, "", allowedExtensions)

        println("Архив успешно создан: $archivePath")

    } catch (e: IOException) {
        println("Ошибка при создании архива: ${e.message}")
        e.printStackTrace()
    } finally {
        zipOutputStream?.close()
    }
}