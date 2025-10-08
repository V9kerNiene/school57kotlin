package ru.tbank.education.school.homework


/**
 * Исключение, которое выбрасывается при попытке забронировать занятое место
 */
class SeatAlreadyBookedException(message: String) : Exception(message)

/**
 * Исключение, которое выбрасывается при попытке забронировать место при отсутствии свободных мест
 */
class NoAvailableSeatException(message: String) : Exception(message)

data class BookedSeat(
    val movieId: String, // идентификатор фильма // CHANGE:
    val seat: Int, // номер места
    // не использую, потому что был задействован map...
)

class MovieBookingService(
    private val maxQuantityOfSeats: Int // Максимальное кол-во мест
) {
    init {
        if (0 >= maxQuantityOfSeats) {
            throw IllegalArgumentException("Количество мест должно быть не меньше 1!")
        }
    }
    val seaats = mutableMapOf<String, MutableList<Boolean>>() /*

    Основная причина зачем - скорость обращения. Пусть N - maxQuantityOfSeats, а Q - количество фильмов, то тогда количество объектов - N*Q, которые добавляются в произвольном порядке.
    Искать среди них нужное место в нужном зале было бы ужасно неэффективно по времени: N*Q в худшем случае.
    MutableMap всё меняет - время обращение по ключу словаря - 1 или Const, что явно меньше N*Q => эффективнее при большом объёме данных.

    */
    /**
     * Бронирует указанное место для фильма.
     *
     * @param movieId идентификатор фильма
     * @param seat номер места
     * @throws IllegalArgumentException если номер места вне допустимого диапазона +
     * @throws NoAvailableSeatException если нет больше свободных мест
     * @throws SeatAlreadyBookedException если место уже забронировано +
     */
    private var allTaken = false
    fun checkIfEmptyAndDoThatOneThingy(movieId: String) {
        if (seaats[movieId] == null) {
            seaats[movieId] = MutableList(maxQuantityOfSeats+1) {false}
        }
    }
    fun checkAll(movieId: String) {
        checkIfEmptyAndDoThatOneThingy(movieId)
        var j = true
        for (i in seaats[movieId]!!) {
            if (!i) j = false
        }
        allTaken = j
    }
    fun bookSeat(movieId: String, seat: Int) {
        checkIfEmptyAndDoThatOneThingy(movieId)
        checkAll(movieId)
        if ((1 <= seat) and (seat <= maxQuantityOfSeats)) {
            val dan = seaats[movieId]?.get(seat)
            if (allTaken) {
                throw NoAvailableSeatException("На данный фильм более нет свободных мест!")
            }
            if (dan == true) {
                throw SeatAlreadyBookedException("Это место уже занято!")
            }
            else {
                seaats[movieId]?.set(seat, true)
            }
        }
        else throw IllegalArgumentException("Недопустимый номер места!")
        checkAll(movieId)
    }

    /**
     * Отменяет бронь указанного места.
     *
     * @param movieId идентификатор фильма
     * @param seat номер места
     * @throws NoSuchElementException если место не было забронировано
     */
    fun cancelBooking(movieId: String, seat: Int) {
        checkIfEmptyAndDoThatOneThingy(movieId)
        if (seaats[movieId]?.get(seat) == false) throw NoSuchElementException("Это место не забронировано!")
        else {
            seaats[movieId]?.set(seat, true)
            allTaken = false
        }
    }

    /**
     * Проверяет, забронировано ли место
     *
     * @return true если место занято, false иначе
     */
    fun isSeatBooked(movieId: String, seat: Int): Boolean {
        checkIfEmptyAndDoThatOneThingy(movieId)
        return seaats[movieId]!![seat]
    }
}

fun main() {
    var jj = MovieBookingService(15)
    jj.bookSeat("Kino",10)
}