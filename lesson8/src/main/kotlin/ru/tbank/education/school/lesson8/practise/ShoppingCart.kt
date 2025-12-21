package ru.tbank.education.school.lesson8.practise

import java.time.LocalDateTime
import kotlin.math.max

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val weight: Double
)

data class CartItem(
    val product: Product,
    val quantity: Int,
    val addedAt: LocalDateTime = LocalDateTime.now()
)

data class Discount(
    val value: Double,
    val minQuantity: Int = 1,
    val applicableCategories: Set<String> = emptySet()
)

class ShoppingCart {
    private val items: MutableList<CartItem> = mutableListOf()
    private var discounts: MutableList<Discount> = mutableListOf()

    fun addProduct(product: Product, quantity: Int): Boolean {
        var pr = CartItem(product, quantity)
        for (i in items) {
            if (i.product == product) {
                pr = CartItem(product, max(0, i.quantity+quantity), i.addedAt)
                items.remove(i)
            }
        }
        items.add(pr)
        return true
    }
    
    fun removeProduct(productId: String, quantity: Int): Boolean {
        var flag = false
        var pr: CartItem? = null
        for (i in items) {
            if (i.product.id==productId) {
                pr = CartItem(i.product, max(0, i.quantity-quantity), i.addedAt)
                flag = true
                items.remove(i)
            }
        }
        pr?.let { items.add(pr) }
        return flag
    }
    
    fun updateQuantity(productId: String, newQuantity: Int): Boolean {
        var flag = false
        var pr: CartItem? = null
        for (i in items) {
            if (i.product.id==productId) {
                pr = CartItem(i.product, newQuantity, i.addedAt)
                flag = true
                items.remove(i)
            }
        }
        pr?.let { items.add(pr) }
        return flag
    }
    
    fun clear() {
        items.clear()
    }

    fun getSubtotal(): Double {
        TODO("Рассчитать сумму без скидок")
    }
    
    fun getTotalWeight(): Double {
        var totalWeight = 0.0
        for (i in items) {
            totalWeight += i.product.weight
        }
        return totalWeight
    }
    
    fun getTotalWithDiscounts(): Double {
        TODO("Рассчитать итоговую сумму со скидками")
    }
    
    fun applyDiscount(discount: Discount) {
        TODO("Применить скидку к корзине")
    }

    
    fun validateWeightLimit(maxWeight: Double = 50.0): Boolean {
        return getTotalWeight() > maxWeight
    }

    fun getMostExpensiveItem(): CartItem? {
        return when {
            (items.isEmpty()) -> null
            else -> {
                var mx = -1.0
                var mxI: CartItem? = null
                for (i in items) {
                    if (i.product.price > mx) {
                        mx = i.product.price
                        mxI = i
                    }
                }
                mxI
            }
        }

    }
    
    fun getItemsByCategory(category: String): List<CartItem> {
        return items.filter { cartItem ->
            cartItem.product.category==category
        }
    }

    fun getItemCount(): Int {
        TODO("Получить общее количество товаров")
    }
    
    fun getUniqueProductsCount(): Int {
        TODO("Получить количество уникальных товаров")
    }
}