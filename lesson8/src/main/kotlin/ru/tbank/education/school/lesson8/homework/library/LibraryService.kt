package ru.tbank.education.school.lesson8.homework.library

class LibraryService {
    private val books = mutableMapOf<String, Book>()
    private val borrowedBooks = mutableSetOf<String>()
    private val borrowerFines = mutableMapOf<String, Int>()
    private val borrowerBooks = mutableMapOf<String, MutableList<String>>()
    fun addBook(book: Book) {
        books[book.isbn] = book
    }

    fun borrowBook(isbn: String, borrower: String, daysOverdue: Int = 0) {
        if (books.containsKey(isbn)) {
            if (borrowedBooks.contains(isbn)) {
                throw IllegalArgumentException()
            }
            else {
                if (borrowerBooks[borrower] == null) {
                    borrowerBooks[borrower] = mutableListOf(isbn)
                    borrowedBooks.add(isbn)
                }
                else {
                    for (i in borrowerBooks[borrower]!!) {
                        if (calculateOverdueFine(i, daysOverdue) > 0) {
                            throw IllegalArgumentException()
                        }
                    }
                    borrowerBooks[borrower]!!.add(isbn)
                    borrowedBooks.add(isbn)
                }
            }
        } else {
            throw IllegalArgumentException()
        }
    }

    fun returnBook(isbn: String) {
        if (borrowedBooks.contains(isbn)) borrowedBooks.remove(isbn)
        else throw IllegalArgumentException()
    }

    fun isAvailable(isbn: String): Boolean {
        return !borrowedBooks.contains(isbn)
    }

    fun calculateOverdueFine(isbn: String, daysOverdue: Int): Int {
        if (!borrowedBooks.contains(isbn)) {
            return 0
        }
        return if (daysOverdue > 10) {
            (daysOverdue-10)*60
        } else {
            0
        }
    }

    private fun hasOutstandingFines(borrower: String): Boolean {
        return (borrowerFines[borrower] ?: 0) > 0
    }
}